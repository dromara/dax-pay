package cn.bootx.platform.daxpay.service.core.order.reconcile.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 支付对账单订单
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付对账单订单")
@TableName("pay_reconcile_order")
public class PayReconcileOrder extends MpCreateEntity {

    /**
     * 批次号
     * 规则：通道简称 + yyyyMMdd + 两位流水号
     * 例子：wx2024012001、ali2024012002
     */
    @DbColumn(comment = "批次号")
    private String batchNo;

    /** 日期 */
    @DbColumn(comment = "日期")
    private LocalDate date;

    /** 通道 */
    @DbColumn(comment = "通道")
    private String channel;

    /** 是否下载成功 */
    @DbColumn(comment = "是否下载成功")
    private boolean down;

    /** 是否比对完成 */
    @DbColumn(comment = "是否比对完成")
    private boolean compare;

    /** 错误信息 */
    @DbColumn(comment = "错误信息")
    private String errorMsg;

}
