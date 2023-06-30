package cn.bootx.platform.daxpay.param.channel.voucher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 储值卡退款参数
 * @author xxm
 * @since 2023/6/29
 */
@Data
@Accessors(chain = true)
@Schema(title = "储值卡退款参数")
public class VoucherRefundParam {

    @Schema(description = "是否统一退款到指定卡中")
    private boolean refundToOne;

    @Schema(description = "统一退款到的卡号")
    private String refundVoucherNo;

}
