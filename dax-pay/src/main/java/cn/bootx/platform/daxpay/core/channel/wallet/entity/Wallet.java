package cn.bootx.platform.daxpay.core.channel.wallet.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.dto.paymodel.wallet.WalletDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 钱包
 *
 * @author xxm
 * @date 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_wallet")
public class Wallet extends MpBaseEntity implements EntityBaseFunction<WalletDto> {

    /** 关联用户id */
    private Long userId;

    /** 余额 */
    private BigDecimal balance;

    /** 状态 */
    private Integer status;

    @Override
    public WalletDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }

}
