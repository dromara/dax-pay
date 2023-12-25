package cn.bootx.platform.daxpay.dto.channel.wallet;

import cn.bootx.platform.daxpay.dto.order.pay.PayOrderDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钱包支付记录")
public class WalletPayOrderDto extends PayOrderDto implements Serializable {

    private static final long serialVersionUID = 8238920331255597223L;

    @Schema(description = "钱包ID")
    private Long walletId;

}
