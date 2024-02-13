package cn.bootx.platform.daxpay.sdk.payment;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.model.pay.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.sdk.param.pay.PayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import org.junit.Before;
import org.junit.Test;

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

        param.setBusinessNo("P0001");
        param.setTitle("测试接口支付");
        PayChannelParam payChannelParam = new PayChannelParam();
        payChannelParam.setChannel(PayChannelEnum.WECHAT.getCode());
        payChannelParam.setWay(PayWayEnum.QRCODE.getCode());
        payChannelParam.setAmount(1);

        List<PayChannelParam> payChannels = Collections.singletonList(payChannelParam);
        param.setPayChannels(payChannels);
        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }

    /**
     * 多通道支付
     */
    @Test
    public void multiPay() {

    }
}
