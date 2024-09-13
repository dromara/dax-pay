package cn.daxpay.single.service.core.channel.union.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.core.code.RefundSyncStatusEnum;
import cn.daxpay.single.service.code.UnionPayCode;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.daxpay.single.service.sdk.union.api.UnionPayKit;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.map.MapUtil;
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

import static cn.daxpay.single.service.code.UnionPayCode.QUERY_ID;
import static cn.daxpay.single.service.code.UnionPayCode.TXN_TIME;

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
    public PayRemoteSyncResult syncPayStatus(PayOrder order, UnionPayKit unionPayKit) {
        PayRemoteSyncResult syncResult = new PayRemoteSyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);

        AssistOrder query = new AssistOrder();
        query.setOutTradeNo(order.getOrderNo());

        Map<String, Object> result = unionPayKit.query(query);
        syncResult.setSyncInfo(JSONUtil.toJsonStr(result));
        if (!unionPayKit.verify(new NoticeParams(result))) {
            log.warn("查询云闪付订单验签失败:{}", result);
            return syncResult.setErrorMsg("查询订单验签失败");
        }

        String resultCode = MapUtil.getStr(result, UnionPayCode.RESP_CODE);

        // 订单不存在
        if (Objects.equals(resultCode, "34")) {
            return syncResult.setSyncStatus(PaySyncStatusEnum.NOT_FOUND);
        }
        // 查询失败
        if (!UnionPayCode.RESP_SUCCESS.equals(resultCode)) {
            log.warn("查询云闪付订单失败:{}", result);
            return syncResult.setErrorMsg(MapUtil.getStr(result, SDKConstants.param_respMsg));
        }
        // 状态响应码
        String origRespCode = MapUtil.getStr(result, SDKConstants.param_origRespCode);

        // 成功
        if (Objects.equals(origRespCode, UnionPayCode.RESP_SUCCESS)) {
            // 查询流水号, 相当于网关订单号
            String queryId = MapUtil.getStr(result, QUERY_ID);
            String timeEnd = MapUtil.getStr(result, TXN_TIME);
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            return syncResult.setOutOrderNo(queryId).setFinishTime(time).setSyncStatus(PaySyncStatusEnum.SUCCESS);
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

        // 二维码支付无效
        if (Objects.equals(origRespCode, "86")) {
            return syncResult.setSyncStatus(PaySyncStatusEnum.CLOSED).setErrorMsg("支付二维码无效");
        }

        return syncResult;
    }

    /**
     * 退款信息查询
     * 云闪付退款和支付查询接口是一个
     */
    public RefundRemoteSyncResult syncRefundStatus(RefundOrder refundOrder, UnionPayKit unionPayKit){
        RefundRemoteSyncResult syncResult = new RefundRemoteSyncResult();

        AssistOrder query = new AssistOrder();
        query.setOutTradeNo(String.valueOf(refundOrder.getRefundNo()));

        Map<String, Object> result = unionPayKit.query(query);

        if (!unionPayKit.verify(new NoticeParams(result))) {
            log.warn("查询云闪付订单验签失败:{}", result);
            return syncResult.setErrorMsg("查询订单验签失败");
        }
        String resultCode = MapUtil.getStr(result, UnionPayCode.RESP_CODE);

        // 订单不存在
        if (Objects.equals(resultCode, "34")) {
            return syncResult.setSyncStatus(RefundSyncStatusEnum.NOT_FOUND);
        }

        // 查询失败
        if (!UnionPayCode.RESP_SUCCESS.equals(resultCode)) {
            log.warn("查询云闪付订单失败:{}", result);
            return syncResult.setErrorMsg(MapUtil.getStr(result, SDKConstants.param_respMsg));
        }

        // 状态响应码
        String origRespCode = MapUtil.getStr(result, SDKConstants.param_origRespCode);

        // 成功
        if (Objects.equals(origRespCode, UnionPayCode.RESP_SUCCESS)) {
            // 查询流水号, 相当于网关订单号
            String queryId = MapUtil.getStr(result, QUERY_ID);
            String timeEnd = MapUtil.getStr(result, TXN_TIME);
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            return syncResult.setOutRefundNo(queryId).setFinishTime(time).setSyncStatus(RefundSyncStatusEnum.SUCCESS);
        }

        // 退款中
        if (Objects.equals(origRespCode, "05")) {
            return syncResult.setSyncStatus(RefundSyncStatusEnum.PROGRESS);
        }
        return syncResult;
    }
}
