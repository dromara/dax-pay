package cn.daxpay.open.device.admin.param.virtual;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 虚拟设备生成参数
///
/// 原子生成: 产号(VD+日期+随机段, 台账查重) + 批量入库一个事务完成,
/// 消除"先产号再提交"两步间的并发生成同号缝隙。
@Data
@Accessors(chain = true)
@Schema(title = "虚拟设备生成参数")
public class VirtualDeviceGenerateParam {

    @Schema(description = "批次号(唯一)")
    @NotBlank(message = "{validation.field.batchNo.notBlank}")
    @Size(max = 64, message = "{validation.field.batchNo.size}")
    private String batchNo;

    @Schema(description = "生成数量")
    @NotNull(message = "{validation.field.count.notNull}")
    private Integer count;
}
