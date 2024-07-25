package cn.daxpay.multi.channel.alipay.service.sync;

import cn.daxpay.multi.channel.alipay.code.AliPayCode;
import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.service.bo.sync.PaySyncResultBo;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.enums.PaySyncResultEnum;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * 支付宝支付同步
 *
 * @author xxm
 * @since 2021/5/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPaySyncService {
    private final AliPayConfigService aliPayConfigService;

    /**
     * 与支付宝网关同步状态, 退款状态有
     * 1 远程支付成功
     * 2 交易创建，等待买家付款
     * 3 超时关闭
     * 4 查询不到
     * 5 查询失败
     */
    public PaySyncResultBo syncPayStatus(PayOrder payOrder, AliPayConfig aliPayConfig){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);
        PaySyncResultBo syncResult = new PaySyncResultBo().setSyncStatus(PaySyncResultEnum.FAIL);
        // 查询
        try {
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(payOrder.getOrderNo());
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizModel(model);
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            String tradeStatus = response.getTradeStatus();
            syncResult.setSyncInfo(JSONUtil.toJsonStr(response));
            // 设置网关订单号
            syncResult.setOutOrderNo(response.getTradeNo());
            // 支付完成  部分退款无法进行区分, 需要借助对账进行处理
            if (Objects.equals(tradeStatus, AliPayCode.NOTIFY_TRADE_SUCCESS) || Objects.equals(tradeStatus, AliPayCode.NOTIFY_TRADE_FINISHED)) {
                // 支付完成时间
                LocalDateTime payTime = LocalDateTimeUtil.of(response.getSendPayDate());
                return syncResult.setSyncStatus(PaySyncResultEnum.SUCCESS).setFinishTime(payTime);
            }
            // 待支付
            if (Objects.equals(tradeStatus, AliPayCode.NOTIFY_WAIT_BUYER_PAY)) {
                return syncResult.setSyncStatus(PaySyncResultEnum.PROGRESS);
            }
            // 已关闭或支付完成后全额退款
            if (Objects.equals(tradeStatus, AliPayCode.NOTIFY_TRADE_CLOSED)) {
                // 判断是否有支付时间, 有支付时间说明是退款
                if (Objects.nonNull(response.getSendPayDate())){
                    return syncResult.setSyncStatus(PaySyncResultEnum.REFUND);
                } else {
                    return syncResult.setSyncStatus(PaySyncResultEnum.CLOSED);
                }
            }
            // 支付宝支付后, 客户未进行操作将不会创建出订单, 所以交易不存在约等于未查询订单
            if (Objects.equals(response.getSubCode(), AliPayCode.ACQ_TRADE_NOT_EXIST)) {
                return syncResult.setSyncStatus(PaySyncResultEnum.NOT_FOUND_UNKNOWN);
            }
            // 查询失败
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                syncResult.setSyncStatus(PaySyncResultEnum.FAIL);
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

}
