package org.dromara.daxpay.sdk.util;

import org.dromara.daxpay.sdk.code.ChannelEnum;
import org.dromara.daxpay.sdk.code.PayLimitPayEnum;
import org.dromara.daxpay.sdk.code.PayMethodEnum;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.channel.WechatPayParam;
import org.dromara.daxpay.sdk.param.trade.pay.PayParam;
import org.dromara.daxpay.sdk.response.DaxNoticeResult;
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
        String data = PaySignUtil.createLinkString(map);
        log.info("将MAP拼接字符串, 并过滤掉特殊字符: {}",data);
        data = data+ "&key=123456";
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
     * 验签测试
     */
    @Test
    public void verifySign(){
        String json = "{\"noticeType\":\"pay\",\"mchNo\":\"M1723635576766\",\"appId\":\"M8088873888246277\",\"code\":0,\"msg\":\"success\",\"data\":{\"bizOrderNo\":\"20250221230917\",\"orderNo\":\"DEV_P2025022123091870000001\",\"title\":\"扫码支付\",\"allocation\":false,\"autoAllocation\":false,\"channel\":\"ali_pay\",\"method\":\"barcode\",\"amount\":0.01,\"refundableBalance\":0.01,\"status\":\"close\",\"refundStatus\":\"no_refund\",\"closeTime\":\"2025-02-21 23:40:00\",\"expiredTime\":\"2025-02-21 23:39:18\",\"errorMsg\":\"支付失败: 支付失败，获取顾客账户信息失败，请顾客刷新付款码后重新收款，如再次收款失败，请联系管理员处理。[SOUNDWAVE_PARSER_FAIL]\"},\"sign\":\"91ba428dc3a6ca17051d1835c8d24703cf2e10434acb337b0a43cc081f7fe45c\",\"resTime\":\"2025-04-10 23:45:42\",\"traceId\":\"BgSPIlOLsRBx\"}";
        DaxNoticeResult<?> bean = JsonUtil.toBean(json, DaxNoticeResult.class);
        boolean b = DaxPayKit.verifySign(bean);
        System.out.println("验签结果: "+b);

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
