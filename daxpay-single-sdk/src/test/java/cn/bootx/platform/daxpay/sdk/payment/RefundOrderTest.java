package cn.bootx.platform.daxpay.sdk.payment;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.SignTypeEnum;
import cn.bootx.platform.daxpay.sdk.model.refund.RefundModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayConfig;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.refund.RefundChannelParam;
import cn.bootx.platform.daxpay.sdk.param.refund.RefundParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 退款接口测试
 * 支持部分退款和全部退款, 部分退款的次数不要超过10次, 否则可能会出现不可知的错误
 *
 * @author xxm
 * @since 2024/2/5
 */
public class RefundOrderTest {

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
     * 全部退款
     */
    @Test
    public void refundAllOrder(){
        RefundParam param = new RefundParam();
        param.setClientIp("127.0.0.1");
        param.setNotNotify(true);

        param.setBusinessNo("P0001");
        param.setRefundAll(true);
        param.setRefundNo("R0001");

        DaxPayResult<RefundModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }

    /**
     * 部分退款(单通道)
     */
    @Test
    public void refundPartOrder(){
        RefundParam param = new RefundParam();
        param.setClientIp("127.0.0.1");

        param.setBusinessNo("P0001");
        param.setRefundAll(false);
        param.setRefundNo("R0001");

        // 设置具体的退款参数
        RefundChannelParam refundChannelParam = new RefundChannelParam();
        refundChannelParam.setChannel(PayChannelEnum.ALI.getCode());
        refundChannelParam.setAmount(12);
        List<RefundChannelParam> refundChannels = Collections.singletonList(refundChannelParam);
        param.setRefundChannels(refundChannels);

        DaxPayResult<RefundModel> execute = DaxPayKit.execute(param);
        System.out.println(execute);
        System.out.println(execute.getData());
    }

}
