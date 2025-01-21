package org.dromara.daxpay.channel.union.service.sync;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.union.code.UnionPayCode;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.sdk.bean.SDKConstants;
import org.dromara.daxpay.core.enums.RefundStatusEnum;
import org.dromara.daxpay.service.bo.sync.RefundSyncResultBo;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.unisdk.common.bean.AssistOrder;
import org.dromara.daxpay.unisdk.common.bean.NoticeParams;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 云闪付退款同步
 * @author xxm
 * @since 2024/10/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionRefundSyncService {

    /**
     * 同步
     */
    public RefundSyncResultBo sync(RefundOrder refundOrder, UnionPayKit unionPayKit) {
        RefundSyncResultBo syncResult = new RefundSyncResultBo();
        AssistOrder query = new AssistOrder();
        query.setOutTradeNo(String.valueOf(refundOrder.getRefundNo()));

        Map<String, Object> result = unionPayKit.query(query);

        if (!unionPayKit.verify(new NoticeParams(result))) {
            log.warn("查询云闪付订单验签失败:{}", result);
            return syncResult.setSyncErrorMsg("查询订单验签失败").setSyncSuccess(false);
        }
        String resultCode = MapUtil.getStr(result, UnionPayCode.RESP_CODE);

        // 订单不存在
        if (Objects.equals(resultCode, "34")) {
            return syncResult.setRefundStatus(RefundStatusEnum.CLOSE);
        }

        // 查询失败
        if (!UnionPayCode.RESP_SUCCESS.equals(resultCode)) {
            log.warn("查询云闪付订单失败:{}", result);
            return syncResult.setSyncErrorMsg(MapUtil.getStr(result, SDKConstants.param_respMsg)).setSyncSuccess(false);
        }

        // 状态响应码
        String origRespCode = MapUtil.getStr(result, SDKConstants.param_origRespCode);

        // 成功
        if (Objects.equals(origRespCode, UnionPayCode.RESP_SUCCESS)) {
            // 查询流水号, 相当于网关订单号
            String queryId = MapUtil.getStr(result, UnionPayCode.QUERY_ID);
            String timeEnd = MapUtil.getStr(result, UnionPayCode.TXN_TIME);
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            return syncResult.setOutRefundNo(queryId).setFinishTime(time).setRefundStatus(RefundStatusEnum.SUCCESS);
        }

        // 退款中
        if (Objects.equals(origRespCode, "05")) {
            return syncResult.setRefundStatus(RefundStatusEnum.PROGRESS);
        }
        return syncResult;
    }

}
