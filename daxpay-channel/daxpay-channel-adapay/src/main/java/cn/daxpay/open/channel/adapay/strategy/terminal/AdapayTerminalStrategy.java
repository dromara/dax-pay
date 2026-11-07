package cn.daxpay.open.channel.adapay.strategy.terminal;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.req.AdapayTerminalOperateReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayTerminalOperateResp;
import cn.daxpay.open.channel.adapay.service.direct.AdapayDirectConfigAssembler;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.device.terminal.bo.TerminalExtraField;
import cn.daxpay.open.payment.device.terminal.bo.TerminalOperateResult;
import cn.daxpay.open.payment.device.terminal.entity.ChannelTerminal;
import cn.daxpay.open.payment.device.terminal.strategy.AbsChannelTerminalStrategy;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelTerminalStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/// # Adapay 终端报备策略
///
/// 编排壳: 组装凭证与业务参数后经 [AdapayChannelClient] 转发子应用执行
/// (device.add 报备 / device.cancel 注销), 语义移植自商业版 3.x `AdapayTerminalService`。
/// 业务字段(fee_type/device_type/serial_num/省市区/地址等)由通道终端 extra 按汇付参数名直通。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayTerminalStrategy extends AbsChannelTerminalStrategy {

    private final AdapayChannelClient adapayChannelClient;
    private final AdapayDirectConfigAssembler adapayDirectConfigAssembler;

    @Override
    public ProductEnum getProduct() {
        return ProductEnum.ADA_PAY;
    }

    /// 汇付报备扩展参数(device.add 业务字段, 运营端结构化表单展示, 存储于通道终端 extra JSON)
    @Override
    public List<TerminalExtraField> getExtraFields() {
        return List.of(
                TerminalExtraField.input("fee_type", "费率类型", "汇付 fee_type"),
                TerminalExtraField.input("device_type", "设备类型", "汇付 device_type"),
                TerminalExtraField.input("serial_num", "设备序列号", "汇付 serial_num"),
                TerminalExtraField.input("province_code", "省编码", "汇付 province_code"),
                TerminalExtraField.input("city_code", "市编码", "汇付 city_code"),
                TerminalExtraField.input("district_code", "区编码", "汇付 district_code"),
                TerminalExtraField.textarea("device_address", "设备地址", "汇付 device_address"),
                TerminalExtraField.input("device_company_name", "设备公司名称", "汇付 device_company_name"),
                TerminalExtraField.input("device_put_date", "投放日期", "yyyyMMdd"),
                TerminalExtraField.input("device_gps_flag", "GPS标识", "01=有定位 02=无定位"),
                TerminalExtraField.input("device_machines_no", "机型", "汇付 device_machines_no"),
                TerminalExtraField.input("network_license", "网络许可证", "汇付 network_license"),
                TerminalExtraField.input("device_ip", "设备IP", "汇付 device_ip"),
                TerminalExtraField.input("longitude", "经度", "与纬度同时填写"),
                TerminalExtraField.input("latitude", "纬度", "与经度同时填写"));
    }

    /// 报备: 设备注册 device.add, 成功回写状态 submit 与回传 device_id
    @Override
    public TerminalOperateResult submit(ChannelTerminal terminal, String systemTerminalNo) {
        DaxResult<AdapayTerminalOperateResp> result = adapayChannelClient.terminalRegister(this.buildReq(terminal));
        if (result.getCode() != 0) {
            return TerminalOperateResult.fail(result.getMsg());
        }
        return TerminalOperateResult.success(
                ChannelTerminalStatusEnum.SUBMIT.getCode(), result.getData().getDeviceNo());
    }

    /// 注销: 设备注销 device.cancel, 成功状态 logged
    @Override
    public TerminalOperateResult cancel(ChannelTerminal terminal, String systemTerminalNo) {
        DaxResult<Void> result = adapayChannelClient.terminalCancel(this.buildReq(terminal));
        if (result.getCode() != 0) {
            return TerminalOperateResult.fail(result.getMsg());
        }
        return TerminalOperateResult.success(ChannelTerminalStatusEnum.LOGGED.getCode(), null);
    }

    /// 组装转发请求: 直连凭证 + 报送类型(pay_way 映射) + extra 业务字段
    private AdapayTerminalOperateReq buildReq(ChannelTerminal terminal) {
        return new AdapayTerminalOperateReq()
                .setCredential(adapayDirectConfigAssembler.buildConfig(
                        terminal.getMchNo(), terminal.getChannelMchNo(), null))
                .setType(terminal.getType())
                .setOutTerminalNo(terminal.getOutTerminalNo())
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
