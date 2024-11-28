package org.dromara.daxpay.service.result.config.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.CheckoutCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.result.MchAppResult;

/**
 *
 * @author xxm
 * @since 2024/11/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "收银台配置项")
public class CheckoutItemConfigVo extends MchAppResult {

    /** 类目配置Id */
    @Schema(description = "类目配置Id")
    private Long groupId;

    /** 排序 */
    @Schema(description = "排序")
    private Double sortNo;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 图标 */
    @Schema(description = "图标")
    private String icon;

    /**
     * 发起调用的类型
     * @see CheckoutCallTypeEnum
     */
    @Schema(description = "发起调用的类型")
    private String callType;

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

}
