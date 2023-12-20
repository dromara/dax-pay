package cn.bootx.platform.daxpay.core.channel.wallet.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.daxpay.common.entity.BasePayOrder;
import cn.bootx.platform.daxpay.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletPayOrderDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 钱包交易记录表
 *
 * @author xxm
 * @since 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@DbTable(comment = "钱包交易记录")
@Accessors(chain = true)
@TableName("pay_wallet_payment")
public class WalletPayment extends BasePayOrder implements EntityBaseFunction<WalletPayOrderDto> {

    /** 钱包ID */
    private Long walletId;

    @Override
    public WalletPayOrderDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }

}
