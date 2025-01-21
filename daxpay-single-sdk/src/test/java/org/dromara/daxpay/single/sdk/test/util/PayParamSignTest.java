package org.dromara.daxpay.single.sdk.test.util;

import org.dromara.daxpay.single.sdk.param.channel.WechatPayParam;
import org.dromara.daxpay.single.sdk.param.trade.pay.PayParam;
import org.dromara.daxpay.single.sdk.util.PaySignUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

/**
 * 参数签名测试类
 * @author xxm
 * @since 2024/2/7
 */
@Slf4j
public class PayParamSignTest {

    /**
     * 签名测试
     */
    @Test
    public void sign(){

        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");

        param.setBizOrderNo("P0001");
        param.setTitle("测试接口支付");

        Map<String, String> map = PaySignUtil.toMap(param);
        log.info("转换为有序MAP后的内容: {}",map);
        String data = PaySignUtil.createLinkString(map);
        log.info("将MAP拼接字符串, 并过滤掉特殊字符: {}",data);
        String sign = "123456";
        data += "&sign="+sign;
        data = data.toUpperCase();
        log.info("添加秘钥并转换为大写的字符串: {}",data);
        log.info("MD5: {}",PaySignUtil.md5(data));
        log.info("HmacSHA256: {}",PaySignUtil.hmacSha256(data,sign));
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
        WechatPayParam weChatPayParam = new WechatPayParam();
        weChatPayParam.setOpenId("6688812");
        weChatPayParam.setAuthCode("123456");
        param.setExtraParam(weChatPayParam.toJson());

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

}
