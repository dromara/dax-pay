package org.dromara.daxpay.sdk.trade;

import org.dromara.daxpay.sdk.code.GatewayPayTypeEnum;
import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.trade.pay.AggregateBarPayParam;
import org.dromara.daxpay.sdk.param.trade.pay.GatewayPayParam;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.pay.GatewayPayUrlResult;
import org.dromara.daxpay.sdk.result.trade.pay.PayResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 网关支付测试类
 * @author xxm
 * @since 2025/4/10
 */
public class GatewayPayTest {
    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:19999")
                .signSecret("123456")
                .signType(SignTypeEnum.MD5)
                .appId("M8207639754663343")
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 网关支付
     */
    @Test
    public void gatewayPay(){
        GatewayPayParam param = new GatewayPayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试网关支付");
        param.setDescription("这是支付备注");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setGatewayPayType(GatewayPayTypeEnum.H5.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");
        DaxResult<GatewayPayUrlResult> execute = DaxPayKit.execute(param);
        // 验签
        System.out.println("验签结果: " + DaxPayKit.verifySign(execute));
        System.out.println(JsonUtil.toJsonStr(execute));

    }

    /**
     * 网关集合付款码支付
     */
    @Test
    public void aggregateBarPay(){
        AggregateBarPayParam param = new AggregateBarPayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试聚合付款码支付");
        param.setDescription("这是支付备注");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setTerminalNo("66888");
        param.setAuthCode("66888");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");
        DaxResult<PayResult> execute = DaxPayKit.execute(param);
        // 验签
        System.out.println("验签结果: " + DaxPayKit.verifySign(execute));
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
