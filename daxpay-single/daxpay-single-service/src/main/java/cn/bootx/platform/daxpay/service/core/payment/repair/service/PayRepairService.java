package cn.bootx.platform.daxpay.service.core.payment.repair.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.code.PayRepairWayEnum;
import cn.bootx.platform.daxpay.service.code.PaymentTypeEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.payment.notice.service.ClientNoticeService;
import cn.bootx.platform.daxpay.service.core.payment.repair.factory.PayRepairStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.PayRepairResult;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import cn.bootx.platform.daxpay.service.core.record.repair.service.PayRepairRecordService;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import cn.hutool.core.util.IdUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 支付修复服务
 * @author xxm
 * @since 2023/12/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRepairService {

    private final PayOrderService payOrderService;

    private final ClientNoticeService clientNoticeService;

    private final PayChannelOrderManager channelOrderManager;

    private final PayRepairRecordService recordService;

    private final LockTemplate lockTemplate;

    /**
     * 修复支付单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayRepairResult repair(PayOrder order, PayRepairWayEnum repairType){
        // 添加分布式锁
        LockInfo lock = lockTemplate.lock("repair:pay:" + order.getId(), 10000, 200);
        if (Objects.isNull(lock)){
            log.warn("当前支付定单正在修复中: {}", order.getId());
            return new PayRepairResult();
        }

        // 1. 获取支付单管理的通道支付订单
        Map<String, PayChannelOrder> channelOrderMap = channelOrderManager.findAllByPaymentId(order.getOrderNo())
                .stream()
                .collect(Collectors.toMap(PayChannelOrder::getChannel, Function.identity(), CollectorsFunction::retainLatest));
        List<String> channels = new ArrayList<>(channelOrderMap.keySet());

        // 2.1 初始化修复参数
        List<AbsPayRepairStrategy> repairStrategies = PayRepairStrategyFactory.createAsyncLast(channels);
        for (AbsPayRepairStrategy repairStrategy : repairStrategies) {
            repairStrategy.initRepairParam(order,channelOrderMap.get(repairStrategy.getChannel().getCode()));
        }

        // 2.2 执行前置处理
        repairStrategies.forEach(AbsPayRepairStrategy::doBeforeHandler);

        // 3. 根据不同的类型执行对应的修复逻辑
        PayRepairResult repairResult = new PayRepairResult().setBeforeStatus(PayStatusEnum.findByCode(order.getStatus()));
        switch (repairType) {
            case PAY_SUCCESS:
                this.success(order, repairStrategies);
                repairResult.setAfterPayStatus(PayStatusEnum.SUCCESS);
                break;
            case CLOSE_LOCAL:
                this.closeLocal(order, repairStrategies);
                repairResult.setAfterPayStatus(PayStatusEnum.CLOSE);
                break;
            case PROGRESS:
                this.waitPay(order, repairStrategies);
                repairResult.setAfterPayStatus(PayStatusEnum.PROGRESS);
                break;
            case CLOSE_GATEWAY:
                this.closeGateway(order, repairStrategies);
                repairResult.setAfterPayStatus(PayStatusEnum.CLOSE);
                break;
            default:
                log.error("走到了理论上讲不会走到的分支");
                throw new PayFailureException("走到了理论上讲不会走到的分支");
        }
        // 设置修复iD
        repairResult.setRepairNo(IdUtil.getSnowflakeNextIdStr());

        // 发送通知
        List<PayChannelOrder> channelOrders = repairStrategies.stream()
                .map(AbsPayRepairStrategy::getChannelOrder)
                .collect(Collectors.toList());
        // 更新通道订单
        channelOrderManager.updateAllById(channelOrders);
        // 发送通知
        clientNoticeService.registerPayNotice(order, null, channelOrders);
        this.saveRecord(order, repairType, repairResult);
        return repairResult;
    }

    /**
     * 变更未待支付
     * TODO 后期保存为异常订单
     */
    private void waitPay(PayOrder order, List<AbsPayRepairStrategy> repairStrategies) {
        // 待支付批量处理
        repairStrategies.forEach(AbsPayRepairStrategy::doWaitPayHandler);
        // 修改订单支付状态为待支付
        order.setStatus(PayStatusEnum.PROGRESS.getCode())
                .setPayTime(null)
                .setCloseTime(null);
        payOrderService.updateById(order);
    }

    /**
     * 变更为已支付
     * 同步: 将异步支付状态修改为成功
     * 回调: 将异步支付状态修改为成功
     */
    private void success(PayOrder order, List<AbsPayRepairStrategy> strategies) {
        LocalDateTime payTime = PaymentContextLocal.get()
                .getRepairInfo()
                .getFinishTime();
        // 执行个通道的成功处理方法
        strategies.forEach(AbsPayRepairStrategy::doPaySuccessHandler);
        // 修改订单支付状态为成功
        order.setStatus(PayStatusEnum.SUCCESS.getCode());
        // 读取支付网关中的时间
        order.setPayTime(payTime);
        payOrderService.updateById(order);
    }

    /**
     * 关闭支付
     * 同步/对账: 执行支付单所有的支付通道关闭支付逻辑, 不需要调用网关关闭,
     */
    private void closeLocal(PayOrder order, List<AbsPayRepairStrategy> absPayStrategies) {
        // 执行策略的关闭方法
        absPayStrategies.forEach(AbsPayRepairStrategy::doCloseLocalHandler);
        order.setStatus(PayStatusEnum.CLOSE.getCode())
                // TODO 尝试是否可以使用网关返回的时间
                .setCloseTime(LocalDateTime.now());
        payOrderService.updateById(order);
    }
    /**
     * 关闭网关交易, 同时也会关闭本地支付
     * 回调: 执行所有的支付通道关闭支付逻辑
     */
    private void closeGateway(PayOrder payOrder, List<AbsPayRepairStrategy> absPayStrategies) {
        // 执行策略的关闭方法
        absPayStrategies.forEach(AbsPayRepairStrategy::doCloseGatewayHandler);
        payOrder.setStatus(PayStatusEnum.CLOSE.getCode())
                // TODO 尝试是否可以使用网关返回的
                .setCloseTime(LocalDateTime.now());
        payOrderService.updateById(payOrder);
    }

    /**
     * 保存记录
     */
    private void saveRecord(PayOrder order, PayRepairWayEnum recordType, PayRepairResult repairResult){
        // 修复后的状态
        String afterStatus = Optional.ofNullable(repairResult.getAfterPayStatus()).map(PayStatusEnum::getCode).orElse(null);
        // 修复发起来源
        String source = PaymentContextLocal.get()
                .getRepairInfo()
                .getSource().getCode();
        PayRepairRecord payRepairRecord = new PayRepairRecord()
                .setRepairNo(repairResult.getRepairNo())
                .setOrderId(order.getId())
                .setAsyncChannel(order.getAsyncChannel())
                .setOrderNo(order.getBusinessNo())
                .setBeforeStatus(repairResult.getBeforeStatus().getCode())
                .setAfterStatus(afterStatus)
                .setRepairType(PaymentTypeEnum.PAY.getCode())
                .setRepairSource(source)
                .setRepairWay(recordType.getCode());
        recordService.saveRecord(payRepairRecord);
    }
}
