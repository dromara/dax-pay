package org.dromara.daxpay.core.param.trade.pay;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.PaymentCommonParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 统一下单参数
 *
 * @author xxm
 * @since 2020/12/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付参数")
public class PayParam extends PaymentCommonParam {

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    @NotBlank(message = "商户订单号不可为空")
    @Size(max = 100, message = "商户订单号不可超过100位")
    private String bizOrderNo;

    /** 支付标题 */
    @Schema(description = "支付标题")
    @NotBlank(message = "支付标题不可为空")
    @Size(max = 100, message = "支付标题不可超过100位")
    private String title;

    /** 支付描述 */
    @Schema(description = "支付描述")
    @Size(max = 500, message = "支付描述不可超过500位")
    private String description;

    /** 是否开启分账 */
    @Schema(description = "是否开启分账")
    private Boolean allocation;

    /** 自动分账 */
    @Schema(description = "自动分账")
    private Boolean autoAllocation;

    /** 过期时间 */
    @Schema(description = "过期时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime expiredTime;

    /**
     * 支付通道编码
     * @see ChannelEnum#getCode()
     */
    @Schema(description = "支付通道编码")
    @NotBlank(message = "支付通道编码不可为空")
    @Size(max = 20, message = "支付通道编码不可超过20位")
    private String channel;

    /**
     * 支付方式编码
     */
    @Schema(description = "支付方式编码")
    @NotBlank(message = "支付方式不可为空")
    @Size(max = 20, message = "支付方式编码不可超过20位")
    private String method;

    /** 支付金额 */
    @Schema(description = "支付金额")
    @NotNull(message = "支付金额不可为空")
    @DecimalMin(value = "0.01", message = "支付金额不可小于0.01元")
    @Digits(integer = 8, fraction = 2, message = "支付金额精度到分, 且要小于一亿元")
    private BigDecimal amount;

    /**
     * 支付扩展参数
     */
    @Schema(description = "支付扩展参数")
    @Size(max = 2048, message = "支付扩展参数不可超过2048位")
    private String extraParam;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数")
    @Size(max = 500, message = "商户扩展参数不可超过500位")
    private String attach;

    /** 同步跳转地址, 支付完毕后用户浏览器返回到该地址, 不传输跳转到默认地址 */
    @Schema(description = "同步通知URL")
    @Size(max = 200, message = "同步通知URL不可超过200位")
    private String returnUrl;

    /** 用户付款中途退出返回商户网站的地址(部分支付场景中可用) */
    @Schema(description = "退出地址")
    @Size(max = 200, message = "退出地址不可超过200位")
    private String quitUrl;

    /** 异步通知地址 */
    @Schema(description = "异步通知地址")
    @Size(max = 200, message = "异步通知地址不可超过200位")
    private String notifyUrl;
}
