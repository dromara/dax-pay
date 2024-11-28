package org.dromara.daxpay.service.param.config.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CheckoutTypeEnum;

/**
 * 收银台分组配置参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台分组配置参数")
public class CheckoutGroupConfigParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /**
     * 类型 web/h5/小程序
     * @see CheckoutTypeEnum
     */
    @Schema(description = "类型 web/h5/小程序")
    private String type;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 图标 */
    @Schema(description = "图标")
    private String icon;

    /** 排序 */
    @Schema(description = "排序")
    private Double sortNo;

    /** 应用号 */
    @Schema(description = "应用号")
    private String appId;
}
