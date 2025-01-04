package org.dromara.daxpay.channel.wechat.service.callback;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.constant.WxPayConstants.WxpayTradeStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.util.WechatPayUtil;
import org.dromara.daxpay.core.enums.CallbackStatusEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.common.context.CallbackLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.service.record.callback.TradeCallbackRecordService;
import org.dromara.daxpay.service.service.trade.pay.PayCallbackService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    private final PayCallbackService payCallbackService;
    private final TradeCallbackRecordService tradeCallbackRecordService;

    /**
     * 支付回调处理
     */
    public String payHandle(HttpServletRequest request, boolean isv){
        // 解析数据
        if (this.resolve(request,isv)){
            // 执行回调业务处理
            payCallbackService.payCallback();
            // 保存记录
            tradeCallbackRecordService.saveCallbackRecord();
            return WxPayNotifyResponse.success("OK");
        } else {
            // 保存记录
            tradeCallbackRecordService.saveCallbackRecord();
            return WxPayNotifyResponse.fail("FAIL");
        }
    }

    /**
     * 支付回调处理, 解析数据
     */
    public boolean resolve(HttpServletRequest request, boolean isv){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 设置类型和通道
        callbackInfo.setCallbackType(TradeTypeEnum.PAY)
                .setChannel(isv? ChannelEnum.WECHAT_ISV.getCode():ChannelEnum.WECHAT.getCode());

        WechatPayConfig config = wechatPayConfigService.getAndCheckConfig(false);
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        // v2 或 v3
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)) {
            // V2 回调接收处理
            String xml = WechatPayUtil.readV2Data(request);
            callbackInfo.setRawData(xml);
            try {
                // 转换请求
                WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(xml);
                // 解析数据
                this.resolveV2Data(wxPayOrderNotifyResult);
                return true;
            } catch (WxPayException e) {
                log.error("微信支付回调V2处理失败", e);
                callbackInfo.setCallbackStatus(CallbackStatusEnum.FAIL);
                return false;
            }
        } else {
            // V3 回调接收处理
            String body = JakartaServletUtil.getBody(request);
            Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);
            SignatureHeader signatureHeader = new SignatureHeader();
            signatureHeader.setNonce(headerMap.get(WechatPayCode.WECHAT_PAY_NONCE.toLowerCase()));
            signatureHeader.setTimeStamp(headerMap.get(WechatPayCode.WECHAT_PAY_TIMESTAMP.toLowerCase()));
            signatureHeader.setSerial(headerMap.get(WechatPayCode.WECHAT_PAY_SERIAL.toLowerCase()));
            signatureHeader.setSignature(headerMap.get(WechatPayCode.WECHAT_PAY_SIGNATURE.toLowerCase()));
            callbackInfo.setRawData(body);
            try {
                // 转换请求
                WxPayNotifyV3Result wxPayNotifyV3Result = wxPayService.parseOrderNotifyV3Result(body, signatureHeader);
                // 解析数据
                this.resolvePayData(wxPayNotifyV3Result);
            } catch (WxPayException e) {
                callbackInfo.setCallbackStatus(CallbackStatusEnum.FAIL);
                log.error("微信支付回调V3处理失败", e);
                return false;
            }
            return true;
        }
    }

    /**
     * 解析数据 v2
     */
    private void resolveV2Data(WxPayOrderNotifyResult result){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 回调数据
        Map<String, String> map = result.toMap();
        callbackInfo.setCallbackData(map);
        // 网关支付号
        callbackInfo.setOutTradeNo(result.getTransactionId());
        // 支付号
        callbackInfo.setTradeNo(result.getOutTradeNo());
        // 支付状态
        PayStatusEnum payStatus = WechatPayUtil.codeIsOk(result.getResultCode()) ? PayStatusEnum.SUCCESS : PayStatusEnum.FAIL;
        callbackInfo.setTradeStatus(payStatus.getCode());
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
    private void resolvePayData(WxPayNotifyV3Result v3Result){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        var result = v3Result.getResult();
        // 回调数据
        Map<String, Object> map = BeanUtil.beanToMap(result);
        callbackInfo.setCallbackData(map);
        // 网关支付号
        callbackInfo.setOutTradeNo(result.getTransactionId());
        // 支付号
        callbackInfo.setTradeNo(result.getOutTradeNo());
        // 支付状态 - 成功
        if (List.of(WxpayTradeStatus.SUCCESS,WxpayTradeStatus.REFUND).contains(result.getTradeState())){
            callbackInfo.setTradeStatus(PayStatusEnum.SUCCESS.getCode());
        }
        // 支付状态 - 支付中
        if (Objects.equals(result.getTradeState(), WxpayTradeStatus.NOTPAY)){
            callbackInfo.setTradeStatus(PayStatusEnum.PROGRESS.getCode());
        }
        // 支付状态 - 失败
        if (Objects.equals(WxpayTradeStatus.PAY_ERROR, result.getTradeState())){
            callbackInfo.setTradeStatus(PayStatusEnum.FAIL.getCode());
        }
        // 撤销
        if (Objects.equals(result.getTradeState(), WxpayTradeStatus.REVOKED)){
            callbackInfo.setTradeStatus(PayStatusEnum.CANCEL.getCode());
        }
        // 关闭
        if (Objects.equals(result.getTradeState(), WxpayTradeStatus.CLOSED)){
            callbackInfo.setTradeStatus(PayStatusEnum.CLOSE.getCode());
        }

        // 支付金额
        callbackInfo.setAmount(PayUtil.conversionAmount(result.getAmount().getTotal()));
        String timeEnd = result.getSuccessTime();
        if (StrUtil.isNotBlank(timeEnd)) {
            callbackInfo.setFinishTime(WechatPayUtil.parseV3(timeEnd));
        }
    }
}
