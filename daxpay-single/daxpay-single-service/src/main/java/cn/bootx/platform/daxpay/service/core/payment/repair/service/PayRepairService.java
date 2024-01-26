package cn.bootx.platform.daxpay.service.core.payment.repair.service;

import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.code.PayRepairTypeEnum;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayChannelOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayChannelOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.payment.repair.factory.PayRepairStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.PayRepairResult;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import cn.bootx.platform.daxpay.service.core.record.repair.service.PayRepairRecordService;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private final PayChannelOrderManager channelOrderManager;

    private final PayRepairRecordService recordService;

    /**
     * 修复支付单
     */
    @Transactional(rollbackFor = Exception.class)
    public PayRepairResult repair(PayOrder order, PayRepairTypeEnum repairType){
        // 1. 获取支付单管理的通道支付订单
        Map<String, PayChannelOrder> channelOrderMap = channelOrderManager.findAllByPaymentId(order.getId())
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
            case SUCCESS:
                this.success(order, repairStrategies);
                repairResult.setAfterPayStatus(PayStatusEnum.SUCCESS);
                break;
            case CLOSE_LOCAL:
                this.closeLocal(order, repairStrategies);
                repairResult.setAfterPayStatus(PayStatusEnum.CLOSE);
                break;
            case WAIT_PAY:
                this.waitPay(order, repairStrategies);
                repairResult.setAfterPayStatus(PayStatusEnum.PROGRESS);
                break;
            case CLOSE_GATEWAY:
                this.closeGateway(order, repairStrategies);
                repairResult.setAfterPayStatus(PayStatusEnum.CLOSE);
                break;
            default:
                log.error("走到了理论上讲不会走到的分支");
        }
        // 设置修复iD
        repairResult.setRepairId(IdUtil.getSnowflakeNextId());
        this.saveRecord(order, repairType, repairResult);
        return repairResult;
    }

    /**
     * 变更未待支付
     *
     */
    private void waitPay(PayOrder order, List<AbsPayRepairStrategy> repairStrategies) {

        repairStrategies.forEach(AbsPayRepairStrategy::doCloseLocalHandler);
        // 修改订单支付状态为成功
        order.setStatus(PayStatusEnum.PROGRESS.getCode());
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
        order.setStatus(PayStatusEnum.CLOSE.getCode());
        payOrderService.updateById(order);
    }
    /**
     * 关闭网关交易, 同时也会关闭本地支付
     * 回调: 执行所有的支付通道关闭支付逻辑
     */
    private void closeGateway(PayOrder payOrder, List<AbsPayRepairStrategy> absPayStrategies) {
        // 执行策略的关闭方法
        absPayStrategies.forEach(AbsPayRepairStrategy::doCloseGatewayHandler);
        payOrder.setStatus(PayStatusEnum.CLOSE.getCode());
        payOrderService.updateById(payOrder);
    }

    /**
     * 保存记录
     */
    private void saveRecord(PayOrder order, PayRepairTypeEnum recordType, PayRepairResult repairResult){
        // 修复后的状态
        String afterStatus = Optional.ofNullable(repairResult.getAfterPayStatus()).map(PayStatusEnum::getCode).orElse(null);
        // 修复发起来源
        String source = PaymentContextLocal.get()
                .getRepairInfo()
                .getSource();
        PayRepairRecord payRepairRecord = new PayRepairRecord()
                .setRepairId(repairResult.getRepairId())
                .setOrderId(order.getId())
                .setAsyncChannel(order.getAsyncChannel())
                .setOrderNo(order.getBusinessNo())
                .setBeforeStatus(repairResult.getBeforeStatus().getCode())
                .setAfterStatus(afterStatus)
                .setRepairSource(source)
                .setRepairType(recordType.getCode());
        payRepairRecord.setId(repairResult.getRepairId());
        recordService.saveRecord(payRepairRecord);
    }
}
