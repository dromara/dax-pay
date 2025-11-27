package org.dromara.daxpay.sdk.util;

import org.dromara.daxpay.sdk.ApiTestConsent;
import org.dromara.daxpay.sdk.code.ChannelEnum;
import org.dromara.daxpay.sdk.code.PayLimitPayEnum;
import org.dromara.daxpay.sdk.code.PayMethodEnum;
import org.dromara.daxpay.sdk.param.channel.wechat.WechatPayParam;
import org.dromara.daxpay.sdk.param.trade.pay.PayParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 参数签名测试类
 * @author xxm
 * @since 2024/2/7
 */
@Slf4j
public class ParamSignTest {
    /**
     * 签名测试
     */
    @Test
    public void sign(){

        PayParam param = new PayParam();
        param.setBizOrderNo("pay_2021520000012254");
        param.setTitle("测试支付商品");
        param.setDescription("测试支付商户的描述");
        param.setAllocation(false);
        param.setAutoAllocation(false);
        param.setExpiredTime(LocalDateTime.now().plusMinutes(30));
        param.setChannel(ChannelEnum.ALIPAY.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setLimitPay(PayLimitPayEnum.NO_CREDIT.getCode());
        param.setAmount(BigDecimal.valueOf(99.60));
        WechatPayParam wechatPayParam = new WechatPayParam()
                .setOpenIdType("sub");
        param.setExtraParam(JsonUtil.toJsonStr(wechatPayParam));
        param.setAttach("{\"order\":\"order_0000001\"");
        param.setReturnUrl("https://pay.daxpay.cn/returnurl");
        param.setNotifyUrl("https://pay.daxpay.cn/notice");
        param.setClientIp("127.0.0.1");
        param.setNonceStr("ww5gjytfsdfe");

        log.info("参数: {}",JsonUtil.toJsonStr(param));
        Map<String, String> map = ObjectSignStrUtil.flatten(param);
        log.info("转换为有序MAP后的内容: {}",JsonUtil.toJsonStr(map));
        String data = ObjectSignStrUtil.buildSignStr(map);
        log.info("将MAP拼接字符串: {}",data);
        // 密钥为123456
        data = data+ "&key=123456";
        // 转大写
        data = data.toUpperCase();
        log.info("添加秘钥并转换为大写的字符串: {}",data);
        log.info("签名: {}",RsaSignUtil.sign(data, ApiTestConsent.PRIVATE_KEY));
    }

    /**
     * 响应结果和回调结果验签
     * 响应结果为嵌套对象
     */
    @Test
    public void callbackAndVerifySign(){
        String json = "{\n" +
                "  \"mchNo\" : \"M1745845174843\",\n" +
                "  \"appId\" : \"A0646259306964009\",\n" +
                "  \"code\" : 0,\n" +
                "  \"msg\" : \"success\",\n" +
                "  \"data\" : {\n" +
                "    \"bizOrderNo\" : \"PAY_492854192101747473544745\",\n" +
                "    \"orderNo\" : \"DEV_P2025051717190770000003\",\n" +
                "    \"title\" : \"测试支付\",\n" +
                "    \"allocation\" : false,\n" +
                "    \"autoAllocation\" : false,\n" +
                "    \"channel\" : \"alipay_isv\",\n" +
                "    \"method\" : \"other\",\n" +
                "    \"otherMethod\" : \"WX_JSAPI\",\n" +
                "    \"amount\" : 0.01,\n" +
                "    \"refundableBalance\" : 0.01,\n" +
                "    \"status\" : \"close\",\n" +
                "    \"refundStatus\" : \"no_refund\",\n" +
                "    \"closeTime\" : \"2025-05-18 10:14:14\",\n" +
                "    \"expiredTime\" : \"2025-05-17 21:41:28\"\n" +
                "  },\n" +
                "  \"sign\" : \"LRHWbuBdBO3O0WiPqDojFyfNyE3nWKZL3OGkxtBCSRHyZAvITkcmDyk0+cwsb/1UIjzXar5Pm/ImeFHehgiU8LRIqMByD3diEe62mfaj23qYjsZOD8ywp/i8acDrlu0mTUaD9Q5cRFfpmY6KqqfZUwynMy2EXZW/TZF7AEf96bQ/ffjEN0LTYedxLCuNXJNbUMCJtl0OKhpxv1qPchb815LVykEicJwda5VdxMyoWY8dPS3iB5bKNSiLkDQ+03f4fIf/jk06CLakr3XX2HiXXR5ryWZUNVhk/9MphCqhiqrhgzY+3PVckGXSh+rD8MDslxrt1ry23QpGR/E/pFwPjg==\",\n" +
                "  \"resTime\" : \"2025-06-05 17:46:54\",\n" +
                "  \"traceId\" : \"nu2Pezn0FvKd\"\n" +
                "}";
        Map<String, String> map = JsonSignStrUtil.buildSortedMap(json);
        log.info("转换为有序MAP后的内容: {}",map);
        String data = JsonSignStrUtil.buildSignStr(map);
        log.info("拼接字符串: {}",data);
        log.info("签名: {}",RsaSignUtil.sign(data, ApiTestConsent.PRIVATE_KEY));

        // 验签
        boolean verify = PaySignUtil.verify(json, ApiTestConsent.PUBLIC_KEY);
        log.info("验签结果: {}",verify);
    }
}
