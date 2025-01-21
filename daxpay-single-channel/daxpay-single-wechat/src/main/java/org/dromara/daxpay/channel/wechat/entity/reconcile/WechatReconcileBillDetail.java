package org.dromara.daxpay.channel.wechat.entity.reconcile;

import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信交易对账解析文件
 * @author xxm
 * @since 2024/8/11
 */
@Slf4j
@Data
@Accessors(chain = true)
public class WechatReconcileBillDetail {

    // 交易时间
    @Alias("交易时间")
    private String transactionTime;
    // 公众账号ID
    @Alias("公众账号ID")
    private String appid;

    // 商户号
    @Alias("商户号")
    private String mchid;

    // 特约商户号
    @Alias("特约商户号")
    private String subMchid;

    // 设备号
    @Alias("设备号")
    private String deviceInfo;

    // 微信订单号
    @Alias("微信订单号")
    private String transactionId;

    // 商户订单号
    @Alias("商户订单号")
    private String outTradeNo;

    // 用户标识
    @Alias("用户标识")
    private String openid;

    // 交易类型 JSAPI/NATIVE
    @Alias("交易类型")
    private String tradeType;

    // 交易状态
    @Alias("交易状态")
    private String tradeState;

    // 付款银行
    @Alias("付款银行")
    private String bankType;

    // 货币种类
    @Alias("货币种类")
    private String feeType;

    // 应结订单金额
    @Alias("应结订单金额")
    private String settlementTotalFee;

    // 代金券金额
    @Alias("代金券金额")
    private String couponFee;

    // 微信退款单号
    @Alias("微信退款单号")
    private String refundId;

    // 商户退款单号
    @Alias("商户退款单号")
    private String outRefundNo;

    /**
     * 退款金额
     * 该笔退款单参与计费的应结算金额（申请退款金额-免充值券退款金额），
     * 如果该行数据为订单则展示为0.00，非负数、单位元，保留到小数点后2位
      */
    @Alias("退款金额")
    private String refundFee;

    // 充值券退款金额
    @Alias("充值券退款金额")
    private String couponRefundFee;

    // 退款类型
    @Alias("退款类型")
    private String refundType;

    // 退款状态
    @Alias("退款状态")
    private String refundStatus;

    // 商品名称
    @Alias("商品名称")
    private String body;

    // 商户数据包
    @Alias("商户数据包")
    private String attach;

    // 手续费
    @Alias("手续费")
    private String fee;

    // 费率
    @Alias("费率")
    private String feeRate;

    // 订单金额
    @Alias("订单金额")
    private String totalFee;

    // 申请退款金额
    @Alias("申请退款金额")
    private String applyRefundFee;

    // 费率备注
    @Alias("费率备注")
    private String feeRemark;

}
