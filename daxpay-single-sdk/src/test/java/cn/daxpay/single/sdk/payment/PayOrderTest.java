package cn.daxpay.single.sdk.payment;

import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.code.PayMethodEnum;
import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.pay.PayModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.pay.PayParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 统一支付接口
 * @author xxm
 * @since 2024/2/5
 */
public class PayOrderTest {

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

    /**
     * 支付
     */
    @Test
    public void pay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");

        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试接口支付");
        param.setDescription("这是支付备注");
        param.setAmount(1011);
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.WAP.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("https://abc.com/callback");

        DaxPayResult<PayModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));

    }
}
