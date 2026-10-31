package cn.daxpay.open.channel.mock.param.alloc;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Mock 通道分账接收方重新绑定参数
///
/// mock 无应用维度, 重绑只需主键; 接收方账号不可修改(改账号即新接收方, 走新增)。
@Schema(title = "Mock模拟通道分账接收方重新绑定参数")
@Data
@Accessors(chain = true)
public class MockAllocReceiverBindParam {

    /// 记录 id
    @Schema(description = "记录id")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;
}
