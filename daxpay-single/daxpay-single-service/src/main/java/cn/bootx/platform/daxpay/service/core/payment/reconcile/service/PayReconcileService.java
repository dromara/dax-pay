package cn.bootx.platform.daxpay.service.core.payment.reconcile.service;

import cn.bootx.platform.daxpay.service.core.payment.reconcile.factory.PayReconcileStrategyFactory;
import cn.bootx.platform.daxpay.service.func.AbsReconcileStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 支付对账单下载服务
 * @author xxm
 * @since 2024/1/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayReconcileService {
    /**
     * 下载对账单
     */
    public void downAll(LocalDate date){
        List<AbsReconcileStrategy> strategies = PayReconcileStrategyFactory.create();
        strategies.forEach(AbsReconcileStrategy::doBeforeHandler);
    }

    /**
     * 手动发起对账下载
     */
    public void downByChannel(LocalDate date, String channel){
        AbsReconcileStrategy absReconcileStrategy = PayReconcileStrategyFactory.create(channel);
        absReconcileStrategy.doBeforeHandler();
        absReconcileStrategy.downAndSave(date);
    }

    /**
     * 全部对账
     */
    public void offsetting(LocalDate date){
        List<AbsReconcileStrategy> strategies = PayReconcileStrategyFactory.create();
        strategies.forEach(AbsReconcileStrategy::doBeforeHandler);
    }

    /**
     * 全部对账
     */
    public void offsettingByChannel(LocalDate date, String channel){
        AbsReconcileStrategy absReconcileStrategy = PayReconcileStrategyFactory.create(channel);
        absReconcileStrategy.doBeforeHandler();
        absReconcileStrategy.offsetting(date);
    }
}
