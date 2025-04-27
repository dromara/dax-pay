package org.dromara.daxpay.service.result.constant;

import org.dromara.daxpay.core.enums.TerminalTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通道终端报送类型
 * @author xxm
 * @since 2025/3/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道终端报送类型")
public class TerminalConstResult {

    /** 所属通道 */
    @Schema(description = "所属通道")
    private String channel;

    /**
     * 终端报送类型
     * @see TerminalTypeEnum
     */
    @Schema(description = "终端报送类型")
    private String type;

    /** 终端报送名称 */
    @Schema(description = "终端报送名称")
    private String name;

    /** 是否启用 */
    @Schema(description = "是否启用")
    private boolean enable;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
