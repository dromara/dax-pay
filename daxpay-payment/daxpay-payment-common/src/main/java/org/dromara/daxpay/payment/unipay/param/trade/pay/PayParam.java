package org.dromara.daxpay.payment.unipay.param.trade.pay;

import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.enums.unipay.PayLimitPayEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// # 统一下单参数
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付参数")
public class PayParam extends MerchantPaymentCommonParam {

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

    /// 是否开启分账
    @Schema(description = "是否开启分账")
    private Boolean allocation;

    /// 自动分账
    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /// 过期时间
    @Schema(description = "过期时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime expiredTime;

    /// 支付渠道（微信/支付宝/银联）, 路由未指定 product 时必填
    /// @see PayProviderEnum
    @Schema(description = "支付渠道")
    @Size(max = 32, message = "{validation.field.provider.size}")
    private String provider;

    /// 支付产品编码, 为空时由通道路由引擎自动选择
    /// @see ProductEnum
    @Schema(description = "支付产品编码")
    @Size(max = 32, message = "{validation.field.product.size}")
    private String product;

    /// 支付通道编码
    /// @see ChannelEnum#getCode()
    @Schema(description = "支付通道编码")
    @Size(max = 32, message = "{validation.field.channel.size}")
    private String channel;

    /// 支付方式编码, product为空时可由路由引擎回填
    /// @see PayMethodEnum
    @Schema(description = "支付方式编码")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String method;

    /// 其他支付方式, 只有在 支付方式编码(method) 为 其他支付(other)时才会生效
    /// 用于处理各种通道各自定义的支付方式
    @Size(max = 128, message = "{validation.field.otherMethod.size}")
    @Schema(description = "其他支付方式")
    private String otherMethod;

    /// 限制用户支付类型, 目前支持限制信用卡
    /// @see PayLimitPayEnum
    @Schema(description = "限制用户支付类型")
    @Size(max = 128, message = "{validation.field.limitPay.size}")
    private String limitPay;

    /// 支付金额
    @Schema(description = "支付金额")
    @NotNull(message = "{validation.field.amount.notNull}")
    @DecimalMin(value = "0.01", message = "{validation.field.amount.decimalMin}")
    @Digits(integer = 8, fraction = 2, message = "{validation.field.amount.digits}")
    private BigDecimal amount;

    /// opAppId
    @Size(max = 128, message = "{validation.field.opAppId.size}")
    @Schema(description = "opAppId")
    private String opAppId;

    /// 用户标识OpenId
    @Size(max = 128, message = "{validation.field.openId.size}")
    @Schema(description = "用户标识OpenId")
    private String openId;

    /// 付款码
    @Size(max = 128, message = "{validation.field.authCode.size}")
    @Schema(description = "付款码")
    private String authCode;

    /// 终端设备编码
    @Size(max = 128, message = "{validation.field.terminalNo.size}")
    @Schema(description = "终端设备编码")
    private String terminalNo;

    /// 支付扩展参数
    @Schema(description = "支付扩展参数")
    @Size(max = 2048, message = "{validation.field.extraParam.size}")
    private String extraParam;

    /// 商户扩展参数,回调时会原样返回
    @Schema(description = "商户扩展参数")
    @Size(max = 500, message = "{validation.field.attach.size}")
    private String attach;

    /// 订单来源
    @Schema(description = "订单来源", hidden = true)
    @Null(message = "{validation.field.orderSource.mustBeNull}")
    private String source;

    /// 同步跳转地址, 支付完毕后用户浏览器返回到该地址, 不传输跳转到默认地址
    @Schema(description = "同步通知URL")
    @Size(max = 200, message = "{validation.field.returnUrl.size}")
    private String returnUrl;

    /// 用户付款中途退出返回商户网站的地址(部分支付场景中可用)
    @Schema(description = "退出地址")
    @Size(max = 200, message = "{validation.field.quitUrl.size}")
    private String quitUrl;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    private String notifyUrl;
}
