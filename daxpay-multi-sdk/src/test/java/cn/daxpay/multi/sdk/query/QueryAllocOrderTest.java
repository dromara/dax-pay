package cn.daxpay.multi.sdk.query;

import cn.daxpay.multi.sdk.code.PayChannelEnum;
import cn.daxpay.multi.sdk.code.SignTypeEnum;
import cn.daxpay.multi.sdk.result.allocation.AllocOrderModel;
import cn.daxpay.multi.sdk.result.allocation.AllocReceiversModel;
import cn.daxpay.multi.sdk.net.DaxPayConfig;
import cn.daxpay.multi.sdk.net.DaxPayKit;
import cn.daxpay.multi.sdk.param.allocation.QueryAllocOrderParam;
import cn.daxpay.multi.sdk.param.allocation.QueryAllocReceiverParam;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 查询分账订单测试类
 * @author xxm
 * @since 2024/6/3
 */
public class QueryAllocOrderTest {

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:10880")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 分账订单查询
     */
    @Test
    public void queryAllocOrder() {
        QueryAllocOrderParam param = new QueryAllocOrderParam();
        param.setAllocNo("DEVA240602000243630000101");
        param.setClientIp("127.0.0.1");
        DaxPayResult<AllocOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }

    /**
     * 分账接收方查询
     */
    @Test
    public void queryAllocReceiver() {
        QueryAllocReceiverParam param = new QueryAllocReceiverParam();
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setReceiverNo("123");
        param.setClientIp("127.0.0.1");
        DaxPayResult<AllocReceiversModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }


}
