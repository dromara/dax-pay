package cn.bootx.platform.daxpay.core.channel.voucher.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.paymodel.VoucherCode;
import cn.bootx.platform.daxpay.core.channel.voucher.convert.VoucherConvert;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherLogDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 储值卡日志
 *
 * @author xxm
 * @since 2022/3/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@DbTable(comment = "储值卡日志")
@Accessors(chain = true)
@TableName("pay_voucher_log")
public class VoucherLog extends MpBaseEntity implements EntityBaseFunction<VoucherLogDto> {

    /** 储值卡id */
    @DbMySqlIndex(comment = "储值卡ID")
    @DbColumn(comment = "储值卡id")
    private Long voucherId;

    /** 储值卡号 */
    @DbColumn(comment = "储值卡号")
    private String voucherNo;

    /** 金额 */
    @DbColumn(comment = "金额")
    private BigDecimal amount;

    /**
     * 类型
     * @see VoucherCode#LOG_PAY
     */
    @DbColumn(comment = "类型")
    private String type;

    /** 交易记录ID */
    @DbColumn(comment = "交易记录ID")
    private Long paymentId;

    /** 业务ID */
    @DbColumn(comment = "业务ID")
    private String businessId;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /**
     * 转换
     */
    @Override
    public VoucherLogDto toDto() {
        return VoucherConvert.CONVERT.convert(this);
    }
}
