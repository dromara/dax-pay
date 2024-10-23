package cn.daxpay.single.sdk.test.trade;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.trade.refund.RefundParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.model.trade.refund.RefundModel;
import cn.daxpay.single.sdk.util.JsonUtil;
import cn.daxpay.single.sdk.util.PaySignUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 退款接口测试
 * 支持部分退款和全部退款, 部分退款的次数不要超过10次, 否则可能会出现不可知的错误
 *
 * @author xxm
 * @since 2024/2/5
 */
public class RefundOrderTest {

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9999")
                .signSecret("123456")
                .appId("M7934041241299655")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 微信退款
     */
    @Test
    public void refund(){
        RefundParam param = new RefundParam();
        param.setBizRefundNo("R" + RandomUtil.randomNumbers(5));
        param.setBizOrderNo("SDK_1721732511210");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setAttach("{回调参数}");
        param.setClientIp("127.0.0.1");

        DaxPayResult<RefundModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
        System.out.println(PaySignUtil.verifyHmacSha256Sign(execute, "123456", execute.getSign()));
    }
}
