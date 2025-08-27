package org.dromara.daxpay.service.device.result.print;

import cn.bootx.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 打印设备配置
 * @author xxm
 * @since 2025/7/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "打印设备配置")
public class PrintDeviceConfigResult extends BaseResult {

    /** 配置名称 */
    @Schema(description = "配置名称")
    private String name;

    /** 厂商 */
    @Schema(description = "厂商")
    private String vendor;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private Boolean enable;

    /** 配置Json */
    @Schema(description = "配置Json")
    private String config;
}
