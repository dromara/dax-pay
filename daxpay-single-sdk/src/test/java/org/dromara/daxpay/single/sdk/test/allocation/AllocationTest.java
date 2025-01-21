package org.dromara.daxpay.single.sdk.test.allocation;

import lombok.var;
import org.dromara.daxpay.single.sdk.code.SignTypeEnum;
import org.dromara.daxpay.single.sdk.net.DaxPayConfig;
import org.dromara.daxpay.single.sdk.net.DaxPayKit;
import org.dromara.daxpay.single.sdk.param.allocation.AllocFinishParam;
import org.dromara.daxpay.single.sdk.param.allocation.AllocSyncParam;
import org.dromara.daxpay.single.sdk.param.allocation.AllocationParam;
import org.dromara.daxpay.single.sdk.param.allocation.QueryAllocOrderParam;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 分账测试类
 * @author xxm
 * @since 2024/12/18
 */
public class AllocationTest {
    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9999")
                .signSecret("123456")
                .appId("M8207639754663343")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 分账发起接口
     */
    @Test
    public void start() {
        // 请求参数
        AllocationParam param = new AllocationParam();
        param.setBizAllocNo("SDK_"+ System.currentTimeMillis());
        param.setOrderNo("123");
        var execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 分账完结接口
     */
    @Test
    public void finish() {
        // 请求参数
        var param = new AllocFinishParam();
        param.setAllocNo("DEVA2024121721431860000035");
        var execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 分账查询接口
     */
    @Test
    public void query() {
        // 请求参数
        var param = new QueryAllocOrderParam();
        param.setAllocNo("DEVA2024121721431860000035");
        var execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 分账同步接口
     */
    @Test
    public void sync() {
        // 请求参数
        var param = new AllocSyncParam();
        param.setAllocNo("DEVA2024121721431860000035");
        var execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
