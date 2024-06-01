package cn.daxpay.single.sdk.query;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.refund.RefundOrderModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.refund.QueryRefundParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 退款订单查询接口
 * @author xxm
 * @since 2024/2/7
 */
public class QueryRefundOrderTest {

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

    @Test
    public void testRefund() {
        QueryRefundParam param = new QueryRefundParam();

        param.setRefundNo("DEVR24051020531763000014");
        param.setClientIp("127.0.0.1");

        DaxPayResult<RefundOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
        System.out.println(JSONUtil.toJsonStr(execute.getData()));
    }
}
