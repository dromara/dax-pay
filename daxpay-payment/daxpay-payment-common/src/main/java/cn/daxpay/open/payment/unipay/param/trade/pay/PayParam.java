package cn.daxpay.open.payment.unipay.param.trade.pay;

import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import jakarta.validation.Valid;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/// # 统一下单参数（国内普通支付）
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付参数")
public class PayParam extends MerchantPaymentCommonParam {

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

    /// 支付金额（元）
    @Schema(description = "支付金额")
    @NotNull(message = "{validation.field.amount.notNull}")
    @DecimalMin(value = "0.01", message = "{validation.field.amount.decimalMin}")
    @Digits(integer = 8, fraction = 2, message = "{validation.field.amount.digits}")
    private BigDecimal amount;

    // ===== 支付路由 =====

    /// 支付产品编码，为空时由通道路由引擎自动选择
    /// @see ProductEnum
    @Schema(description = "支付产品编码")
    @Size(max = 32, message = "{validation.field.product.size}")
    private String product;

    /// 支付方式编码
    /// @see PayMethodEnum
    @Schema(description = "支付方式编码")
    @NotBlank(message = "{validation.field.method.notBlank}")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String method;

    // ===== 通道专属参数（策略层校验）=====

    /// 用户标识 OpenId（微信 jsapi/mini 场景必填）
    @Size(max = 128, message = "{validation.field.openId.size}")
    @Schema(description = "用户标识OpenId")
    private String openId;

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
    @Schema(description = "过期时间(北京时间，yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime expiredTime;

    // ===== 终端信息（线下场景选填）=====

    /// 终端信息（线下 POS/收银台场景）
    @Valid
    @Schema(description = "终端信息")
    private TerminalInfo terminal;

}
