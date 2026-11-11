package cn.daxpay.open.payment.admin.service.device;

import cn.daxpay.open.payment.admin.param.device.ChannelTerminalParam;
import cn.daxpay.open.payment.admin.param.device.ChannelTerminalRegisterParam;
import cn.daxpay.open.payment.admin.param.device.ChannelTerminalReportParam;
import cn.daxpay.open.payment.admin.param.device.TerminalChannelBindParam;
import cn.daxpay.open.payment.device.terminal.bo.TerminalExtraField;
import cn.daxpay.open.payment.device.terminal.dao.ChannelTerminalManager;
import cn.daxpay.open.payment.device.terminal.entity.DeviceChannelTerminal;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalBindParam;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalQuery;
import cn.daxpay.open.payment.device.terminal.param.ChannelTerminalSaveParam;
import cn.daxpay.open.payment.device.terminal.result.ChannelTerminalResult;
import cn.daxpay.open.payment.device.terminal.result.TerminalDeviceResult;
import cn.daxpay.open.payment.device.terminal.service.ChannelTerminalManageSupport;
import cn.daxpay.open.payment.device.terminal.service.ChannelTerminalQuerySupport;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/// # 通道终端台账管理(运营端)
///
/// 运营端入口薄委托: 业务编排统一在多端共用的 [ChannelTerminalManageSupport],
/// 本类只负责运营端参数的转参与查询透传, 对外方法签名保持不变。
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelTerminalAdminService {

    private final ChannelTerminalManageSupport channelTerminalManageSupport;
    private final ChannelTerminalQuerySupport channelTerminalQuerySupport;
    private final ChannelTerminalManager channelTerminalManager;

    /// 新增通道终端台账(建档即绑定商户终端)
    @Transactional(rollbackFor = Exception.class)
    public void add(ChannelTerminalParam param) {
        channelTerminalManageSupport.add(this.toSaveParam(param), param.getMchNo());
    }

    /// 修改通道终端(仅名称/备注/报备参数; 通道商户/类型/商户终端绑定与登记状态不在此维护)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChannelTerminalParam param) {
        channelTerminalManageSupport.update(this.toSaveParam(param));
    }

    /// 手动登记通道终端(手工订正通道侧终端号与登记状态; 不调通道)
    @Transactional(rollbackFor = Exception.class)
    public void register(ChannelTerminalRegisterParam param) {
        channelTerminalManageSupport.register(this.toCoreRegisterParam(param));
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
                .map(DeviceChannelTerminal::toResult)
                .collect(Collectors.toList());
    }

    /// 删除(级联解绑)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        channelTerminalManageSupport.delete(id);
    }

    /// 绑定商户终端(存量记录修复入口; 正常关联由新增建档建立)
    @Transactional(rollbackFor = Exception.class)
    public void bind(TerminalChannelBindParam param) {
        channelTerminalManageSupport.bind(this.toCoreBindParam(param));
    }

    /// 解绑商户终端
    @Transactional(rollbackFor = Exception.class)
    public void unbind(TerminalChannelBindParam param) {
        channelTerminalManageSupport.unbind(this.toCoreBindParam(param));
    }

    /// 查询通道终端已绑定的商户终端列表
    public List<TerminalDeviceResult> listBoundSystem(Long channelTerminalId) {
        return channelTerminalManageSupport.listBoundSystem(channelTerminalId);
    }

    /// 查询通道支持的终端报送类型(无报备策略的通道返回空列表)
    public List<String> terminalTypes(String product) {
        return channelTerminalManageSupport.terminalTypes(product);
    }

    /// 查询通道差异化报备参数字段定义(运营端结构化表单用; 无策略或未定义返回空)
    public List<TerminalExtraField> extraFields(String product) {
        return channelTerminalManageSupport.extraFields(product);
    }

    /// 终端报备(报备即绑定): 调通道 API 报备并同步建立关联
    @Transactional(rollbackFor = Exception.class)
    public void report(ChannelTerminalReportParam param) {
        channelTerminalManageSupport.report(this.toCoreReportParam(param));
    }

    /// 终端注销(自动通道调通道 API; 人工通道直接置已注销)
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        channelTerminalManageSupport.cancel(id);
    }

    /// 终端状态同步(调通道 API 回读实际状态与通道侧终端号)
    @Transactional(rollbackFor = Exception.class)
    public void sync(Long id) {
        channelTerminalManageSupport.sync(id);
    }

    /// 运营端建档/修改参数 → 内核无端身份参数
    private ChannelTerminalSaveParam toSaveParam(ChannelTerminalParam param) {
        return new ChannelTerminalSaveParam()
                .setId(param.getId())
                .setChannelMchNo(param.getChannelMchNo())
                .setType(param.getType())
                .setSystemTerminalNo(param.getSystemTerminalNo())
                .setName(param.getName())
                .setExtra(param.getExtra())
                .setRemark(param.getRemark());
    }

    /// 运营端手动登记参数 → 内核无端身份参数(与运营端参数同名类, 需全限定名区分)
    private cn.daxpay.open.payment.device.terminal.param.ChannelTerminalRegisterParam toCoreRegisterParam(
            ChannelTerminalRegisterParam param) {
        return new cn.daxpay.open.payment.device.terminal.param.ChannelTerminalRegisterParam()
                .setId(param.getId())
                .setOutTerminalNo(param.getOutTerminalNo())
                .setStatus(param.getStatus());
    }

    /// 运营端报备参数 → 内核无端身份参数(与运营端参数同名类, 需全限定名区分)
    private cn.daxpay.open.payment.device.terminal.param.ChannelTerminalReportParam toCoreReportParam(
            ChannelTerminalReportParam param) {
        return new cn.daxpay.open.payment.device.terminal.param.ChannelTerminalReportParam()
                .setId(param.getId())
                .setSystemTerminalNo(param.getSystemTerminalNo());
    }

    /// 运营端绑定参数 → 内核无端身份参数
    private ChannelTerminalBindParam toCoreBindParam(TerminalChannelBindParam param) {
        return new ChannelTerminalBindParam()
                .setSystemTerminalNo(param.getSystemTerminalNo())
                .setChannelTerminalId(param.getChannelTerminalId());
    }
}
