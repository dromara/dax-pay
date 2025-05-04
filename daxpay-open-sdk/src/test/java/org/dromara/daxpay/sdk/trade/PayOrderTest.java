package org.dromara.daxpay.sdk.trade;

import org.dromara.daxpay.sdk.code.ChannelEnum;
import org.dromara.daxpay.sdk.code.PayMethodEnum;
import org.dromara.daxpay.sdk.code.SignTypeEnum;
import org.dromara.daxpay.sdk.net.DaxPayConfig;
import org.dromara.daxpay.sdk.net.DaxPayKit;
import org.dromara.daxpay.sdk.param.channel.AdaPayParam;
import org.dromara.daxpay.sdk.param.channel.AlipayParam;
import org.dromara.daxpay.sdk.param.channel.WechatPayParam;
import org.dromara.daxpay.sdk.param.trade.pay.PayParam;
import org.dromara.daxpay.sdk.response.DaxResult;
import org.dromara.daxpay.sdk.result.trade.pay.PayResult;
import org.dromara.daxpay.sdk.util.JsonUtil;
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
                .serviceUrl("http://127.0.0.1:19999")
                .signSecret("123456")
                .signType(SignTypeEnum.MD5)
                .appId("M8207639754663343")
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
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");
        DaxResult<PayResult> execute = DaxPayKit.execute(param);
        // 验签
        System.out.println("验签结果: " + DaxPayKit.verifySign(execute));
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 微信付款码
     */
    @Test
    public void wxBarCode(){
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试微信付款码支付");
        param.setAmount(BigDecimal.valueOf(0.01));
        param.setChannel(ChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.BARCODE.getCode());

        WechatPayParam wechatPayParam = new WechatPayParam();
        param.setAuthCode("131513396074955617");
        param.setExtraParam(JsonUtil.toJsonStr(wechatPayParam));
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setOpenId("11111openid");

        WechatPayParam wechatPayParam = new WechatPayParam();
        param.setExtraParam(JsonUtil.toJsonStr(wechatPayParam));

        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setChannel(ChannelEnum.ALIPAY.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setChannel(ChannelEnum.ALIPAY.getCode());
        param.setMethod(PayMethodEnum.BARCODE.getCode());

        AlipayParam alipayParam = new AlipayParam();
        param.setAuthCode("287109871028487115");
        param.setExtraParam(JsonUtil.toJsonStr(alipayParam));

        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setChannel(ChannelEnum.ALIPAY.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setChannel(ChannelEnum.ALIPAY.getCode());
        param.setMethod(PayMethodEnum.WEB.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setChannel(ChannelEnum.ALIPAY.getCode());
        param.setMethod(PayMethodEnum.JSAPI.getCode());
        param.setOpenId("06599D4kvsqTsdNkN1xG05ZACe29h4bm2hi78vsAEVnGCI2");

        AlipayParam alipayParam = new AlipayParam();
        alipayParam.setOpAppId("9021000135649359");
        param.setExtraParam(JsonUtil.toJsonStr(alipayParam));

        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
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
        param.setChannel(ChannelEnum.ALIPAY.getCode());
        param.setMethod(PayMethodEnum.APP.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }

    /**
     * 三方平台的其他类型支付
     */
    @Test
    public void otherPay() {
        PayParam param = new PayParam();
        param.setClientIp("127.0.0.1");
        param.setBizOrderNo("SDK_"+ System.currentTimeMillis());
        param.setTitle("测试汇付的其他支付方式");
        param.setDescription("这是汇付支付宝小程序支付");
        param.setAmount(BigDecimal.valueOf(1.52));
        param.setChannel(ChannelEnum.ADA_PAY.getCode());
        param.setMethod(PayMethodEnum.OTHER.getCode());
        // 使用支付宝小程序支付类型
        param.setOtherMethod("alipay_lite");
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        AdaPayParam adaPayParam = new AdaPayParam();
        adaPayParam.setOpenId("9021000135649359");
        param.setExtraParam(JsonUtil.toJsonStr(adaPayParam));

        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("http://127.0.0.1:19999/test/callback/notify");

        DaxResult<PayResult> execute = DaxPayKit.execute(param);
        System.out.println(JsonUtil.toJsonStr(execute));
    }
}
