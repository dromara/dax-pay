package cn.daxpay.single.service.core.channel.alipay.service;

import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.daxpay.single.code.PaySyncStatusEnum;
import cn.daxpay.single.service.code.AliPayCode;
import cn.daxpay.single.service.core.payment.sync.result.PayRemoteSyncResult;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.exception.pay.PayFailureException;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.ijpay.alipay.AliPayApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 支付宝支付取消和支付关闭
 *
 * @author xxm
 * @since 2021/4/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayCloseService {
    private final AliPaySyncService aliPaySyncService;

    /**
     * 关闭支付 此处使用交易关闭接口, 支付宝支持 交易关闭 和 交易撤销 两种关闭订单的方式, 区别如下
     * 交易关闭: 只有订单在未支付的状态下才可以进行关闭, 商户不需要额外申请就有此接口的权限
     * 交易撤销: 如果用户支付成功，会将此订单资金退还给用户. 限制时间为1天，过了24小时，该接口无法再使用。可以视为一个特殊的接口, 需要专门签约这个接口的权限
     * <p>
     * 1. 如果未查询到支付单,视为支付关闭,
     * 2. 如果返回"当前交易状态不支持此操作", 同步网关支付状态, 判断网关是否已经被关闭
     *
     */
    @Retryable(value = RetryableException.class)
    public void close(PayOrder payOrder) {
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(payOrder.getOrderNo());

        try {
            AlipayTradeCloseResponse response = AliPayApi.tradeCloseToResponse(model);
            if (!Objects.equals(AliPayCode.SUCCESS, response.getCode())) {
                // 如果返回"当前交易状态不支持此操作", 同步网关支付状态, 判断网关是否已经被关闭
                if (Objects.equals(response.getSubCode(),AliPayCode.ACQ_TRADE_STATUS_ERROR)){
                    if (this.syncStatus(payOrder)){
                        return;
                    }
                }
                // 返回"交易不存在"视同关闭成功
                if (Objects.equals(response.getSubCode(),AliPayCode.ACQ_TRADE_NOT_EXIST)){
                    return;
                }
                log.error("网关返回关闭失败: {}", response.getSubMsg());
                throw new PayFailureException(response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("关闭订单失败:", e);
            throw new PayFailureException("关闭订单失败");
        }
    }


    /**
     * 关闭失败后, 获取支付网关的状态, 如果是关闭返回true, 其他情况抛出异常
     */
    private boolean syncStatus(PayOrder payOrder){
        PayRemoteSyncResult gatewaySyncResult = aliPaySyncService.syncPayStatus(payOrder);
        // 已经关闭
        if (Objects.equals(gatewaySyncResult.getSyncStatus(), PaySyncStatusEnum.CLOSED)){
            return true;
        }
        // 同步错误
        else if (Objects.equals(gatewaySyncResult.getSyncStatus(), PaySyncStatusEnum.FAIL)){
            throw new PayFailureException("关闭失败, 原因: "+gatewaySyncResult.getErrorMsg());
        }
        // 其他状态
        else {
            throw new PayFailureException("当前交易状态不支持关闭操作, 请对订单同步状态后再进行操作");
        }
    }
}
