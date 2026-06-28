package cn.daxpay.open.payment.old.pay.service.trade.pay;

import cn.daxpay.open.platform.core.exception.RepetitiveOperationException;
import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.platform.core.exception.system.DataErrorException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.exception.system.SystemUnknownErrorException;
import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.old.pay.bo.sync.PaySyncResultBo;
import cn.daxpay.open.payment.old.pay.convert.order.pay.PayOrderConvert;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderExpandManager;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderManager;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.entity.record.sync.TradeSyncRecord;
import cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.daxpay.open.payment.old.pay.exception.TradeNotExistException;
import cn.daxpay.open.payment.old.pay.exception.TradeStatusErrorException;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.old.pay.service.order.pay.PayOrderQueryService;
import cn.daxpay.open.payment.old.pay.service.record.sync.TradeSyncRecordService;
import cn.daxpay.open.payment.old.pay.service.trade.TradeUniHandleService;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.PaySyncParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.PaySyncResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

/// # 支付同步服务
///
@Slf4j
@Service("oldPaySyncService")
@RequiredArgsConstructor
public class PaySyncService {
    private final PayOrderQueryService payOrderQueryService;

    private final PayOrderManager payOrderManager;

    private final TradeSyncRecordService tradeSyncRecordService;

    private final LockTemplate lockTemplate;

    private final PayOrderExpandManager payOrderExpandManager;

    private final TradeUniHandleService tradeUniHandleService;

    private final PaymentContext apiContext;

    /// 支付同步, 开启一个新的事务, 不受外部抛出异常的影响
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PaySyncResult sync(PaySyncParam param) {
        // 校验参数
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())&& Objects.isNull(param.getOutOrderNo())){
            // 支付订单号不能都为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        PayOrder payOrder = payOrderQueryService.findAnyOrderNo(param.getOrderNo(), param.getBizOrderNo(), param.getAppId())
                // 订单: 支付订单不存在
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        // 执行订单同步逻辑
        return this.syncPayOrder(payOrder);
    }
    /// 同步支付状态, 开启一个新的事务, 不受外部抛出异常的影响
    /// 1. 如果状态一致, 不进行处理， 直接返回
    /// 2. 如果状态不一致, 更新状态/完成时间/关联网关订单号
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PaySyncResult syncPayOrder(PayOrder payOrder) {
        // 待支付状态不允许同步
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.WAIT.getCode())){
            // 订单未开始支付, 请重新确认支付状态
            throw new TradeStatusErrorException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.syncNotStarted");
        }
        // 加锁
        LockInfo lock = lockTemplate.lock("sync:pay:" + payOrder.getId(),10000,200);
        if (Objects.isNull(lock)){
            // 支付同步处理中，请勿重复操作
            throw new RepetitiveOperationException();
        }
        // 获取支付同步策略类并初始化
        var syncPayStrategy = PaymentStrategyFactory.createByProduct(payOrder.getProduct(), AbsSyncPayOrderStrategy.class);
        PayTrade trade = new PayTrade();
        trade.setId(payOrder.getId());
        trade.setProduct(payOrder.getProduct());
        trade.setChannel(payOrder.getChannel());
        trade.setMethod(payOrder.getMethod());
        trade.setAmount(payOrder.getAmount().multiply(BigDecimal.valueOf(100)).longValue());
        try {
            // 执行操作, 获取支付网关同步的结果
            var newResult = syncPayStrategy.doSync(trade);
            PaySyncResultBo syncResult = new PaySyncResultBo();
            syncResult.setSyncSuccess(newResult.isSyncSuccess());
            if (newResult.getPayStatus() != null) {
                syncResult.setPayStatus(switch (newResult.getPayStatus()) {
                    case INIT -> PayStatusEnum.WAIT;
                    case PROCESSING -> PayStatusEnum.PROGRESS;
                    case SUCCESS -> PayStatusEnum.SUCCESS;
                    case FAIL -> PayStatusEnum.FAIL;
                    case CLOSE -> PayStatusEnum.CLOSE;
                });
            }
            syncResult.setOutOrderNo(newResult.getOutOrderNo());
            syncResult.setAmount(newResult.getAmount() != null ? BigDecimal.valueOf(newResult.getAmount(), 2) : null);
            syncResult.setRealAmount(newResult.getRealAmount() != null ? BigDecimal.valueOf(newResult.getRealAmount(), 2) : null);
            syncResult.setFinishTime(newResult.getFinishTime());
            syncResult.setSyncData(newResult.getSyncData());
            syncResult.setSyncErrorCode(newResult.getSyncErrorCode());
            syncResult.setSyncErrorMsg(newResult.getSyncErrorMsg());
            syncResult.setBuyerId(newResult.getBuyerId());
            syncResult.setUserId(newResult.getUserId());
            syncResult.setTradeProduct(newResult.getTradeProduct());
            syncResult.setTradeWay(newResult.getTradeWay());
            syncResult.setBankType(newResult.getBankType());
            syncResult.setProvider(newResult.getProvider());
            syncResult.setTransOrderNo(newResult.getTransOrderNo());
            syncResult.setPromotionType(newResult.getPromotionType());
            // 支付订单的网关订单号是否一致, 不一致进行更新
            if (!Objects.equals(syncResult.getOutOrderNo(), payOrder.getOutOrderNo())){
                payOrder.setOutOrderNo(syncResult.getOutOrderNo());
                payOrderManager.updateById(payOrder);
            }
            // 判断网关状态是否和支付单一致, 同时特定情况下更新网关同步状态或记录异常信息
            boolean statusSync = this.checkAndAdjust(syncResult,payOrder);
            try {
                // 状态不一致，执行支付单调整逻辑
                if (!statusSync){
                    this.adjustHandler(syncResult, payOrder);
                }
            } catch (PayFailureException e) {
                // 同步失败, 返回失败响应, 同时记录失败的日志
                syncResult.setSyncSuccess(false).setSyncErrorMsg(e.getMessage());
            }
            if (syncResult.isSyncSuccess()){
                // 同步成功记录日志
                this.saveRecord(payOrder, syncResult, !statusSync);
            } else {
                // 同步失败记录日志
                this.saveRecord(payOrder, syncResult, true);
            }
            return new PaySyncResult()
                    .setOrderStatus(payOrder.getStatus())
                    .setAdjust(statusSync);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 判断支付单和网关状态是否一致, 同时待支付状态下, 支付单支付超时进行状态的更改
    /// 如果本地订单状态为支付中, 会对订单信息进行调整
    /// 如果本地订单状态不为为支付中, 状态不一致, 未来将 记录异常情况, 现在不会进行任何操作
    private boolean checkAndAdjust(PaySyncResultBo payRemoteSyncResult, PayOrder order){
        // 返回值默认为支付中
        var payStatus = Optional.ofNullable(payRemoteSyncResult.getPayStatus())
                .orElse(PayStatusEnum.PROGRESS);
        String orderStatus = order.getStatus();
        // 如果本地订单为失败时, 直接返回需要进行调整
        if (orderStatus.equals(PayStatusEnum.FAIL.getCode())){
            return false;
        }
        // 本地订单为支付中时, 对状态进行比较,
        if (orderStatus.equals(PayStatusEnum.PROGRESS.getCode())){
            // 如果返回订单也是支付中
            if (Objects.equals(PayStatusEnum.PROGRESS, payStatus)){
                // 判断支付单是否支付超时, 如果待支付状态下触发超时
                if (DateTimeUtil.le(order.getExpiredTime(), OffsetDateTime.now(ZoneOffset.UTC))){
                    // 将支付单同步状态状态调整为支付超时, 进行订单的关闭
                    payRemoteSyncResult.setPayStatus(PayStatusEnum.TIMEOUT);
                    return false;
                }
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /// 根据同步的结果对支付单进行调整处理
    private void adjustHandler(PaySyncResultBo payRemoteSyncResult, PayOrder payOrder){
        var payStatus = payRemoteSyncResult.getPayStatus();
        // 对支付网关同步的结果进行处理
        switch (payStatus) {
            // 支付成功 支付宝退款时也是支付成功状态, 除非支付完成
            case PROGRESS -> {}
            case SUCCESS -> this.success(payOrder, payRemoteSyncResult);
            case CLOSE, CANCEL -> this.closeLocal(payOrder);
            // 超时关闭和交易不存在(特殊) 关闭本地支付订单, 同时调用网关进行关闭, 确保后续这个订单不能被支付
            case TIMEOUT -> this.closeRemote(payOrder);
            // 同步失败处理
            case FAIL -> this.failLocal(payOrder,payRemoteSyncResult);
            default -> // 代码有问题
            throw new SystemUnknownErrorException();
        }
    }

    /// 变更为已支付, 更新扩展记录
    /// 同步: 将异步支付状态修改为成功
    /// 回调: 将异步支付状态修改为成功
    private void success(PayOrder payOrder, PaySyncResultBo resultBo) {
        // 订单: 支付订单扩展信息不存在
        var payOrderExpand = payOrderExpandManager.findById(payOrder.getId()).orElseThrow(() -> new DataErrorException("error.payment.order.payOrderExtNotExist"));
        // 修改订单支付状态为成功
        payOrder.setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(resultBo.getFinishTime())
                .setCloseTime(null)
                .setErrorMsg(null);
        // 支付渠道存在进行更新
        if ((resultBo.getProvider() != null)){
            payOrder.setProvider(resultBo.getProvider().getCode());
        }
        PayOrderConvert.CONVERT.copy(resultBo, payOrderExpand);
        // 统一处理支付成功逻辑
        tradeUniHandleService.payAfterHandel(payOrder,payOrderExpand);
    }

    /// 关闭支付
    /// 同步: 执行支付单所有的支付通道关闭支付逻辑, 不需要调用网关关闭,
    private void closeLocal(PayOrder order) {
        tradeUniHandleService.payClose(order,PayStatusEnum.CLOSE);
    }

    /// 同步失败
    /// 同步失败后, 讲订单设置为失败状态, 预防无限重试, 失败不会触发消息通知
    private void failLocal(PayOrder order, PaySyncResultBo syncResult) {
        tradeUniHandleService.payFail(order, syncResult.getSyncErrorMsg());
    }
    /// 关闭网关交易, 同时也会关闭本地支付
    /// 回调: 执行所有的支付通道关闭支付逻辑
    private void closeRemote(PayOrder order) {
        // 初始化调整参数
        AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(order.getProduct(), AbsPayCloseStrategy.class);
        PayTrade closeTrade = new PayTrade();
        closeTrade.setId(order.getId());
        closeTrade.setProduct(order.getProduct());
        closeTrade.setChannel(order.getChannel());
        closeTrade.setMethod(order.getMethod());
        strategy.doBeforeClose(closeTrade);
        // 执行策略的关闭方法
        strategy.doClose(closeTrade, false);
        // 关闭统一处理
        tradeUniHandleService.payClose(order, PayStatusEnum.CLOSE);
    }

    /// 保存同步记录
    /// @param payOrder 支付单
    /// @param payRemoteSyncResult 同步结果
    /// @param adjust 是否调整
    private void saveRecord(PayOrder payOrder, PaySyncResultBo payRemoteSyncResult, boolean adjust){
        TradeSyncRecord tradeSyncRecord = new TradeSyncRecord()
                .setBizTradeNo(payOrder.getBizOrderNo())
                .setTradeNo(payOrder.getOrderNo())
                .setOutTradeNo(payOrder.getOutOrderNo())
                .setTradeType(TradeTypeEnum.PAY.getCode())
                .setChannel(payOrder.getChannel())
                .setProduct(payOrder.getProduct())
                .setSyncInfo(payRemoteSyncResult.getSyncData())
                .setAdjust(adjust)
                .setErrorCode(payRemoteSyncResult.getSyncErrorCode())
                .setErrorMsg(payRemoteSyncResult.getSyncErrorMsg());
        if (payRemoteSyncResult.isSyncSuccess()){
            tradeSyncRecord.setOutTradeStatus(payRemoteSyncResult.getPayStatus().getCode());
        }
        tradeSyncRecordService.saveRecord(tradeSyncRecord);
    }

}

