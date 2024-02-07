package cn.bootx.platform.daxpay.sdk.payment;

import cn.bootx.platform.daxpay.sdk.model.sync.RefundSyncModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.sync.RefundSyncParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
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
                .serviceUrl("http://127.0.0.1:9000")
                .signSecret("123456")
                .build();
        DaxPayKit.initConfig(config);
    }

    @Test
    public void testPay() {
        RefundSyncParam param = new RefundSyncParam();

        param.setRefundNo("R0001");

        DaxPayResult<RefundSyncModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }

}
