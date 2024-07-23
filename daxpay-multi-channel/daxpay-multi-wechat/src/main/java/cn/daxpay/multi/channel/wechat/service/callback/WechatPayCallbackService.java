package cn.daxpay.multi.channel.wechat.service.callback;

import cn.daxpay.multi.channel.wechat.code.WechatPayCode;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.channel.wechat.util.WechatPayUtil;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.PayStatusEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.github.binarywang.wxpay.bean.notify.*;
import com.github.binarywang.wxpay.constant.WxPayConstants.WxpayTradeStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.wechat.pay.java.core.http.Constant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 微信支付回调处理服务
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayCallbackService {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 支付回调处理, 解析数据
     *
     */
    public String pay(HttpServletRequest request){
        WechatPayConfig config = wechatPayConfigService.getWechatPayConfig();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        // v2 或 v3
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)) {
            // V2 回调接收处理
            String xml = WechatPayUtil.readV2Data(request);
            try {
                // 转换请求
                WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(xml);
                // 解析数据
                this.resolveV2PayData(wxPayOrderNotifyResult);
                return WxPayNotifyResponse.success("OK");
            } catch (WxPayException e) {
                log.error("微信支付V2回调处理失败", e);
                return WxPayNotifyResponse.fail("FAIL");
            }
        } else {
            // V3 回调接收处理
            String body = JakartaServletUtil.getBody(request);
            Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);
            SignatureHeader signatureHeader = new SignatureHeader();
            signatureHeader.setNonce(headerMap.get(Constant.WECHAT_PAY_NONCE.toLowerCase()));
            signatureHeader.setTimeStamp(headerMap.get(Constant.WECHAT_PAY_TIMESTAMP.toLowerCase()));
            signatureHeader.setSerial(headerMap.get(Constant.WECHAT_PAY_SERIAL.toLowerCase()));
            signatureHeader.setSignature(headerMap.get(Constant.WECHAT_PAY_SIGNATURE.toLowerCase()));
            try {
                // 转换请求
                WxPayNotifyV3Result wxPayNotifyV3Result = wxPayService.parseOrderNotifyV3Result(body, signatureHeader);
                // 解析数据
                this.resolveV3PayData(wxPayNotifyV3Result);
            } catch (WxPayException e) {
                log.error("微信支付V3回调处理失败", e);
                return WxPayNotifyV3Response.fail("FAIL");
            }
            return WxPayNotifyV3Response.success("OK");
        }
    }

    /**
     * 解析数据 v2
     */
    private void resolveV2PayData(WxPayOrderNotifyResult result){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 设置类型和通道
        callbackInfo.setCallbackType(TradeTypeEnum.PAY)
                .setChannel(ChannelEnum.ALI.getCode());
        // 回调数据
        Map<String, String> map = result.toMap();
        callbackInfo.setCallbackData(map);
        // 网关支付号
        callbackInfo.setOutTradeNo(result.getTransactionId());
        // 支付号
        callbackInfo.setTradeNo(result.getOutTradeNo());
        // 支付状态
        PayStatusEnum payStatus = WechatPayUtil.codeIsOk(result.getResultCode()) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callbackInfo.setOutStatus(payStatus.getCode());
        // 支付金额
        callbackInfo.setAmount(PayUtil.conversionAmount(result.getTotalFee()));
        String timeEnd = result.getTimeEnd();
        if (StrUtil.isNotBlank(timeEnd)) {
            LocalDateTime time = WechatPayUtil.parseV2(timeEnd);
            callbackInfo.setFinishTime(time);
        }
    }
    /**
     * 解析数据 v3
     */
    private void resolveV3PayData(WxPayNotifyV3Result v3Result){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        var result = v3Result.getResult();
        // 设置类型和通道
        callbackInfo.setCallbackType(TradeTypeEnum.PAY)
                .setChannel(ChannelEnum.ALI.getCode());
        // 回调数据
        Map<String, Object> map = BeanUtil.beanToMap(result);
        callbackInfo.setCallbackData(map);
        // 网关支付号
        callbackInfo.setOutTradeNo(result.getTransactionId());
        // 支付号
        callbackInfo.setTradeNo(result.getOutTradeNo());
        // 支付状态 - 成功
        if (Arrays.asList(WxpayTradeStatus.SUCCESS,WxpayTradeStatus.REFUND).contains(result.getTradeState())){
            callbackInfo.setOutStatus(PayStatusEnum.SUCCESS.getCode());
        }
        // 支付状态 - 支付中
        if (Objects.equals(result.getTradeState(), WxpayTradeStatus.NOTPAY)){
            callbackInfo.setOutStatus(PayStatusEnum.PROGRESS.getCode());
        }
        // 支付状态 - 失败
        if (Objects.equals(WxpayTradeStatus.PAY_ERROR, result.getTradeState())){
            callbackInfo.setOutStatus(PayStatusEnum.FAIL.getCode());
        }
        // 撤销
        if (Objects.equals(result.getTradeState(), WxpayTradeStatus.REVOKED)){
            callbackInfo.setOutStatus(PayStatusEnum.CANCEL.getCode());
        }
        // 关闭
        if (Objects.equals(result.getTradeState(), WxpayTradeStatus.CLOSED)){
            callbackInfo.setOutStatus(PayStatusEnum.CLOSE.getCode());
        }

        // 支付金额
        callbackInfo.setAmount(PayUtil.conversionAmount(result.getAmount().getTotal()));
        String timeEnd = result.getSuccessTime();
        if (StrUtil.isNotBlank(timeEnd)) {
            callbackInfo.setFinishTime(WechatPayUtil.parseV3(timeEnd));
        }
    }
}
