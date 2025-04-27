package org.dromara.daxpay.sdk.query;

import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.trade.pay.QueryPayParam;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
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
                .serviceUrl("http://127.0.0.1:19999")
                .signSecret("123456")
                .signType(SignTypeEnum.MD5)
                .mchNo("M1723635576766")
                .appId("M8207639754663343")
                .build();
        DaxPayKit.initConfig(config);
    }

    @Test
    public void testPay() {
        QueryPayParam param = new QueryPayParam();

        param.setOrderNo("DEV_P2025041010494470000002");
        param.setClientIp("127.0.0.1");

        DaxResult<PayOrderResult> execute = DaxPayKit.execute(param);
        System.out.println("验签结果: " + DaxPayKit.verifySign(execute));
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
