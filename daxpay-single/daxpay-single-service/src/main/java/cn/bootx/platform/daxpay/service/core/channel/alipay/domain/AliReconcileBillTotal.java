package cn.bootx.platform.daxpay.service.core.channel.alipay.domain;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * 支付宝业务汇总对账单
 * @author xxm
 * @since 2024/1/17
 */
@Data
public class AliReconcileBillTotal {
    @Alias("门店编号")
    private String storeId;
    @Alias("门店名称")
    private String storeName;
    @Alias("交易订单总笔数")
    private String totalNum;
    @Alias("退款订单总笔数")
    private String totalRefundNum;
    @Alias("订单金额（元）")
    private String totalOrderAmount;
    @Alias("商家实收（元）")
    private String totalAmount;
    @Alias("支付宝优惠（元）")
    private String totalDiscountAmount;
    @Alias("商家优惠（元）")
    private String totalCouponAmount;
    // 卡消费金额（元）	服务费（元）	分润（元）	实收净额（元）
    @Alias("卡消费金额（元）")
    private String totalConsumeAmount;
    @Alias("服务费（元）")
    private String totalServiceAmount;
    @Alias("分润（元）")
    private String totalShareAmount;
    @Alias("实收净额（元）")
    private String totalNetAmount;
}