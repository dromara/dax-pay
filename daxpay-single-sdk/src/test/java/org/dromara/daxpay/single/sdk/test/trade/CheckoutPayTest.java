package org.dromara.daxpay.single.sdk.test.trade;

import org.dromara.daxpay.single.sdk.code.CheckoutTypeEnum;
import org.dromara.daxpay.single.sdk.code.SignTypeEnum;
import org.dromara.daxpay.single.sdk.model.trade.pay.CheckoutUrlModel;
import org.dromara.daxpay.single.sdk.net.DaxPayConfig;
import org.dromara.daxpay.single.sdk.net.DaxPayKit;
import org.dromara.daxpay.single.sdk.param.trade.pay.CheckoutCreatParam;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import org.dromara.daxpay.single.sdk.util.PaySignUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 收银台测试类
 * @author xxm
 * @since 2024/12/3
 */
public class CheckoutPayTest {

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9999")
                .signSecret("123456")
                .appId("M8207639754663343")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 获取收银台链接
     */
    @Test
    public void creatCheck() {
        CheckoutCreatParam param = new CheckoutCreatParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试收银台支付");
        param.setDescription("这是支付备注");
        param.setCheckoutType(CheckoutTypeEnum.PC.getCode());
        param.setAmount(BigDecimal.valueOf(1.00));
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<CheckoutUrlModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
        System.out.println(PaySignUtil.hmacSha256Sign(execute, "123456"));
    }
}
