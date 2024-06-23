package cn.daxpay.single.sdk.payment;

import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.code.SignTypeEnum;
import cn.daxpay.single.sdk.code.TransferPayeeTypeEnum;
import cn.daxpay.single.sdk.model.transfer.TransferModel;
import cn.daxpay.single.sdk.net.DaxPayConfig;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.transfer.TransferParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;

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
                .serviceUrl("http://127.0.0.1:9000")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }


    /**
     * 发起转账操作
     */
    @Test
    public void transfer() {
        // 构建参数
        TransferParam param = new TransferParam();
        param.setBizTransferNo("T"+System.currentTimeMillis());
        param.setTitle("测试转账");
        param.setReason("我要转个账");
        param.setAmount(500);
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setPayeeType(TransferPayeeTypeEnum.ALI_OPEN_ID.getCode());
        param.setClientIp("127.0.0.1");
        // 使用OpenId
        param.setPayeeAccount("065a9aEjER9Fa__hxYyvgYDlkhUiw_6RINhYHB2oegpWAo5");
        // 发起请求
        DaxPayResult<TransferModel> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }
}
