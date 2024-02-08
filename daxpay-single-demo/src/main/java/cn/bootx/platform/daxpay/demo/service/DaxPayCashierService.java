package cn.bootx.platform.daxpay.demo.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.daxpay.demo.param.CashierSimplePayParam;
import cn.bootx.platform.daxpay.demo.result.PayOrderResult;
import cn.bootx.platform.daxpay.sdk.code.PayChannelEnum;
import cn.bootx.platform.daxpay.sdk.code.PayStatusEnum;
import cn.bootx.platform.daxpay.sdk.code.PayWayEnum;
import cn.bootx.platform.daxpay.sdk.model.pay.PayOrderModel;
import cn.bootx.platform.daxpay.sdk.model.pay.QueryPayOrderModel;
import cn.bootx.platform.daxpay.sdk.net.DaxPayKit;
import cn.bootx.platform.daxpay.sdk.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.sdk.param.channel.WeChatPayParam;
import cn.bootx.platform.daxpay.sdk.param.pay.QueryPayOrderParam;
import cn.bootx.platform.daxpay.sdk.param.pay.SimplePayParam;
import cn.bootx.platform.daxpay.sdk.response.DaxPayResult;
import cn.hutool.core.bean.BeanUtil;
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
public class DaxPayCashierService {

    /**
     * 结算台简单支付, 创建支付订单并拉起支付
     */
    public PayOrderResult simplePayCashier(CashierSimplePayParam param){
        // 将参数转换为简单支付参数
        SimplePayParam simplePayParam = new SimplePayParam();
        simplePayParam.setBusinessNo(param.getBusinessNo());
        int amount = param.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValue();
        simplePayParam.setTitle(param.getTitle());
        simplePayParam.setAmount(amount);
        simplePayParam.setChannel(param.getChannel());
        simplePayParam.setPayWay(param.getPayWay());

        // 支付宝通道
        if (Objects.equals(PayChannelEnum.ALI.getCode(), param.getChannel())){
            // 付款码支付
            if (Objects.equals(PayWayEnum.BARCODE.getCode(), param.getPayWay())){
                AliPayParam aliPayParam = new AliPayParam();
                aliPayParam.setAuthCode(param.getAuthCode());
                simplePayParam.setChannelParam(aliPayParam);
            }
        }
        // 微信通道
        if (Objects.equals(PayChannelEnum.WECHAT.getCode(), param.getChannel())){
            WeChatPayParam wechatPayParam = new WeChatPayParam();
            // 付款码支付
            if (Objects.equals(PayWayEnum.BARCODE.getCode(), param.getPayWay())){
                wechatPayParam.setAuthCode(param.getAuthCode());
                simplePayParam.setChannelParam(wechatPayParam);
            }
            // 微信jsapi 方式支付
            if (Objects.equals(PayWayEnum.JSAPI.getCode(), param.getPayWay())){
                wechatPayParam.setOpenId(param.getOpenId());
                simplePayParam.setChannelParam(wechatPayParam);
            }
        }
        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(ServletUtil::getClientIP)
                .orElse("127.0.0.1");
        simplePayParam.setClientIp(ip);
        // 异步回调地址
        simplePayParam.setNotifyUrl("http://localhost:9000");
        // 同步回调地址
        simplePayParam.setReturnUrl("http://localhost:9000");

        // 发起支付
        DaxPayResult<PayOrderModel> execute = DaxPayKit.execute(simplePayParam);
        // 判断是否支付成功
        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        // 判断是否发起支付成功
        PayOrderModel payOrderModel = execute.getData();

        // 状态 支付中或支付完成返回
        List<String> list = Arrays.asList(PayStatusEnum.PROGRESS.getCode(), PayStatusEnum.SUCCESS.getCode());
        if (list.contains(payOrderModel.getStatus())){
            PayOrderResult payOrderResult = new PayOrderResult();
            BeanUtil.copyProperties(payOrderModel, payOrderResult);
            return payOrderResult;
        } else {
            throw new BizException("订单状态异常，无法进行支付");
        }
    }

    /**
     * 订单查询接口
     */
    public boolean queryPayOrder(Long paymentId){
        QueryPayOrderParam queryPayOrderParam = new QueryPayOrderParam();
        queryPayOrderParam.setPaymentId(paymentId);
        DaxPayResult<QueryPayOrderModel> execute = DaxPayKit.execute(queryPayOrderParam);
        if (execute.getCode() != 0){
            throw new BizException(execute.getMsg());
        }
        QueryPayOrderModel data = execute.getData();
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
}
