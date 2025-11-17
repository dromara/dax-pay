package org.dromara.daxpay.payment.merchant.param.gateway;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.payment.pay.enums.ChannelEnum;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.unipay.enums.CheckoutCounterTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关收银台配置项参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台配置项参数")
public class CheckoutCounterConfigParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;


    /** 名称 */
    @NotBlank(message = "名称不可为空", groups = ValidationGroup.add.class)
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
    @NotBlank(message = "支付通道不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "支付通道")
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    @NotBlank(message = "支付方式不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "支付方式")
    private String payMethod;

    /** 应用号 */
    @Schema(description = "应用号")
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    private String appId;
}
