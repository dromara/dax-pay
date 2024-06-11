package cn.daxpay.single.service.core.payment.cancel.service;

import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.daxpay.single.code.PayStatusEnum;
import cn.daxpay.single.exception.pay.PayFailureException;
import cn.daxpay.single.param.payment.pay.PayCancelParam;
import cn.daxpay.single.result.pay.PayCancelResult;
import cn.daxpay.single.service.code.PayCloseTypeEnum;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.record.close.entity.PayCloseRecord;
import cn.daxpay.single.service.core.record.close.service.PayCloseRecordService;
import cn.daxpay.single.service.func.AbsPayCancelStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 支付关闭和撤销服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCancelService {
    private final PayOrderService payOrderService;
    private final PayOrderQueryService payOrderQueryService;
    private final PayCloseRecordService payCloseRecordService;
    private final ClientNoticeService clientsService;;

    private final LockTemplate lockTemplate;

    /**
     * 撤销支付
     */
    public PayCancelResult cancel(PayCancelParam param){
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo())
                .orElseThrow(() -> new PayFailureException("支付订单不存在"));
        LockInfo lock = lockTemplate.lock("payment:cancel:" + payOrder.getId(),10000, 50);
        if (Objects.isNull(lock)){
            throw new RepetitiveOperationException("支付订单已在撤销中，请勿重复发起");
        }
        try {
            PayCancelResult result = new PayCancelResult();
            // 状态检查, 只有支付中可以进行撤销支付
            if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
                throw new PayFailureException("订单不是支付中, 无法进行撤销订单");
            }
            try {
                AbsPayCancelStrategy strategy = PayStrategyFactory.create(payOrder.getChannel(), AbsPayCancelStrategy.class);
                // 设置支付订单
                strategy.setOrder(payOrder);
                // 撤销前准备
                strategy.doBeforeCancelHandler();
                // 执行撤销策略
                strategy.doCancelHandler();
                // 成功处理
                this.successHandler(payOrder);
                // 返回结果
                return result;
            } catch (Exception e) {
                log.error("撤销订单失败, id: {}:", payOrder.getId());
                log.error("撤销订单失败:", e);
                // 记录撤销失败的记录
                this.saveRecord(payOrder, false, e.getMessage());
                throw new PayFailureException("撤销订单失败");
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 成功后处理方法
     */
    private void successHandler(PayOrder payOrder){
        // 撤销订单
        payOrder.setStatus(PayStatusEnum.CANCEL.getCode())
                .setCloseTime(LocalDateTime.now());
        payOrderService.updateById(payOrder);
        // 发送通知
        clientsService.registerPayNotice(payOrder);
        this.saveRecord(payOrder,true,null);
    }

    /**
     * 保存撤销记录
     */
    private void saveRecord(PayOrder payOrder, boolean closed, String errMsg){
        String clientIp = PaymentContextLocal.get()
                .getClientInfo()
                .getClientIp();
        PayCloseRecord record = new PayCloseRecord()
                .setOrderNo(payOrder.getOrderNo())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setChannel(payOrder.getChannel())
                .setCloseType(PayCloseTypeEnum.CANCEL.getCode())
                .setClosed(closed)
                .setErrorMsg(errMsg)
                .setClientIp(clientIp);
        payCloseRecordService.saveRecord(record);
    }

}
