package cn.daxpay.single.sdk.test.query;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.trade.pay.PayOrderModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.trade.pay.PayQueryParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 查询支付订单测试类
 * @author xxm
 * @since 2024/2/7
 */
public class QueryPayOrderTest {

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

    @Test
    public void testPay() {
        PayQueryParam param = new PayQueryParam();

        param.setBizOrderNoeNo("P17141882417921");
        param.setClientIp("127.0.0.1");

        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));

    }
}
