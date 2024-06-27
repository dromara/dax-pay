package cn.daxpay.single.service.dto.system.config;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付接口管理
 * @author xxm
 * @since 2024/1/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付接口管理")
public class PayApiConfigDto extends BaseDto {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "接口地址")
    private String api;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "是否启用")
    private boolean enable;

    @Schema(description = "备注")
    private String remark;

}
