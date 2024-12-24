package org.dromara.daxpay.channel.alipay.bo;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付宝业务明细对账单解析类
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlipayReconcileBillDetail extends MpIdEntity {
    @Alias("支付宝交易号")
    private String tradeNo;
    @Alias("商户订单号")
    private String outTradeNo;
    @Alias("业务类型")
    private String tradeType;
    @Alias("商品名称")
    private String subject;
    @Alias("创建时间")
    private String createTime;
    /** yyyy-MM-dd HH:mm:ss */
    @Alias("完成时间")
    private String endTime;
    @Alias("门店编号")
    private String storeId;
    @Alias("门店名称")
    private String storeName;
    @Alias("操作员")
    private String operator;
    @Alias("终端号")
    private String terminalId;
    @Alias("对方账户")
    private String otherAccount;
    @Alias("订单金额（元）")
    private String orderAmount;
    @Alias("商家实收（元）")
    private String realAmount;
    @Alias("支付宝红包（元）")
    private String alipayAmount;
    @Alias("集分宝（元）")
    private String jfbAmount;
    @Alias("支付宝优惠（元）")
    private String alipayDiscountAmount;
    @Alias("商家优惠（元）")
    private String discountAmount;
    @Alias("券核销金额（元）")
    private String couponDiscountAmount;
    @Alias("券名称")
    private String couponName;
    @Alias("商家红包消费金额（元）")
    private String couponAmount;
    @Alias("卡消费金额（元）")
    private String cardAmount;
    @Alias("退款批次号/请求号")
    private String batchNo;
    @Alias("服务费（元）")
    private String serviceAmount;
    @Alias("分润（元）")
    private String splitAmount;
    @Alias("备注")
    private String remark;
}
