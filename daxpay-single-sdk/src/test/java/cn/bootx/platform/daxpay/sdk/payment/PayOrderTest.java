package cn.bootx.platform.daxpay.sdk.payment;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.code.SignTypeEnum;
import cn.bootx.platform.daxpay.sdk.model.pay.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.channel.VoucherPayParam;
import cn.bootx.platform.daxpay.sdk.param.channel.WalletPayParam;
import cn.bootx.platform.daxpay.sdk.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.sdk.param.pay.PayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 通用支付接口
 * @author xxm
 * @since 2024/2/5
 */
public class PayOrderTest {

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9000")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 单通道支付
     */
    @Test
    public void onePay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setNotNotify(true);

        param.setBusinessNo("P0004");
        param.setTitle("测试接口支付");
        PayChannelParam payChannelParam = new PayChannelParam();
        payChannelParam.setChannel(PayChannelEnum.UNION_PAY.getCode());
        payChannelParam.setWay(PayWayEnum.QRCODE.getCode());
        payChannelParam.setAmount(1);

        List<PayChannelParam> payChannels = Collections.singletonList(payChannelParam);
        param.setPayChannels(payChannels);
        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }

    /**
     * 多通道支付. 全部为同步支付方式
     */
    @Test
    public void multiSyncPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setNotNotify(true);

        param.setBusinessNo("P0005");
        param.setTitle("测试组合支付(全同步)");
        // 现金支付
        PayChannelParam cash = new PayChannelParam();
        cash.setChannel(PayChannelEnum.CASH.getCode());
        cash.setWay(PayWayEnum.NORMAL.getCode());
        cash.setAmount(22);

        // 储值卡支付
        PayChannelParam card = new PayChannelParam();
        card.setChannel(PayChannelEnum.VOUCHER.getCode());
        card.setWay(PayWayEnum.NORMAL.getCode());
        card.setAmount(20001);
        // 储值卡通道参数
        VoucherPayParam voucherPayParam = new VoucherPayParam();
        voucherPayParam.setCardNo("123456");
        card.setChannelParam(voucherPayParam);

        // 钱包支付
        PayChannelParam wallet = new PayChannelParam();
        wallet.setChannel(PayChannelEnum.WALLET.getCode());
        wallet.setWay(PayWayEnum.NORMAL.getCode());
        wallet.setAmount(10);
        // 钱包通道支付参数
        WalletPayParam walletPayParam = new WalletPayParam();
        walletPayParam.setUserId("1");
        wallet.setChannelParam(walletPayParam);

        // 组装组合支付的参数
        List<PayChannelParam> payChannels = Arrays.asList(cash, card, wallet);
        param.setPayChannels(payChannels);
        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }

    /**
     * 多通道支付. 包含异步支付
     */
    @Test
    public void multiAsyncPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setNotNotify(true);

        param.setBusinessNo("P0015");
        param.setTitle("测试组合支付(包含异步支付)");
        // 现金支付
        PayChannelParam cash = new PayChannelParam();
        cash.setChannel(PayChannelEnum.CASH.getCode());
        cash.setWay(PayWayEnum.NORMAL.getCode());
        cash.setAmount(201);

        // 异步支付
        PayChannelParam async = new PayChannelParam();
        async.setChannel(PayChannelEnum.ALI.getCode());
        async.setWay(PayWayEnum.WEB.getCode());
        async.setAmount(2207);

        // 组装组合支付的参数
        List<PayChannelParam> payChannels = Arrays.asList(cash, async);
        param.setPayChannels(payChannels);
        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }
}
