package cn.daxpay.single.param.payment.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分账接收方列表
 * @author xxm
 * @since 2024/5/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "分账接收方列表")
public class AllocReceiverParam {

    /** 分账接收方编号 */
    @Schema(description = "分账接收方编号")
    private String receiverNo;

    /** 分账金额 */
    @Schema(description = "分账金额")
    private Integer amount;
}
