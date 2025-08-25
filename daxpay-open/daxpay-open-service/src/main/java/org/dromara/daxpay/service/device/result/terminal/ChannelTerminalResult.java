package org.dromara.daxpay.service.device.result.terminal;

import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.ChannelTerminalStatusEnum;
import org.dromara.daxpay.core.enums.TerminalTypeEnum;
import org.dromara.daxpay.service.merchant.result.info.MchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通道终端设备上报记录
 * @author xxm
 * @since 2025/3/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "通道终端设备上报记录")
public class ChannelTerminalResult extends MchResult {

    /** 终端设备ID */
    @Schema(description = "终端设备ID")
    private Long terminalId;

    /** 终端设备编码 */
    @Schema(description = "终端设备编码")
    private String terminalNo;

    /**
     * 通道
     * @see ChannelEnum
     */
    @Schema(description = "通道")
    private String channel;

    /**
     * 终端报送类型
     * @see TerminalTypeEnum
     */
    @Schema(description = "终端报送类型")
    private String type;

    /**
     * 状态
     * @see ChannelTerminalStatusEnum
     */
    @Schema(description = "状态")
    private String status;

    /** 通道终端号 */
    @Schema(description = "通道终端号")
    private String outTerminalNo;

    /** 错误信息 */
    @Schema(description = "错误信息")
    private String errorMsg;
}
