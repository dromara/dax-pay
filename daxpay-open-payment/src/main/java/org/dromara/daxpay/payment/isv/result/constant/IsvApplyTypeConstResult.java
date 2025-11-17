package org.dromara.daxpay.payment.isv.result.constant;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 服务商进件申请类型
 * @author xxm
 * @since 2025/2/6
 */
@Data
@Accessors(chain = true)
@Schema(title = "服务商进件申请类型")
public class IsvApplyTypeConstResult {

    /** 服务商通道 */
    @TableId
    @Schema(description = "服务商通道")
    private String channel;

    /** 进件类型 */
    @Schema(description = "进件类型")
    private String applyType;

    /** 进件类型名称 */
    @Schema(description = "进件类型名称")
    private String applyTypeName;
}
