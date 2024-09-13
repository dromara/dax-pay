package cn.daxpay.single.sdk.query;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.transfer.TransferOrderModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.transfer.QueryTransferParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 分账查询
 * @author xxm
 * @since 2024/6/30
 */
public class QueryTransferOrderTest {
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
    public void testTransfer() {

        QueryTransferParam param = new QueryTransferParam();

        param.setBizTransferNo("T1719214156174");
        param.setClientIp("127.0.0.1");

        DaxPayResult<TransferOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }
}
