package cn.daxpay.multi.sdk.trade;

import cn.daxpay.multi.sdk.code.SignTypeEnum;
import cn.daxpay.multi.sdk.net.DaxPayConfig;
import cn.daxpay.multi.sdk.net.DaxPayKit;
import cn.daxpay.multi.sdk.param.trade.pay.PayCloseParam;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 支付关闭接口测试类
 * @author xxm
 * @since 2024/2/5
 */
public class CloseOrderTest {


    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:10880")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .mchNo("M1723635576766")
                .appId("M8088873888246277")
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 关闭
     */
    @Test
    public void close(){
        PayCloseParam param = new PayCloseParam();
        param.setOrderNo("DEVP24051019404463000001");
        param.setClientIp("127.0.0.1");
        DaxPayResult<Void> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 撤销
     */
    @Test
    public void cancel(){
        PayCloseParam param = new PayCloseParam();
        param.setBizOrderNo("SDK_1721807811589");
        param.setClientIp("127.0.0.1");
        param.setUseCancel(true);
        DaxPayResult<Void> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
