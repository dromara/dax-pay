package org.dromara.daxpay.sdk.util;

import org.dromara.daxpay.sdk.code.ChannelEnum;
import org.dromara.daxpay.sdk.code.PayLimitPayEnum;
import org.dromara.daxpay.sdk.code.PayMethodEnum;
import org.dromara.daxpay.sdk.param.channel.wechat.WechatPayParam;
import org.dromara.daxpay.sdk.param.trade.pay.PayParam;
import org.dromara.daxpay.sdk.response.DaxNoticeResult;
import org.dromara.daxpay.sdk.result.trade.pay.PayOrderResult;
import cn.hutool.core.lang.TypeReference;
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
        Map<String, String> map = PaySignUtil.toMap(param);
        log.info("转换为有序MAP后的内容: {}",JsonUtil.toJsonStr(map));
        // " 和 \ 特殊符号过滤
        String data = PaySignUtil.createLinkString(map);
        log.info("将MAP拼接字符串, 并过滤掉特殊字符: {}",data);
        // 密钥为123456
        data = data+ "&key=123456";
        // 转大写
        data = data.toUpperCase();
        log.info("添加秘钥并转换为大写的字符串: {}",data);
        log.info("MD5: {}",PaySignUtil.md5(data));
    }

    /**
     * 多层嵌套对象签名测试
     */
    @Test
    public void sign2(){

        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");

        param.setBizOrderNo("P0002");
        param.setTitle("测试接口支付");
        param.setOpenId("6688812");
        WechatPayParam weChatPayParam = new WechatPayParam();
        param.setAuthCode("123456");
        param.setExtraParam(JsonUtil.toJsonStr(weChatPayParam));

        Map<String, String> map = PaySignUtil.toMap(param);
        log.info("转换为有序MAP后的内容: {}",map);
        // " 和 \ 特殊符号过滤
        String data = PaySignUtil.createLinkString(map).replaceAll("\\\"","").replaceAll("\"","");
        log.info("将MAP拼接字符串, 并过滤掉特殊字符: {}",data);
        String sign = "123456";
        data += "&sign="+sign;
        data = data.toUpperCase();
        log.info("添加秘钥并转换为大写的字符串: {}",data);
        log.info("MD5: {}",PaySignUtil.md5(data));
        log.info("HmacSHA256: {}",PaySignUtil.hmacSha256(data,sign));
    }


    /**
     * 响应结果和回调结果验签
     */
    @Test
    public void callbackAndVerifySign(){
        String data = "{\n" +
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
                "  \"sign\" : \"86604c1eb66dc3ef22de28be09d993cb91eb46bcd77ee51b0142240ae7ba50fe\",\n" +
                "  \"resTime\" : \"2025-06-05 17:46:54\",\n" +
                "  \"traceId\" : \"nu2Pezn0FvKd\"\n" +
                "}";
        log.info("notify/callback:{}",data);
        // 转换成实体类, 使用sdk中内置的json工具类转换
        DaxNoticeResult<PayOrderResult> x = JsonUtil.toBean(data, new TypeReference<DaxNoticeResult<PayOrderResult>>() {});
        boolean s1 = PaySignUtil.verifyHmacSha256Sign(x, "bc5b5d592cc34434a27fb57fe923dacc5374da52a4174ff5874768a8215e5fd3", x.getSign());
        log.info("验签结果: {}",s1);
        // 使用map方式验签
        DaxNoticeResult<Map<String,Object>> bean = JsonUtil.toBean(data, new TypeReference<DaxNoticeResult<Map<String,Object>>>() {});
        boolean s2 = PaySignUtil.verifyHmacSha256Sign(bean, "bc5b5d592cc34434a27fb57fe923dacc5374da52a4174ff5874768a8215e5fd3", bean.getSign());
        log.info("验签结果: {}",s2);
    }

}
