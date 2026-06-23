package cn.daxpay.open.demo.channel.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/// # 通道连通性 Demo 支付结果
///
/// 展示主应用 → 子应用往返的完整信息, 包含双端 traceId 用于链路对照。
@Data
@Schema(description = "通道 Demo 支付结果")
public class ChannelDemoPayResult {

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /// 通道侧订单号
    @Schema(description = "通道侧订单号")
    private String outOrderNo;

    /// 支付内容(支付链接 / 二维码 / 表单等)
    @Schema(description = "支付内容")
    private String payBody;

    /// 支付内容类型(qr_code / form / order_id)
    @Schema(description = "支付内容类型")
    private String payBodyType;

    /// 主应用 traceId
    @Schema(description = "主应用 traceId")
    private String mainAppTraceId;

    /// 子应用 traceId
    @Schema(description = "子应用 traceId")
    private String subAppTraceId;
}
