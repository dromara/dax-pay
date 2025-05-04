package org.dromara.daxpay.sdk.notice;

import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.response.DaxNoticeResult;
import org.dromara.daxpay.sdk.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
import org.dromara.daxpay.sdk.util.PaySignUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 消息测试
 * @author xxm
 * @since 2025/4/7
 */
@Slf4j
public class NoticeTest {


    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:19999")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .appId("M8088873888246277")
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 测试支付消息通知
     */
    @Test
    public void testNotice() {
        String json = "{\"noticeType\":\"pay\",\"mchNo\":\"M1723635576766\",\"appId\":\"M8088873888246277\",\"code\":0,\"msg\":\"success\",\"data\":{\"bizOrderNo\":\"20250221230917\",\"orderNo\":\"DEV_P2025022123091870000001\",\"title\":\"扫码支付\",\"allocation\":false,\"autoAllocation\":false,\"channel\":\"ali_pay\",\"method\":\"barcode\",\"amount\":0.01,\"refundableBalance\":0.01,\"status\":\"close\",\"refundStatus\":\"no_refund\",\"closeTime\":\"2025-02-21 23:40:00\",\"expiredTime\":\"2025-02-21 23:39:18\",\"errorMsg\":\"支付失败: 支付失败，获取顾客账户信息失败，请顾客刷新付款码后重新收款，如再次收款失败，请联系管理员处理。[SOUNDWAVE_PARSER_FAIL]\"},\"sign\":\"91ba428dc3a6ca17051d1835c8d24703cf2e10434acb337b0a43cc081f7fe45c\",\"resTime\":\"2025-04-10 23:45:42\",\"traceId\":\"BgSPIlOLsRBx\"}";
        DaxNoticeResult<PayOrderResult> bean = JsonUtil.toBean(json, new TypeReference<DaxNoticeResult<PayOrderResult>>() {
            /**
             * 获取用户定义的泛型参数
             *
             * @return 泛型参数
             */
            @Override
            public Type getType() {
                return super.getType();
            }
        });
        boolean b = DaxPayKit.verifySign(bean);
        System.out.println("验签结果: "+b);

        // 转换成对象

        log.info("参数: {}",JsonUtil.toJsonStr(bean));
        Map<String, String> map = PaySignUtil.toMap(bean);
        map.remove("sign");
        log.info("转换为有序MAP后的内容: {}",JsonUtil.toJsonStr(map));
        String data = PaySignUtil.createLinkString(map);
        log.info("将MAP拼接字符串, 并过滤掉特殊字符: {}",data);
        data = data+ "&key=123456";
        data = data.toUpperCase();
        log.info("添加秘钥并转换为大写的字符串: {}",data);
        log.info("hmacSha256: {}",PaySignUtil.hmacSha256(data, "123456"));

    }
}
