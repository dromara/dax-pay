package cn.daxpay.single.service.core.payment.repair.service;

import cn.daxpay.single.core.code.PayStatusEnum;
import cn.daxpay.single.core.exception.OperationProcessingException;
import cn.daxpay.single.core.exception.SystemUnknownErrorException;
import cn.daxpay.single.core.exception.TradeStatusErrorException;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import cn.daxpay.single.service.code.TradeTypeEnum;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.pay.service.PayOrderService;
import cn.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import cn.daxpay.single.service.core.payment.repair.param.PayRepairParam;
import cn.daxpay.single.service.core.record.flow.service.TradeFlowRecordService;
import cn.daxpay.single.service.core.record.repair.entity.TradeAdjustRecord;
import cn.daxpay.single.service.core.record.repair.service.TradeAdjustRecordService;
import cn.daxpay.single.service.func.AbsPayAdjustStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 支付订单调整服务
 * @author xxm
 * @since 2024/7/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayAdjustService {

    private final ClientNoticeService clientNoticeService;

    private final LockTemplate lockTemplate;
    private final TradeAdjustRecordService tradeAdjustRecordService;
    private final PayOrderService payOrderService;
    private final TradeFlowRecordService tradeFlowRecordService;

    /**
     * 调整服务
     */
    public String adjust(PayOrder order, PayRepairParam param){
        // 添加分布式锁
        LockInfo lock = lockTemplate.lock("repair:pay:" + order.getId(), 10000, 200);
        if (Objects.isNull(lock)){
            log.warn("当前支付订单正在修复中: {}", order.getId());
            throw new OperationProcessingException("当前支付订单正在修复中");
        }
        // 如果到达终态不能向前回滚
        if (Objects.equals(order.getStatus(), PayStatusEnum.SUCCESS.getCode())){
            throw new TradeStatusErrorException("当前支付订单已支付成功");
        }

        // 初始化调整参数
        AbsPayAdjustStrategy repairStrategy = PayStrategyFactory.create(order.getChannel(), AbsPayAdjustStrategy.class);
        repairStrategy.setOrder(order);

        // 执行前置处理
        repairStrategy.doBeforeHandler();
        String beforeStatus = order.getStatus();
        // 根据不同的调整方式执行对应的修复逻辑
        switch (param.getAdjustWay()) {
            case SUCCESS:
                this.success(order);
                break;
            case CLOSE_LOCAL:
                this.closeLocal(order);
                break;
            case CLOSE_GATEWAY:
                this.closeRemote(order, repairStrategy);
                break;
            default:
                log.error("走到了理论上讲不会走到的分支");
                throw new SystemUnknownErrorException("走到了理论上讲不会走到的分支");
        }

        // 发送通知
        clientNoticeService.registerPayNotice(order);
        TradeAdjustRecord record = this.saveRecord(order, param, beforeStatus);
        return record.getRepairNo();
    }

    /**
     * 变更为已支付
     * 同步: 将异步支付状态修改为成功
     * 回调: 将异步支付状态修改为成功
     */
    private void success(PayOrder order) {
        // 读取支付网关中的完成时间
        LocalDateTime payTime = PaymentContextLocal.get()
                .getRepairInfo()
                .getFinishTime();
        // 修改订单支付状态为成功
        order.setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(payTime)
                .setCloseTime(null);
        tradeFlowRecordService.savePay(order);
        payOrderService.updateById(order);
    }

    /**
     * 关闭支付
     * 同步/对账: 执行支付单所有的支付通道关闭支付逻辑, 不需要调用网关关闭,
     */
    private void closeLocal(PayOrder order) {
        // 执行策略的关闭方法
        order.setStatus(PayStatusEnum.CLOSE.getCode())
                .setCloseTime(LocalDateTime.now());
        payOrderService.updateById(order);
    }
    /**
     * 关闭网关交易, 同时也会关闭本地支付
     * 回调: 执行所有的支付通道关闭支付逻辑
     */
    private void closeRemote(PayOrder payOrder, AbsPayAdjustStrategy strategy) {
        // 执行策略的关闭方法
        strategy.doCloseRemoteHandler();
        payOrder.setStatus(PayStatusEnum.CLOSE.getCode())
                .setCloseTime(LocalDateTime.now());
        payOrderService.updateById(payOrder);
    }

    /**
     * 保存记录
     */
    private TradeAdjustRecord saveRecord(PayOrder order, PayRepairParam param, String beforeStatus){
        // 修复后的状态
        TradeAdjustRecord record = new TradeAdjustRecord()
                .setRepairNo(TradeNoGenerateUtil.adjust())
                .setTradeId(order.getId())
                .setChannel(order.getChannel())
                .setSource(param.getSource().getCode())
                .setTradeNo(order.getOrderNo())
                .setBeforeStatus(beforeStatus)
                .setAfterStatus(order.getStatus())
                .setType(TradeTypeEnum.PAY.getCode())
                .setWay(param.getAdjustWay().getCode());
        tradeAdjustRecordService.saveRecord(record);
        return record;
    }

}
