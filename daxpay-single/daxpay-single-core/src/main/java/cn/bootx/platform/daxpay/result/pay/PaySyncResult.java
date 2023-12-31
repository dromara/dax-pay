package cn.bootx.platform.daxpay.result.pay;

import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static cn.bootx.platform.daxpay.code.PaySyncStatusEnum.NOT_SYNC;

/**
 * 支付单同步结果
 * @author xxm
 * @since 2023/12/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付单同步结果")
public class PaySyncResult extends PayCommonResult{

    /**
     * 支付网关同步状态
     * @see PaySyncStatusEnum#NOT_SYNC
     */
    private String syncStatus = NOT_SYNC.getCode();

    @Schema(description = "是否同步成功")
    private boolean success;

    @Schema(description = "是否进行了修复")
    private boolean repair;
}
