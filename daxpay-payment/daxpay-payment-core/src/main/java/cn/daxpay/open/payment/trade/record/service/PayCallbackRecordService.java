package cn.daxpay.open.payment.trade.record.service;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.trade.record.dao.PayCallbackRecordManager;
import cn.daxpay.open.payment.trade.record.entity.PayCallbackRecord;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.platform.common.json.util.JsonUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeFlowTypeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/// # 通道入站回调记录服务
///
/// 新开事务保存, 不受业务事务回滚影响; 只审计不重放
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCallbackRecordService {

    private final PayCallbackRecordManager callbackRecordManager;
    private final ChannelMerchantManager channelMerchantManager;
    private final PaymentContext paymentContext;

    /// 保存支付回调记录
    /// @param channelMchNo 通道商户号(回调 path 入站身份)
    /// @param data 回调解析数据
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void savePay(String channelMchNo, CallbackData data) {
        if (data == null) {
            return;
        }
        PayCallbackRecord record = new PayCallbackRecord()
                .setTradeNo(data.getTradeNo())
                .setOutTradeNo(data.getOutTradeNo())
                .setProduct(channelMerchantManager.findProductByChannelMchNo(channelMchNo))
                .setChannelMchNo(channelMchNo)
                .setCallbackType(TradeFlowTypeEnum.PAY.getCode())
                .setNotifyInfo(toNotifyInfo(data.getCallbackData()))
                .setStatus(resolveStatus(data.getCallbackStatus()))
                .setErrorMsg(StrUtil.blankToDefault(data.getCallbackErrorMsg(), data.getTradeErrorMsg()));
        // 显式写入商户号, 避免运营端/无上下文场景踩 Fill
        record.setMchNo(paymentContext.getMchNo());
        callbackRecordManager.save(record);
    }

    /// 保存退款回调记录(trade_no 存 refundNo, out_trade_no 存 outRefundNo)
    /// @param channelMchNo 通道商户号(回调 path 入站身份)
    /// @param data 退款回调解析数据
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveRefund(String channelMchNo, RefundCallbackData data) {
        if (data == null) {
            return;
        }
        PayCallbackRecord record = new PayCallbackRecord()
                .setTradeNo(data.getRefundNo())
                .setOutTradeNo(data.getOutRefundNo())
                .setProduct(channelMerchantManager.findProductByChannelMchNo(channelMchNo))
                .setChannelMchNo(channelMchNo)
                .setCallbackType(TradeFlowTypeEnum.REFUND.getCode())
                .setNotifyInfo(toNotifyInfo(data.getCallbackData()))
                .setStatus(resolveStatus(data.getCallbackStatus()))
                .setErrorMsg(StrUtil.blankToDefault(data.getCallbackErrorMsg(), data.getTradeErrorMsg()));
        record.setMchNo(paymentContext.getMchNo());
        callbackRecordManager.save(record);
    }

    /// 保存转账回调记录(trade_no 存 transferNo, out_trade_no 存 outTransferNo)
    /// @param channelMchNo 通道商户号(回调 path 入站身份)
    /// @param data 转账回调解析数据
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveTransfer(String channelMchNo, CallbackData data) {
        if (data == null) {
            return;
        }
        PayCallbackRecord record = new PayCallbackRecord()
                .setTradeNo(data.getTradeNo())
                .setOutTradeNo(data.getOutTradeNo())
                .setProduct(channelMerchantManager.findProductByChannelMchNo(channelMchNo))
                .setChannelMchNo(channelMchNo)
                .setCallbackType(TradeFlowTypeEnum.TRANSFER.getCode())
                .setNotifyInfo(toNotifyInfo(data.getCallbackData()))
                .setStatus(resolveStatus(data.getCallbackStatus()))
                .setErrorMsg(StrUtil.blankToDefault(data.getCallbackErrorMsg(), data.getTradeErrorMsg()));
        // 显式写入商户号, 避免无上下文场景踩 Fill
        record.setMchNo(paymentContext.getMchNo());
        callbackRecordManager.save(record);
    }

    /// 序列化回调报文; 空 Map 落 {}
    private String toNotifyInfo(Map<String, ?> callbackData) {
        Map<String, ?> map = Optional.ofNullable(callbackData).orElse(Collections.emptyMap());
        return JsonUtil.toJsonStr(map);
    }

    /// 取处理状态码, 缺省 fail
    private String resolveStatus(CallbackStatusEnum status) {
        return status != null ? status.getCode() : CallbackStatusEnum.FAIL.getCode();
    }
}
