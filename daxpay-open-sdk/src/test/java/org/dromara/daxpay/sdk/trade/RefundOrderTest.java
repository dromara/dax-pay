package org.dromara.daxpay.sdk.trade;

import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.trade.refund.RefundParam;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.refund.RefundResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
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
                .serviceUrl("http://127.0.0.1:19999")
                .signSecret("123456")
                .signType(SignTypeEnum.MD5)
                .appId("M8207639754663343")
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
        param.setOrderNo("DEV_P2025041010494470000001");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setAttach("{回调参数}");
        param.setClientIp("127.0.0.1");

        DaxResult<RefundResult> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
        System.out.println(DaxPayKit.verifySign(execute));
    }
}
