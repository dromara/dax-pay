package org.dromara.daxpay.service.isv.result.gateway;

import org.dromara.daxpay.core.enums.GatewayCashierTypeEnum;
import org.dromara.daxpay.service.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 网关收银台分组配置
 * @author xxm
 * @since 2024/11/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "网关收银台分组配置")
public class IsvCashierGroupConfigResult extends MchResult {
    /**
     * 收银台类型 web/h5/小程序
     * @see GatewayCashierTypeEnum
     */
    @Schema(description = "收银台类型")
    private String CashierType;

    /** 名称 */
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

    /** 配置项 */
    @Schema(description = "配置项列表")
    private List<IsvCashierItemConfigResult> items;

}
