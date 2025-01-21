package org.dromara.daxpay.channel.union.service.sync;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.union.code.UnionPayCode;
import org.dromara.daxpay.channel.union.sdk.api.UnionPayKit;
import org.dromara.daxpay.channel.union.sdk.bean.SDKConstants;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.service.bo.sync.PaySyncResultBo;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.unisdk.common.bean.AssistOrder;
import org.dromara.daxpay.unisdk.common.bean.NoticeParams;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

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
     * 同步
     */
    public PaySyncResultBo sync(PayOrder order, UnionPayKit unionPayKit) {

        PaySyncResultBo syncResult = new PaySyncResultBo();
        AssistOrder query = new AssistOrder();
        query.setOutTradeNo(order.getOrderNo());

        Map<String, Object> result = unionPayKit.query(query);
        syncResult.setSyncData(JSONUtil.toJsonStr(result));
        if (!unionPayKit.verify(new NoticeParams(result))) {
            log.warn("查询云闪付订单验签失败:{}", result);
            return syncResult.setSyncSuccess(false).setSyncErrorMsg("查询订单验签失败");
        }

        String resultCode = MapUtil.getStr(result, UnionPayCode.RESP_CODE);

        // 订单不存在
        if (Objects.equals(resultCode, "34")) {
            return syncResult.setPayStatus(PayStatusEnum.CLOSE);
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
            return syncResult.setOutOrderNo(queryId).setFinishTime(time).setPayStatus(PayStatusEnum.SUCCESS);
        }

        // 支付超时  交易不在受理时间范围内
        if (Objects.equals(origRespCode, "39")) {
            return syncResult.setPayStatus(PayStatusEnum.CLOSE);
        }

        // 待支付
        if (Objects.equals(origRespCode, "05")) {
            return syncResult.setPayStatus(PayStatusEnum.PROGRESS);
        }

        // 二维码支付无效
        if (Objects.equals(origRespCode, "86")) {
            return syncResult.setPayStatus(PayStatusEnum.CLOSE);
        }
        return syncResult;
    }
}
