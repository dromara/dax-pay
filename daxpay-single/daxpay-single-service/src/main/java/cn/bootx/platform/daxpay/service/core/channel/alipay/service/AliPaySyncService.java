package cn.bootx.platform.daxpay.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.service.code.AliPayCode;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.payment.sync.result.GatewaySyncResult;
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
import java.util.Objects;

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
     * 与支付宝网关同步状态, 退款状态会
     * 1 远程支付成功
     * 2 交易创建，等待买家付款
     * 3 超时关闭
     * 4 查询不到
     * 5 查询失败
     */
    public GatewaySyncResult syncPayStatus(PayOrder payOrder) {
        GatewaySyncResult syncResult = new GatewaySyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
        // 查询
        try {
            AlipayTradeQueryModel queryModel = new AlipayTradeQueryModel();
            queryModel.setOutTradeNo(String.valueOf(payOrder.getId()));
//            queryModel.setQueryOptions(Collections.singletonList("trade_settle_info"));
            // 查询参数
            AlipayTradeQueryResponse response = AliPayApi.tradeQueryToResponse(queryModel);
            String tradeStatus = response.getTradeStatus();
            syncResult.setSyncPayInfo(JSONUtil.toJsonStr(response));
            // 支付完成 TODO 部分退款也在这个地方, 但无法进行区分, 需要借助对账进行处理
            if (Objects.equals(tradeStatus, AliPayCode.PAYMENT_TRADE_SUCCESS) || Objects.equals(tradeStatus, AliPayCode.PAYMENT_TRADE_FINISHED)) {
                PaymentContextLocal.get().getAsyncPayInfo().setTradeNo(response.getTradeNo());
                // 支付完成时间
                LocalDateTime payTime = LocalDateTimeUtil.of(response.getSendPayDate());
                PaymentContextLocal.get().getAsyncPayInfo().setPayTime(payTime);
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_SUCCESS);
            }
            // 待支付
            if (Objects.equals(tradeStatus, AliPayCode.PAYMENT_WAIT_BUYER_PAY)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_WAIT);
            }
            // 已关闭或支付完成后全额退款
            if (Objects.equals(tradeStatus, AliPayCode.PAYMENT_TRADE_CLOSED)) {
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
        }
        catch (AlipayApiException e) {
            log.error("查询订单失败:", e);
            syncResult.setErrorMsg(e.getErrMsg());
        }
        return syncResult;
    }

    /**
     * 退款同步查询
     */
    private void syncRefundStatus(PayOrder payOrder) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryModel queryModel = new AlipayTradeFastpayRefundQueryModel();
        queryModel.setOutTradeNo(String.valueOf(payOrder.getId()));
        AlipayTradeFastpayRefundQueryResponse response = AliPayApi.tradeRefundQueryToResponse(queryModel);
        response.getRefundStatus();
    }
}