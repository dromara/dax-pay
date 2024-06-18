package cn.daxpay.single.service.param.system.config;

import cn.daxpay.single.core.code.PaySignTypeEnum;
import cn.daxpay.single.service.code.TradeNotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 平台配置
 * @author xxm
 * @since 2024/1/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "平台配置")
public class PlatformConfigParam {

    @NotBlank(message = "网站地址必填")
    @Size(min = 1, max = 200, message = "网站地址不可超过200位")
    @Schema(description = "网站地址")
    private String websiteUrl;

    /** 是否对请求进行验签 */
    @Schema(description = "是否对请求进行验签")
    private boolean reqSign;

    /**
     * 请求有效时长(秒)
     * 如果传输的请求时间早于当前服务时间, 而且差值超过配置的时长, 将会请求失败
     * 如果传输的请求时间比服务时间大于配置的时长(超过一分钟), 将会请求失败
     */
    @Min(value = 1, message = "请求有效时长不可小于1秒")
    @Schema(description = "请求有效时长(秒)")
    private Integer reqTimeout;

    /**
     * 签名方式
     * @see PaySignTypeEnum
     */
    @Size(max = 20, message = "签名方式不可超过20位")
    @Schema(description = "签名方式")
    private String signType;

    /** 签名秘钥 */
    @Size(max = 50, message = "签名秘钥不可超过50位")
    @Schema(description = "签名秘钥")
    private String signSecret;

    /**
     * 消息通知方式, 目前只支持http
     * @see TradeNotifyTypeEnum
     */
    @Size(max = 20, message = "消息通知方式不可超过20位")
    @Schema(description = "消息通知方式")
    private String notifyType;

    /** 消息通知地址 */
    @Size(max = 200, message = "消息通知地址不可超过200位")
    @Schema(description = "消息通知地址")
    private String notifyUrl;

    /** 同步支付跳转地址 */
    @Size(max = 200, message = "同步支付跳转地址不可超过200位")
    @Schema(description = "同步支付跳转地址")
    private String returnUrl;

    /** 订单默认超时时间(分钟) */
    @Min(value = 5, message = "订单默认超时时间不可小于5分钟")
    @Schema(description = "订单默认超时时间(分钟)")
    private Integer orderTimeout;

    /** 订单支付限额(分) */
    @Min(value = 1, message = "订单支付限额不可小于0.01元")
    @Schema(description = "订单支付限额(分)")
    private Integer limitAmount;
}
