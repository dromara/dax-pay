package cn.bootx.platform.daxpay.sdk.payment;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.code.SignTypeEnum;
import cn.bootx.platform.daxpay.sdk.model.allocation.AllocationModel;
import cn.bootx.platform.daxpay.sdk.model.pay.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.allocation.AllocationParam;
import cn.bootx.platform.daxpay.sdk.param.pay.SimplePayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
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
     * 创建用于分账的订单
     */
    @Test
    public void simplePay() {
        // 简单支付参数
        SimplePayParam param = new SimplePayParam();
        param.setBusinessNo("P"+ RandomUtil.randomNumbers(5));
        param.setAmount(10);
        param.setTitle("测试分账支付");
        param.setChannel(PayChannelEnum.WECHAT.getCode());
        param.setPayWay(PayWayEnum.QRCODE.getCode());
        param.setClientIp("127.0.0.1");
        param.setNotNotify(true);
        param.setAllocation(true);

        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        PayOrderModel data = execute.getData();
        System.out.println(data);
    }
    /**
     * 开启分账
     */
    @Test
    public void allocation() {
        // 分账参数
        AllocationParam param = new AllocationParam();
        param.setAllocationNo("A"+ RandomUtil.randomNumbers(5));
        param.setDescription("测试分账");
        param.setAllocationGroupId(1L);
        param.setClientIp("127.0.0.1");
        param.setPaymentId(1L);

        DaxPayResult<AllocationModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
    }

}
