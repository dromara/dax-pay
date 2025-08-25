package org.dromara.daxpay.service.device.result.print;

import org.dromara.daxpay.service.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 打印设备
 * @author xxm
 * @since 2025/7/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "打印设备")
public class PrintDeviceResult extends MchResult {

    /** 名称 */
    @Schema(description = "名称")
    private String name;

    /** 设备号 */
    @Schema(description = "设备号")
    private  String deviceNo;

    /** 批次号 */
    @Schema(description = "批次号")
    private String batchNo;

    /** 设备厂商 */
    @Schema(description = "设备厂商")
    private String vendor;

    /** 配置ID */
    @Schema(description = "配置ID")
    private Long configId;

    /** 状态 */
    @Schema(description = "状态")
    private Boolean enable;
}
