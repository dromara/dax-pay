package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.PayGatewaySyncResult;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundGatewaySyncResult;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.bootx.platform.daxpay.service.sdk.union.bean.UnionRefundOrder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.egzosn.pay.common.bean.AssistOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static cn.bootx.platform.daxpay.service.code.UnionPayCode.*;

/**
 * 云闪付支付同步
 * @author xxm
 * @since 2024/3/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPaySyncService {

    /**
     * 支付信息查询
     */
    public PayGatewaySyncResult syncPayStatus(PayOrder order, UnionPayKit unionPayKit) {
        PayGatewaySyncResult syncResult = new PayGatewaySyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);

        AssistOrder query = new AssistOrder();
        query.setOutTradeNo(String.valueOf(order.getId()));
        Map<String, Object> result = unionPayKit.query(query);

        syncResult.setSyncInfo(JSONUtil.toJsonStr(result));

//        String status = result.get(STATUS);
//        String returnCode = result.get(RESULT_CODE);
//
//        // 判断查询是否成功
//        if (!(Objects.equals(SUCCESS, status) && Objects.equals(SUCCESS, returnCode))){
//            log.warn("查询云闪付订单失败:{}", result);
//            return syncResult;
//        }
//
//        // 设置微信支付网关订单号
//        syncResult.setGatewayOrderNo(result.get(TRANSACTION_ID));
//        // 查询到订单的状态
//        String tradeStatus = result.get(TRADE_STATE);
//        // 支付完成
//        if (Objects.equals(tradeStatus, SUCCESS)) {
//            String timeEnd = result.get(TIME_END);
//            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
//            return syncResult.setPayTime(time).setSyncStatus(PaySyncStatusEnum.SUCCESS);
//        }
//        // 待支付
//        if (Objects.equals(tradeStatus, TRADE_NOT_PAY)) {
//            return syncResult.setSyncStatus(PaySyncStatusEnum.PROGRESS);
//        }
//
//        // 已退款/退款中
//        if (Objects.equals(tradeStatus, TRADE_REFUND)) {
//            return syncResult.setSyncStatus(PaySyncStatusEnum.REFUND);
//        }
//        // 已关闭
//        if (Objects.equals(tradeStatus, TRADE_CLOSED)) {
//            return syncResult.setSyncStatus(PaySyncStatusEnum.CLOSED);
//        }

        return syncResult;
    }

    /**
     * 退款信息查询
     */
    public RefundGatewaySyncResult syncRefundStatus(RefundOrder refundOrder, UnionPayKit unionPayKit){
        RefundGatewaySyncResult syncResult = new RefundGatewaySyncResult();

        UnionRefundOrder query = new UnionRefundOrder();
        query.setRefundNo(String.valueOf(refundOrder.getId()));
        Map<String, Object> results = unionPayKit.refundquery(query);


        try {
//            String xmlResult = WxPayApi.orderRefundQuery(false, params);
//            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
//            syncResult.setSyncInfo(JSONUtil.toJsonStr(result));

            // 设置微信支付网关订单号
//            syncResult.setGatewayOrderNo(result.get(UnionPayCode.REFUND_ID));
            // 状态
//            String tradeStatus = result.get(UnionPayCode.REFUND_STATUS);
//            // 退款成功
//            if (Objects.equals(tradeStatus, UnionPayCode.REFUND_SUCCESS)) {
//                String timeEnd = result.get(UnionPayCode.REFUND_SUCCESS_TIME);
//                LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.NORM_DATETIME_PATTERN);
//                return syncResult.setRefundTime(time).setSyncStatus(RefundSyncStatusEnum.SUCCESS);
//            }
//            // 退款中
//            if (Objects.equals(tradeStatus, UnionPayCode.REFUND_PROCESSING)) {
//                return syncResult.setSyncStatus(RefundSyncStatusEnum.PROGRESS);
//            }
//            String errorMsg = this.getErrorMsg(result);
//            return syncResult.setSyncStatus(RefundSyncStatusEnum.FAIL).setErrorMsg(errorMsg);
        } catch (Exception e) {
            log.error("查询退款订单失败:", e);
            syncResult.setSyncStatus(RefundSyncStatusEnum.PROGRESS).setErrorMsg(e.getMessage());
        }
        return syncResult;
    }

    /**
     * 验证错误信息
     */
    private String getErrorMsg(Map<String, String> result) {
        String status = result.get(UnionPayCode.STATUS);
        String returnCode = result.get(UnionPayCode.RESULT_CODE);

        // 判断查询是否成功
        if (!(Objects.equals(SUCCESS, status) && Objects.equals(SUCCESS, returnCode))){
            String errorMsg = result.get(ERR_MSG);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(MESSAGE);
            }
            log.error("订单查询失败 {}", errorMsg);
            return errorMsg;
        }
        return null;
    }
}
