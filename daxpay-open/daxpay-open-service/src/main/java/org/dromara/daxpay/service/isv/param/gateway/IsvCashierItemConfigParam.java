package org.dromara.daxpay.service.isv.param.gateway;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
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
public class IsvCashierItemConfigParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /** 类目配置Id */
    @NotNull(message = "类目ID不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "类目配置Id")
    private Long groupId;

    /** 名称 */
    @NotBlank(message = "名称不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "名称")
    private String name;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    @NotBlank(message = "调用类型不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "发起调用的类型")
    private String callType;

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

    @Schema(description = "其他支付方式")
    private String otherMethod;

    /** 背景色 */
    @Schema(description = "背景色")
    private String bgColor;

    /** 是否推荐 */
    @Schema(description = "是否推荐")
    private Boolean recommend;

    /** 图标 */
    @Schema(description = "图标")
    private String icon;

    /** 排序 */
    @Schema(description = "排序")
    private Double sortNo;

}
