package cn.daxpay.single.service.core.channel.wallet.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.daxpay.single.service.code.WalletRecordTypeEnum;
import cn.daxpay.single.service.core.channel.wallet.convert.WalletConvert;
import cn.daxpay.single.service.dto.channel.wallet.WalletRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlIndex;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 钱包记录
 * @author xxm
 * @since 2024/2/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "钱包记录")
@TableName("pay_wallet_record")
public class WalletRecord extends MpCreateEntity implements EntityBaseFunction<WalletRecordDto> {

    /** 钱包id */
    @DbMySqlIndex(comment = "钱包ID")
    @DbColumn(comment = "钱包id")
    private Long walletId;

    /** 标题 */
    @DbColumn(comment = "标题")
    private String title;

    /**
     * 业务类型
     * @see WalletRecordTypeEnum
     */
    @DbColumn(comment = "业务类型")
    private String type;

    /** 金额 */
    @DbColumn(comment = "金额")
    private Integer amount;

    /** 变动之前的金额 */
    @DbColumn(comment = "变动之前的金额")
    private Integer oldAmount;

    /** 变动之后的金额 */
    @DbColumn(comment = "变动之后的金额")
    private Integer newAmount;

    /**
     * 交易订单号
     * 支付订单/退款订单
     */
    @DbColumn(comment = "交易订单号")
    private String orderId;

    /** 终端ip */
    @DbColumn(comment = "终端ip")
    private String ip;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /**
     * 转换
     */
    @Override
    public WalletRecordDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }
}
