package cn.bootx.platform.daxpay.service.core.channel.wallet.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.code.WalletStatusEnum;
import cn.bootx.platform.daxpay.service.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

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
@FieldNameConstants
public class Wallet extends MpBaseEntity implements EntityBaseFunction<WalletDto> {

    /** 关联用户id */
    @DbMySqlIndex(name = "用户ID索引")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @DbColumn(comment = "关联用户id")
    private String userId;

    /** 钱包名称 */
    @DbColumn(comment = "钱包名称")
    private String name;

    /** 余额 */
    @DbColumn(comment = "余额")
    private Integer balance;

    /**
     * 状态
     * @see WalletStatusEnum
     */
    @DbColumn(comment = "状态")
    private String status;

    @Override
    public WalletDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }

}
