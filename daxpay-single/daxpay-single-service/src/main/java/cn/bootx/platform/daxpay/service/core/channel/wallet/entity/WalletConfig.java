package cn.bootx.platform.daxpay.service.core.channel.wallet.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.service.dto.channel.wallet.WalletConfigDto;
import cn.bootx.platform.daxpay.service.param.channel.wechat.WalletConfigParam;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
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
@TableName("pay_wallet_config")
public class WalletConfig extends MpBaseEntity implements EntityBaseFunction<WalletConfigDto> {

    /** 默认余额 */
    @DbColumn(comment = "默认余额")
    private BigDecimal defaultBalance;

    /** 状态 */
    @DbColumn(comment = "状态")
    private String state;

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

    public static WalletConfig init(WalletConfigParam in){
        return WalletConvert.CONVERT.convert(in);
    }
}
