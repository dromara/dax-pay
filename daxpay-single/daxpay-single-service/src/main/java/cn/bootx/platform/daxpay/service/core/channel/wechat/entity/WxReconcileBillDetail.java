package cn.bootx.platform.daxpay.service.core.channel.wechat.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信对账单明细
 * @author xxm
 * @since 2024/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pay_wechat_reconcile_bill_detail")
public class WxReconcileBillDetail extends MpCreateEntity {

    @DbColumn(comment = "关联对账订单ID")
    private Long recordOrderId;

    @Alias("交易时间")
    @DbColumn(comment = "交易时间")
    private String transactionTime;

    @Alias("公众账号ID")
    @DbColumn(comment = "公众账号ID")
    private String appId;

    @Alias("商户号")
    @DbColumn(comment = "商户号")
    private String merchantId;

    @Alias("特约商户号")
    @DbColumn(comment = "特约商户号")
    private String subMerchantId;

    @Alias("微信订单号")
    @DbColumn(comment = "微信订单号")
    private String weiXinOrderNo;

    @Alias("商户订单号")
    @DbColumn(comment = "商户订单号")
    private String mchOrderNo;

    @Alias("用户标识")
    @DbColumn(comment = "用户标识")
    private String userId;

    @Alias("设备号")
    @DbColumn(comment = "设备号")
    private String deviceInfo;

    @Alias("交易类型")
    @DbColumn(comment = "交易类型")
    private String type;

    @Alias("交易状态")
    @DbColumn(comment = "交易状态")
    private String status;

    @Alias("付款银行")
    @DbColumn(comment = "付款银行")
    private String bank;

    @Alias("货币种类")
    @DbColumn(comment = "货币种类")
    private String currency;

    @Alias("应结订单金额")
    @DbColumn(comment = "应结订单金额")
    private String amount;

    @Alias("代金券金额")
    @DbColumn(comment = "代金券金额")
    private String envelopeAmount;

    @Alias("微信退款单号")
    @DbColumn(comment = "微信退款单号")
    private String name;

    @Alias("商户退款单号")
    @DbColumn(comment = "商户退款单号")
    private String packet;

    @Alias("退款金额")
    @DbColumn(comment = "退款金额")
    private String poundage;


    @Alias("充值券退款金额")
    @DbColumn(comment = "充值券退款金额")
    private String amount2;

    @Alias("退款类型")
    @DbColumn(comment = "退款类型")
    private String rate;

    @Alias("退款状态")
    @DbColumn(comment = "退款状态")
    private String orderAmount;

    @Alias("商品名称")
    @DbColumn(comment = "商品名称")
    private String packet2;

    @Alias("商户数据包")
    @DbColumn(comment = "商户数据包")
    private String packet3;

    @Alias("手续费")
    @DbColumn(comment = "手续费")
    private String packet4;

    @Alias("费率")
    @DbColumn(comment = "费率")
    private String packet5;

    @Alias("订单金额")
    @DbColumn(comment = "订单金额")
    private String packet6;

    @Alias("申请退款金额")
    @DbColumn(comment = "申请退款金额")
    private String packet7;

    @Alias("费率备注")
    @DbColumn(comment = "费率备注")
    private String packet8;

}
