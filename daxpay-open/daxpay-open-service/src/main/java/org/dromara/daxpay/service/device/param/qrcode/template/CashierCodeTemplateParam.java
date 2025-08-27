package org.dromara.daxpay.service.device.param.qrcode.template;

import org.dromara.daxpay.service.bo.qrcode.CashierCodeIcon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收款码牌模板参数
 * @author xxm
 * @since 2025/7/1
 */
@Data
@Accessors(chain = true)
@Schema(title = "收款码牌模板参数")
public class CashierCodeTemplateParam {

    /** 主键 */
    @Schema(description = "主键")
    private Long id;

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 模板类型 */
    @Schema(description = "模板类型")
    private String type;

    /** 显示ID */
    @Schema(description = "显示ID")
    private Boolean showId;

    /** 显示名称 */
    @Schema(description = "显示名称")
    private Boolean showName;

    /** 显示金额 */
    @Schema(description = "显示金额")
    private Boolean showAmount;

    /** icon列表 */
    @Schema(description = "icon列表")
    private CashierCodeIcon icon;

    /** 背景颜色类型 */
    @Schema(description = "背景颜色类型")
    private String bgColorType;

    /** 背景颜色 */
    @Schema(description = "背景颜色")
    private String backgroundColor;

    /** 主Logo */
    @Schema(description = "主Logo")
    private String mainLogo;

    /** 二维码Logo */
    @Schema(description = "二维码Logo")
    private String qrLogo;
}
