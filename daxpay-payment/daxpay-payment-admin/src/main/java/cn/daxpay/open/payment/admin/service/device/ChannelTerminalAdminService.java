package cn.daxpay.open.payment.admin.service.device;

import cn.daxpay.open.payment.admin.param.device.ChannelTerminalParam;
import cn.daxpay.open.payment.admin.param.device.ChannelTerminalRegisterParam;
import cn.daxpay.open.payment.admin.param.device.ChannelTerminalReportParam;
import cn.daxpay.open.payment.admin.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.device.enums.TerminalTypeEnum;
import cn.daxpay.open.payment.device.terminal.bo.TerminalExtraField;
import cn.daxpay.open.payment.device.terminal.bo.TerminalOperateResult;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.TerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.payment.device.terminal.service.ChannelTerminalQuerySupport;
import cn.daxpay.open.payment.device.terminal.service.TerminalBindSupport;
import cn.daxpay.open.payment.device.terminal.strategy.AbsChannelTerminalStrategy;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/// # 通道终端台账管理(运营端)
///
/// 挂在通道商户下维护通道侧终端号与状态。关联关系在新增建档/报备/手动绑定时建立(报备即绑定):
/// - 一条通道终端只归属一个系统终端; 同一系统终端在同一通道商户下按报送类型各一条;
/// - 自动报备通道: 报备/注销/同步经通道报备策略([AbsChannelTerminalStrategy])调通道 API 回写状态与通道侧终端号;
/// - 无报备策略的通道走人工登记([ChannelTerminalAdminService#register]), 不调通道。
/// 绑定规则校验见 [TerminalBindSupport]。
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelTerminalAdminService {

    private final ChannelTerminalManager channelTerminalManager;
    private final TerminalChannelBindManager terminalChannelBindManager;
    private final TerminalDeviceManager terminalDeviceManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final PayProductManager payProductManager;
    private final MerchantInfoManager merchantInfoManager;
    private final TransService transService;
    private final TerminalBindSupport terminalBindSupport;
    private final ChannelTerminalQuerySupport channelTerminalQuerySupport;

    /// 新增通道终端台账(建档即绑定系统终端)
    @Transactional(rollbackFor = Exception.class)
    public void add(ChannelTerminalParam param) {
        merchantInfoManager.findByMchNo(param.getMchNo())
                // 商户: 商户不存在
                .orElseThrow(() -> new BizException(CommonCode.FAIL_CODE, "error.payment.merchant.mchNotExist"));
        // 校验报送类型
        TerminalTypeEnum typeEnum = TerminalTypeEnum.findByCode(param.getType());
        // 通道商户须存在且归属商户
        ChannelMerchant channelMerchant = channelMerchantManager
                .findByMchNoAndChannelMchNo(param.getMchNo(), param.getChannelMchNo())
                // 通道: 通道商户不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
        // 从产品解析通道编码
        String product = channelMerchant.getProduct();
        PayProduct payProduct = payProductManager.findByCode(product)
                // 产品: 支付产品不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.product.notExist", product));
        // 校验系统终端(存在性/同商户)
        TerminalDevice systemTerminal = requireSystemTerminal(param.getSystemTerminalNo(), param.getMchNo());

        ChannelTerminal entity = new ChannelTerminal();
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMerchant.getChannelMchNo());
        entity.setProduct(product);
        entity.setChannel(payProduct.getChannel());
        entity.setType(typeEnum.getCode());
        entity.setName(param.getName());
        // 建档即初始状态; 通道侧终端号/状态由手动登记动作回写
        entity.setStatus(ChannelTerminalStatusEnum.INIT.getCode());
        entity.setExtra(param.getExtra());
        entity.setRemark(param.getRemark());
        channelTerminalManager.save(entity);

        // 建档即绑定: 同一系统终端在同一通道商户下按报送类型唯一
        if (terminalBindSupport.hasTypeConflict(systemTerminal.getTerminalNo(), entity, entity.getId())) {
            // 终端: 同报送类型下已有通道终端
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.typeConflict", entity.getType());
        }
        terminalChannelBindManager.save(terminalBindSupport.buildBind(systemTerminal.getTerminalNo(), entity));
    }

    /// 修改通道终端(仅名称/备注; 通道商户/类型/系统终端绑定与登记状态不在此维护)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChannelTerminalParam param) {
        ChannelTerminal entity = channelTerminalManager.findById(param.getId())
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        entity.setName(param.getName());
        entity.setExtra(param.getExtra());
        entity.setRemark(param.getRemark());
        channelTerminalManager.updateById(entity);
    }

    /// 手动登记通道终端(手工订正通道侧终端号与登记状态; 不调通道)
    ///
    /// - 任意状态可用: 通道控制台手工操作后系统状态可据此对齐;
    /// - 通道侧终端号可空 = 只改状态; 登记视为一次人工修复, 清空错误信息。
    @Transactional(rollbackFor = Exception.class)
    public void register(ChannelTerminalRegisterParam param) {
        ChannelTerminal entity = channelTerminalManager.findById(param.getId())
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        // 目标状态按实际选择, 校验枚举合法性(含空值)
        ChannelTerminalStatusEnum targetStatus = ChannelTerminalStatusEnum.findByCode(param.getStatus());
        if (StrUtil.isNotBlank(param.getOutTerminalNo())) {
            entity.setOutTerminalNo(param.getOutTerminalNo().trim());
        }
        entity.setStatus(targetStatus.getCode());
        entity.setErrorMsg(null);
        channelTerminalManager.updateById(entity);
    }

    /// 分页
    public PageResult<ChannelTerminalResult> page(PageParam pageParam, ChannelTerminalQuery query) {
        return channelTerminalQuerySupport.page(pageParam, query);
    }

    /// 详情
    public ChannelTerminalResult findById(Long id) {
        return channelTerminalQuerySupport.findById(id);
    }

    /// 商户下通道终端列表(绑定下拉用)
    public List<ChannelTerminalResult> listByMchNo(String mchNo) {
        return channelTerminalManager.findAllByMchNo(mchNo).stream()
                .map(ChannelTerminal::toResult)
                .collect(Collectors.toList());
    }

    /// 删除(级联解绑)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        channelTerminalManager.findById(id)
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        terminalChannelBindManager.deleteByChannelTerminalId(id);
        channelTerminalManager.deleteById(id);
    }

    /// 绑定系统终端(存量记录修复入口; 正常关联由新增建档建立)
    @Transactional(rollbackFor = Exception.class)
    public void bind(TerminalChannelBindParam param) {
        TerminalDevice system = terminalDeviceManager.findByTerminalNo(param.getSystemTerminalNo())
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        ChannelTerminal channel = channelTerminalManager.findById(param.getChannelTerminalId())
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        if (!Objects.equals(system.getMchNo(), channel.getMchNo())) {
            // 终端: 系统终端与通道终端须属于同一商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.mchMismatch");
        }
        if (terminalChannelBindManager.existsBind(param.getSystemTerminalNo(), param.getChannelTerminalId())) {
            // 终端: 绑定关系已存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.bindExists");
        }
        // 一条通道终端只归属一个系统终端
        if (terminalBindSupport.hasOtherOwner(channel.getId(), param.getSystemTerminalNo())) {
            // 终端: 通道终端已绑定其他系统终端
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.ownerExists");
        }
        // 同一系统终端在同一通道商户、同一报送类型下仅一条
        if (terminalBindSupport.hasTypeConflict(param.getSystemTerminalNo(), channel, channel.getId())) {
            // 终端: 同报送类型下已有通道终端
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.typeConflict", channel.getType());
        }
        terminalChannelBindManager.save(terminalBindSupport.buildBind(param.getSystemTerminalNo(), channel));
    }

    /// 解绑系统终端
    @Transactional(rollbackFor = Exception.class)
    public void unbind(TerminalChannelBindParam param) {
        if (!terminalChannelBindManager.existsBind(param.getSystemTerminalNo(), param.getChannelTerminalId())) {
            // 终端: 绑定关系不存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.bindNotFound");
        }
        terminalChannelBindManager.deleteBind(param.getSystemTerminalNo(), param.getChannelTerminalId());
    }

    /// 查询通道终端已绑定的系统终端列表
    public List<TerminalDeviceResult> listBoundSystem(Long channelTerminalId) {
        channelTerminalManager.findById(channelTerminalId)
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        List<String> terminalNos = terminalChannelBindManager.findByChannelTerminalId(channelTerminalId).stream()
                .map(TerminalChannelBind::getSystemTerminalNo)
                .toList();
        if (terminalNos.isEmpty()) {
            return List.of();
        }
        List<TerminalDeviceResult> results = terminalDeviceManager.lambdaQuery()
                .in(TerminalDevice::getTerminalNo, terminalNos)
                .list()
                .stream()
                .map(TerminalDevice::toResult)
                .collect(Collectors.toList());
        transService.translate(results);
        return results;
    }

    /// 查询通道支持的终端报送类型(无报备策略的通道返回空列表)
    public List<String> terminalTypes(String product) {
        return PaymentStrategyFactory.findOptionallyByProduct(product, AbsChannelTerminalStrategy.class)
                .map(AbsChannelTerminalStrategy::getTerminalTypes)
                .orElse(List.of());
    }

    /// 查询通道差异化报备参数字段定义(运营端结构化表单用; 无策略或未定义返回空)
    public List<TerminalExtraField> extraFields(String product) {
        return PaymentStrategyFactory.findOptionallyByProduct(product, AbsChannelTerminalStrategy.class)
                .map(AbsChannelTerminalStrategy::getExtraFields)
                .orElse(List.of());
    }

    /// 终端报备(报备即绑定): 调通道 API 报备并同步建立关联
    ///
    /// - 已绑定的通道终端沿绑发起, 未绑定须选择系统终端;
    /// - 已报送状态拒绝重复报备, 须先注销;
    /// - 更换系统终端须先注销(通道侧设备标识已上交, 换终端等于换设备);
    /// - 通道调用失败保留绑定关系并落 error, 重试无需重新选择;
    /// - 不支持自动报备的通道请使用手动登记([ChannelTerminalAdminService#register])。
    @Transactional(rollbackFor = Exception.class)
    public void report(ChannelTerminalReportParam param) {
        ChannelTerminal entity = channelTerminalManager.findById(param.getId())
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        if (ChannelTerminalStatusEnum.SUBMIT.getCode().equals(entity.getStatus())) {
            // 终端: 已报送不可重复报备
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.reportDuplicate");
        }
        String systemTerminalNo = this.resolveAndEnsureBind(entity, param.getSystemTerminalNo());
        // 无报备策略的通道不支持自动报备
        AbsChannelTerminalStrategy strategy = PaymentStrategyFactory
                .findOptionallyByProduct(entity.getProduct(), AbsChannelTerminalStrategy.class).orElse(null);
        if (strategy == null) {
            // 终端: 通道无报备策略
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.strategyNotFound");
        }
        // 通道调用异常也按失败回写(不向上抛, 保证已建立的绑定不被事务回滚)
        TerminalOperateResult result;
        try {
            result = strategy.submit(entity, systemTerminalNo);
        } catch (Exception e) {
            log.warn("终端报备调用异常: channelMchNo={}, systemTerminalNo={}",
                    entity.getChannelMchNo(), systemTerminalNo, e);
            result = TerminalOperateResult.fail(e.getMessage());
        }
        this.applyOperateResult(entity, result);
    }

    /// 终端注销(自动通道调通道 API; 人工通道直接置已注销)
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        ChannelTerminal entity = channelTerminalManager.findById(id)
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        AbsChannelTerminalStrategy strategy = PaymentStrategyFactory
                .findOptionallyByProduct(entity.getProduct(), AbsChannelTerminalStrategy.class).orElse(null);
        if (strategy == null) {
            // 人工通道无通道调用: 直接置已注销, 保留通道侧终端号与绑定关系(便于重新报备)
            if (!ChannelTerminalStatusEnum.LOGGED.getCode().equals(entity.getStatus())) {
                entity.setStatus(ChannelTerminalStatusEnum.LOGGED.getCode());
                entity.setErrorMsg(null);
                channelTerminalManager.updateById(entity);
            }
            return;
        }
        TerminalOperateResult result = strategy.cancel(entity, this.requireBoundSystemTerminalNo(entity));
        this.applyOperateResult(entity, result);
    }

    /// 终端状态同步(调通道 API 回读实际状态与通道侧终端号)
    @Transactional(rollbackFor = Exception.class)
    public void sync(Long id) {
        ChannelTerminal entity = channelTerminalManager.findById(id)
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        AbsChannelTerminalStrategy strategy = PaymentStrategyFactory
                .findOptionallyByProduct(entity.getProduct(), AbsChannelTerminalStrategy.class)
                .orElseThrow(() -> new OperationFailException(CommonCode.FAIL_CODE,
                        "error.device.terminal.strategyNotFound"));
        TerminalOperateResult result = strategy.sync(entity, this.requireBoundSystemTerminalNo(entity));
        this.applyOperateResult(entity, result);
    }

    /// 取报备锚点的系统终端号(绑定关系中的首个)
    private String requireBoundSystemTerminalNo(ChannelTerminal entity) {
        return terminalChannelBindManager.findByChannelTerminalId(entity.getId()).stream()
                .map(TerminalChannelBind::getSystemTerminalNo)
                .findFirst()
                // 终端: 未绑定系统终端
                .orElseThrow(() -> new OperationFailException(CommonCode.FAIL_CODE,
                        "error.device.terminal.ownerMissing"));
    }

    /// 解析报备锚定的系统终端号并确保绑定就绪(入参优先, 沿用已绑定, 均无则报错)
    ///
    /// - 首次绑定: 校验系统终端存在性与同商户、同报送类型唯一后建立绑定;
    /// - 更换系统终端: 已报送须先注销; 未报送则替换原绑定。
    private String resolveAndEnsureBind(ChannelTerminal entity, String inputTerminalNo) {
        TerminalChannelBind owner = terminalChannelBindManager.findByChannelTerminalId(entity.getId()).stream()
                .findFirst()
                .orElse(null);
        String systemTerminalNo = StrUtil.isNotBlank(inputTerminalNo)
                ? inputTerminalNo.trim()
                : (owner == null ? null : owner.getSystemTerminalNo());
        if (StrUtil.isBlank(systemTerminalNo)) {
            // 终端: 未选择系统终端
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.terminalNotSelected");
        }
        // 更换系统终端: 已报送须先注销; 未报送则替换原绑定
        if (owner != null && !Objects.equals(owner.getSystemTerminalNo(), systemTerminalNo)) {
            if (ChannelTerminalStatusEnum.SUBMIT.getCode().equals(entity.getStatus())) {
                // 终端: 已报送不可直接换绑
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.rebindSubmitted");
            }
            terminalChannelBindManager.deleteByChannelTerminalId(entity.getId());
            owner = null;
        }
        if (owner == null) {
            this.requireSystemTerminal(systemTerminalNo, entity.getMchNo());
            if (terminalBindSupport.hasTypeConflict(systemTerminalNo, entity, entity.getId())) {
                // 终端: 同报送类型下已有通道终端
                throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.typeConflict",
                        entity.getType());
            }
            terminalChannelBindManager.save(terminalBindSupport.buildBind(systemTerminalNo, entity));
        }
        return systemTerminalNo;
    }

    /// 回写报备操作结果: 成功落状态与通道侧终端号, 失败落错误状态与原因(不清既有终端号)
    private void applyOperateResult(ChannelTerminal entity, TerminalOperateResult result) {
        if (result.isSuccess()) {
            entity.setStatus(result.getStatus());
            if (StrUtil.isNotBlank(result.getDeviceNo())) {
                entity.setOutTerminalNo(result.getDeviceNo());
            }
            entity.setErrorMsg(null);
        } else {
            entity.setStatus(ChannelTerminalStatusEnum.ERROR.getCode());
            entity.setErrorMsg(StrUtil.sub(result.getErrorMsg(), 0, 255));
        }
        channelTerminalManager.updateById(entity);
    }

    /// 校验系统终端(存在性/同商户), 返回系统终端实体
    private TerminalDevice requireSystemTerminal(String systemTerminalNo, String mchNo) {
        TerminalDevice systemTerminal = terminalDeviceManager.findByTerminalNo(systemTerminalNo)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        if (!Objects.equals(systemTerminal.getMchNo(), mchNo)) {
            // 终端: 系统终端与通道终端须属于同一商户
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.mchMismatch");
        }
        return systemTerminal;
    }
}
