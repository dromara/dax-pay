package cn.daxpay.multi.sdk.payment;

import cn.daxpay.multi.sdk.code.SignTypeEnum;
import cn.daxpay.multi.sdk.model.sync.RefundSyncModel;
import cn.daxpay.multi.sdk.net.DaxPayConfig;
import cn.daxpay.multi.sdk.net.DaxPayKit;
import cn.daxpay.multi.sdk.param.refund.RefundSyncParam;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
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
                .serviceUrl("http://127.0.0.1:10880")
                .signSecret("123456")
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
        System.out.println(JSONUtil.toJsonStr(execute));

    }

}
