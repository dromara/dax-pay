package cn.bootx.platform.daxpay.service.dto.system.config;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付通道信息")
public class PayChannelConfigDto extends BaseDto {

    /** 需要与系统中配置的枚举一致 */
    @Schema(description = "代码")
    private String code;

    /** 需要与系统中配置的枚举一致 */
    @Schema(description = "名称")
    private String name;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** logo图片 */
    @Schema(description = "logo图片")
    private Long iconId;

    /** 卡牌背景色 */
    @Schema(description = "卡牌背景色")
    private String bgColor;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
