package cn.bootx.platform.daxpay.core.channel.wallet.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.paymodel.WalletCode;
import cn.bootx.platform.daxpay.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 钱包
 *
 * @author xxm
 * @since 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "钱包")
@Accessors(chain = true)
@TableName("pay_wallet")
public class Wallet extends MpBaseEntity implements EntityBaseFunction<WalletDto> {

    /** 关联用户id */
    @DbMySqlIndex
    @DbColumn(comment = "关联用户id")
    private Long userId;

    /** 余额 */
    @DbColumn(comment = "余额")
    private BigDecimal balance;

    /** 预冻结额度 */
    @DbColumn(comment = "预冻结额度")
    private BigDecimal freezeBalance;

    /**
     * 状态
     * @see WalletCode#STATUS_FORBIDDEN
     */
    @DbColumn(comment = "状态")
    private String status;

    @Override
    public WalletDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }

}
