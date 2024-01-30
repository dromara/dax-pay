package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayRefundSyncStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.dao.PayRefundChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.PayRefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.PayGatewaySyncResult;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundGatewaySyncResult;
import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.OrderQueryModel;
import com.ijpay.wxpay.model.RefundQueryModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 微信支付同步服务
 *
 * @author xxm
 * @since 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPaySyncService {
    private final PayChannelOrderManager payChannelOrderManager;
    private final PayRefundChannelOrderManager refundChannelOrderManager;

    /**
     * 同步查询
     */
    public PayGatewaySyncResult syncPayStatus(PayOrder order, WeChatPayConfig weChatPayConfig) {
        PayGatewaySyncResult syncResult = new PayGatewaySyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
        Map<String, String> params = OrderQueryModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                .out_trade_no(String.valueOf(order.getId()))
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        try {
            String xmlResult = WxPayApi.orderQuery(params);
            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
            syncResult.setSyncInfo(JSONUtil.toJsonStr(result));
            // 查询失败
            if (!WxPayKit.codeIsOk(result.get(WeChatPayCode.RETURN_CODE))) {
                log.warn("查询微信订单失败:{}", result);
                return syncResult;
            }

            // 未查到订单
            if (!WxPayKit.codeIsOk(result.get(WeChatPayCode.RESULT_CODE))) {
                log.warn("疑似未查询到订单:{}", result);
                return syncResult.setSyncStatus(PaySyncStatusEnum.NOT_FOUND);
            }
            // 查询到订单的状态
            String tradeStatus = result.get(WeChatPayCode.TRADE_STATE);
            // 支付完成
            if (Objects.equals(tradeStatus, WeChatPayCode.PAY_SUCCESS) || Objects.equals(tradeStatus, WeChatPayCode.PAY_ACCEPT)) {
                String timeEnd = result.get(WeChatPayCode.TIME_END);
                LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
                PaymentContextLocal.get().getPaySyncInfo().setPayTime(time);
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_SUCCESS);
            }
            // 待支付
            if (Objects.equals(tradeStatus, WeChatPayCode.PAY_NOTPAY)
                    || Objects.equals(tradeStatus, WeChatPayCode.PAY_USERPAYING)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_WAIT);
            }

            // 已退款/退款中
            if (Objects.equals(tradeStatus, WeChatPayCode.PAY_REFUND)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.REFUND);
            }
            // 已关闭
            if (Objects.equals(tradeStatus, WeChatPayCode.PAY_CLOSED)
                    || Objects.equals(tradeStatus, WeChatPayCode.PAY_REVOKED)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_PAYERROR)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.CLOSED);
            }
        }
        catch (RuntimeException e) {
            log.error("查询订单失败:", e);
            syncResult.setErrorMsg(e.getMessage());
        }
        return syncResult;
    }

    /**
     * 退款信息查询
     */
    public RefundGatewaySyncResult syncRefundStatus(PayRefundOrder refundOrder, WeChatPayConfig weChatPayConfig){
        PayRefundChannelOrder orderChannel = refundChannelOrderManager.findByRefundIdAndChannel(refundOrder.getId(), PayChannelEnum.WECHAT.getCode())
                .orElseThrow(() -> new PayFailureException("支付订单通道信息不存在"));

        Map<String, String> params = RefundQueryModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                .out_refund_no(String.valueOf(refundOrder.getId()))
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.orderRefundQuery(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        // TODO 处理退款同步的情况



        Integer refundFee = Integer.valueOf(result.get(WeChatPayCode.REFUND_FEE));
        if (Objects.equals(refundFee, orderChannel.getAmount())){
            return new RefundGatewaySyncResult().setSyncStatus(PayRefundSyncStatusEnum.REFUNDING);
        }
        return new RefundGatewaySyncResult().setSyncInfo(JSONUtil.toJsonStr(result));
    }
}
