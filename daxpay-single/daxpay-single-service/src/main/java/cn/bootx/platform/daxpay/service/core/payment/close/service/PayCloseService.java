package cn.bootx.platform.daxpay.service.core.payment.close.service;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.service.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.payment.close.factory.PayCloseStrategyFactory;
import cn.bootx.platform.daxpay.service.core.record.pay.service.PayOrderService;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.exception.pay.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.service.func.AbsPayCloseStrategy;
import cn.bootx.platform.daxpay.param.pay.PayCloseParam;
import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付关闭和撤销服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseService {
    private final PayOrderService payOrderService;

    /**
     * 关闭支付
     */
    @Transactional(rollbackFor = Exception.class)
    public void close(PayCloseParam param){
        PayOrder payOrder = null;
        if (Objects.nonNull(param.getPaymentId())){
            payOrder = payOrderService.findById(param.getPaymentId())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        if (Objects.isNull(payOrder)){
            payOrder = payOrderService.findByBusinessNo(param.getBusinessNo())
                    .orElseThrow(() -> new PayFailureException("未查询到支付订单"));
        }
        this.close(payOrder);
    }

    /**
     * 取消支付记录
     */
    private void close(PayOrder payOrder) {
        // 状态检查, 只有支付中可以进行取消支付
        if (!Objects.equals(payOrder.getStatus(), PayStatusEnum.PROGRESS.getCode())){
            throw new PayFailureException("订单不是支付中, 无法进行关闭订单");
        }

        // 1.获取支付方式(退款列表中提取)，通过工厂生成对应的策略组
        List<String> channels = payOrder.getRefundableInfos()
                .stream()
                .map(OrderRefundableInfo::getChannel)
                .collect(Collectors.toList());
        List<AbsPayCloseStrategy> payCloseStrategies = PayCloseStrategyFactory.createAsyncLast(channels);
        if (CollectionUtil.isEmpty(payCloseStrategies)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化关闭支付的参数
        payCloseStrategies.forEach(strategy -> strategy.initCloseParam(payOrder));

        try {
            // 3.关闭前准备
            payCloseStrategies.forEach(AbsPayCloseStrategy::doBeforeCloseHandler);

            // 4.执行关闭策略
            payCloseStrategies.forEach(AbsPayCloseStrategy::doCloseHandler);
        }
        catch (Exception e) {
            // TODO 记录关闭失败的记录
            throw e;
        }

        // 5.关闭成功后处理
        this.successHandler(payOrder);
    }

    /**
     * 成功后处理方法
     */
    private void successHandler(PayOrder payOrder){
        // 取消订单
        payOrder.setStatus(PayStatusEnum.CLOSE.getCode());
        payOrderService.updateById(payOrder);
    }
}
