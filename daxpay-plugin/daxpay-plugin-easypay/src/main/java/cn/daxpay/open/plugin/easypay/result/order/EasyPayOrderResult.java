package cn.daxpay.open.plugin.easypay.result.order;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.annotation.Trans;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 易支付协议订单结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "易支付订单结果")
public class EasyPayOrderResult extends MchBaseResult {

    /// 商户名称(由 mchNo 翻译, 走系统 @Trans 机制)
    @Trans(
            entity = MerchantInfo.class,
            source = MchBaseResult.Fields.mchNo,
            result = MerchantInfo.Fields.mchName)
    @Schema(description = "商户名称")
    private String mchName;

    /// 关联内核容器 ID（NormalPayOrder.id）
    @Schema(description = "关联内核容器ID")
    private Long orderId;

    /// 易支付商户号
    @Schema(description = "易支付商户号")
    private Integer pid;

    /// 应用号
    @Schema(description = "应用号")
    private String appId;

    /// 平台业务单号（对外 trade_no）
    @Schema(description = "平台业务单号")
    private String tradeNo;

    /// 商户订单号
    @Schema(description = "商户订单号")
    private String outTradeNo;

    /// 通道订单号
    @Schema(description = "通道订单号")
    private String apiTradeNo;

    /// 协议支付方式 alipay/wxpay/aggregate
    @Schema(description = "支付方式")
    private String type;

    /// 协议状态 0待付 1成功
    @Schema(description = "协议状态 0待付 1成功")
    private Integer status;

    /// 创建时间
    @Schema(description = "创建时间")
    private OffsetDateTime addTime;

    /// 完成时间
    @Schema(description = "完成时间")
    private OffsetDateTime endTime;

    /// 商品名称
    @Schema(description = "商品名称")
    private String name;

    /// 金额（元）
    @Schema(description = "金额（元）")
    private BigDecimal money;

    /// 已退款金额（元）
    @Schema(description = "已退款金额（元）")
    private BigDecimal refundMoney;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    private String notifyUrl;

    /// 同步跳转地址
    @Schema(description = "同步跳转地址")
    private String returnUrl;

    /// 业务扩展参数
    @Schema(description = "业务扩展参数")
    private String param;

    /// 支付用户标识
    @Schema(description = "支付用户标识")
    private String buyer;

    /// 客户端 IP
    @Schema(description = "客户端IP")
    private String clientIp;

    /// API 版本 v1/v2
    @Schema(description = "API版本")
    private String apiVersion;

    /// PC 调用方式
    @Schema(description = "PC调用方式")
    private String pcCallType;

    /// 支付链接
    @Schema(description = "支付链接")
    private String payUrl;

    /// 支付参数体
    @Schema(description = "支付参数体")
    private String payBody;
}
