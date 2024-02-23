package cn.bootx.platform.daxpay.core.util;

import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.util.PaySignUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
        payParam.setBusinessNo("123");
        payParam.setClientIp("127.0.0.1");
        payParam.setNotNotify(true);
        payParam.setNotifyUrl("http://127.0.0.1:8080/pay/notify");
        payParam.setReturnUrl("http://127.0.0.1:8080/pay/return");
        // 传入的话需要传输时间戳
        payParam.setReqTime(LocalDateTime.now());

        PayChannelParam p1 = new PayChannelParam();
        p1.setAmount(100);
        p1.setChannel("wechat");
        p1.setWay("wx_app");
        AliPayParam aliPayParam = new AliPayParam();
        aliPayParam.setAuthCode("6688");
        p1.setChannelParam(BeanUtil.beanToMap(aliPayParam,false,true));

        PayChannelParam p2 = new PayChannelParam();
        p2.setAmount(100);
        p2.setChannel("wechat");
        p2.setWay("wx_app");
        WeChatPayParam weChatPayParam = new WeChatPayParam();
        weChatPayParam.setOpenId("w2qsz2xawe3gbhyyff28fs01fd");
        weChatPayParam.setAuthCode("8866");
        p2.setChannelParam(BeanUtil.beanToMap(aliPayParam,false,true));
        List<PayChannelParam> payWays = Arrays.asList(p1, p2);
        payParam.setPayChannels(payWays);


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
