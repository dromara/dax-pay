package org.dromara.daxpay.single.sdk.test.trade;

import org.dromara.daxpay.single.sdk.code.ChannelEnum;
import org.dromara.daxpay.single.sdk.code.PayMethodEnum;
import org.dromara.daxpay.single.sdk.code.SignTypeEnum;
import org.dromara.daxpay.single.sdk.model.trade.pay.PayResultModel;
import org.dromara.daxpay.single.sdk.net.DaxPayConfig;
import org.dromara.daxpay.single.sdk.net.DaxPayKit;
import org.dromara.daxpay.single.sdk.param.channel.AlipayParam;
import org.dromara.daxpay.single.sdk.param.channel.WechatPayParam;
import org.dromara.daxpay.single.sdk.param.trade.pay.PayParam;
import org.dromara.daxpay.single.sdk.response.DaxPayResult;
import org.dromara.daxpay.single.sdk.util.JsonUtil;
import org.dromara.daxpay.single.sdk.util.PaySignUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * 统一支付接口
 * @author xxm
 * @since 2024/2/5
 */
public class PayOrderTest {

    @Before
    public void init() {
        // 初始化支付配置
        DaxPayConfig config = DaxPayConfig.builder()
                .serviceUrl("http://127.0.0.1:9999")
                .signSecret("123456")
                .appId("M8207639754663343")
                .signType(SignTypeEnum.HMAC_SHA256)
                .build();
        DaxPayKit.initConfig(config);
    }

    /**
     * 微信支付(二维码扫码)
     */
    @Test
    public void wxQrPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试微信扫码支付");
        param.setDescription("这是支付备注");
        param.setAmount(BigDecimal.valueOf(200.00));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
        System.out.println(PaySignUtil.hmacSha256Sign(execute, "123456"));
    }

    /**
     * 微信付款码
     */
    @Test
    public void wxBarCode(){
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试微信条码支付");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.BARCODE.getCode());

        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setAuthCode("131513396074955617");
        param.setExtraParam(JsonUtil.toJsonStr(wechatPayParam));
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 微信jsapi
     */
    @Test
    public void wxPayJsApi() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试微信公众号预支付");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.JSAPI.getCode());

        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setOpenId("");
        param.setExtraParam(JsonUtil.toJsonStr(wechatPayParam));

        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 微信h5支付
     */
    @Test
    public void wxPayH5Pay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试微信h5支付");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.WAP.getCode());

        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 微信APP支付
     */
    @Test
    public void wxPayAppPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试微信应用支付");
        param.setDescription("这是备注");
        param.setAmount(BigDecimal.valueOf(10));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.APP.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 支付宝支付(二维码扫码)
     */
    @Test
    public void aliPayQrPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试支付宝扫码支付");
        param.setDescription("这是支付宝扫码支付");
        param.setAmount(BigDecimal.valueOf(10));
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }


    /**
     * 支付宝付款码
     */
    @Test
    public void aliPayBarCode(){
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试支付宝付款码支付");
        param.setDescription("这是支付宝付款码支付");
        param.setAmount(BigDecimal.valueOf(1));
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.BARCODE.getCode());

        AlipayParam alipayParam = new AlipayParam();
        alipayParam.setAuthCode("287109871028487115");
        param.setExtraParam(JsonUtil.toJsonStr(alipayParam));

        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 支付宝h5支付
     */
    @Test
    public void aliPayH5Pay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试支付宝H5支付");
        param.setDescription("这是支付宝H5支付");
        param.setAmount(BigDecimal.valueOf(1.53));
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 支付宝web支付
     */
    @Test
    public void aliPayWebPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试支付宝WEB支付");
        param.setDescription("这是支付宝WEB支付");
        param.setAmount(BigDecimal.valueOf(1.52));
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.WEB.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 支付宝jsapi支付
     */
    @Test
    public void aliPayJsApiPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试支付宝JsApi支付");
        param.setDescription("这是支付宝WEB支付");
        param.setAmount(BigDecimal.valueOf(1.52));
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.JSAPI.getCode());

        AlipayParam alipayParam = new AlipayParam();
        alipayParam.setOpAppId("9021000135649359");
        alipayParam.setOpenId("06599D4kvsqTsdNkN1xG05ZACe29h4bm2hi78vsAEVnGCI2");
        param.setExtraParam(JsonUtil.toJsonStr(alipayParam));

        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 支付宝应用支付
     */
    @Test
    public void aliPayAppPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试支付宝APP支付");
        param.setAmount(BigDecimal.valueOf(1.52));
        param.setChannel(ChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.APP.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:10880/test/callback/notify");

        DaxPayResult<PayResultModel> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
