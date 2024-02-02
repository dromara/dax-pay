package cn.bootx.platform.daxpay.sdk;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.model.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.pay.SimplePayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;

/**
 *
 * @author xxm
 * @since 2024/2/2
 */
public class SimplePayOrderTest {

    public static void main(String[] args) {
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9000")
                .signSecret("123456")
                .build();
        DaxPayKit.initConfig(config);

        // 简单支付参数
        SimplePayParam param = new SimplePayParam();
        param.setBusinessNo("1");
        param.setAmount(1);
        param.setTitle("测试接口支付");
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setPayWay(PayWayEnum.QRCODE.getCode());
        param.setClientIp("127.0.0.1");
        param.setNotNotify(true);
        param.setNotReturn(true);

        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param, true);
        System.out.println(execute);
    }
}
