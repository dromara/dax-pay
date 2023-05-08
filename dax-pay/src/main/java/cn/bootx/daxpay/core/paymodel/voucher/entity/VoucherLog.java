package cn.bootx.daxpay.core.paymodel.voucher.entity;

import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.daxpay.code.paymodel.VoucherCode;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 储值卡日志
 *
 * @author xxm
 * @date 2022/3/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_voucher_log")
public class VoucherLog extends MpBaseEntity {

    /** 储值卡id */
    private Long voucherId;

    /** 储值卡号 */
    private String voucherNo;

    /** 金额 */
    private BigDecimal amount;

    /**
     * 类型
     * @see VoucherCode
     */
    private Integer type;

    /** 交易记录ID */
    private Long paymentId;

    /** 业务ID */
    private String businessId;

    /** 备注 */
    private String remark;

}
