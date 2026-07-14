package cn.daxpay.open.payment.unipay.param.trade.pay;

import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.List;

/// # 普通支付下单参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付参数")
public class NormalPayParam extends MerchantPaymentCommonParam {

    // ===== 业务核心 =====

    /// 商户订单号
    @Schema(description = "商户订单号")
    @NotBlank(message = "{validation.field.bizOrderNo.notBlank}")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    /// 支付标题
    @Schema(description = "支付标题")
    @NotBlank(message = "{validation.field.title.notBlank}")
    @Size(max = 100, message = "{validation.field.title.size}")
    private String title;

    /// 支付描述
    @Schema(description = "支付描述")
    @Size(max = 50, message = "{validation.field.description.size}")
    private String description;

    /// 支付金额（分，最小货币单位）
    @Schema(description = "支付金额(分)")
    @NotNull(message = "{validation.field.amount.notNull}")
    @Min(value = 1, message = "{validation.field.amount.min}")
    @Max(value = 9999999999L, message = "{validation.field.amount.max}")
    private Long amount;

    // ===== 支付路由 =====

    /// 支付产品编码，为空时由通道路由引擎自动选择
    /// @see ProductEnum
    @Schema(description = "支付产品编码")
    @Size(max = 32, message = "{validation.field.product.size}")
    private String product;

    /// 支付方式编码
    /// - 路由模式: 一般必填; 被扫场景可空, 有 authCode 时由平台按前缀识别回填分钱包 method
    /// - 直接指定(已传 channelMchNo+capability): 可空, 由 PayRouteService 按能力反推回填
    /// - 聚合扫码: 传 `aggregate_pay_qrcode` 走通道原生通扫码
    /// @see PayMethodEnum
    @Schema(description = "支付方式编码(跟随通道路由时一般必填; 被扫可仅传authCode; 直接指定可空)")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String method;

    /// 支付能力编码：跟随通道路由时由路由引擎回填；直接指定(已传 channelMchNo)时作为输入参与校验
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @Schema(description = "支付能力编码")
    @Size(max = 32, message = "{validation.field.capability.size}")
    private String capability;

    // ===== 通道专属参数（策略层校验）=====

    /// 用户标识 OpenId（微信 jsapi/mini 场景必填）
    @Size(max = 128, message = "{validation.field.openId.size}")
    @Schema(description = "用户标识OpenId")
    private String openId;

    /// 通道应用 AppId（微信 wxAppId 等）；非空则强制使用(须预配)，空则按 capability 路由解析
    @Size(max = 128, message = "{validation.field.channelAppId.size}")
    @Schema(description = "通道应用AppId")
    private String channelAppId;

    /// 付款码（被扫支付必填）
    @Size(max = 128, message = "{validation.field.authCode.size}")
    @Schema(description = "付款码")
    private String authCode;

    /// 限制支付类型列表，如限制信用卡
    /// @see cn.daxpay.open.platform.core.enums.unipay.PayLimitPayEnum
    @Schema(description = "限制支付类型")
    @Size(max = 10, message = "{validation.field.limitPay.size}")
    private List<String> limitPay;

    /// 支付扩展参数（JSON 格式，通道特有的长尾参数）
    @Schema(description = "支付扩展参数")
    @Size(max = 2048, message = "{validation.field.extraParam.size}")
    private String extraParam;

    /// 订单商品明细列表
    /// 传给支付渠道（支付宝 goods_detail / 微信 detail.goods_detail），
    /// 用于单品营销、电子发票等场景
    @Valid
    @Size(max = 50, message = "{validation.field.goodsDetail.size}")
    @Schema(description = "订单商品明细列表")
    private List<GoodsDetail> goodsDetail;

    // ===== 通知回调 =====

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    private String notifyUrl;

    /// 同步跳转地址，支付完毕后用户浏览器返回到该地址
    @Schema(description = "同步通知URL")
    @Size(max = 200, message = "{validation.field.returnUrl.size}")
    private String returnUrl;

    /// 商户扩展参数，回调时会原样返回
    @Schema(description = "商户扩展参数")
    @Size(max = 500, message = "{validation.field.attach.size}")
    private String attach;

    // ===== 时间 =====

    /// 过期时间（北京时间，格式 yyyy-MM-dd HH:mm:ss，空则默认 30 分钟）
    /// 注: 实际反序列化由全局 DaxpayOffsetDateTimeDeserializer 处理(按 Asia/Shanghai 解析后转 UTC),
    /// 此处不标 @JsonFormat 以免误导(注解会被全局反序列化器覆盖)
    @Schema(description = "过期时间(北京时间，yyyy-MM-dd HH:mm:ss)")
    private OffsetDateTime expiredTime;

    // ===== 终端信息（线下场景选填）=====

    /// 终端信息（线下 POS/收银台场景）
    @Valid
    @Schema(description = "终端信息")
    private TerminalInfo terminal;

}
