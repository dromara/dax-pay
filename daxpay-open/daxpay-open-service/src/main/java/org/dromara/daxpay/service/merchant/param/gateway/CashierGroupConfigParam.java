package org.dromara.daxpay.service.merchant.param.gateway;

import cn.bootx.platform.core.validation.ValidationGroup;
import org.dromara.daxpay.core.enums.GatewayCashierTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关收银台分组配置参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台分组配置参数")
public class CashierGroupConfigParam {

    /** 主键 */
    @Null(message = "Id需要为空", groups = ValidationGroup.add.class)
    @NotNull(message = "Id不可为空", groups = ValidationGroup.edit.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 收银台类型 web/h5/小程序
     * @see GatewayCashierTypeEnum
     */
    @Schema(description = "收银台类型")
    private String CashierType;

    /** 名称 */
    @NotBlank(message = "名称不可为空", groups = ValidationGroup.add.class)
    @Schema(description = "名称")
    private String name;

    /** 图标 */
    @Schema(description = "图标")
    private String icon;

    /** 背景色 */
    @Schema(description = "背景色")
    private String bgColor;

    /** 是否推荐 */
    @Schema(description = "是否推荐")
    private boolean recommend;

    /** 排序 */
    @Schema(description = "排序")
    private Double sortNo;

    /** 应用号 */
    @Schema(description = "应用号")
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    private String appId;
}
