package cn.bootx.platform.daxpay.core.payment.common.service;

import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.channel.WeChatPayParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.kit.PayKit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 支付签名服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
class PaymentSignServiceTest {

    @Test
    void toMap() {
        PayParam payParam = new PayParam();
        payParam.setBusinessNo("123");
        payParam.setClientIp("127.0.0.1");
        payParam.setNotNotify(true);
        payParam.setNotReturn(true);
        payParam.setNotifyUrl("http://127.0.0.1:8080/pay/notify");
        payParam.setReturnUrl("http://127.0.0.1:8080/pay/return");
        payParam.setSignType("MD5");
        payParam.setVersion("1.0");

        PayWayParam p1 = new PayWayParam();
        p1.setAmount(100);
        p1.setChannel("wechat");
        p1.setWay("wx_app");
        AliPayParam aliPayParam = new AliPayParam();
        aliPayParam.setAuthCode("6688");
        p1.setChannelExtra(JSONUtil.toJsonStr(aliPayParam));

        PayWayParam p2 = new PayWayParam();
        p2.setAmount(100);
        p2.setChannel("wechat");
        p2.setWay("wx_app");
        WeChatPayParam weChatPayParam = new WeChatPayParam();
        weChatPayParam.setOpenId("w2qsz2xawe3gbhyyff28fs01fd");
        weChatPayParam.setAuthCode("8866");
        p2.setChannelExtra(JSONUtil.toJsonStr(weChatPayParam));
        List<PayWayParam> payWays = Arrays.asList(p1, p2);
        payParam.setPayWays(payWays);
        Map<String, String> map = payParam.toMap();
        log.info("map: {}",map);
        String data = PayKit.createLinkString(map);

        log.info("拼接字符串: {}",data);
        String sign = "123456";
        data += "&sign="+sign;
        data = data.toUpperCase();
        log.info("添加秘钥并转换为大写的字符串: {}",data);
        log.info("MD5: {}",PayKit.md5(data));
        log.info("HmacSHA256: {}",PayKit.hmacSha256(data,sign));

    }

}
