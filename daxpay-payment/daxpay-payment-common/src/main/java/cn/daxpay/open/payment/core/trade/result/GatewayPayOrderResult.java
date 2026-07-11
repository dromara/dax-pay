package cn.daxpay.open.payment.core.trade.result;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 网关支付业务单(管理)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "网关支付业务单")
public class GatewayPayOrderResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "平台网关单号")
    private String orderNo;

    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    @Schema(description = "网关类型")
    private String gatewayType;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "业务状态")
    private String status;

    @Schema(description = "金额(分)")
    private Long amount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "过期时间")
    private OffsetDateTime expiredTime;

    @Schema(description = "支付通道")
    private String channel;

    @Schema(description = "支付方式")
    private String method;

    @Schema(description = "支付产品")
    private String product;

    @Schema(description = "收银场景")
    private String scene;

    @Schema(description = "设备")
    private String device;

    @Schema(description = "支付成功时间")
    private OffsetDateTime payTime;

    @Schema(description = "关闭时间")
    private OffsetDateTime closeTime;

    @Schema(description = "异步通知地址")
    private String notifyUrl;

    @Schema(description = "同步跳转地址")
    private String returnUrl;

    @Schema(description = "商户附加参数")
    private String attach;

    // ===== Trade 联表 =====
    @Schema(description = "资金交易号")
    private String tradeNo;

    @Schema(description = "通道订单号")
    private String outOrderNo;

    @Schema(description = "资金状态")
    private String fundStatus;

    @Schema(description = "可退金额")
    private Long refundableBalance;

    @Schema(description = "错误信息")
    private String errorMsg;
}
