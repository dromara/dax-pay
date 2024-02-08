package cn.bootx.platform.daxpay.sdk.query;

import cn.bootx.platform.daxpay.sdk.model.pay.QueryPayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.pay.QueryPayOrderParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
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
                .serviceUrl("http://127.0.0.1:9000")
                .signSecret("123456")
                .build();
        DaxPayKit.initConfig(config);
    }

    @Test
    public void testPay() {
        QueryPayOrderParam param = new QueryPayOrderParam();

        param.setBusinessNo("2");

        DaxPayResult<QueryPayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }
}
