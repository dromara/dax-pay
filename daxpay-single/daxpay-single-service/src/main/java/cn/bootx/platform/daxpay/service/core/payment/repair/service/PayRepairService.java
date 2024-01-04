package cn.bootx.platform.daxpay.service.core.payment.repair.service;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.service.common.local.PaymentContextLocal;
import cn.bootx.platform.daxpay.service.core.payment.repair.factory.PayRepairStrategyFactory;
import cn.bootx.platform.daxpay.service.core.payment.repair.param.PayRepairParam;
import cn.bootx.platform.daxpay.service.core.payment.repair.result.RepairResult;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.record.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * 修复支付单
     */
    @Transactional(rollbackFor = Exception.class )
    public RepairResult repair(PayOrder order, PayRepairParam repairParam){
        // 从退款记录中获取支付通道 退款记录中的支付通道跟支付时关联的支付通道一致
        List<String> channels = order.getRefundableInfos()
                .stream()
                .map(OrderRefundableInfo::getChannel)
                .collect(Collectors.toList());
        // 初始化修复参数
        List<AbsPayRepairStrategy> repairStrategies = PayRepairStrategyFactory.createAsyncLast(channels);
        repairStrategies.forEach(repairStrategy -> repairStrategy.initRepairParam(order, repairParam.getRepairSource()));
        RepairResult repairResult = new RepairResult().setOldStatus(PayStatusEnum.findByCode(order.getStatus()));
        // 根据不同的类型执行对应的修复逻辑
        switch (repairParam.getRepairType()) {
            case SUCCESS:
                this.success(order, repairStrategies);
                repairResult.setRepairStatus(PayStatusEnum.SUCCESS);
                break;
            case CLOSE:
                this.close(order, repairStrategies);
                repairResult.setRepairStatus(PayStatusEnum.CLOSE);
                break;
            case TIMEOUT:
                this.timeout(order, repairStrategies);
                repairResult.setRepairStatus(PayStatusEnum.TIMEOUT);
                break;
            case REFUND:
                this.refund(order, repairStrategies);
                break;
            default:
                break;
        }
        return repairResult;
    }

    /**
     * 变更为已支付
     * 同步: 将异步支付状态修改为成功
     * 回调: 将异步支付状态修改为成功
     */
    private void success(PayOrder payment, List<AbsPayRepairStrategy> strategies) {
        LocalDateTime payTime = PaymentContextLocal.get()
                .getAsyncPayInfo()
                .getPayTime();

        // 执行个通道的成功处理方法
        strategies.forEach(AbsPayRepairStrategy::doSuccessHandler);

        // 修改订单支付状态为成功
        payment.setStatus(PayStatusEnum.SUCCESS.getCode());
        // TODO 读取支付网关中的时间
        payment.setPayTime(payTime);
        payOrderService.updateById(payment);
    }

    /**
     * 关闭支付
     * 同步: 执行支付单所有的支付通道关闭支付逻辑, 不再重复调用网关进行支付的关闭
     * 回调: 执行所有的支付通道关闭支付逻辑
     */
    private void close(PayOrder payOrder, List<AbsPayRepairStrategy> absPayStrategies) {
        // 执行策略的关闭方法
        absPayStrategies.forEach(AbsPayRepairStrategy::doCloseHandler);
        payOrder.setStatus(PayStatusEnum.CLOSE.getCode());
        payOrderService.updateById(payOrder);
    }
    /**
     * 支付超时关闭订单
     */
    private void timeout(PayOrder payOrder, List<AbsPayRepairStrategy> absPayStrategies) {
        // 执行策略的关闭方法
        absPayStrategies.forEach(AbsPayRepairStrategy::doTimeoutHandler);
        payOrder.setStatus(PayStatusEnum.TIMEOUT.getCode());
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
}
