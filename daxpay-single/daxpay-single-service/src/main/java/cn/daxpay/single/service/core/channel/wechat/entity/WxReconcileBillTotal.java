package cn.daxpay.single.service.core.channel.wechat.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信对账单汇总
 * @author xxm
 * @since 2024/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "微信对账单汇总")
@TableName("pay_wechat_reconcile_bill_total")
public class WxReconcileBillTotal extends MpIdEntity {

    @DbColumn(comment = "关联对账订单ID")
    private Long recordOrderId;

    @Alias("总交易单数")
    @DbColumn(comment = "总交易单数")
    private String totalNum;

    @Alias("应结订单总金额")
    @DbColumn(comment = "应结订单总金额")
    private String totalAmount;

    @Alias("退款总金额")
    @DbColumn(comment = "退款总金额")
    private String totalRefundAmount;

    @Alias("充值券退款总金额")
    @DbColumn(comment = "充值券退款总金额")
    private String totalRefundCouponAmount;

    @Alias("手续费总金额")
    @DbColumn(comment = "手续费总金额")
    private String totalFee;

    @Alias("订单总金额")
    @DbColumn(comment = "订单总金额")
    private String totalOrderAmount;

    @Alias("申请退款总金额")
    @DbColumn(comment = "申请退款总金额")
    private String applyTotalRefundAmount;

}
