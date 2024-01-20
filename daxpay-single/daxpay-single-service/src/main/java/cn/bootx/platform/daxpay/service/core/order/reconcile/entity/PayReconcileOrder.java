package cn.bootx.platform.daxpay.service.core.order.reconcile.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 支付对账单记录
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付对账单记录")
@TableName("pay_reconcile_record")
public class PayReconcileOrder extends MpCreateEntity {

    /**
     * 批次号
     * 规则：通道简称 + yyyyMMdd + 两位流水号
     * 例子：wx2024012001、ali2024012002
     */
    private String batchNo;

    /** 日期 */
    private LocalDate date;

    /** 通道 */
    private String channel;

    /** 对账单是否下载成功 */
    private boolean down;

    /** 是否比对完成 */
    private boolean compare;

    /** 错误信息 */
    private String errorMsg;

}
