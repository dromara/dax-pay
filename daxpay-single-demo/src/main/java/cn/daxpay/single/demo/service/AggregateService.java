package cn.daxpay.single.demo.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.daxpay.single.demo.code.AggregatePayEnum;
import cn.daxpay.single.demo.configuration.DaxPayDemoProperties;
import cn.daxpay.single.demo.domain.AggregatePayInfo;
import cn.daxpay.single.demo.param.AggregateSimplePayParam;
import cn.daxpay.single.demo.result.PayOrderResult;
import cn.daxpay.single.demo.result.WxJsapiSignResult;
import cn.daxpay.single.sdk.code.PayChannelEnum;
import cn.daxpay.single.sdk.code.PayMethodEnum;
import cn.daxpay.single.sdk.model.assist.WxAccessTokenModel;
import cn.daxpay.single.sdk.model.assist.WxAuthUrlModel;
import cn.daxpay.single.sdk.model.pay.PayModel;
import cn.daxpay.single.sdk.net.DaxPayKit;
import cn.daxpay.single.sdk.param.assist.WxAccessTokenParam;
import cn.daxpay.single.sdk.param.assist.WxAuthUrlParam;
import cn.daxpay.single.sdk.param.channel.BarCodePayParam;
import cn.daxpay.single.sdk.param.channel.WeChatPayParam;
import cn.daxpay.single.sdk.param.pay.PayParam;
import cn.daxpay.single.sdk.response.DaxPayResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 聚合支付
 * @author xxm
 * @since 2024/2/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateService {
    private final DaxPayDemoProperties daxPayDemoProperties;
    private final RedisClient redisClient;

    private final String PREFIX_KEY = "payment:aggregate:";

    /**
     * 创建聚合支付码连接
     */
    public String createUrl(AggregateSimplePayParam param) {
        int amount = param.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValue();
        AggregatePayInfo aggregatePayInfo = new AggregatePayInfo()
                .setTitle(param.getTitle())
                .setBusinessNo(param.getBusinessNo())
                .setAllocation(param.getAllocation())
                .setAmount(amount);
        String code = IdUtil.getSnowflakeNextIdStr();
        String serverUrl = daxPayDemoProperties.getServerUrl();
        // 有效期五分钟
        redisClient.setWithTimeout(PREFIX_KEY + code, JSONUtil.toJsonStr(aggregatePayInfo), 5 * 60 * 1000);
        return StrUtil.format("{}/demo/aggregate/qrPayPage/{}", serverUrl,code);
    }

    /**
     * 获取聚合支付信息
     */
    public AggregatePayInfo getInfo(String key) {
        String jsonStr = Optional.ofNullable(redisClient.get(PREFIX_KEY + key))
                .orElseThrow(() -> new BizException("聚合支付码已过期..."));
        return JSONUtil.toBean(jsonStr, AggregatePayInfo.class);
    }

    /**
     * 聚合支付扫码跳转中间页
     */
    public String qrPayPage(String code, String ua){
        // 判断是否过期
        boolean exists = redisClient.exists(PREFIX_KEY + code);
        if (!exists){
            // 跳转到过期页面
            return StrUtil.format("{}/result/error?msg={}", daxPayDemoProperties.getFrontH5Url(), URLEncodeUtil.encode("聚合支付码已过期..."));
        }
        // 根据UA判断是什么环境
        if (ua.contains(AggregatePayEnum.UA_ALI_PAY.getCode())) {
            // 跳转支付宝中间页
            return StrUtil.format("{}/aggregate/alipay?code={}", daxPayDemoProperties.getFrontH5Url(),code);
        }
        // 微信支付
        else if (ua.contains(AggregatePayEnum.UA_WECHAT_PAY.getCode())) {
            // 微信重定向到中间页, 因为微信需要授权后才能发起支付
            return this.wxJsapiAuthPage(code);
        }
        // 云闪付
        else if (ua.contains(AggregatePayEnum.UA_UNION_PAY.getCode())) {
            return StrUtil.format("{}/aggregate/wechatPay?code={}", daxPayDemoProperties.getFrontH5Url(),code);
        } else {
            // 跳转到异常页
            return StrUtil.format("{}/result/error?msg={}", daxPayDemoProperties.getFrontH5Url(), URLEncodeUtil.encode("请使用微信或支付宝扫码支付"));
        }
    }

    /**
     * 微信jsapi支付 - 跳转到授权页面
     */
    private String wxJsapiAuthPage(String code) {
        // 回调地址为 结算台微信jsapi支付的回调地址
        WxAuthUrlParam wxAuthUrlParam = new WxAuthUrlParam();
        wxAuthUrlParam.setState(code);

        String url = StrUtil.format("{}/demo/aggregate/wxAuthCallback", daxPayDemoProperties.getServerUrl());
        wxAuthUrlParam.setUrl(url);
        wxAuthUrlParam.setState(code);
        DaxPayResult<WxAuthUrlModel> execute = DaxPayKit.execute(wxAuthUrlParam);
        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        // 微信授权页面链接
        return execute.getData().getUrl();
    }


    /**
     * 微信授权回调页面, 然后重定向到前端页面中, 携带openid
     */
    public String wxAuthCallback(String aggregateCode, String authCode) {
        // 获取微信OpenId
        String openId = this.getOpenId(authCode);
        return StrUtil.format("{}/aggregate/wechatPay?aggregateCode={}&openId={}",
                daxPayDemoProperties.getFrontH5Url(),
                aggregateCode,
                openId);
    }

    /**
     * 调用微信支付, 返回jsapi信息, 从页面调起支付
     */
    public WxJsapiSignResult wxJsapiPrePay(String aggregateCode, String openId) {
        AggregatePayInfo aggregatePayInfo = getInfo(aggregateCode);
        // 拼装支付发起参数
        PayParam simplePayParam = new PayParam();
        simplePayParam.setBizOrderNo(aggregatePayInfo.getBusinessNo());
        simplePayParam.setTitle(aggregatePayInfo.getTitle());
        simplePayParam.setAmount(aggregatePayInfo.getAmount());
        simplePayParam.setChannel(PayChannelEnum.WECHAT.getCode());
        simplePayParam.setMethod(PayMethodEnum.JSAPI.getCode());

        // 设置微信专属请求参数
        WeChatPayParam weChatPayParam = new WeChatPayParam();
        weChatPayParam.setOpenId(openId);
        simplePayParam.setExtraParam(weChatPayParam);

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("127.0.0.1");
        simplePayParam.setClientIp(ip);
        // 异步回调地址
        simplePayParam.setNotifyUrl(StrUtil.format("{}/result/success", daxPayDemoProperties.getFrontH5Url()));
        // 同步回调地址
        simplePayParam.setReturnUrl(StrUtil.format("{}/result/success", daxPayDemoProperties.getFrontH5Url()));

        DaxPayResult<PayModel> execute = DaxPayKit.execute(simplePayParam);
        // 判断是否支付成功
        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        return JSONUtil.toBean(execute.getData().getPayBody(), WxJsapiSignResult.class);
    }

    /**
     * 支付宝发起支付 使用 wep 支付调起支付
     */
    public PayOrderResult aliH5Pay(String code) {
        AggregatePayInfo aggregatePayInfo = getInfo(code);
        PayParam payParam = new PayParam();
        payParam.setBizOrderNo(aggregatePayInfo.getBusinessNo());
        payParam.setTitle(aggregatePayInfo.getTitle());
        payParam.setAmount(aggregatePayInfo.getAmount());
        payParam.setChannel(PayChannelEnum.ALI.getCode());
        payParam.setMethod(PayMethodEnum.WAP.getCode());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("127.0.0.1");
        payParam.setClientIp(ip);
        // 异步回调地址
        payParam.setNotNotify(false);
        // 支付成功同步回调地址
        payParam.setReturnUrl(StrUtil.format("{}/result/success", daxPayDemoProperties.getFrontH5Url()));
        // 中途退出 目前经测试不生效
        payParam.setQuitUrl(StrUtil.format("{}/result/error", daxPayDemoProperties.getFrontH5Url()));

        DaxPayResult<PayModel> execute = DaxPayKit.execute(payParam);

        // 判断是否支付成功
        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        PayOrderResult payOrderResult = new PayOrderResult();
        BeanUtil.copyProperties(execute.getData(),payOrderResult);
        return payOrderResult;
    }

    /**
     * 条码支付
     */
    public PayOrderResult barPay(AggregateSimplePayParam param){
        // 判断通道属于什么
        PayChannelEnum payChannel = this.getPayChannel(param.getAuthCode());

        int amount = param.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValue();

        PayParam simplePayParam = new PayParam();
        simplePayParam.setBizOrderNo(param.getBusinessNo());
        simplePayParam.setAllocation(param.getAllocation());
        simplePayParam.setTitle(param.getTitle());
        simplePayParam.setAmount(amount);
        simplePayParam.setChannel(payChannel.getCode());
        simplePayParam.setMethod(PayMethodEnum.BARCODE.getCode());
        BarCodePayParam barCodePayParam = new BarCodePayParam();
        barCodePayParam.setAuthCode(param.getAuthCode());
        simplePayParam.setExtraParam(barCodePayParam);

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("127.0.0.1");
        simplePayParam.setClientIp(ip);

        DaxPayResult<PayModel> execute = DaxPayKit.execute(simplePayParam);

        // 判断是否支付成功
        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        PayOrderResult payOrderResult = new PayOrderResult();
        BeanUtil.copyProperties(execute.getData(),payOrderResult);
        return payOrderResult;
    }

    /**
     * 根据付款码判断属于那种支付通道
     */
    private PayChannelEnum getPayChannel(String authCode) {
        if (StrUtil.isBlank(authCode)) {
            throw new BizException("付款码不可为空");
        }
        String[] wx = { "10", "11", "12", "13", "14", "15" };
        String[] ali = { "25", "26", "27", "28", "29", "30" };
        String[] union = { "62" };

        // 微信
        if (StrUtil.startWithAny(authCode.substring(0, 2), wx)) {
            return PayChannelEnum.WECHAT;
        }
        // 支付宝
        else if (StrUtil.startWithAny(authCode.substring(0, 2), ali)) {
            return PayChannelEnum.ALI;
        }
        // 云闪付
        else if (StrUtil.startWithAny(authCode.substring(0, 2), union)) {
            return PayChannelEnum.UNION_PAY;
        }
        else {
            throw new BizException("不支持的支付方式");
        }
    }

    /**
     * 获取微信OpenId
     */
    private String getOpenId(String authCode) {
        // 获取OpenId
        WxAccessTokenParam param = new WxAccessTokenParam();
        param.setCode(authCode);

        DaxPayResult<WxAccessTokenModel> result = DaxPayKit.execute(param);
        // 判断是否支付成功
        if (result.getCode() != 0){
            throw new BizException(result.getMsg());
        }
        return result.getData().getOpenId();
    }

}
