package org.dromara.daxpay.sdk.query;

import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.trade.refund.QueryRefundParam;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.refund.RefundOrderResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 退款订单查询接口
 * @author xxm
 * @since 2024/2/7
 */
public class QueryRefundOrderTest {
    private DaxPayKit daxPayKit;

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
        this.daxPayKit =  new DaxPayKit(config);
    }

    @Test
    public void testRefund() {
        QueryRefundParam param = new QueryRefundParam();

        param.setRefundNo("DEV_R2025032911313670000021");
        param.setClientIp("127.0.0.1");

        DaxResult<RefundOrderResult> execute = daxPayKit.execute(param);
        System.out.println("验签结果: " + daxPayKit.verifySign(execute));
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
