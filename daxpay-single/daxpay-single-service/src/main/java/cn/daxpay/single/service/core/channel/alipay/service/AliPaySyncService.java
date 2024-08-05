package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.core.code.RefundSyncStatusEnum;
import cn.daxpay.single.core.code.TransferStatusEnum;
import cn.daxpay.single.core.result.sync.TransferSyncResult;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import cn.daxpay.single.service.core.payment.sync.result.RefundRemoteSyncResult;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

import static cn.daxpay.single.service.code.AliPayCode.GMT_REFUND_PAY;

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
    private final AliPayConfigService aliPayConfigService;

    /**
     * 与支付宝网关同步状态, 退款状态有
     * 1 远程支付成功
     * 2 交易创建，等待买家付款
     * 3 超时关闭
     * 4 查询不到
     * 5 查询失败
     */
    public PayRemoteSyncResult syncPayStatus(PayOrder payOrder, AliPayConfig aliPayConfig){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(aliPayConfig);
        PayRemoteSyncResult syncResult = new PayRemoteSyncResult().setSyncStatus(PaySyncStatusEnum.FAIL);
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
                return syncResult.setSyncStatus(PaySyncStatusEnum.SUCCESS).setFinishTime(payTime);
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
    public RefundRemoteSyncResult syncRefundStatus(RefundOrder refundOrder, AliPayConfig config){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(config);
        RefundRemoteSyncResult syncResult = new RefundRemoteSyncResult().setSyncStatus(RefundSyncStatusEnum.FAIL);
        try {
            AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
            // 退款请求号
            model.setOutRequestNo(String.valueOf(refundOrder.getRefundNo()));
            // 商户订单号
            model.setOutTradeNo(String.valueOf(refundOrder.getOrderNo()));
            // 设置返回退款完成时间
            model.setQueryOptions(Collections.singletonList(GMT_REFUND_PAY));
            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            request.setBizModel(model);
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
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
                return syncResult.setFinishTime(localDateTime).setSyncStatus(RefundSyncStatusEnum.SUCCESS);
            } else {
                return syncResult.setSyncStatus(RefundSyncStatusEnum.FAIL).setErrorMsg("支付宝网关退款未成功");
            }
        } catch (AlipayApiException e) {
            log.error("退款订单同步失败:", e);
            syncResult.setErrorMsg(e.getErrMsg());
        }
        return syncResult;
    }

    /**
     * 转账同步
     */
    @SneakyThrows
    public TransferSyncResult syncTransferStatus(TransferOrder transferOrder){
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient();
        // 构造请求参数以调用接口
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();
        // 设置销售产品码
        model.setProductCode("STD_RED_PACKET");
        // 设置描述特定的业务场景
        model.setBizScene("PERSONAL_PAY");
        // 设置商户转账唯一订单号
        model.setOutBizNo(transferOrder.getTransferNo());
        request.setBizModel(model);
        AlipayFundTransCommonQueryResponse response = alipayClient.execute(request);
        return new TransferSyncResult().setStatus(TransferStatusEnum.FAIL.getCode());
    }
}
