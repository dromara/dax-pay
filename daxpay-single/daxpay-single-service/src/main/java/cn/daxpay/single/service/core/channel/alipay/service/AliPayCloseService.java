package cn.daxpay.single.service.core.channel.alipay.service;

import cn.daxpay.single.core.code.PaySyncStatusEnum;
import cn.daxpay.single.core.exception.OperationFailException;
import cn.daxpay.single.core.exception.TradeStatusErrorException;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付宝支付撤销和支付关闭
 *
 * @author xxm
 * @since 2021/4/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayCloseService {
    private final AliPaySyncService aliPaySyncService;

    private final AliPayConfigService aliPayConfigService;

    /**
     * 关闭支付 此处使用交易关闭接口
     * 交易关闭: 只有订单在未支付的状态下才可以进行关闭, 商户不需要额外申请就有此接口的权限
     * <p>
     * 1. 如果未查询到支付单,视为支付关闭,
     * 2. 如果返回"当前交易状态不支持此操作", 同步网关支付状态, 判断网关是否已经被关闭
     *
     */
    public void close(PayOrder payOrder, AliPayConfig config) {
        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(config);
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(payOrder.getOrderNo());
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizModel(model);
        try {
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                // 如果返回"当前交易状态不支持此操作", 同步网关支付状态, 判断网关是否已经被关闭
                if (Objects.equals(response.getSubCode(),AliPayCode.ACQ_TRADE_STATUS_ERROR)){
                    if (this.syncStatus(payOrder, config)){
                        return;
                    }
                }
                // 返回"交易不存在"视同关闭成功
                if (Objects.equals(response.getSubCode(),AliPayCode.ACQ_TRADE_NOT_EXIST)){
                    return;
                }
                log.error("网关返回关闭失败: {}", response.getSubMsg());
                throw new OperationFailException(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("关闭订单失败:", e);
            throw new OperationFailException("关闭订单失败");
        }
    }

    /**交易撤销: 如果用户支付成功，会将此订单资金退还给用户. 限制时间为1天，过了24小时，该接口无法再使用。
     * 可以视为一个特殊的接口, 需要专门签约这个接口的权限
     *
     */
    public void cancel(PayOrder payOrder, AliPayConfig config) {

        AlipayClient alipayClient = aliPayConfigService.getAlipayClient(config);
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setOutTradeNo(payOrder.getOrderNo());
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(model);
        try {
            AlipayTradeCancelResponse response = alipayClient.execute(request);;
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                    // 如果返回"当前交易状态不支持此操作", 同步网关支付状态, 判断网关是否已经被关闭
                    if (Objects.equals(response.getSubCode(),AliPayCode.ACQ_TRADE_STATUS_ERROR)){
                        if (this.syncStatus(payOrder, config)){
                            return;
                        }
                    }
                    // 返回"交易不存在"视同关闭成功
                    if (Objects.equals(response.getSubCode(),AliPayCode.ACQ_TRADE_NOT_EXIST)){
                        return;
                    }
                    log.error("网关返回关闭失败: {}", response.getSubMsg());
                    throw new OperationFailException(response.getSubMsg());
                }
            }
        } catch (AlipayApiException e) {
            log.error("关闭订单失败:", e);
            throw new OperationFailException("关闭订单失败");
        }
    }


    /**
     * 关闭失败后, 获取支付网关的状态, 如果是关闭返回true, 其他情况抛出异常
     */
    private boolean syncStatus(PayOrder payOrder, AliPayConfig config){
        PayRemoteSyncResult gatewaySyncResult = aliPaySyncService.syncPayStatus(payOrder,config);
        // 已经关闭
        if (Objects.equals(gatewaySyncResult.getSyncStatus(), PaySyncStatusEnum.CLOSED)){
            return true;
        }
        // 同步错误
        else if (Objects.equals(gatewaySyncResult.getSyncStatus(), PaySyncStatusEnum.FAIL)){
            throw new OperationFailException("关闭失败, 原因: "+gatewaySyncResult.getErrorMsg());
        }
        // 其他状态
        else {
            throw new TradeStatusErrorException("当前交易无法关闭操作, 请对订单同步状态后再进行操作");
        }
    }
}
