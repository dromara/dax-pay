package cn.bootx.platform.daxpay.core.channel.wallet.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.dto.paymodel.wallet.WalletLogDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 钱包日志表
 *
 * @author xxm
 * @date 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_wallet_log")
public class WalletLog extends MpBaseEntity implements EntityBaseFunction<WalletLogDto> {

    /** 钱包id */
    private Long walletId;

    /** 用户id */
    private Long userId;

    /** 类型 */
    private Integer type;

    /** 交易记录ID */
    private Long paymentId;

    /** 备注 */
    private String remark;

    /** 业务ID */
    private String businessId;

    /** 操作类型 */
    private int operationSource;

    /** 金额 */
    private BigDecimal amount;

    @Override
    public WalletLogDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }

}
