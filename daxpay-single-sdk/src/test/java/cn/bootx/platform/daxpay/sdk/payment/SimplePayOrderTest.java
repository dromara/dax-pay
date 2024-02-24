package cn.bootx.platform.daxpay.sdk.payment;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.model.pay.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.pay.SimplePayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.util.RandomUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 简单支付
 * @author xxm
 * @since 2024/2/2
 */
public class SimplePayOrderTest {

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
     * 异步通道测试
     */
    @Test
    public void simplePay() {
        // 简单支付参数
        SimplePayParam param = new SimplePayParam();
        param.setBusinessNo("1");
        param.setAmount(1);
        param.setTitle("测试接口支付");
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setPayWay(PayWayEnum.QRCODE.getCode());
        param.setClientIp("127.0.0.1");
        param.setNotNotify(true);

        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        PayOrderModel data = execute.getData();
        System.out.println(data);
    }

    /**
     * 同步通道+回调测试
     */
    @Test
    public void simplePayCallback() {
        SimplePayParam param = new SimplePayParam();
        param.setBusinessNo("P"+ RandomUtil.randomNumbers(5));
        param.setAmount(12);
        param.setTitle("测试接口支付");
        param.setChannel(PayChannelEnum.CASH.getCode());
        param.setPayWay(PayWayEnum.NORMAL.getCode());
        param.setClientIp("127.0.0.1");
        param.setNotifyUrl("http://127.0.0.1:9000/demo/callback/pay");
        param.setNotifyUrl("http://127.0.0.1:9000/demo/callback/payObject");
        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        PayOrderModel data = execute.getData();
        System.out.println(data);
    }
}
