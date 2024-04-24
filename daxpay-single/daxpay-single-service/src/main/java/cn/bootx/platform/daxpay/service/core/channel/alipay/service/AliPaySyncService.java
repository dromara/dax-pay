package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.service.code.AliPayCode;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.refund.entity.RefundOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.PaySyncResult;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.RefundSyncResult;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

import static cn.bootx.platform.daxpay.service.code.AliPayCode.GMT_REFUND_PAY;

/**
 * 支付宝同步
 *
 * @author xxm
 * @since 2021/5/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPaySyncService {
    /**
     * 与支付宝网关同步状态, 退款状态有
     * 1 远程支付成功
     * 2 交易创建，等待买家付款
     * 3 超时关闭
     * 4 查询不到
     * 5 查询失败
     */
    public PaySyncResult syncPayStatus(PayOrder payOrder) {
        PaySyncResult syncResult = new PaySyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
        // 查询
        try {
            AlipayTradeQueryModel queryModel = new AlipayTradeQueryModel();
            queryModel.setOutTradeNo(payOrder.getOrderNo());
            AlipayTradeQueryResponse response = AliPayApi.tradeQueryToResponse(queryModel);
            String tradeStatus = response.getTradeStatus();
            syncResult.setSyncInfo(JSONUtil.toJsonStr(response));
            // 设置网关订单号
            syncResult.setOutTradeNo(response.getTradeNo());
            // 支付完成 TODO 部分退款也在这个地方, 但无法进行区分, 需要借助对账进行处理
            if (Objects.equals(tradeStatus, AliPayCode.NOTIFY_TRADE_SUCCESS) || Objects.equals(tradeStatus, AliPayCode.NOTIFY_TRADE_FINISHED)) {
                // 支付完成时间
                LocalDateTime payTime = LocalDateTimeUtil.of(response.getSendPayDate());
                return syncResult.setSyncStatus(PaySyncStatusEnum.SUCCESS).setPayTime(payTime);
            }
            // 待支付
            if (Objects.equals(tradeStatus, AliPayCode.NOTIFY_WAIT_BUYER_PAY)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PROGRESS);
            }
            // 已关闭或支付完成后全额退款
            if (Objects.equals(tradeStatus, AliPayCode.NOTIFY_TRADE_CLOSED)) {
                // 判断是否有支付时间, 有支付时间说明是退款
                if (Objects.nonNull(response.getSendPayDate())){
                    return syncResult.setSyncStatus(PaySyncStatusEnum.REFUND);
                } else {
                    return syncResult.setSyncStatus(PaySyncStatusEnum.CLOSED);
                }
            }
            // 支付宝支付后, 客户未进行操作将不会创建出订单, 所以交易不存在约等于未查询订单
            if (Objects.equals(response.getSubCode(), AliPayCode.ACQ_TRADE_NOT_EXIST)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.NOT_FOUND_UNKNOWN);
            }
            // 查询失败
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                syncResult.setSyncStatus(PaySyncStatusEnum.FAIL);
                syncResult.setErrorCode(response.getSubCode());
                syncResult.setErrorMsg(response.getSubMsg());
                return syncResult;
            }
        }
        catch (AlipayApiException e) {
            log.error("支付订单同步失败:", e);
            syncResult.setErrorMsg(e.getErrMsg());
        }
        return syncResult;
    }

    /**
     * 退款同步查询
     * 注意: 支付宝退款没有网关订单号, 网关订单号是支付单的
     */
    public RefundSyncResult syncRefundStatus(RefundOrder refundOrder) {
        RefundSyncResult syncResult = new RefundSyncResult().setSyncStatus(RefundSyncStatusEnum.FAIL);
        try {
            AlipayTradeFastpayRefundQueryModel queryModel = new AlipayTradeFastpayRefundQueryModel();
            // 退款请求号
            queryModel.setOutRequestNo(String.valueOf(refundOrder.getId()));
            // 商户订单号
            queryModel.setOutTradeNo(String.valueOf(refundOrder.getRefundNo()));
            // 设置返回退款完成时间
            queryModel.setQueryOptions(Collections.singletonList(GMT_REFUND_PAY));
            AlipayTradeFastpayRefundQueryResponse response = AliPayApi.tradeRefundQueryToResponse(queryModel);
            syncResult.setSyncInfo(JSONUtil.toJsonStr(response));
            // 失败
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                syncResult.setSyncStatus(RefundSyncStatusEnum.FAIL);
                syncResult.setErrorCode(response.getSubCode());
                syncResult.setErrorMsg(response.getSubMsg());
                return syncResult;
            }
            String tradeStatus = response.getRefundStatus();
            // 成功
            if (Objects.equals(tradeStatus, AliPayCode.REFUND_SUCCESS)){
                LocalDateTime localDateTime = LocalDateTimeUtil.of(response.getGmtRefundPay());
                return syncResult.setRefundTime(localDateTime).setSyncStatus(RefundSyncStatusEnum.SUCCESS);
            } else {
                return syncResult.setSyncStatus(RefundSyncStatusEnum.FAIL).setErrorMsg("支付宝网关反正退款未成功");
            }
        } catch (AlipayApiException e) {
            log.error("退款订单同步失败:", e);
            syncResult.setErrorMsg(e.getErrMsg());
        }
        return syncResult;
    }
}
