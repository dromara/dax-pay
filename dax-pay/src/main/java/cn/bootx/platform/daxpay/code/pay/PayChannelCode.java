package cn.bootx.platform.daxpay.code.pay;

import java.util.Arrays;
import java.util.List;

/**
 * 支付通道
 *
 * @author xxm
 * @date 2021/2/24
 */
public interface PayChannelCode {

    /**
     * 支付通道类型 1.支付宝 2.微信 3.云闪付 4.现金 5.钱包 6.储值卡 8.信用卡 9.ApplePay 10.渠道方支付 99.聚合支付
     */
    int ALI = 1;

    int WECHAT = 2;

    int UNION_PAY = 3;

    int CASH = 4;

    int WALLET = 5;

    int VOUCHER = 6;

    int CREDIT_CARD = 8;

    int APPLE_PAY = 9;

    int CHANNEL_PAY = 10;

    int AGGREGATION = 99;

    /** 支付宝 UA */
    String UA_ALI_PAY = "Alipay";

    /** 微信 UA */
    String UA_WECHAT_PAY = "MicroMessenger";

    /** 异步支付通道 */
    List<Integer> ASYNC_TYPE = Arrays.asList(ALI, WECHAT, UNION_PAY, APPLE_PAY);

}
