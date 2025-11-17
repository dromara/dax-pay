package org.dromara.daxpay.sdk.trade;

import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.trade.pay.PayCloseParam;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 支付关闭接口测试类
 * @author xxm
 * @since 2024/2/5
 */
public class CloseOrderTest {
    private DaxPayKit daxPayKit;


    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:19999")
                .signSecret("123456")
                .signType(SignTypeEnum.MD5)
                .mchNo("M1723635576766")
                .appId("M8207639754663343")
                .build();
        this.daxPayKit =  new DaxPayKit(config);
    }

    /**
     * 关闭
     */
    @Test
    public void close(){
        PayCloseParam param = new PayCloseParam();
        param.setOrderNo("DEVP24051019404463000001");
        param.setClientIp("127.0.0.1");
        DaxResult<Void> execute = daxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 撤销
     */
    @Test
    public void cancel(){
        PayCloseParam param = new PayCloseParam();
        param.setOrderNo("DEV_P2025041017085370000011");
        param.setClientIp("127.0.0.1");
        param.setUseCancel(true);
        DaxResult<Void> execute = daxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
