package cn.bootx.platform.daxpay.service.core.payment.repair.service;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.entity.RefundableInfo;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.payment.repair.factory.PayRepairStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.repair.param.PayRepairParam;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.RepairResult;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.order.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.core.record.repair.entity.PayRepairRecord;
import cn.bootx.platform.daxpay.service.core.record.repair.service.PayRepairRecordService;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    private final PayRepairRecordService recordService;

    /**
     * 修复支付单
     */
    @Transactional(rollbackFor = Exception.class)
    public RepairResult repair(PayOrder order, PayRepairParam repairParam){
        // 从退款记录中获取支付通道 退款记录中的支付通道跟支付时关联的支付通道一致
        List<String> channels = order.getRefundableInfos()
                .stream()
                .map(RefundableInfo::getChannel)
                .collect(Collectors.toList());

        // 初始化修复参数
        List<AbsPayRepairStrategy> repairStrategies = PayRepairStrategyFactory.createAsyncLast(channels);
        repairStrategies.forEach(repairStrategy -> repairStrategy.initRepairParam(order, repairParam.getRepairSource()));
        repairStrategies.forEach(AbsPayRepairStrategy::doBeforeHandler);
        RepairResult repairResult = new RepairResult().setBeforeStatus(PayStatusEnum.findByCode(order.getStatus()));

        // 根据不同的类型执行对应的修复逻辑
        switch (repairParam.getRepairType()) {
            case SUCCESS:
                this.success(order, repairStrategies);
                repairResult.setRepairStatus(PayStatusEnum.SUCCESS);
                break;
            case CLOSE_LOCAL:
                this.closeLocal(order, repairStrategies);
                repairResult.setRepairStatus(PayStatusEnum.CLOSE);
                break;
            case WAIT:
                this.wait(order, repairStrategies);
                repairResult.setRepairStatus(PayStatusEnum.PROGRESS);
                break;
            case CLOSE_GATEWAY:
                this.closeGateway(order, repairStrategies);
                repairResult.setRepairStatus(PayStatusEnum.CLOSE);
                break;
            case REFUND:
                this.refund(order, repairStrategies);
//                repairResult.setRepairStatus(PayStatusEnum.REFUNDED);
                break;
            default:
                repairResult.setRepairStatus(repairResult.getBeforeStatus());
                break;
        }
        PayRepairRecord payRepairRecord = this.saveRecord(order, repairParam, repairResult);
        return repairResult.setId(payRepairRecord.getId());
    }

    /**
     * 变更未待支付
     *
     */
    private void wait(PayOrder order, List<AbsPayRepairStrategy> repairStrategies) {
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
                .getAsyncPayInfo()
                .getPayTime();
        // 执行个通道的成功处理方法
        strategies.forEach(AbsPayRepairStrategy::doSuccessHandler);

        // 修改订单支付状态为成功
        order.setStatus(PayStatusEnum.SUCCESS.getCode());
        // 读取支付网关中的时间
        order.setPayTime(payTime);
        payOrderService.updateById(order);
    }

    /**
     * 关闭支付
     * 同步: 执行支付单所有的支付通道关闭支付逻辑, 如果来源是网关同步, 则不需要调用网关关闭
     * 回调: 执行所有的支付通道关闭支付逻辑
     */
    private void closeLocal(PayOrder order, List<AbsPayRepairStrategy> absPayStrategies) {
        // 执行策略的关闭方法
        absPayStrategies.forEach(AbsPayRepairStrategy::doCloseLocalHandler);
        order.setStatus(PayStatusEnum.CLOSE.getCode());
        payOrderService.updateById(order);
    }
    /**
     * 关闭网关交易, 同时也会关闭本地支付
     */
    private void closeGateway(PayOrder payOrder, List<AbsPayRepairStrategy> absPayStrategies) {
        // 执行策略的关闭方法
        absPayStrategies.forEach(AbsPayRepairStrategy::doCloseGatewayHandler);
        payOrder.setStatus(PayStatusEnum.CLOSE.getCode());
        payOrderService.updateById(payOrder);
    }

    /**
     * 退款 TODO 需要调用退款同步策略进行补偿
     * 同步:
     * 回调:
     */
    public void refund(PayOrder payOrder, List<AbsPayRepairStrategy> strategies){
        // 判断修复后价格总价格是否为0

        // 为0变更为全部退款

        // 不为0调用退款同步接口

    }

    /**
     * 保存记录
     */
    private PayRepairRecord saveRecord(PayOrder order, PayRepairParam repairParam, RepairResult repairResult){
        // 修复后的状态
        String afterStatus = Optional.ofNullable(repairResult.getRepairStatus()).map(PayStatusEnum::getCode).orElse(null);

        PayRepairRecord payRepairRecord = new PayRepairRecord()
                .setPaymentId(order.getId())
                .setAsyncChannel(order.getAsyncChannel())
                .setBusinessNo(order.getBusinessNo())
                .setBeforeStatus(repairResult.getBeforeStatus().getCode())
                .setAfterStatus(afterStatus)
                .setAmount(repairParam.getAmount())
                .setRepairSource(repairParam.getRepairSource().getCode())
                .setRepairType(repairParam.getRepairType().getCode());
        recordService.saveRecord(payRepairRecord);
        return payRepairRecord;
    }
}
