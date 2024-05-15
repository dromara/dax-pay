package cn.daxpay.single.sdk.payment;

import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.model.allocation.AllocationModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.allocation.AllocationParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.util.RandomUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * 支付分账测试
 * @author xxm
 * @since 2024/4/6
 */
public class PayAllocationTest {

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
     * 开启分账
     */
    @Test
    public void allocation() {
        // 分账参数
        AllocationParam param = new AllocationParam();
        param.setBizAllocationNo("A"+ RandomUtil.randomNumbers(5));
        param.setDescription("测试分账");
        param.setAllocationGroupId(1L);
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("112324");

        DaxPayResult<AllocationModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
    }

}
