package cn.daxpay.single.service.dto.system.config;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
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

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
