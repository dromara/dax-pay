package cn.daxpay.single.core.util;

import cn.daxpay.single.param.channel.AliPayParam;
import cn.daxpay.single.param.payment.pay.PayParam;
import cn.daxpay.single.util.PaySignUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付签名服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
class PayParamSignTest {

    @Test
    void toMap() {
        PayParam payParam = new PayParam();
        payParam.setBizOrderNo("123");
        payParam.setClientIp("127.0.0.1");
        payParam.setNotNotify(true);
        payParam.setNotifyUrl("http://127.0.0.1:8080/pay/notify");
        payParam.setReturnUrl("http://127.0.0.1:8080/pay/return");
        // 传入的话需要传输时间戳
        payParam.setReqTime(LocalDateTime.now());

        AliPayParam aliPayParam = new AliPayParam();
        aliPayParam.setAuthCode("6688");

        payParam.setExtraParam(BeanUtil.beanToMap(aliPayParam,false,true));


        Map<String, String> map = PaySignUtil.toMap(payParam);
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

}
