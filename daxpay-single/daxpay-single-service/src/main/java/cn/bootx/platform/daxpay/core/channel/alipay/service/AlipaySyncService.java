package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.daxpay.code.AliPayCode;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.payment.sync.result.GatewaySyncResult;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class AlipaySyncService {

    /**
     * 与支付宝网关同步状态 1 远程支付成功 2 交易创建，等待买家付款 3 超时关闭 4 查询不到 5 查询失败
     */
    public GatewaySyncResult syncPayStatus(Long paymentId) {
        GatewaySyncResult syncResult = new GatewaySyncResult().setSyncStatus(PaySyncStatusEnum.FAIL.getCode());

        // 查询
        try {
            AlipayTradeQueryModel queryModel = new AlipayTradeQueryModel();
            queryModel.setOutTradeNo(String.valueOf(paymentId));
            // 查询退款参数
            AlipayTradeQueryResponse response = AliPayApi.tradeQueryToResponse(queryModel);
            String tradeStatus = response.getTradeStatus();
            syncResult.setJson(JSONUtil.toJsonStr(response));
            // 支付完成
            if (Objects.equals(tradeStatus, AliPayCode.PAYMENT_TRADE_SUCCESS) || Objects.equals(tradeStatus, AliPayCode.PAYMENT_TRADE_FINISHED)) {

                HashMap<String, String> map = new HashMap<>(1);
                // TODO 写入到
                map.put(AliPayCode.TRADE_NO, response.getTradeNo());
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_SUCCESS.getCode()).setMap(map);
            }
            // 待支付
            if (Objects.equals(tradeStatus, AliPayCode.PAYMENT_WAIT_BUYER_PAY)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.PAY_WAIT.getCode());
            }
            // 已关闭
            if (Objects.equals(tradeStatus, AliPayCode.PAYMENT_TRADE_CLOSED)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.CLOSED.getCode());
            }
            // 未找到
            if (Objects.equals(response.getSubCode(), AliPayCode.ACQ_TRADE_NOT_EXIST)) {
                return syncResult.setSyncStatus(PaySyncStatusEnum.NOT_FOUND.getCode());
            }
            // 退款 支付宝查不到这个状态

        }
        catch (AlipayApiException e) {
            log.error("查询订单失败:", e);
            syncResult.setMsg(e.getErrMsg());
        }
        return syncResult;
    }

    /**
     * 比对网关状态和支付单状态
     */
    public boolean isStatusSync(GatewaySyncResult syncResult, PayOrder payOrder) {
        String syncStatus = syncResult.getSyncStatus();
        String orderStatus = payOrder.getStatus();
        // 支付成功比对
        if (orderStatus.equals(PayStatusEnum.SUCCESS.getCode()) && syncStatus.equals(PaySyncStatusEnum.PAY_SUCCESS.getCode())){
            return true;
        }
        // 待支付比对
        if (orderStatus.equals(PayStatusEnum.PROGRESS.getCode()) && syncStatus.equals(PaySyncStatusEnum.PAY_WAIT.getCode())){
            return true;
        }

        // 支付关闭比对
        if (orderStatus.equals(PayStatusEnum.CLOSE.getCode()) && syncStatus.equals(PaySyncStatusEnum.CLOSED.getCode())){
            return true;
        }
        return false;
    }
}
