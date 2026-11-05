package cn.daxpay.open.payment.device.terminal.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 系统终端批量绑定门店参数(运营端/商户端共用)
///
/// 门店工作台一次勾选多台终端批量绑定门店; 任一终端不存在/归属不符整批拒绝。
@Data
@Accessors(chain = true)
@Schema(title = "系统终端批量绑定门店参数")
public class TerminalBindStoreParam {

    /// 系统终端主键列表
    @Schema(description = "系统终端主键列表")
    @NotEmpty(message = "{validation.field.ids.notEmpty}")
    private List<Long> terminalIds;

    /// 门店号(须存在且与终端同商户)
    @Schema(description = "门店号")
    @NotBlank(message = "{validation.field.storeNo.notBlank}")
    @Size(max = 64, message = "{validation.field.storeNo.size}")
    private String storeNo;
}
