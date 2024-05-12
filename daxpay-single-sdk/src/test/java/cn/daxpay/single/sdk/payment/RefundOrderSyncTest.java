package cn.daxpay.single.sdk.payment;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.sync.SyncModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.sync.RefundSyncParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
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
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    @Test
    public void testPay() {
        RefundSyncParam param = new RefundSyncParam();

        param.setRefundNo("R0001");

        DaxPayResult<SyncModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }

}
