package cn.bootx.platform.daxpay.core.channel.wallet.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.mybatis.table.modify.mybatis.mysq.constants.MySqlIndexType;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.paymodel.WalletCode;
import cn.bootx.platform.daxpay.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletDto;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

import static cn.bootx.platform.daxpay.core.channel.wallet.entity.Wallet.Fields.*;

/**
 * 钱包
 *
 * @author xxm
 * @since 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbMySqlIndex(comment = "用户和应用编码联合唯一索引",fields = {userId,mchAppCode},type = MySqlIndexType.UNIQUE)
@DbTable(comment = "钱包")
@Accessors(chain = true)
@TableName("pay_wallet")
@FieldNameConstants
public class Wallet extends MpBaseEntity implements EntityBaseFunction<WalletDto> {

    /** 商户编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @DbColumn(comment = "商户编码")
    private String mchCode;

    /** 商户应用编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @DbColumn(comment = "商户应用编码")
    private String mchAppCode;

    /** 关联用户id */
    @TableField(updateStrategy = FieldStrategy.NEVER)
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
