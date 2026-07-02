package cn.daxpay.open.payment.old.pay.service.trade.pay;

import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderManager;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.entity.record.close.PayCloseRecord;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum;
import cn.daxpay.open.payment.old.pay.exception.TradeNotExistException;
import cn.daxpay.open.payment.old.pay.exception.TradeProcessingException;
import cn.daxpay.open.payment.old.pay.exception.TradeStatusErrorException;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.old.pay.service.notice.MerchantNoticeService;
import cn.daxpay.open.payment.old.pay.service.order.pay.PayOrderQueryService;
import cn.daxpay.open.payment.old.pay.service.record.close.PayCloseRecordService;
import cn.daxpay.open.payment.old.pay.service.trade.TradeUniHandleService;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayCloseParam;
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
@Service("oldPayCloseService")
@RequiredArgsConstructor
public class PayCloseService {
    private final PayOrderManager payOrderManager;

    private final PayOrderQueryService payOrderQueryService;

    private final PayCloseRecordService payCloseRecordService;

    private final MerchantNoticeService merchantNoticeService;

    private final LockTemplate lockTemplate;
    private final TradeUniHandleService tradeUniHandleService;

    /// 关闭支付
    public void close(NormalPayCloseParam param){
        // 校验参数
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())){
            // 支付订单号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        PayOrder payOrder = payOrderQueryService.findAnyOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                // 订单: 支付订单不存在
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
                PayTrade trade = new PayTrade();
                trade.setId(payOrder.getId());
                trade.setProduct(payOrder.getProduct());
                trade.setChannel(payOrder.getChannel());
                trade.setMethod(payOrder.getMethod());
                // 关闭前准备
                PayStrategyContext closeContext = new PayStrategyContext().setTrade(trade);
                strategy.doBeforeClose(closeContext);
                // 执行关闭策略, 返回关闭的方式
                closeType = strategy.doClose(closeContext, useCancel);
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
