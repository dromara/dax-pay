package cn.daxpay.open.payment.unipay.param.trade.pay;

import cn.daxpay.open.payment.device.terminal.service.TerminalResolveService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 终端信息
///
/// 线下支付场景（POS/收银台/自助终端）的设备信息
@Data
@Schema(title = "终端信息")
public class TerminalInfo {

    /// 终端设备号
    @Size(max = 64, message = "{validation.field.terminalNo.size}")
    @Schema(description = "终端设备号")
    private String terminalNo;

    /// 门店编号
    @Size(max = 64, message = "{validation.field.storeNo.size}")
    @Schema(description = "门店编号")
    private String storeNo;

    /// 操作员号
    @Size(max = 64, message = "{validation.field.operatorId.size}")
    @Schema(description = "操作员号")
    private String operatorId;

    /// 设备名称
    @Size(max = 128, message = "{validation.field.deviceName.size}")
    @Schema(description = "设备名称")
    private String deviceName;

    /// 设备 IP 地址
    @Size(max = 64, message = "{validation.field.deviceIp.size}")
    @Schema(description = "设备IP地址")
    private String deviceIp;

    /// 经度
    @Schema(description = "经度")
    private Double longitude;

    /// 纬度
    @Schema(description = "纬度")
    private Double latitude;

    /// 解析出的通道侧终端号, 服务端回填, 客户端传入无效
    ///
    /// [TerminalResolveService] 在通道路由落定后按绑定关系解析回填,
    /// 供建单落库到订单 outTerminalNo; 不传终端号场景下服务端不覆写,
    /// 公网预置值可绕过终端绑定直上通道, 故隐藏并不接受反序列化。
    @Schema(hidden = true)
    @JsonIgnore
    private String resolvedOutTerminalNo;
}
