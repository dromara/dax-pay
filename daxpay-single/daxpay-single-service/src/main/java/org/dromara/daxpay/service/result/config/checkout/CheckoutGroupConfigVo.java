package org.dromara.daxpay.service.result.config.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CheckoutTypeEnum;
import org.dromara.daxpay.core.result.MchAppResult;

/**
 * 收银台分类配置
 * @author xxm
 * @since 2024/11/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "收银台分类配置")
public class CheckoutGroupConfigVo extends MchAppResult {

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

    /** 排序 */
    @Schema(description = "排序")
    private Double sortNo;
}
