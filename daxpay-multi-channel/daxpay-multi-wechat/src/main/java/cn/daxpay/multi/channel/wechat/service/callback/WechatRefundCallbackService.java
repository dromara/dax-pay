package cn.daxpay.multi.channel.wechat.service.callback;

import cn.daxpay.multi.channel.wechat.code.WechatPayCode;
import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.channel.wechat.util.WechatPayUtil;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.RefundStatusEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.util.PayUtil;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.github.binarywang.wxpay.bean.notify.*;
import com.github.binarywang.wxpay.constant.WxPayConstants.RefundStatus;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.wechat.pay.java.core.http.Constant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 微信退款回调
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatRefundCallbackService {
    private final WechatPayConfigService wechatPayConfigService;

    /**
     * 退款回调处理, 解析数据
     */
    public String refund(HttpServletRequest request){
        WechatPayConfig config = wechatPayConfigService.getWechatPayConfig();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        // v2 或 v3
        if (Objects.equals(config.getApiVersion(), WechatPayCode.API_V2)) {
            // V2 回调接收处理
            String xml = WechatPayUtil.readV2Data(request);
            try {
                // 转换请求
                var result = wxPayService.parseRefundNotifyResult(xml);
                // 解析数据
                resolveV2PayData(result);
                return WxPayNotifyResponse.success("OK");
            } catch (WxPayException e) {
                log.error("微信退款V2回调处理失败", e);
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
                var result = wxPayService.parseRefundNotifyV3Result(body, signatureHeader);
                // 解析数据
                this.resolveV3PayData(result);
            } catch (WxPayException e) {
                log.error("微信退款V3回调处理失败", e);
                return WxPayNotifyV3Response.fail("FAIL");
            }
            return WxPayNotifyV3Response.success("OK");
        }
    }

    /**
     * 解析数据 v2
     */
    public void resolveV2PayData(WxPayRefundNotifyResult notifyResult){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 解密的数据
        var result = notifyResult.getReqInfo();

        // 设置类型和通道
        callbackInfo.setCallbackType(TradeTypeEnum.PAY)
                .setChannel(ChannelEnum.WECHAT.getCode());
        // 回调数据
        callbackInfo.setCallbackData(BeanUtil.beanToMap(result));
        // 网关退款号
        callbackInfo.setOutTradeNo(result.getTransactionId());
        // 退款号
        callbackInfo.setTradeNo(result.getRefundId());
        // 退款状态 - 成功
        if (Objects.equals(RefundStatus.SUCCESS, result.getRefundStatus())){
            callbackInfo.setOutStatus(RefundStatusEnum.SUCCESS.getCode());
        }
        // 退款状态 - 退款关闭
        if (Objects.equals(result.getRefundStatus(), RefundStatus.REFUND_CLOSE)){
            callbackInfo.setOutStatus(RefundStatusEnum.CLOSE.getCode());
        }
        // 退款状态 - 失败
        if (Objects.equals(RefundStatus.CHANGE, result.getRefundStatus())){
            callbackInfo.setOutStatus(RefundStatusEnum.FAIL.getCode());
        }
        // 退款金额和时间
        callbackInfo.setAmount(PayUtil.conversionAmount(result.getRefundFee()));
        String timeEnd = result.getSuccessTime();
        if (StrUtil.isNotBlank(timeEnd)){
            callbackInfo.setFinishTime(WechatPayUtil.parseV2(timeEnd));
        }

    }
    /**
     * 解析数据 v3
     */
    public void resolveV3PayData(WxPayRefundNotifyV3Result v3Result){
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        var result = v3Result.getResult();
        // 设置类型合同到
        callbackInfo.setCallbackType(TradeTypeEnum.PAY)
                .setChannel(ChannelEnum.WECHAT.getCode());
        // 回调数据
        callbackInfo.setCallbackData(BeanUtil.beanToMap(result));
        // 网关退款号
        callbackInfo.setOutTradeNo(result.getRefundId());
        // 退款号
        callbackInfo.setTradeNo(result.getOutRefundNo());
        // 退款状态 - 成功
        if (Objects.equals(RefundStatus.SUCCESS, result.getRefundStatus())){
            callbackInfo.setOutStatus(RefundStatusEnum.SUCCESS.getCode());
        }
        // 退款状态 - 退款关闭
        if (Objects.equals(result.getRefundStatus(), RefundStatus.CLOSED)){
            callbackInfo.setOutStatus(RefundStatusEnum.CLOSE.getCode());
        }
        // 退款状态 - 失败
        if (Objects.equals(RefundStatus.ABNORMAL, result.getRefundStatus())){
            callbackInfo.setOutStatus(RefundStatusEnum.FAIL.getCode());
        }
        // 退款金额
        callbackInfo.setAmount(PayUtil.conversionAmount(result.getAmount().getTotal()));
        callbackInfo.setFinishTime(WechatPayUtil.parseV3(result.getSuccessTime()));
    }
}
