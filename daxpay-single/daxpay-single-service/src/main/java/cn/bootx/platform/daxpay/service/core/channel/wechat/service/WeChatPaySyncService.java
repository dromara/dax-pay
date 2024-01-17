package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderChannelManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrderChannel;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.GatewaySyncResult;
import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.model.OrderQueryModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
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
    private final PayOrderChannelManager payOrderChannelManager;

    /**
     * 同步查询
     */
    public GatewaySyncResult syncPayStatus(PayOrder order, WeChatPayConfig weChatPayConfig) {
        GatewaySyncResult syncResult = new GatewaySyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
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
            syncResult.setSyncPayInfo(JSONUtil.toJsonStr(result));
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
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_SUCCESS) || Objects.equals(tradeStatus, WeChatPayCode.TRADE_ACCEPT)) {
                String timeEnd = result.get(WeChatPayCode.TIME_END);
                LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
                PaymentContextLocal.get().getAsyncPayInfo().setPayTime(time);
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_SUCCESS);
            }
            // 待支付
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_NOTPAY)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_USERPAYING)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_WAIT);
            }

            // 已退款/退款中 触发一下退款记录的查询
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_REFUND)) {
                this.syncRefundStatus(order, weChatPayConfig);
            }
            // 已关闭
            if (Objects.equals(tradeStatus, WeChatPayCode.TRADE_CLOSED)
                    || Objects.equals(tradeStatus, WeChatPayCode.TRADE_REVOKED)
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
     * 退款查询
     */
    private GatewaySyncResult syncRefundStatus(PayOrder order, WeChatPayConfig weChatPayConfig){
        PayOrderChannel orderChannel = payOrderChannelManager.findByPaymentIdAndChannel(order.getId(), PayChannelEnum.WECHAT.getCode())
                .orElseThrow(() -> new PayFailureException("支付订单通道信息不存在"));

        Map<String, String> params = UnifiedOrderModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                .out_trade_no(String.valueOf(order.getId()))
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);
        String xmlResult = WxPayApi.orderRefundQuery(false, params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        // 获取



        // 判断是否全部退款
        Integer refundFee = Integer.valueOf(result.get(WeChatPayCode.REFUND_FEE));
        if (Objects.equals(refundFee, orderChannel.getAmount())){
            return new GatewaySyncResult().setSyncStatus(PaySyncStatusEnum.REFUND);
        }
        return new GatewaySyncResult().setSyncPayInfo(JSONUtil.toJsonStr(result));
    }
}
