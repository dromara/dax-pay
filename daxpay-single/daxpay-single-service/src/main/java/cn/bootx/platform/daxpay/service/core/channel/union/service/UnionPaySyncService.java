package cn.bootx.platform.daxpay.service.core.channel.union.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.service.code.UnionPayCode;
import cn.bootx.platform.daxpay.service.core.channel.union.entity.UnionPayConfig;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.PayGatewaySyncResult;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundGatewaySyncResult;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.unionpay.UnionPayApi;
import com.ijpay.unionpay.enums.ServiceEnum;
import com.ijpay.unionpay.model.OrderQueryModel;
import com.ijpay.unionpay.model.RefundQueryModel;
import com.ijpay.wxpay.WxPayApi;
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
    public PayGatewaySyncResult syncPayStatus(PayOrder order, UnionPayConfig unionPayConfig) {
        PayGatewaySyncResult syncResult = new PayGatewaySyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
        Map<String, String> params = OrderQueryModel.builder()
                .service(ServiceEnum.QUERY.toString())
                .mch_id(unionPayConfig.getMachId())
                .out_trade_no(String.valueOf(order.getId()))
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(unionPayConfig.getAppKey(), SignType.MD5);

        String xmlResult = UnionPayApi.execution(unionPayConfig.getServerUrl(), params);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        syncResult.setSyncInfo(JSONUtil.toJsonStr(result));

        String status = result.get(STATUS);
        String returnCode = result.get(RESULT_CODE);

        // 判断查询是否成功
        if (!(Objects.equals(SUCCESS, status) && Objects.equals(SUCCESS, returnCode))){
            log.warn("查询云闪付订单失败:{}", result);
            return syncResult;
        }

        // 设置微信支付网关订单号
        syncResult.setGatewayOrderNo(result.get(TRANSACTION_ID));
        // 查询到订单的状态
        String tradeStatus = result.get(TRADE_STATE);
        // 支付完成
        if (Objects.equals(tradeStatus, SUCCESS)) {
            String timeEnd = result.get(TIME_END);
            LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
            return syncResult.setPayTime(time).setSyncStatus(PaySyncStatusEnum.SUCCESS);
        }
        // 待支付
        if (Objects.equals(tradeStatus, TRADE_NOT_PAY)) {
            return syncResult.setSyncStatus(PaySyncStatusEnum.PROGRESS);
        }

        // 已退款/退款中
        if (Objects.equals(tradeStatus, TRADE_REFUND)) {
            return syncResult.setSyncStatus(PaySyncStatusEnum.REFUND);
        }
        // 已关闭
        if (Objects.equals(tradeStatus, TRADE_CLOSED)) {
            return syncResult.setSyncStatus(PaySyncStatusEnum.CLOSED);
        }

        return syncResult;
    }

    /**
     * 退款信息查询
     */
    public RefundGatewaySyncResult syncRefundStatus(RefundOrder refundOrder, UnionPayConfig unionPayConfig){
        RefundGatewaySyncResult syncResult = new RefundGatewaySyncResult();
        Map<String, String> params = RefundQueryModel.builder()
                .service(ServiceEnum.REFUND_QUERY.toString())
                .mch_id(unionPayConfig.getMachId())
                .refund_id(refundOrder.getGatewayOrderNo())
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(unionPayConfig.getAppKey(), SignType.MD5);

        try {
            String xmlResult = WxPayApi.orderRefundQuery(false, params);
            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
            syncResult.setSyncInfo(JSONUtil.toJsonStr(result));

            // 设置微信支付网关订单号
            syncResult.setGatewayOrderNo(result.get(UnionPayCode.REFUND_ID));
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
            String errorMsg = this.getErrorMsg(result);
            return syncResult.setSyncStatus(RefundSyncStatusEnum.FAIL).setErrorMsg(errorMsg);
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
