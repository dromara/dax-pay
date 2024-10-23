package cn.daxpay.single.sdk.test.trade;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.trade.pay.PaySyncParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.model.trade.pay.PaySyncModel;
import cn.daxpay.single.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 支付同步接口
 * @author xxm
 * @since 2024/2/5
 */
public class PayOrderSyncTest {

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9999")
                .signSecret("123456")
                .appId("M7934041241299655")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 支付订单同步
     * 1. 微信
     * 2. 支付宝
     */
    @Test
    public void testPay() {
        PaySyncParam param = new PaySyncParam();
        param.setBizOrderNo("SDK_1715341621498");
        DaxPayResult<PaySyncModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));

    }

}
