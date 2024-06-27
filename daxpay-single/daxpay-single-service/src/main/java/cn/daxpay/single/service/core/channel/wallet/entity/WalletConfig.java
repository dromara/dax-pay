package cn.daxpay.single.service.core.channel.wallet.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import cn.daxpay.single.service.core.channel.wallet.convert.WalletConvert;
import cn.daxpay.single.service.dto.channel.wallet.WalletConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 钱包配置
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "钱包配置")
@Accessors(chain = true)
@TableName(value = "pay_wallet_config",autoResultMap = true)
public class WalletConfig extends MpBaseEntity implements EntityBaseFunction<WalletConfigDto> {

    /** 是否启用 */
    @DbColumn(comment = "是否启用")
    private Boolean enable;

    /** 单次支付最多多少金额 */
    @DbColumn(comment = "单次支付最多多少金额 ")
    private Integer limitAmount;

    /** 可用支付方式 */
    @DbColumn(comment = "可用支付方式")
    @DbMySqlFieldType(MySqlFieldTypeEnum.VARCHAR)
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> payWays;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /**
     * 转换
     */
    @Override
    public WalletConfigDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }
}
