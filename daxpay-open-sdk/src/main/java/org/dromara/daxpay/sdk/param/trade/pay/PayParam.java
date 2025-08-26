package org.dromara.daxpay.sdk.param.trade.pay;

import org.dromara.daxpay.sdk.code.ChannelEnum;
import org.dromara.daxpay.sdk.code.PayLimitPayEnum;
import org.dromara.daxpay.sdk.code.PayMethodEnum;
import org.dromara.daxpay.sdk.net.DaxPayRequest;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.pay.PayResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.TypeReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付参数
 * @author xxm
 * @since 2024/2/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付参数")
public class PayParam extends DaxPayRequest<PayResult> {

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
    @Size(max = 32, message = "支付通道编码不可超过32位")
    private String channel;

    /**
     * 支付方式编码
     * @see PayMethodEnum
     */
    @Schema(description = "支付方式编码")
    @NotBlank(message = "支付方式不可为空")
    @Size(max = 32, message = "支付方式编码不可超过32位")
    private String method;

    /**
     * 其他支付方式, 只有在 支付方式编码(method) 为 其他支付(other)时才会生效
     * 用于处理各种通道各自定义的支付方式
     */
    @Size(max = 128, message = "支付方式编码不可超过128位")
    @Schema(description = "其他支付方式")
    private String otherMethod;

    /**
     * 限制用户支付类型, 目前支持限制信用卡
     * @see PayLimitPayEnum
     */
    @Schema(description = "限制用户支付类型")
    @Size(max = 128, message = "限制用户支付类型不能超过128位")
    private String limitPay;

    /** 支付金额 */
    @Schema(description = "支付金额")
    @NotNull(message = "支付金额不可为空")
    @DecimalMin(value = "0.01", message = "支付金额不可小于0.01元")
    @Digits(integer = 8, fraction = 2, message = "支付金额精度到分, 且要小于一亿元")
    private BigDecimal amount;

    /** 用户标识OpenId */
    @Size(max = 128, message = "用户标识不可超过128位")
    @Schema(description = "用户标识OpenId")
    private String openId;

    /** 付款码 */
    @Size(max = 128, message = "付款码不可超过128位")
    @Schema(description = "付款码")
    private String authCode;

    /** 终端设备编码 */
    @Size(max = 128, message = "终端设备编码不可超过128位")
    @Schema(description = "终端设备编码")
    private String terminalNo;

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


    /**
     * 方法请求路径
     */
    @Override
    public String path() {
        return "/unipay/pay";
    }

    /**
     * 将请求返回结果反序列化为实体类
     */
    @Override
    public DaxResult<PayResult> toModel(String json) {
        return JsonUtil.toBean(json, new TypeReference<DaxResult<PayResult>>() {});
    }
}
