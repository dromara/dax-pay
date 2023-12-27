package cn.bootx.platform.daxpay.core.channel.alipay.service;

import cn.bootx.platform.daxpay.code.AliPayCode;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import cn.bootx.platform.daxpay.core.payment.sync.result.SyncResult;
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
    public SyncResult syncPayStatus(Long paymentId) {
        SyncResult syncResult = new SyncResult().setSyncStatus(PaySyncStatusEnum.FAIL.getCode());

        // 查询
        try {
            AlipayTradeQueryModel queryModel = new AlipayTradeQueryModel();
            queryModel.setOutTradeNo(String.valueOf(paymentId));
            // 查询退款参数
            AlipayTradeQueryResponse response = AliPayApi.tradeQueryToResponse(queryModel);
            String tradeStatus = response.getTradeStatus();
            syncResult.setJson(JSONUtil.toJsonStr(response));
            // 支付完成
            if (Objects.equals(tradeStatus, AliPayCode.PAYMENT_TRADE_SUCCESS)
                    || Objects.equals(tradeStatus, AliPayCode.PAYMENT_TRADE_FINISHED)) {

                HashMap<String, String> map = new HashMap<>(1);
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
            // 退款 支付宝查不到

        }
        catch (AlipayApiException e) {
            log.error("查询订单失败:", e);
            syncResult.setMsg(e.getErrMsg());
        }
        return syncResult;
    }

}
