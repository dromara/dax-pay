package org.dromara.daxpay.service.param.config.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.CheckoutCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;

/**
 * 收银台配置项参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台配置项参数")
public class CheckoutItemConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 类目配置Id */
    @Schema(description = "类目配置Id")
    private Long groupId;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 图标 */
    @Schema(description = "图标")
    private String icon;

    /** 排序 */
    @Schema(description = "排序")
    private Double sortNo;

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

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;
}
