package org.dromara.daxpay.payment.merchant.result.gateway;

import org.dromara.daxpay.payment.merchant.result.info.MchResult;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.unipay.enums.CheckoutCounterTypeEnum;
import org.dromara.daxpay.payment.unipay.enums.GatewayCallTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2024/11/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "收银台配置项")
public class CheckoutCounterConfigResult extends MchResult {
    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /**
     * 类型
     * @see CheckoutCounterTypeEnum
     */
    @Schema(description = "类型")
    private String type;

    /** 是否推荐 */
    @Schema(description = "是否推荐")
    private Boolean recommend;

    /** 背景色 */
    @Schema(description = "背景色")
    private String bgColor;

    /** 边框色 */
    @Schema(description = "边框色")
    private String borderColor;

    /** 字体颜色 */
    @Schema(description = "字体颜色")
    private String fontColor;

    /** 图标 */
    @Schema(description = "图标")
    private String icon;

    /** 排序 */
    @Schema(description = "排序")
    private Double sortNo;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @Schema(description = "支付方式")
    private String payMethod;

    /**
     * 调用方式
     * @see GatewayCallTypeEnum
     */
    @Schema(description = "调用方式")
    private String callType;
}
