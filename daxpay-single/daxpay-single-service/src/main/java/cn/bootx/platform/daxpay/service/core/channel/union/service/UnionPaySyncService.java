package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.PayGatewaySyncResult;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundGatewaySyncResult;
import cn.bootx.platform.daxpay.service.sdk.union.api.UnionPayKit;
import cn.bootx.platform.daxpay.service.sdk.union.bean.UnionRefundOrder;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.union.bean.SDKConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if (!unionPayKit.verify(new NoticeParams(result))) {
            log.warn("查询云闪付订单验签失败:{}", result);
            return syncResult.setErrorMsg("查询订单验签失败");
        }

        // 查询失败
        String resultCode = MapUtil.getStr(result, SDKConstants.param_respCode);
        if (!SDKConstants.OK_RESP_CODE.equals(resultCode)) {
            log.warn("查询云闪付订单失败:{}", result);
            return syncResult.setErrorMsg(MapUtil.getStr(result, SDKConstants.param_respMsg));
        }
        String origRespCode = MapUtil.getStr(result, SDKConstants.param_origRespCode);

        // 查询流水号, 相当于网关订单号


        // 成功
        if (Objects.equals(origRespCode, SDKConstants.OK_RESP_CODE)) {
            String queryId = MapUtil.getStr(result, QUERY_ID);
            String timeEnd = MapUtil.getStr(result, TXN_TIME);
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            return syncResult.setGatewayOrderNo(queryId).setPayTime(time).setSyncStatus(PaySyncStatusEnum.SUCCESS);
        }
        // 支付超时  交易不在受理时间范围内
        if (Objects.equals(origRespCode, "39")) {
            return syncResult.setSyncStatus(PaySyncStatusEnum.TIMEOUT)
                    .setErrorMsg(MapUtil.getStr(result, SDKConstants.param_origRespMsg));
        }

        // 待支付
        if (Objects.equals(origRespCode, "05")) {
            return syncResult.setSyncStatus(PaySyncStatusEnum.PROGRESS);
        }
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
