package cn.bootx.platform.daxpay.sdk;

import cn.bootx.platform.daxpay.sdk.model.sync.PaySyncModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.sync.PaySyncParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
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
                .serviceUrl("http://127.0.0.1:9000")
                .signSecret("123456")
                .build();
        DaxPayKit.initConfig(config);
    }

    @Test
    public void testPay() {
        PaySyncParam param = new PaySyncParam();

        param.setBusinessNo("P0001");

        DaxPayResult<PaySyncModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }

}
