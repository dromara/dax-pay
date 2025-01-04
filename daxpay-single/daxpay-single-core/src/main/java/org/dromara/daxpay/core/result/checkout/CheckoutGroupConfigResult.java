package org.dromara.daxpay.core.result.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CheckoutTypeEnum;

import java.util.List;

/**
 * 收银台分类配置
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台分类配置")
public class CheckoutGroupConfigResult{
    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /**
     * 类型 web/h5/小程序
     * @see CheckoutTypeEnum
     */
    @Schema(description = "类型")
    private String type;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 图标 */
    @Schema(description = "图标")
    private String icon;


    /** 配置项 */
    @Schema(description = "配置项列表")
    private List<CheckoutItemConfigResult> items;

}
