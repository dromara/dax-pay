package cn.daxpay.multi.sdk.payment;

import cn.daxpay.multi.sdk.code.SignTypeEnum;
import cn.daxpay.multi.sdk.model.refund.RefundModel;
import cn.daxpay.multi.sdk.net.DaxPayConfig;
import cn.daxpay.multi.sdk.net.DaxPayKit;
import cn.daxpay.multi.sdk.param.refund.RefundParam;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.PaySignUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

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
                .serviceUrl("http://127.0.0.1:8888")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 退款
     */
    @Test
    public void refund(){
        RefundParam param = new RefundParam();
        param.setBizRefundNo("R" + RandomUtil.randomNumbers(5));
        param.setBizOrderNo("P1719153314573");
        param.setAmount(1);
        param.setAttach("{回调参数}");
        param.setClientIp("127.0.0.1");

        DaxPayResult<RefundModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
        System.out.println(PaySignUtil.verifyHmacSha256Sign(execute.getData(), "123456", execute.getData().getSign()));
    }

}
