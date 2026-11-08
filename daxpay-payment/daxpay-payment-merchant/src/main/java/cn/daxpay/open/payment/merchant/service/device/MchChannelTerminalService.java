package cn.daxpay.open.payment.merchant.service.device;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.device.terminal.bo.TerminalExtraField;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.dao.TerminalDeviceManager;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.entity.TerminalDevice;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalBindParam;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalSaveParam;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalCapabilityResult;
import cn.daxpay.open.payment.device.terminal.service.ChannelTerminalManageSupport;
import cn.daxpay.open.payment.device.terminal.service.ChannelTerminalQuerySupport;
import cn.daxpay.open.payment.device.terminal.service.TerminalBindSupport;
import cn.daxpay.open.payment.merchant.param.device.MchChannelTerminalAddParam;
import cn.daxpay.open.payment.merchant.param.device.MchChannelTerminalRegisterParam;
import cn.daxpay.open.payment.merchant.param.device.MchChannelTerminalReportParam;
import cn.daxpay.open.payment.merchant.param.device.MchChannelTerminalUpdateParam;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
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

/// # 通道终端台账管理（商户端）
///
/// 商户可维护自己名下的通道终端(建档/报备/手动调整/注销/同步/删除/解绑),
/// 业务编排统一委托内核 [ChannelTerminalManageSupport]。
///
/// 商户隔离(CRITICAL): 商户号强制取自 [PaymentContext]; 每个写方法先做归属校验,
/// 不存在与不属于当前商户统一按"不存在"处理, 避免拿他人主键探测数据是否存在。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchChannelTerminalService {

    private final ChannelTerminalManageSupport channelTerminalManageSupport;
    private final ChannelTerminalQuerySupport channelTerminalQuerySupport;
    private final ChannelTerminalManager channelTerminalManager;
    private final TerminalDeviceManager terminalDeviceManager;
    private final TerminalBindSupport terminalBindSupport;
    private final PaymentContext paymentContext;

    /// 当前登录商户号(上下文未装载直接报错, 不做降级)
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (StrUtil.isBlank(mchNo)) {
            // 终端: 商户上下文未装载, 无法进行数据隔离操作
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验通道终端归属当前商户; 不存在与不属于当前商户统一报"不存在"
    private ChannelTerminal requireOwnedChannelTerminal(Long id) {
        ChannelTerminal entity = channelTerminalManager.findById(id)
                // 终端: 通道终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.channelNotFound"));
        if (!Objects.equals(entity.getMchNo(), requireMchNo())) {
            // 越权探测: 他人终端统一按不存在处理
            throw new DataNotExistException("error.device.terminal.channelNotFound");
        }
        return entity;
    }

    /// 校验系统终端归属当前商户; 不存在与不属于当前商户统一报"不存在"
    private TerminalDevice requireOwnedSystemTerminal(String terminalNo) {
        TerminalDevice entity = terminalDeviceManager.findByTerminalNo(terminalNo)
                // 终端: 系统终端不存在
                .orElseThrow(() -> new DataNotExistException("error.device.terminal.systemNotFound"));
        if (!Objects.equals(entity.getMchNo(), requireMchNo())) {
            // 越权探测: 他人终端统一按不存在处理
            throw new DataNotExistException("error.device.terminal.systemNotFound");
        }
        return entity;
    }

    /// 分页查询(查询与系统终端回填复用内核共用逻辑, 此处只负责商户号隔离)
    public PageResult<ChannelTerminalResult> page(PageParam pageParam, ChannelTerminalQuery query) {
        // 商户端强制覆盖商户号, 防越权查询其它商户的通道终端
        query.setMchNo(requireMchNo());
        return channelTerminalQuerySupport.page(pageParam, query);
    }

    /// 详情(先校验归属, 再走共用查询与翻译)
    public ChannelTerminalResult get(Long id) {
        this.requireOwnedChannelTerminal(id);
        return channelTerminalQuerySupport.findById(id);
    }

    /// 新增通道终端台账(建档即绑定系统终端)
    @Transactional(rollbackFor = Exception.class)
    public void add(MchChannelTerminalAddParam param) {
        ChannelTerminalSaveParam saveParam = new ChannelTerminalSaveParam()
                .setChannelMchNo(param.getChannelMchNo())
                .setType(param.getType())
                .setSystemTerminalNo(param.getSystemTerminalNo())
                .setName(param.getName())
                .setExtra(param.getExtra())
                .setRemark(param.getRemark());
        // 商户号取上下文, 通道商户与系统终端归属由内核校验
        channelTerminalManageSupport.add(saveParam, requireMchNo());
    }

    /// 修改通道终端(名称/备注/报备参数)
    @Transactional(rollbackFor = Exception.class)
    public void update(MchChannelTerminalUpdateParam param) {
        this.requireOwnedChannelTerminal(param.getId());
        ChannelTerminalSaveParam saveParam = new ChannelTerminalSaveParam()
                .setId(param.getId())
                .setName(param.getName())
                .setExtra(param.getExtra())
                .setRemark(param.getRemark());
        channelTerminalManageSupport.update(saveParam);
    }

    /// 手动调整通道终端(通道终端号/登记状态; 不调通道)
    @Transactional(rollbackFor = Exception.class)
    public void register(MchChannelTerminalRegisterParam param) {
        this.requireOwnedChannelTerminal(param.getId());
        channelTerminalManageSupport.register(
                new cn.daxpay.open.payment.device.terminal.param.ChannelTerminalRegisterParam()
                        .setId(param.getId())
                        .setOutTerminalNo(param.getOutTerminalNo())
                        .setStatus(param.getStatus()));
    }

    /// 删除通道终端(级联解绑)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.requireOwnedChannelTerminal(id);
        channelTerminalManageSupport.delete(id);
    }

    /// 终端报备(报备即绑定; 入参带系统终端时先校验该系统终端归属)
    @Transactional(rollbackFor = Exception.class)
    public void report(MchChannelTerminalReportParam param) {
        this.requireOwnedChannelTerminal(param.getId());
        if (StrUtil.isNotBlank(param.getSystemTerminalNo())) {
            // 防用他人系统终端报备
            this.requireOwnedSystemTerminal(param.getSystemTerminalNo());
        }
        channelTerminalManageSupport.report(
                new cn.daxpay.open.payment.device.terminal.param.ChannelTerminalReportParam()
                        .setId(param.getId())
                        .setSystemTerminalNo(param.getSystemTerminalNo()));
    }

    /// 终端状态同步(调通道 API 回读实际状态与通道侧终端号)
    @Transactional(rollbackFor = Exception.class)
    public void sync(Long id) {
        this.requireOwnedChannelTerminal(id);
        channelTerminalManageSupport.sync(id);
    }

    /// 终端注销(自动通道调通道 API; 人工通道直接置已注销)
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        this.requireOwnedChannelTerminal(id);
        channelTerminalManageSupport.cancel(id);
    }

    /// 解绑当前绑定的系统终端(只传通道终端主键: 绑定关系由后端取, 商户无法伪造终端号)
    @Transactional(rollbackFor = Exception.class)
    public void unbind(Long id) {
        this.requireOwnedChannelTerminal(id);
        String systemTerminalNo = terminalBindSupport.findOwner(id);
        if (StrUtil.isBlank(systemTerminalNo)) {
            // 终端: 绑定关系不存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.device.terminal.bindNotFound");
        }
        // 防伪造绑定: 系统终端同样须归属当前商户
        this.requireOwnedSystemTerminal(systemTerminalNo);
        channelTerminalManageSupport.unbind(new ChannelTerminalBindParam()
                .setSystemTerminalNo(systemTerminalNo)
                .setChannelTerminalId(id));
    }

    /// 查询通道支持的终端报送类型(无报备策略的通道返回空列表)
    public List<String> terminalTypes(String product) {
        return channelTerminalManageSupport.terminalTypes(product);
    }

    /// 查询通道差异化报备参数字段定义(商户端结构化表单用; 无策略或未定义返回空)
    public List<TerminalExtraField> extraFields(String product) {
        return channelTerminalManageSupport.extraFields(product);
    }

    /// 查询通道终端能力(报备/注销/同步, 供商户端按能力置灰按钮)
    public TerminalCapabilityResult capabilities(String product) {
        return channelTerminalManageSupport.capabilities(product);
    }
}
