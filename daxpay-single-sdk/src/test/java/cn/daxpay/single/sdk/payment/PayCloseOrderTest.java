package cn.daxpay.single.sdk.payment;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.pay.PayCancelModel;
import cn.daxpay.single.sdk.model.pay.PayCloseModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.pay.PayCancelParam;
import cn.daxpay.single.sdk.param.pay.PayCloseParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 支付关闭接口测试类
 * @author xxm
 * @since 2024/2/5
 */
public class PayCloseOrderTest {


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
    public void close(){
        PayCloseParam param = new PayCloseParam();
        param.setOrderNo("DEVP24051019404463000001");
        param.setClientIp("127.0.0.1");
        DaxPayResult<PayCloseModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }

    @Test
    public void cancel(){
        PayCancelParam param = new PayCancelParam();
        param.setOrderNo("DEVP24060518083863000001");
        param.setClientIp("127.0.0.1");
        DaxPayResult<PayCancelModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }
}
