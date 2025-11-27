package org.dromara.daxpay.sdk.notice;

import org.dromara.daxpay.sdk.ApiTestConsent;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.response.DaxNoticeResult;
import org.dromara.daxpay.sdk.trade.pay.PayOrderResult;
import org.dromara.daxpay.sdk.util.JsonSignStrUtil;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.dromara.daxpay.sdk.util.PaySignUtil;
import org.dromara.daxpay.sdk.util.RsaSignUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * 消息测试
 * @author xxm
 * @since 2025/4/7
 */
@Slf4j
public class NoticeTest {
    private DaxPayKit daxPayKit;

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl(ApiTestConsent.PAY_URL)
                .publicKey(ApiTestConsent.PUBLIC_KEY)
                .privateKey(ApiTestConsent.PRIVATE_KEY)
                .mchNo("M1723635576766")
                .appId("M8088873888246277")
                .build();
        this.daxPayKit =  new DaxPayKit(config);
    }

    /**
     * 测试支付消息通知
     */
    @Test
    public void testNotice() {
        String json = "{\n" +
                "  \"mchNo\" : \"M1753406237836\",\n" +
                "  \"appId\" : \"3353666928175077\",\n" +
                "  \"code\" : 0,\n" +
                "  \"msg\" : \"success\",\n" +
                "  \"data\" : {\n" +
                "    \"bizOrderNo\" : \"SDK_1755238329760\",\n" +
                "    \"orderNo\" : \"DEV_P2025081514121070000001\",\n" +
                "    \"outOrderNo\" : \"771f226e3d1649f19968fb67257dcaa5\",\n" +
                "    \"title\" : \"测试微信扫码支付\",\n" +
                "    \"description\" : \"这是支付备注\",\n" +
                "    \"allocation\" : false,\n" +
                "    \"autoAllocation\" : false,\n" +
                "    \"channel\" : \"vbill_pay\",\n" +
                "    \"method\" : \"qrcode\",\n" +
                "    \"amount\" : 0.01,\n" +
                "    \"realAmount\" : 0.01,\n" +
                "    \"refundableBalance\" : 0.01,\n" +
                "    \"status\" : \"fail\",\n" +
                "    \"refundStatus\" : \"no_refund\",\n" +
                "    \"settleStatus\" : \"not_settle\",\n" +
                "    \"closeTime\" : \"2025-08-15 16:01:57\",\n" +
                "    \"expiredTime\" : \"2025-08-15 14:42:10\",\n" +
                "    \"attach\" : \"{回调参数}\",\n" +
                "    \"errorMsg\" : \"更新支付订单失败\"\n" +
                "  },\n" +
                "  \"sign\" : \"UOurSfIWJuYGguQjUYcT8WmqfLcq/xIae/4BJtZshe33lAfbrwwvRKZ9nhIB46ede5LuqfegB91dCp7NAmn3VOwcCLxPP0/QQp1L/5/yTka6DUs3hpqsDeJUGytNROOsCeScStqB6iqdJ/3LDBZzB3X7nppwtbhdWVdm3FUuOnErtlJKGT/MvWKZhXShIOYYu4HWBXZIFh+H4TrfK2ZoxVbR7St287VwbEQRxGhLBNc3B6e33gE7qMyKS0yLZ8cqzMChpiUIGqOWKUivxEuBXWke3J1HHzZvvcyz33nSSYy4nRI+4F2AUfVDOPxk0V4YMkoj5jDD9O5zDcuPzmJrHg==\",\n" +
                "  \"resTime\" : \"2025-08-27 21:03:26\",\n" +
                "  \"traceId\" : \"3jdJgrUW0IK5\"\n" +
                "}";
        DaxNoticeResult<PayOrderResult> bean = JsonUtil.toBean(json, new TypeReference<DaxNoticeResult<PayOrderResult>>() {});
        boolean b = daxPayKit.verifySign(bean);
        log.info("SDK验签结果: "+b);

        // 转换成map
        Map<String, String> map = JsonSignStrUtil.buildSortedMap(json);
        log.info("转换为有序MAP后的内容: {}",map);
        String data = JsonSignStrUtil.buildSignStr(map);
        log.info("拼接字符串: {}",data);
        log.info("签名: {}", RsaSignUtil.sign(data, ApiTestConsent.PRIVATE_KEY));

        // 验签
        boolean verify = PaySignUtil.verify(json, ApiTestConsent.PUBLIC_KEY);
        log.info("验签结果: {}",verify);
    }
}
