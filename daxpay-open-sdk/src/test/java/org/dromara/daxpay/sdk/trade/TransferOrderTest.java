package org.dromara.daxpay.sdk.trade;

import org.dromara.daxpay.sdk.code.ChannelEnum;
import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.code.TransferPayeeTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.trade.transfer.TransferParam;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.transfer.TransferResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 转账测试
 * @author xxm
 * @since 2024/6/20
 */
public class TransferOrderTest {


    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:19999")
                .signSecret("123456")
                .signType(SignTypeEnum.MD5)
                .appId("M8207639754663343")
                .build();
        DaxPayKit.initConfig(config);
    }


    /**
     * 发起转账操作(支付宝)
     */
    @Test
    public void aliTransfer() {
        // 构建参数
        TransferParam param = new TransferParam();
        param.setBizTransferNo("T"+System.currentTimeMillis());
        param.setTitle("测试支付宝转账");
        param.setReason("我要转个账");
        param.setAmount(BigDecimal.valueOf(0.1));
        param.setChannel(ChannelEnum.ALIPAY.getCode());
        param.setPayeeType(TransferPayeeTypeEnum.OPEN_ID.getCode());
        param.setClientIp("127.0.0.1");
        // 使用OpenId
        param.setPayeeAccount("-G8AkkjjVhUl_VAf");
        // 发起请求
        DaxResult<TransferResult> execute = DaxPayKit.execute(param);
        System.out.println("验签: "+ DaxPayKit.verifySign(execute));
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 发起转账操作(微信)
     */
    @Test
    public void transfer() {
        // 构建参数
        TransferParam param = new TransferParam();
        param.setBizTransferNo("T"+System.currentTimeMillis());
        param.setTitle("测试微信转账");
        param.setReason("我要转个账");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setPayeeType(TransferPayeeTypeEnum.OPEN_ID.getCode());
        param.setClientIp("127.0.0.1");
        // 使用OpenId
        param.setPayeeAccount("");
        // 发起请求
        DaxResult<TransferResult> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
