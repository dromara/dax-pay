package cn.bootx.platform.daxpay.demo.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.daxpay.demo.code.AggregatePayEnum;
import cn.bootx.platform.daxpay.demo.configuration.DaxPayDemoProperties;
import cn.bootx.platform.daxpay.demo.param.CashierSimplePayParam;
import cn.bootx.platform.daxpay.demo.result.PayOrderResult;
import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayMethodEnum;
import cn.bootx.platform.daxpay.sdk.code.PayStatusEnum;
import cn.bootx.platform.daxpay.sdk.model.assist.WxAccessTokenModel;
import cn.bootx.platform.daxpay.sdk.model.assist.WxAuthUrlModel;
import cn.bootx.platform.daxpay.sdk.model.pay.PayModel;
import cn.bootx.platform.daxpay.sdk.model.pay.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.assist.WxAccessTokenParam;
import cn.bootx.platform.daxpay.sdk.param.assist.WxAuthUrlParam;
import cn.bootx.platform.daxpay.sdk.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.sdk.param.channel.WeChatPayParam;
import cn.bootx.platform.daxpay.sdk.param.pay.PayParam;
import cn.bootx.platform.daxpay.sdk.param.pay.QueryPayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付结算台服务类
 * @author xxm
 * @since 2024/2/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierService {
    private final DaxPayDemoProperties daxPayDemoProperties;

    /**
     * 结算台简单支付, 创建支付订单并拉起支付
     */
    public PayOrderResult simplePayCashier(CashierSimplePayParam param){
        // 将参数转换为简单支付参数
        PayParam payParam = new PayParam();
        payParam.setBizOrderNo(param.getBizOrderNo());
        payParam.setAllocation(param.getAllocation());
        int amount = param.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValue();
        payParam.setTitle(param.getTitle());
        payParam.setAmount(amount);
        payParam.setChannel(param.getChannel());
        payParam.setMethod(param.getMethod());

        // 支付宝通道
        if (Objects.equals(PayChannelEnum.ALI.getCode(), param.getChannel())){
            // 付款码支付
            if (Objects.equals(PayMethodEnum.BARCODE.getCode(), param.getMethod())){
                AliPayParam aliPayParam = new AliPayParam();
                aliPayParam.setAuthCode(param.getAuthCode());
                payParam.setExtraParam(aliPayParam);
            }
        }
        // 微信通道
        if (Objects.equals(PayChannelEnum.WECHAT.getCode(), param.getChannel())){
            WeChatPayParam wechatPayParam = new WeChatPayParam();
            // 付款码支付
            if (Objects.equals(PayMethodEnum.BARCODE.getCode(), param.getMethod())){
                wechatPayParam.setAuthCode(param.getAuthCode());
                payParam.setExtraParam(wechatPayParam);
            }
            // 微信jsapi 方式支付
            if (Objects.equals(PayMethodEnum.JSAPI.getCode(), param.getMethod())){
                wechatPayParam.setOpenId(param.getOpenId());
                payParam.setExtraParam(wechatPayParam);
            }
        }
        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("127.0.0.1");
        payParam.setClientIp(ip);
        // 同步回调地址
        payParam.setReturnUrl(StrUtil.format("{}/result/success", daxPayDemoProperties.getFrontH5Url()));

        // 发起支付
        DaxPayResult<PayModel> execute = DaxPayKit.execute(payParam);
        // 判断是否支付成功
        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        // 判断是否发起支付成功
        PayModel payModel = execute.getData();

        // 状态 支付中或支付完成返回
        List<String> list = Arrays.asList(PayStatusEnum.PROGRESS.getCode(), PayStatusEnum.SUCCESS.getCode());
        if (list.contains(payModel.getStatus())){
            PayOrderResult payOrderResult = new PayOrderResult();
            BeanUtil.copyProperties(payModel, payOrderResult);
            return payOrderResult;
        } else {
            throw new BizException("订单状态异常，无法进行支付");
        }
    }

    /**
     * 查询订单是否支付成功
     */
    public boolean queryPayOrderSuccess(String businessNo){
        QueryPayParam queryPayOrderParam = new QueryPayParam();
        queryPayOrderParam.setBizOrderNoeNo(businessNo);
        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(queryPayOrderParam);
        // 未查询到订单
        if (execute.getCode() == 10010){
            return false;
        }

        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        PayOrderModel data = execute.getData();
        String status = data.getStatus();
        if (Objects.equals(status, PayStatusEnum.PROGRESS.getCode())){
            return false;
        }
        else if (Objects.equals(status, PayStatusEnum.SUCCESS.getCode())){
            return true;
        } else {
            throw new BizException("订单状态不是支付中或支付完成，请检查");
        }
    }

    /**
     * 获取当前可用的支付环境
     */
    public String getPayEnv(String ua){
        if (ua.contains(AggregatePayEnum.UA_ALI_PAY.getCode())) {
            return PayChannelEnum.ALI.getCode();
        } else if (ua.contains(AggregatePayEnum.UA_WECHAT_PAY.getCode())) {
            return PayChannelEnum.WECHAT.getCode();
        } else {
            // 普通浏览器可以使用全部支付通道
            return "all";
        }
    }

    /**
     * 获取手机收银台链接
     */
    public String getUniCashierUrl() {
        return StrUtil.format("{}/cashier/uniCashier", daxPayDemoProperties.getFrontH5Url());
    }

    /**
     * 微信jsapi支付 - 跳转到授权页面
     */
    public String getWxAuthUrl() {
        // 回调地址为 结算台微信jsapi支付的回调地址
        WxAuthUrlParam wxAuthUrlParam = new WxAuthUrlParam();
        String url = StrUtil.format("{}/demo/cashier/wxAuthCallback", daxPayDemoProperties.getServerUrl());
        wxAuthUrlParam.setUrl(url);
        DaxPayResult<WxAuthUrlModel> execute = DaxPayKit.execute(wxAuthUrlParam);
        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        // 微信授权页面链接
        return execute.getData().getUrl();
    }

    /**
     * 微信授权回调页面, 然后重定向到统一收银台中, 携带openid
     */
    public String wxAuthCallback(String code) {
        return StrUtil.format("{}/cashier/uniCashier?source=redirect&openId={}", daxPayDemoProperties.getFrontH5Url(), this.getOpenId(code));
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
