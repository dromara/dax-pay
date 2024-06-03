package cn.daxpay.single.sdk.query;

import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.allocation.AllocOrderModel;
import cn.daxpay.single.sdk.model.allocation.AllocReceiversModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.allocation.QueryAllocOrderParam;
import cn.daxpay.single.sdk.param.allocation.QueryAllocReceiverParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
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
                .serviceUrl("http://127.0.0.1:9000")
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
        param.setAllocationNo("DEVA240602000243630000101");
        param.setClientIp("127.0.0.1");
        DaxPayResult<AllocOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }

    /**
     * 分账接收方
     */
    @Test
    public void queryAllocReceiver() {
        QueryAllocReceiverParam param = new QueryAllocReceiverParam();
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setClientIp("127.0.0.1");
        DaxPayResult<AllocReceiversModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }


}
