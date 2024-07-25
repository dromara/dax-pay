package cn.daxpay.multi.core.result.trade.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 支付同步结果
 * @author xxm
 * @since 2023/12/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付同步结果")
public class PaySyncResult  {

    /**
     * 同步到的订单状态
     */
    @Schema(description = "同步状态")
    private String status;

}
