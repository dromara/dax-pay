package cn.bootx.platform.daxpay.service.core.channel.wechat.domain;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

/**
 * 微信对账单汇总
 * @author xxm
 * @since 2024/1/17
 */
@Data
public class WxReconcileBillTotal {

    @Alias("总交易单数")
    private String totalNum;

    @Alias("应结订单总金额")
    private String totalAmount;

    @Alias("退款总金额")
    private String totalRefundAmount;

    @Alias("充值券退款总金额")
    private String totalRefundCouponAmount;

    @Alias("手续费总金额")
    private String totalFee;

    @Alias("订单总金额")
    private String totalOrderAmount;

    @Alias("申请退款总金额")
    private String applyTotalRefundAmount;

}
