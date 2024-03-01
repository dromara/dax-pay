package cn.bootx.platform.daxpay.service.core.order.reconcile.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.ReconcileTradeEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 对账差异单
 * @author xxm
 * @since 2024/2/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "对账差异单")
@TableName("pay_reconcile_diff_record")
public class PayReconcileDiffRecord extends MpBaseEntity {

    /** 对账单ID */
    @DbColumn(comment = "对账单ID")
    private Long recordId;

    /** 对账单明细ID */
    @DbColumn(comment = "对账单明细ID")
    private Long detailId;

    /** 本地订单id */
    @DbColumn(comment = "本地订单id")
    private Long orderId;

    /** 订单标题 */
    @DbColumn(comment = "订单标题")
    private String title;

    /**
     * 对账订单类型
     * @see ReconcileTradeEnum
     */
    @DbColumn(comment = "对账订单类型")
    private String orderType;

    /**
     * 差异类型
     *
     */
    @DbColumn(comment = "差异类型")
    private String diffType;

    /** 网关订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;

    /** 交易金额 */
    @DbColumn(comment = "交易金额")
    private Integer amount;

    /** 订单时间 */
    @DbColumn(comment = "订单时间")
    private LocalDateTime orderTime;
}
