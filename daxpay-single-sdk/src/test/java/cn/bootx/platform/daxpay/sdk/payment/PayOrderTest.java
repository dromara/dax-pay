package cn.bootx.platform.daxpay.sdk.payment;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.SignTypeEnum;
import cn.bootx.platform.daxpay.sdk.model.pay.PayModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.pay.PayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import org.junit.Before;
import org.junit.Test;

/**
 * 统一支付接口
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
     * 支付
     */
    @Test
    public void pay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setNotNotify(true);

        param.setBizOrderNo("P0004");
        param.setTitle("测试接口支付");
        param.setChannel(PayChannelEnum.ALI.getCode());

        DaxPayResult<PayModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }
}
