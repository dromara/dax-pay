package cn.daxpay.single.param.payment.pay;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.PayMethodEnum;
import cn.daxpay.single.param.PaymentCommonParam;
import cn.daxpay.single.param.channel.AliPayParam;
import cn.daxpay.single.param.channel.WalletPayParam;
import cn.daxpay.single.param.channel.WeChatPayParam;
import cn.daxpay.single.serializer.TimestampToLocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Map;

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

    @Schema(description = "自动分账")
    private Boolean autoAllocation;


    /** 过期时间 */
    @Schema(description = "过期时间")
    @JsonDeserialize(using = TimestampToLocalDateTimeDeserializer.class)
    private LocalDateTime expiredTime;

    /**
     * 支付通道编码
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "支付通道编码")
    @NotBlank(message = "支付通道编码不可为空")
    @Size(max = 20, message = "支付通道编码不可超过20位")
    private String channel;

    /**
     * 支付方式编码
     * @see PayMethodEnum#getCode()
     */
    @Schema(description = "支付方式编码")
    @NotBlank(message = "支付方式不可为空")
    @Size(max = 20, message = "支付方式编码不可超过20位")
    private String method;

    /** 支付金额 */
    @Schema(description = "支付金额")
    @NotNull(message = "支付金额不可为空")
    @Min(1)
    private Integer amount;

    /**
     * 支付扩展参数
     * @see AliPayParam
     * @see WeChatPayParam
     * @see WalletPayParam
     */
    @Schema(description = "支付扩展参数")
    @Size(max = 2048, message = "支付扩展参数不可超过2048位")
    private Map<String, Object> extraParam;

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
