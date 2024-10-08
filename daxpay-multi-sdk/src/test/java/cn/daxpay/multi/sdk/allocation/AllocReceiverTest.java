package cn.daxpay.multi.sdk.allocation;

import cn.daxpay.multi.sdk.code.AllocReceiverTypeEnum;
import cn.daxpay.multi.sdk.code.AllocRelationTypeEnum;
import cn.daxpay.multi.sdk.code.ChannelEnum;
import cn.daxpay.multi.sdk.code.SignTypeEnum;
import cn.daxpay.multi.sdk.net.DaxPayConfig;
import cn.daxpay.multi.sdk.net.DaxPayKit;
import cn.daxpay.multi.sdk.param.allocation.AllocReceiverAddParam;
import cn.daxpay.multi.sdk.param.allocation.AllocReceiverRemoveParam;
import cn.daxpay.multi.sdk.param.allocation.QueryAllocReceiverParam;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.daxpay.multi.sdk.result.allocation.AllocReceiverAddModel;
import cn.daxpay.multi.sdk.result.allocation.AllocReceiverRemoveModel;
import cn.daxpay.multi.sdk.result.allocation.AllocReceiversModel;
import cn.daxpay.multi.sdk.util.JsonUtil;
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
                .serviceUrl("http://127.0.0.1:10880")
                .signSecret("123456")
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
        param.setReceiverType(AllocReceiverTypeEnum.ALI_USER_ID.getCode());
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
    }

    /**
     * 查询
     */
    @Test
    public void query() {
        QueryAllocReceiverParam param = new QueryAllocReceiverParam();
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setReceiverNo("1231");
        param.setClientIp("127.0.0.1");
        DaxPayResult<AllocReceiversModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

}
