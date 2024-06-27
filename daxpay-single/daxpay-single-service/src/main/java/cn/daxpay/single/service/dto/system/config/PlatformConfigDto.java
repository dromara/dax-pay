package cn.daxpay.single.service.dto.system.config;

import cn.daxpay.single.core.code.PaySignTypeEnum;
import cn.daxpay.single.service.code.TradeNotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 平台配置
 * @author xxm
 * @since 2024/1/2
 */
@Data
@Accessors(chain = true)
@Schema(title = "平台配置")
public class PlatformConfigDto {

    @Schema(description = "网站地址")
    private String websiteUrl;

    /**
     * 签名方式
     * @see PaySignTypeEnum
     */
    @Schema(description = "签名方式")
    private String signType;

    @Schema(description = "签名秘钥")
    private String signSecret;

    /** 是否对请求进行验签 */
    @Schema(description = "是否对请求进行验签")
    private boolean reqSign;

    /**
     * 请求有效时长(秒)
     * 如果传输的请求时间早于当前服务时间, 而且差值超过配置的时长, 将会请求失败
     * 如果传输的请求时间比服务时间大于配置的时长(超过一分钟), 将会请求失败
     */
    @Schema(description = "请求有效时长(秒)")
    private Integer reqTimeout;

    /**
     * 消息通知方式, 目前只支持http
     * @see TradeNotifyTypeEnum
     */
    @Schema(description = "消息通知方式")
    private String notifyType;

    /** 消息通知地址 */

    @Schema(description = "消息通知地址")
    private String notifyUrl;

    @Schema(description = "同步支付跳转地址")
    private String returnUrl;

    @Schema(description = "订单默认超时时间(分钟)")
    private Integer orderTimeout;

    @Schema(description = "支付限额")
    private Integer limitAmount;
}
