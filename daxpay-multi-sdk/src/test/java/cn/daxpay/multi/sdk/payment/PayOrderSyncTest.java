package cn.daxpay.multi.sdk.payment;

import cn.daxpay.multi.sdk.code.SignTypeEnum;
import cn.daxpay.multi.sdk.model.sync.PaySyncModel;
import cn.daxpay.multi.sdk.net.DaxPayConfig;
import cn.daxpay.multi.sdk.net.DaxPayKit;
import cn.daxpay.multi.sdk.param.pay.PaySyncParam;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
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
                .serviceUrl("http://127.0.0.1:8888")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    @Test
    public void testPay() {
        PaySyncParam param = new PaySyncParam();
        param.setBizOrderNo("SDK_1715341621498");
        DaxPayResult<PaySyncModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));

    }

}
