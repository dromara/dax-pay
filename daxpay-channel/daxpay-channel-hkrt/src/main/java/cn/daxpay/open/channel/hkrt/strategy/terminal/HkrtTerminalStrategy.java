package cn.daxpay.open.channel.hkrt.strategy.terminal;

import cn.daxpay.open.channel.hkrt.client.HkrtChannelClient;
import cn.daxpay.open.channel.hkrt.client.req.HkrtTerminalOperateReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtTerminalOperateResp;
import cn.daxpay.open.channel.hkrt.service.isv.HkrtIsvConfigAssembler;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.device.terminal.bo.TerminalExtraField;
import cn.daxpay.open.payment.device.terminal.bo.TerminalOperateResult;
import cn.daxpay.open.payment.device.terminal.entity.DeviceChannelTerminal;
import cn.daxpay.open.payment.device.terminal.strategy.AbsChannelTerminalStrategy;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/// # 海科融通终端报备策略
///
/// 编排壳: 组装凭证与差异参数后经 [HkrtChannelClient] 转发子应用执行
/// (绑定 new-bind / 解绑 new-unbound / 查询 new-query), 语义移植自商业版 3.x。
/// 报备以系统终端号为 SN, 成功回传海科 pn 作通道侧终端号。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtTerminalStrategy extends AbsChannelTerminalStrategy {

    private final HkrtChannelClient hkrtChannelClient;
    private final HkrtIsvConfigAssembler hkrtIsvConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.HKRT_PAY;
    }

    /// 海科报备扩展参数(运营端结构化表单展示, 存储于通道终端 extra JSON)
    @Override
    public List<TerminalExtraField> getExtraFields() {
        return List.of(
                TerminalExtraField.input("storeId", "门店编号", "对应海科 storeId(可空)"),
                TerminalExtraField.input("code", "终端码", "对应海科 code(可空)"),
                TerminalExtraField.input("address", "安装地址", "对应海科 terminal_address(可空)"));
    }

    /// 报备: 终端绑定, 成功回写状态 submit 与回传 pn
    @Override
    public TerminalOperateResult submit(DeviceChannelTerminal terminal, String systemTerminalNo) {
        DaxResult<HkrtTerminalOperateResp> result = hkrtChannelClient.terminalRegister(this.buildReq(terminal, systemTerminalNo));
        if (result.getCode() != 0) {
            return TerminalOperateResult.fail(result.getMsg());
        }
        return TerminalOperateResult.success(
                ChannelTerminalStatusEnum.SUBMIT.getCode(), result.getData().getDeviceNo());
    }

    /// 注销: 终端解绑, 成功状态 logged
    @Override
    public TerminalOperateResult cancel(DeviceChannelTerminal terminal, String systemTerminalNo) {
        DaxResult<Void> result = hkrtChannelClient.terminalCancel(this.buildReq(terminal, systemTerminalNo));
        if (result.getCode() != 0) {
            return TerminalOperateResult.fail(result.getMsg());
        }
        return TerminalOperateResult.success(ChannelTerminalStatusEnum.LOGGED.getCode(), null);
    }

    /// 同步: 绑定查询, 回传当前 pn
    @Override
    public TerminalOperateResult sync(DeviceChannelTerminal terminal, String systemTerminalNo) {
        DaxResult<HkrtTerminalOperateResp> result = hkrtChannelClient.terminalSync(this.buildReq(terminal, systemTerminalNo));
        if (result.getCode() != 0) {
            return TerminalOperateResult.fail(result.getMsg());
        }
        return TerminalOperateResult.success(
                ChannelTerminalStatusEnum.SUBMIT.getCode(), result.getData().getDeviceNo());
    }

    /// 海科实现了终端解绑(new-unbound), 支持注销
    @Override
    public boolean supportsCancel() {
        return true;
    }

    /// 海科实现了终端绑定查询(new-query), 支持同步
    @Override
    public boolean supportsSync() {
        return true;
    }

    /// 组装转发请求: 服务商凭证 + 系统终端号(SN) + extra 差异参数
    private HkrtTerminalOperateReq buildReq(DeviceChannelTerminal terminal, String systemTerminalNo) {
        return new HkrtTerminalOperateReq()
                .setCredential(hkrtIsvConfigAssembler.buildConfig(
                        terminal.getMchNo(), terminal.getChannelMchNo(), null))
                .setSystemTerminalNo(systemTerminalNo)
                .setExtra(this.parseExtra(terminal.getExtra()));
    }

    /// extra JSON → 参数 Map(空安全)
    private Map<String, Object> parseExtra(String extra) {
        if (extra == null || extra.isBlank()) {
            return Map.of();
        }
        return JacksonUtil.toBean(extra, Map.class);
    }
}
