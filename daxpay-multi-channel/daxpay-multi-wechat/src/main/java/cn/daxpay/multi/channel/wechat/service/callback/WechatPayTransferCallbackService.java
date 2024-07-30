package cn.daxpay.multi.channel.wechat.service.callback;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.result.transfer.WxPayTransferBatchesNotifyV3Result;
import cn.daxpay.multi.channel.wechat.service.config.WechatPayConfigService;
import cn.daxpay.multi.core.enums.CallbackStatusEnum;
import cn.daxpay.multi.core.enums.ChannelEnum;
import cn.daxpay.multi.core.enums.TradeTypeEnum;
import cn.daxpay.multi.core.enums.TransferStatusEnum;
import cn.daxpay.multi.service.common.context.CallbackLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.service.trade.transfer.TransferCallbackService;
import cn.daxpay.multi.service.service.record.callback.TradeCallbackRecordService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Response;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.wechat.pay.java.core.http.Constant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 微信转账回调处理类
 * @author xxm
 * @since 2024/7/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayTransferCallbackService {


    private final WechatPayConfigService wechatPayConfigService;
    private final TransferCallbackService transferCallbackService;
    private final TradeCallbackRecordService tradeCallbackRecordService;

    /**
     * 回调处理
     */
    public String transferHandle(HttpServletRequest request) {
        // 执行回调数据解析, 返回响应对象
        String msg = this.callback(request);
        // 执行回调业务处理
        transferCallbackService.transferCallback();
        // 保存记录
        tradeCallbackRecordService.saveCallbackRecord();
        return msg;
    }

    /**
     * 微信转账到零钱回调
     */
    public String callback(HttpServletRequest request) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        callbackInfo.setChannel(ChannelEnum.WECHAT.getCode())
                .setCallbackType(TradeTypeEnum.TRANSFER);
        WechatPayConfig config = wechatPayConfigService.getWechatPayConfig();
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        // V3 回调接收处理
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setNonce(headerMap.get(Constant.WECHAT_PAY_NONCE.toLowerCase()));
        signatureHeader.setTimeStamp(headerMap.get(Constant.WECHAT_PAY_TIMESTAMP.toLowerCase()));
        signatureHeader.setSerial(headerMap.get(Constant.WECHAT_PAY_SERIAL.toLowerCase()));
        signatureHeader.setSignature(headerMap.get(Constant.WECHAT_PAY_SIGNATURE.toLowerCase()));
        callbackInfo.setRawData(body);
        try {
            var notifyV3Result = wxPayService.baseParseOrderNotifyV3Result(body, signatureHeader, WxPayTransferBatchesNotifyV3Result.class, WxPayTransferBatchesNotifyV3Result.DecryptNotifyResult.class);
            // 解析数据
            this.resolveData(notifyV3Result);
        } catch (WxPayException e) {
            callbackInfo.setCallbackStatus(CallbackStatusEnum.FAIL);
            log.error("微信转账回调V3处理失败", e);
            return WxPayNotifyV3Response.fail("FAIL");
        }
        return WxPayNotifyV3Response.success("OK");
    }

    /**
     * 微信转账到零钱回调
     */
    public void resolveData(WxPayTransferBatchesNotifyV3Result notifyV3Result) {
        CallbackLocal callbackInfo = PaymentContextLocal.get().getCallbackInfo();
        // 解析数据
        var result = notifyV3Result.getResult();

        // 设置类型和通道
        callbackInfo.setCallbackType(TradeTypeEnum.TRANSFER)
                .setChannel(ChannelEnum.WECHAT.getCode());
        // 回调数据
        callbackInfo.setCallbackData(BeanUtil.beanToMap(result));
        // 网关转账批次号
        callbackInfo.setOutTradeNo(result.getBatchId());
        // 转账批次号
        callbackInfo.setTradeNo(result.getOutBatchNo());

        // FINISHED:已完成。批次内的所有转账明细单都已处理完成
        if ("FINISHED".equals(result.getBatchStatus())){
            callbackInfo.setTradeStatus(TransferStatusEnum.SUCCESS.getCode());
        }
        // CLOSED:已关闭。可查询具体的批次关闭原因确认
        if ("CLOSED".equals(result.getBatchStatus())){
            callbackInfo.setTradeStatus(TransferStatusEnum.CLOSE.getCode());
        }
        // WAIT_PAY: 待付款确认。需要付款出资商户在商家助手小程序或服务商助手小程序进行付款确认
        // ACCEPTED:已受理。批次已受理成功，若发起批量转账的30分钟后，转账批次单仍处于该状态，可能原因是商户账户余额不足等。商户可查询账户资金流水，若该笔转账批次单的扣款已经发生，则表示批次已经进入转账中，请再次查单确认
        // PROCESSING:转账中。已开始处理批次内的转账明细单
        if (List.of("WAIT_PAY", "ACCEPTED", "PROCESSING").contains(result.getBatchStatus())){
            callbackInfo.setTradeStatus(TransferStatusEnum.PROGRESS.getCode());
        }
    }
}
