package cn.bootx.platform.daxpay.service.core.channel.alipay.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付宝业务明细对账单
 * @author xxm
 * @since 2024/1/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "支付宝业务明细对账单")
@TableName("pay_ali_reconcile_bill_detail")
public class AliReconcileBillDetail extends MpIdEntity {
    /** 关联对账订单ID */
    @DbColumn(comment = "关联对账订单ID")
    private Long recordOrderId;
    @Alias("支付宝交易号")
    @DbColumn(comment = "支付宝交易号")
    private String tradeNo;
    @Alias("商户订单号")
    @DbColumn(comment = "商户订单号")
    private String outTradeNo;
    @Alias("业务类型")
    @DbColumn(comment = "业务类型")
    private String tradeType;
    @Alias("商品名称")
    @DbColumn(comment = "商品名称")
    private String subject;
    @Alias("完成时间")
    @DbColumn(comment = "完成时间")
    private String endTime;
    @Alias("门店编号")
    @DbColumn(comment = "门店编号")
    private String storeId;
    @Alias("门店名称")
    @DbColumn(comment = "门店名称")
    private String storeName;
    @Alias("操作员")
    @DbColumn(comment = "操作员")
    private String operator;
    @Alias("终端号")
    @DbColumn(comment = "终端号")
    private String terminalId;
    @Alias("对方账户")
    @DbColumn(comment = "对方账户")
    private String otherAccount;
    @Alias("订单金额（元）")
    @DbColumn(comment = "订单金额（元）")
    private String orderAmount;
    @Alias("商家实收（元）")
    @DbColumn(comment = "商家实收（元）")
    private String realAmount;
    @Alias("支付宝红包（元）")
    @DbColumn(comment = "支付宝红包（元）")
    private String alipayAmount;
    @Alias("集分宝（元）")
    @DbColumn(comment = "集分宝（元）")
    private String jfbAmount;
    @Alias("支付宝优惠（元）")
    @DbColumn(comment = "支付宝优惠（元）")
    private String alipayDiscountAmount;
    @Alias("商家优惠（元）")
    @DbColumn(comment = "商家优惠（元）")
    private String discountAmount;
    @Alias("券核销金额（元）")
    @DbColumn(comment = "券核销金额（元）")
    private String couponDiscountAmount;
    @Alias("券名称")
    @DbColumn(comment = "券名称")
    private String couponName;
    @Alias("商家红包消费金额（元）")
    @DbColumn(comment = "商家红包消费金额（元）")
    private String couponAmount;
    @Alias("卡消费金额（元）")
    @DbColumn(comment = "卡消费金额（元）")
    private String cardAmount;
    @Alias("退款批次号/请求号")
    @DbColumn(comment = "退款批次号/请求号")
    private String batchNo;
    @Alias("服务费（元）")
    @DbColumn(comment = "服务费（元）")
    private String serviceAmount;
    @Alias("分润（元）")
    @DbColumn(comment = "分润（元）")
    private String splitAmount;
    @Alias("备注")
    @DbColumn(comment = "备注")
    private String remark;
}
