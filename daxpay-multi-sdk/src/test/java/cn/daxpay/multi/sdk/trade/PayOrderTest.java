package cn.daxpay.multi.sdk.trade;

import cn.daxpay.multi.sdk.code.PayChannelEnum;
import cn.daxpay.multi.sdk.code.PayMethodEnum;
import cn.daxpay.multi.sdk.code.SignTypeEnum;
import cn.daxpay.multi.sdk.param.channel.WechatPayParam;
import cn.daxpay.multi.sdk.result.trade.pay.PayResult;
import cn.daxpay.multi.sdk.net.DaxPayConfig;
import cn.daxpay.multi.sdk.net.DaxPayKit;
import cn.daxpay.multi.sdk.param.trade.pay.PayParam;
import cn.daxpay.multi.sdk.response.DaxPayResult;
import cn.hutool.json.JSONUtil;
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
                .serviceUrl("http://127.0.0.1:10880")
                .signSecret("123456")
                .signType(SignTypeEnum.HMAC_SHA256)
                .mchNo("test")
                .appId("test")
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
        param.setChannel(PayChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("https://abc.com/callback");

        DaxPayResult<PayResult> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
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
        param.setChannel(PayChannelEnum.WECHAT.getCode());
        param.setMethod(PayMethodEnum.BARCODE.getCode());

        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setAuthCode("131513396074955617");
        param.setExtraParam(JSONUtil.toJsonStr(wechatPayParam));
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("https://abc.com/callback");

        DaxPayResult<PayResult> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }

    /**
     * 微信jsapi
     */
    @Test
    public void aliPayJsApi() {
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
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("https://abc.com/callback");

        DaxPayResult<PayResult> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
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
        param.setAmount(BigDecimal.valueOf(1));
        param.setChannel(PayChannelEnum.ALI.getCode());
        param.setMethod(PayMethodEnum.QRCODE.getCode());
        param.setAttach("{回调参数}");
        param.setAllocation(false);
        param.setReturnUrl("https://abc.com/returnurl");
        param.setNotifyUrl("https://abc.com/callback");

        DaxPayResult<PayResult> execute = DaxPayKit.execute(param);
        System.out.println(JSONUtil.toJsonStr(execute));
    }

    /**
     * 支付宝付款码
     */
    @Test
    public void aliPayBarCode(){

    }
}
