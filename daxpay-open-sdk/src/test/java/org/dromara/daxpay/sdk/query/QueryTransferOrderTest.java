package org.dromara.daxpay.sdk.query;

import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.trade.transfer.QueryTransferParam;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.transfer.TransferOrderResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 查询转账订单
 * @author xxm
 * @since 2025/4/7
 */
public class QueryTransferOrderTest {


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
        DaxPayKit.initConfig(config);
    }

    /**
     * 转账
     */
    @Test
    public void queryTransferOrder() {
        QueryTransferParam param = new QueryTransferParam();
        param.setTransferNo("DEV_T2025041111124570000021");
        DaxResult<TransferOrderResult> execute = DaxPayKit.execute(param);
        System.out.println("验签结果: " + DaxPayKit.verifySign(execute));
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
