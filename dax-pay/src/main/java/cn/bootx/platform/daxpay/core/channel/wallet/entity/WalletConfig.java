package cn.bootx.platform.daxpay.core.channel.wallet.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletConfigDto;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 钱包配置
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "钱包配置")
@Accessors(chain = true)
@TableName("pay_wallet")
public class WalletConfig extends MpBaseEntity implements EntityBaseFunction<WalletConfigDto> {

    /** 商户编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @DbColumn(comment = "商户编码")
    private String mchCode;

    /** 商户应用编码 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @DbMySqlIndex(comment = "商户应用编码唯一索引")
    @DbColumn(comment = "商户应用编码")
    private String mchAppCode;

    /** 默认余额 */
    @DbColumn(comment = "默认余额")
    private BigDecimal defaultBalance;

    /**
     * 转换
     */
    @Override
    public WalletConfigDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }
}
