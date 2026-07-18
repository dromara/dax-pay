package cn.daxpay.open.payment.admin.service.device;

import cn.daxpay.open.payment.admin.param.device.ChannelTerminalParam;
import cn.daxpay.open.payment.admin.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.device.enums.TerminalTypeEnum;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalChannelBindManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.TerminalChannelBind;
import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.masterdata.dao.product.PayProductManager;
import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
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
/// 挂在通道商户下人工登记通道侧终端号与状态; 不调用通道报备 API。
/// 与系统终端多对多绑定。
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

    /// 新增通道终端台账
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
        String status = StrUtil.isBlank(param.getStatus())
                ? ChannelTerminalStatusEnum.INIT.getCode()
                : ChannelTerminalStatusEnum.findByCode(param.getStatus()).getCode();

        ChannelTerminal entity = new ChannelTerminal();
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        entity.setMchNo(param.getMchNo());
        entity.setChannelMchNo(channelMerchant.getChannelMchNo());
        entity.setProduct(product);
        entity.setChannel(payProduct.getChannel());
        entity.setType(typeEnum.getCode());
        entity.setName(param.getName());
        entity.setOutTerminalNo(StrUtil.blankToDefault(param.getOutTerminalNo(), null));
        entity.setStatus(status);
        entity.setErrorMsg(param.getErrorMsg());
        entity.setRemark(param.getRemark());
        channelTerminalManager.save(entity);
    }

    /// 修改通道终端(名称/通道号/状态/备注; 不改通道商户与 type)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChannelTerminalParam param) {
        ChannelTerminal entity = channelTerminalManager.findById(param.getId())
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        entity.setName(param.getName());
        entity.setOutTerminalNo(StrUtil.blankToDefault(param.getOutTerminalNo(), null));
        if (StrUtil.isNotBlank(param.getStatus())) {
            entity.setStatus(ChannelTerminalStatusEnum.findByCode(param.getStatus()).getCode());
        }
        entity.setErrorMsg(param.getErrorMsg());
        entity.setRemark(param.getRemark());
        channelTerminalManager.updateById(entity);
    }

    /// 分页
    public PageResult<ChannelTerminalResult> page(PageParam pageParam, ChannelTerminalQuery query) {
        PageResult<ChannelTerminalResult> pageResult = MpUtil.toPageResult(channelTerminalManager.page(pageParam, query));
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情
    public ChannelTerminalResult findById(Long id) {
        ChannelTerminalResult result = channelTerminalManager.findById(id)
                .map(ChannelTerminal::toResult)
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        transService.translate(result);
        return result;
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

    /// 绑定系统终端
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
        TerminalChannelBind bind = new TerminalChannelBind();
        // 运营端写 MchBaseEntity 必须显式 setMchNo
        bind.setMchNo(channel.getMchNo());
        bind.setSystemTerminalNo(param.getSystemTerminalNo());
        bind.setChannelTerminalId(param.getChannelTerminalId());
        terminalChannelBindManager.save(bind);
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
}
