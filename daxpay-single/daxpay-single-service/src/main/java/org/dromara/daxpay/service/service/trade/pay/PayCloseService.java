package org.dromara.daxpay.service.service.trade.pay;

import org.dromara.daxpay.core.enums.CloseTypeEnum;
import org.dromara.daxpay.core.enums.PayStatusEnum;
import org.dromara.daxpay.core.exception.*;
import org.dromara.daxpay.core.param.trade.pay.PayCloseParam;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.record.close.PayCloseRecord;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import org.dromara.daxpay.service.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.service.service.record.close.PayCloseRecordService;
import org.dromara.daxpay.service.strategy.AbsPayCloseStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import cn.hutool.core.util.StrUtil;
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
public class PayCloseService {
    private final PayOrderManager payOrderManager;

    private final PayOrderQueryService payOrderQueryService;

    private final PayCloseRecordService payCloseRecordService;

    private final MerchantNoticeService merchantNoticeService;

    private final LockTemplate lockTemplate;

    /**
     * 关闭支付
     */
    public void close(PayCloseParam param){
        PayOrder payOrder = payOrderQueryService.findByBizOrOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        this.closeOrder(payOrder, param.isUseCancel());

    }

    /**
     * 关闭支付记录
     */
    public void closeOrder(PayOrder payOrder, boolean useCancel) {
        // 状态检查, 只有支付中可以进行取消支付
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())) {
            throw new TradeStatusErrorException("订单不是支付中, 无法进行关闭订单");
        }
        LockInfo lock = lockTemplate.lock("payment:close:" + payOrder.getId(),10000, 50);
        if (Objects.isNull(lock)){
            throw new TradeProcessingException("支付订单已在关闭中，请勿重复发起");
        }
        try {
            AbsPayCloseStrategy strategy = PaymentStrategyFactory.create(payOrder.getChannel(), AbsPayCloseStrategy.class);
            // 初始化参数
            strategy.init(payOrder, useCancel);
            // 关闭前准备
            strategy.doBeforeCloseHandler();
            // 执行关闭策略, 返回关闭的方式
            CloseTypeEnum closeType = strategy.doCloseHandler();
            // 成功处理 关闭或撤销订单
            var payStatusEnum = useCancel ? PayStatusEnum.CANCEL : PayStatusEnum.CLOSE;
            payOrder.setStatus(payStatusEnum.getCode())
                    .setCloseTime(LocalDateTime.now());
            payOrderManager.updateById(payOrder);
            // 发送通知
            merchantNoticeService.registerPayNotice(payOrder);
            this.saveRecord(payOrder,closeType,null);
            // 返回结果
        } catch (Exception e) {
            log.error("关闭订单失败, id: {}:", payOrder.getId());
            log.error("关闭订单失败:", e);
            // 记录关闭失败的记录
            this.saveRecord(payOrder, CloseTypeEnum.CANCEL, e.getMessage());
            if (e instanceof PayFailureException){
                throw e;
            }
            throw new OperationFailException("关闭订单失败");
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 保存关闭记录
     */
    private void saveRecord(PayOrder payOrder, CloseTypeEnum closeType, String errMsg){
        String clientIp = PaymentContextLocal.get()
                .getClientInfo()
                .getClientIp();
        PayCloseRecord record = new PayCloseRecord()
                .setOrderNo(payOrder.getOrderNo())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setChannel(payOrder.getChannel())
                .setCloseType(closeType.getCode())
                .setClosed(StrUtil.isBlank(errMsg))
                .setErrorMsg(errMsg)
                .setClientIp(clientIp);
        payCloseRecordService.saveRecord(record);
    }
}
