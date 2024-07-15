package cn.daxpay.single.service.core.channel.wechat.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.core.code.RefundSyncStatusEnum;
import cn.daxpay.single.service.code.WeChatPayCode;
import cn.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
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

import static cn.daxpay.single.service.code.WeChatPayCode.TRANSACTION_ID;

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
    public PayRemoteSyncResult syncPayStatus(PayOrder order, WeChatPayConfig weChatPayConfig) {
        PayRemoteSyncResult syncResult = new PayRemoteSyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
        Map<String, String> params = OrderQueryModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                .out_trade_no(order.getOrderNo())
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
            syncResult.setOutOrderNo(result.get(TRANSACTION_ID));
            // 查询到订单的状态
            String tradeStatus = result.get(WeChatPayCode.TRADE_STATE);
            // 支付完成
            if (Objects.equals(tradeStatus, WeChatPayCode.PAY_SUCCESS) || Objects.equals(tradeStatus, WeChatPayCode.PAY_ACCEPT)) {
                String timeEnd = result.get(WeChatPayCode.TIME_END);
                LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.PURE_DATETIME_PATTERN);
                return syncResult.setFinishTime(time).setSyncStatus(PaySyncStatusEnum.SUCCESS);
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
    public RefundRemoteSyncResult syncRefundStatus(RefundOrder refundOrder, WeChatPayConfig weChatPayConfig){
        RefundRemoteSyncResult syncResult = new RefundRemoteSyncResult();
        Map<String, String> params = RefundQueryModel.builder()
                .appid(weChatPayConfig.getWxAppId())
                .mch_id(weChatPayConfig.getWxMchId())
                .nonce_str(WxPayKit.generateStr())
                // 使用退款单号查询, 只返回当前这条, 如果使用支付订单号查询,返回所有相关的
                .out_refund_no(refundOrder.getRefundNo())
                .build()
                .createSign(weChatPayConfig.getApiKeyV2(), SignType.HMACSHA256);

        try {
            String xmlResult = WxPayApi.orderRefundQuery(false, params);
            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
            syncResult.setSyncInfo(JSONUtil.toJsonStr(result));

            // 设置微信支付网关订单号
            syncResult.setOutRefundNo(result.get(WeChatPayCode.REFUND_ID));
            // 返回码
            String errCode = result.get(WeChatPayCode.ERR_CODE);

            // 退款单不存在
            if (Objects.equals(errCode, WeChatPayCode.REFUND_NOTEXIST)) {
                return syncResult.setSyncStatus(RefundSyncStatusEnum.NOT_FOUND);
            }
            // 状态
            String tradeStatus = result.get(WeChatPayCode.REFUND_STATUS);
            // 退款成功
            if (Objects.equals(tradeStatus, WeChatPayCode.REFUND_SUCCESS)) {
                String timeEnd = result.get(WeChatPayCode.REFUND_SUCCESS_TIME);
                LocalDateTime time = LocalDateTimeUtil.parse(timeEnd, DatePattern.NORM_DATETIME_PATTERN);
                return syncResult.setFinishTime(time).setSyncStatus(RefundSyncStatusEnum.SUCCESS);
            }
            // 退款中
            if (Objects.equals(tradeStatus, WeChatPayCode.REFUND_PROCESSING)) {
                return syncResult.setSyncStatus(RefundSyncStatusEnum.PROGRESS);
            }
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
        String returnCode = result.get(WeChatPayCode.RETURN_CODE);
        String resultCode = result.get(WeChatPayCode.RESULT_CODE);
        if (!WxPayKit.codeIsOk(returnCode) || !WxPayKit.codeIsOk(resultCode)) {
            String errorMsg = result.get(WeChatPayCode.ERR_CODE_DES);
            if (StrUtil.isBlank(errorMsg)) {
                errorMsg = result.get(WeChatPayCode.RETURN_MSG);
            }
            return errorMsg;
        }
        return null;
    }
}
