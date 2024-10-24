package cn.daxpay.single.sdk.test.allocation;

import cn.daxpay.single.sdk.code.AllocReceiverTypeEnum;
import cn.daxpay.single.sdk.code.AllocRelationTypeEnum;
import cn.daxpay.single.sdk.code.ChannelEnum;
import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.allocation.AllocReceiverAddModel;
import cn.daxpay.single.sdk.model.allocation.AllocReceiverModel;
import cn.daxpay.single.sdk.model.allocation.AllocReceiverRemoveModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.allocation.AllocReceiverAddParam;
import cn.daxpay.single.sdk.param.allocation.AllocReceiverRemoveParam;
import cn.daxpay.single.sdk.param.allocation.QueryAllocReceiverParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.daxpay.single.sdk.util.JsonUtil;
import cn.daxpay.single.sdk.util.PaySignUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 分账接收者测试类
 * @author xxm
 * @since 2024/5/27
 */
public class AllocReceiverTest {

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
     * 添加
     */
    @Test
    public void add() {
        AllocReceiverAddParam param = new AllocReceiverAddParam();
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setClientIp("127.0.0.1");
        param.setRelationType(AllocRelationTypeEnum.HEADQUARTER.getCode());
        param.setReceiverNo("123456");
        param.setReceiverType(AllocReceiverTypeEnum.USER_ID.getCode());
        param.setReceiverAccount("20881233343");
        DaxPayResult<AllocReceiverAddModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 删除
     */
    @Test
    public void remove() {
        AllocReceiverRemoveParam param = new AllocReceiverRemoveParam();
        param.setClientIp("127.0.0.1");
        param.setReceiverNo("123456");
        DaxPayResult<AllocReceiverRemoveModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
        System.out.println(PaySignUtil.hmacSha256Sign(execute, "123456"));
    }

    /**
     * 列表
     */
    @Test
    public void list() {
        QueryAllocReceiverParam param = new QueryAllocReceiverParam();
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setClientIp("127.0.0.1");
        DaxPayResult<AllocReceiverModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
        System.out.println(PaySignUtil.hmacSha256Sign(execute, "123456"));
    }

}
