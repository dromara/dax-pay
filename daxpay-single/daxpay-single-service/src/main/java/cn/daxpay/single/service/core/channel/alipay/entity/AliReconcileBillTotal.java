package cn.daxpay.single.service.core.channel.alipay.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付宝业务汇总对账单
 * @author xxm
 * @since 2024/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "支付宝业务汇总对账单")
@TableName("pay_alipay_reconcile_bill_total")
public class AliReconcileBillTotal extends MpIdEntity {
    /** 关联对账订单ID */
    @DbColumn(comment = "关联对账订单ID")
    private Long recordOrderId;
    @Alias("门店编号")
    @DbColumn(comment = "门店编号")
    private String storeId;
    @Alias("门店名称")
    @DbColumn(comment = "门店名称")
    private String storeName;
    @Alias("交易订单总笔数")
    @DbColumn(comment = "交易订单总笔数")
    private String totalNum;
    @Alias("退款订单总笔数")
    @DbColumn(comment = "退款订单总笔数")
    private String totalRefundNum;
    @Alias("订单金额（元）")
    @DbColumn(comment = "订单金额（元）")
    private String totalOrderAmount;
    @Alias("商家实收（元）")
    @DbColumn(comment = "商家实收（元）")
    private String totalAmount;
    @Alias("支付宝优惠（元）")
    @DbColumn(comment = "支付宝优惠（元）")
    private String totalDiscountAmount;
    @Alias("商家优惠（元）")
    @DbColumn(comment = "商家优惠（元）")
    private String totalCouponAmount;
    // 卡消费金额（元）	服务费（元）	分润（元）	实收净额（元）
    @Alias("卡消费金额（元）")
    @DbColumn(comment = "卡消费金额（元）")
    private String totalConsumeAmount;
    @Alias("服务费（元）")
    @DbColumn(comment = "服务费（元）")
    private String totalServiceAmount;
    @Alias("分润（元）")
    @DbColumn(comment = "分润（元）")
    private String totalShareAmount;
    @Alias("实收净额（元）")
    @DbColumn(comment = "实收净额（元）")
    private String totalNetAmount;
}
