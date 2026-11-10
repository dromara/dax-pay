package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.req.MockTerminalOperateReq;
import cn.daxpay.open.channel.mock.client.resp.MockTerminalOperateResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.device.terminal.bo.TerminalOperateResult;
import cn.daxpay.open.payment.device.terminal.entity.DeviceChannelTerminal;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/// # Mock 通道终端报备业务服务
///
/// 通过 [MockChannelClient] 调用 mock 子应用的终端报备端点, 并把 DaxResult 转为
/// 终端策略统一回执 [TerminalOperateResult](操作成功必须显式带状态, 避免编排层把台账状态置空):
/// - 报备/同步成功 → status=submit + 回传通道侧终端号;
/// - 注销成功 → status=logged;
/// - 任意业务失败 → [TerminalOperateResult#fail](错误文案透传)。
///
/// 凭证按产品二选一由 [MockCredentialSupport] 统一分发(直连空壳 / 服务商反解)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockTerminalService {

    private final MockChannelClient mockChannelClient;
    private final MockCredentialSupport mockCredentialSupport;

    /// 终端报备(mock 侧按三元组幂等, 重复报备返回原通道侧终端号)
    public TerminalOperateResult submit(DeviceChannelTerminal terminal, String systemTerminalNo) {
        DaxResult<MockTerminalOperateResp> result = mockChannelClient.terminalRegister(
                this.buildReq(terminal, systemTerminalNo));
        if (result.getCode() != 0) {
            log.warn("模拟通道终端报备失败: channelMchNo={}, systemTerminalNo={}, msg={}",
                    terminal.getChannelMchNo(), systemTerminalNo, result.getMsg());
            return TerminalOperateResult.fail(result.getMsg());
        }
        MockTerminalOperateResp resp = result.getData();
        log.info("模拟通道终端报备成功: channelMchNo={}, systemTerminalNo={}, deviceNo={}",
                terminal.getChannelMchNo(), systemTerminalNo, resp == null ? null : resp.getDeviceNo());
        return TerminalOperateResult.success(ChannelTerminalStatusEnum.SUBMIT.getCode(),
                resp == null ? null : resp.getDeviceNo());
    }

    /// 终端注销(mock 侧置 CANCELLED, 已注销重复注销幂等)
    public TerminalOperateResult cancel(DeviceChannelTerminal terminal, String systemTerminalNo) {
        DaxResult<Void> result = mockChannelClient.terminalCancel(this.buildReq(terminal, systemTerminalNo));
        if (result.getCode() != 0) {
            log.warn("模拟通道终端注销失败: channelMchNo={}, systemTerminalNo={}, msg={}",
                    terminal.getChannelMchNo(), systemTerminalNo, result.getMsg());
            return TerminalOperateResult.fail(result.getMsg());
        }
        return TerminalOperateResult.success(ChannelTerminalStatusEnum.LOGGED.getCode(), null);
    }

    /// 终端同步(mock 侧回读当前通道侧终端号与状态)
    public TerminalOperateResult sync(DeviceChannelTerminal terminal, String systemTerminalNo) {
        DaxResult<MockTerminalOperateResp> result = mockChannelClient.terminalSync(
                this.buildReq(terminal, systemTerminalNo));
        if (result.getCode() != 0) {
            log.warn("模拟通道终端同步失败: channelMchNo={}, systemTerminalNo={}, msg={}",
                    terminal.getChannelMchNo(), systemTerminalNo, result.getMsg());
            return TerminalOperateResult.fail(result.getMsg());
        }
        MockTerminalOperateResp resp = result.getData();
        return TerminalOperateResult.success(ChannelTerminalStatusEnum.SUBMIT.getCode(),
                resp == null ? null : resp.getDeviceNo());
    }

    /// 组装转发请求: 凭证按产品装配 + 台账归属/报送类型/系统终端号/终端号/扩展参数
    private MockTerminalOperateReq buildReq(DeviceChannelTerminal terminal, String systemTerminalNo) {
        return new MockTerminalOperateReq()
                .setCredential(mockCredentialSupport.build(terminal.getProduct(), terminal.getChannelMchNo()))
                .setChannelMchNo(terminal.getChannelMchNo())
                .setType(terminal.getType())
                .setSystemTerminalNo(systemTerminalNo)
                .setOutTerminalNo(terminal.getOutTerminalNo())
                .setExtra(this.parseExtra(terminal.getExtra()));
    }

    /// extra JSON → 参数 Map(空安全, 与 Adapay/HKRT 终端策略同构)
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseExtra(String extra) {
        if (extra == null || extra.isBlank()) {
            return Map.of();
        }
        return JacksonUtil.toBean(extra, Map.class);
    }
}
