package cn.bootx.platform.daxpay.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.service.code.WeChatPayCode;
import cn.bootx.platform.daxpay.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
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

import static cn.bootx.platform.daxpay.service.code.WeChatPayCode.TRANSACTION_ID;

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

    /**
     * 支付信息查询
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

            // 设置微信支付网关订单号
            syncResult.setGatewayOrderNo(result.get(TRANSACTION_ID));
            // 查询到订单的状态
            String tradeStatus = result.get(WeChatPayCode.TRADE_STATE);
            // 支付完成
            if (Objects.equals(tradeStatus, WeChatPayCode.PAY_SUCCESS) || Objects.equals(tradeStatus, WeChatPayCode.PAY_ACCEPT)) {
                String timeEnd = result.get(WeChatPayCode.TIME_END);
                LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
                return syncResult.setPayTime(time).setSyncStatus(PaySyncStatusEnum.SUCCESS);
            }
            // 待支付
            if (Objects.equals(tradeStatus, WeChatPayCode.PAY_NOTPAY)
                    || Objects.equals(tradeStatus, WeChatPayCode.PAY_USERPAYING)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PROGRESS);
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
            log.error("查询支付订单失败:", e);
            syncResult.setErrorMsg(e.getMessage());
        }
        return syncResult;
    }

    /**
     * 退款信息查询
     */
    public RefundGatewaySyncResult syncRefundStatus(RefundOrder refundOrder, WeChatPayConfig weChatPayConfig){
        RefundGatewaySyncResult syncResult = new RefundGatewaySyncResult();
        Map<String, String> params = RefundQueryModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                // 使用退款单号查询, 只返回当前这条, 如果使用支付订单号查询,
                .out_refund_no(String.valueOf(refundOrder.getId()))
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        try {
            String xmlResult = WxPayApi.orderRefundQuery(false, params);
            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
            syncResult.setSyncInfo(JSONUtil.toJsonStr(result));

            // 设置微信支付网关订单号
            syncResult.setGatewayOrderNo(result.get(WeChatPayCode.REFUND_ID));
            // 状态
            String tradeStatus = result.get(WeChatPayCode.REFUND_STATUS);
            // 退款成功
            if (Objects.equals(tradeStatus, WeChatPayCode.REFUND_SUCCESS)) {
                String timeEnd = result.get(WeChatPayCode.REFUND_SUCCESS_TIME);
                LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.NORM_DATETIME_PATTERN);
                return syncResult.setRefundTime(time).setSyncStatus(RefundSyncStatusEnum.SUCCESS);
            }
            // 退款中
            if (Objects.equals(tradeStatus, WeChatPayCode.REFUND_PROCESSING)) {
                return syncResult.setSyncStatus(RefundSyncStatusEnum.PROGRESS);
            }
            return syncResult.setSyncStatus(RefundSyncStatusEnum.FAIL);
        } catch (Exception e) {
            log.error("查询退款订单失败:", e);
            syncResult.setSyncStatus(RefundSyncStatusEnum.PROGRESS).setErrorMsg(e.getMessage());
        }
        return syncResult;

    }
}
