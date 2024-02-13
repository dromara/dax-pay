package cn.bootx.platform.daxpay.sdk.util;

import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.param.channel.WeChatPayParam;
import cn.bootx.platform.daxpay.sdk.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.sdk.param.pay.PayParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
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
        param.setNotNotify(true);

        param.setBusinessNo("P0001");
        param.setTitle("测试接口支付");
        PayChannelParam payChannelParam = new PayChannelParam();
        payChannelParam.setChannel(PayChannelEnum.WECHAT.getCode());
        payChannelParam.setWay(PayWayEnum.QRCODE.getCode());
        payChannelParam.setAmount(1);

        List<PayChannelParam> payChannels = Collections.singletonList(payChannelParam);
        param.setPayChannels(payChannels);

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

        param.setBusinessNo("P0002");
        param.setTitle("测试接口支付");
        PayChannelParam payChannelParam = new PayChannelParam();
        payChannelParam.setChannel(PayChannelEnum.WECHAT.getCode());
        payChannelParam.setWay(PayWayEnum.QRCODE.getCode());
        payChannelParam.setAmount(1);
        WeChatPayParam weChatPayParam = new WeChatPayParam();
        weChatPayParam.setOpenId("6688812");
        weChatPayParam.setAuthCode("123456");
        payChannelParam.setChannelParam(weChatPayParam);

        List<PayChannelParam> payChannels = Collections.singletonList(payChannelParam);
        param.setPayChannels(payChannels);

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
