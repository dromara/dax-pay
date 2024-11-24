package org.dromara.daxpay.single.sdk.test.trade;

import org.dromara.daxpay.single.sdk.code.SignTypeEnum;
import org.dromara.daxpay.single.sdk.net.DaxPayConfig;
import org.dromara.daxpay.single.sdk.net.DaxPayKit;
import org.dromara.daxpay.single.sdk.param.trade.refund.RefundSyncParam;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.model.trade.refund.RefundSyncModel;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 退款同步接口
 * @author xxm
 * @since 2024/2/5
 */
public class RefundOrderSyncTest {

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
        RefundSyncParam param = new RefundSyncParam();
        param.setRefundNo("DEVR24051020530263000002");
        param.setClientIp("127.0.0.1");
        DaxPayResult<RefundSyncModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));

    }

}
