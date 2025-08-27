package org.dromara.daxpay.service.device.param.terminal;

import org.dromara.daxpay.core.enums.ChannelTerminalStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通道终端设备上报记录参数
 * @author xxm
 * @since 2025/3/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道终端设备上报记录参数")
public class ChannelTerminalParam {

    /**  主键*/
    @NotNull(message = "Id不可为空")
    @Schema(description = "主键")
    private Long id;

    /**
     * 状态
     * @see ChannelTerminalStatusEnum
     */
    @NotNull(message = "状态不能为空")
    @Schema(description = "状态")
    private String status;

    /** 通道终端号 */
    @Schema(description = "通道终端号")
    private String outTerminalNo;

    /** 清除错误信息 */
    @Schema(description = "清除错误信息")
    private boolean clearErrMsg;

}
