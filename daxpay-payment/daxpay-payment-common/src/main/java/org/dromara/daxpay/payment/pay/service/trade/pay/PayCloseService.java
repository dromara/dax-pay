package org.dromara.daxpay.payment.pay.service.trade.pay;

import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.exception.PayFailureException;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.entity.record.close.PayCloseRecord;
import org.dromara.daxpay.platform.core.enums.pay.pay.CloseTypeEnum;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayStatusEnum;
import org.dromara.daxpay.payment.pay.exception.TradeNotExistException;
import org.dromara.daxpay.payment.pay.exception.TradeProcessingException;
import org.dromara.daxpay.payment.pay.exception.TradeStatusErrorException;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.pay.service.notice.MerchantNoticeService;
import org.dromara.daxpay.payment.pay.service.order.pay.PayOrderQueryService;
import org.dromara.daxpay.payment.pay.service.record.close.PayCloseRecordService;
import org.dromara.daxpay.payment.pay.service.trade.TradeUniHandleService;
import org.dromara.daxpay.payment.pay.strategy.AbsPayCloseStrategy;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayCloseParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 支付关闭和撤销服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseService {
    private final PayOrderManager payOrderManager;

    private final PayOrderQueryService payOrderQueryService;

    private final PayCloseRecordService payCloseRecordService;

    private final MerchantNoticeService merchantNoticeService;

    private final LockTemplate lockTemplate;
    private final TradeUniHandleService tradeUniHandleService;

    private final PaymentContext apiContext;

    /// 关闭支付
    public void close(PayCloseParam param){
        // 校验参数
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())){
            // 支付订单号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        PayOrder payOrder = payOrderQueryService.findAnyOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        this.closeOrder(payOrder, param.isUseCancel());

    }

    /// 关闭支付记录
    public void closeOrder(PayOrder payOrder, boolean useCancel) {
        // 状态检查, 只有待支付和支付中可以进行取消支付
        if (!List.of(PayStatusEnum.WAIT.getCode(),PayStatusEnum.PROGRESS.getCode()).contains(payOrder.getStatus())) {
            // 订单不是支付中, 无法进行关闭订单
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.closeNotPaying");
        }
        LockInfo lock = lockTemplate.lock("payment:close:" + payOrder.getId(),10000, 50);
        if (Objects.isNull(lock)){
            // 支付订单已在关闭中，请勿重复发起
            throw new TradeProcessingException();
        }

        try {
            CloseTypeEnum closeType;
            PayStatusEnum payStatusEnum;
            if (Objects.equals(PayStatusEnum.WAIT.getCode(), payOrder.getStatus())){
                closeType = CloseTypeEnum.CLOSE;
                payStatusEnum = PayStatusEnum.CLOSE;
            } else {
                AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(payOrder.getProduct(), AbsPayCloseStrategy.class);
                // 初始化参数
                strategy.init(payOrder, useCancel);
                // 关闭前准备
                strategy.doBeforeCloseHandler();
                // 执行关闭策略, 返回关闭的方式
                closeType = strategy.doCloseHandler();
                // 成功处理 关闭或撤销订单
                payStatusEnum = useCancel ? PayStatusEnum.CANCEL : PayStatusEnum.CLOSE;
            }
            // 关闭处理
            tradeUniHandleService.payClose(payOrder, payStatusEnum);
            this.saveRecord(payOrder,closeType,null);
        } catch (Exception e) {
            log.error("关闭订单失败, id: {}:", payOrder.getId());
            log.error("关闭订单失败:", e);
            // 记录关闭失败的记录
            this.saveRecord(payOrder, useCancel?CloseTypeEnum.CANCEL:CloseTypeEnum.CLOSE, e.getMessage());
            if (e instanceof PayFailureException){
                throw e;
            }
            // 关闭订单失败
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.pay.closeFailed");
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 保存关闭记录
    private void saveRecord(PayOrder payOrder, CloseTypeEnum closeType, String errMsg){
        PayCloseRecord record = new PayCloseRecord()
                .setOrderNo(payOrder.getOrderNo())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setChannel(payOrder.getChannel())
                .setProduct(payOrder.getProduct())
                .setCloseType(closeType.getCode())
                .setClosed(StrUtil.isBlank(errMsg))
                .setErrorMsg(errMsg);
        payCloseRecordService.saveRecord(record);
    }
}
