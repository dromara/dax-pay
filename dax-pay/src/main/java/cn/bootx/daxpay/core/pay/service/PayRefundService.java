package cn.bootx.daxpay.core.pay.service;

import cn.bootx.common.core.util.BigDecimalUtil;
import cn.bootx.common.spring.util.WebServletUtil;
import cn.bootx.daxpay.code.pay.PayStatusCode;
import cn.bootx.daxpay.core.pay.builder.PaymentBuilder;
import cn.bootx.daxpay.core.pay.factory.PayStrategyFactory;
import cn.bootx.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.daxpay.core.pay.func.PayStrategyConsumer;
import cn.bootx.daxpay.core.pay.local.AsyncRefundLocal;
import cn.bootx.daxpay.core.payment.dao.PaymentManager;
import cn.bootx.daxpay.core.payment.entity.Payment;
import cn.bootx.daxpay.core.payment.service.PaymentService;
import cn.bootx.daxpay.core.refund.dao.RefundRecordManager;
import cn.bootx.daxpay.core.refund.entity.RefundRecord;
import cn.bootx.daxpay.dto.payment.RefundableInfo;
import cn.bootx.daxpay.exception.payment.PayAmountAbnormalException;
import cn.bootx.daxpay.exception.payment.PayFailureException;
import cn.bootx.daxpay.exception.payment.PayUnsupportedMethodException;
import cn.bootx.daxpay.param.pay.PayModeParam;
import cn.bootx.daxpay.param.pay.PayParam;
import cn.bootx.daxpay.param.refund.RefundModeParam;
import cn.bootx.daxpay.param.refund.RefundParam;
import cn.bootx.starter.auth.util.SecurityUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.bootx.daxpay.code.pay.PayStatusCode.*;

/**
 * 支付退款
 *
 * @author xxm
 * @date 2022/2/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundService {

    private final PaymentService paymentService;

    private final PaymentManager paymentManager;

    private final RefundRecordManager refundRecordManager;

    /**
     * 退款
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund(RefundParam refundParam) {
        Payment payment = paymentService.findByBusinessId(refundParam.getBusinessId())
            .orElseThrow(() -> new PayFailureException("未找到支付单"));

        this.refundPayment(payment, refundParam.getRefundModeParams());
    }

    /**
     * 根据业务id取消支付记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void refundByBusinessId(String businessId) {
        Payment payment = paymentService.findByBusinessId(businessId)
            .orElseThrow(() -> new PayFailureException("未找到支付单"));
        List<RefundModeParam> refundModeParams = payment.getRefundableInfo()
            .stream()
            .map(o -> new RefundModeParam().setPayChannel(o.getPayChannel()).setAmount(o.getAmount()))
            .collect(Collectors.toList());
        this.refundPayment(payment, refundModeParams);

    }

    /**
     * 退款
     */
    private void refundPayment(Payment payment, List<RefundModeParam> refundModeParams) {
        // 状态判断, 支付中/失败/撤销不处理
        List<Integer> trades = Arrays.asList(TRADE_PROGRESS, TRADE_CANCEL, TRADE_FAIL);
        if (trades.contains(payment.getPayStatus())) {
            throw new PayFailureException("状态非法, 无法退款");
        }

        // 过滤退款金额为0的支付通道参数
        refundModeParams
            .removeIf(refundModeParam -> BigDecimalUtil.compareTo(refundModeParam.getAmount(), BigDecimal.ZERO) == 0);
        // 获取 paymentParam
        PayParam payParam = PaymentBuilder.buildPayParamByPayment(payment);
        // 退款参数检查
        this.payModeCheck(refundModeParams, payment.getRefundableInfo());

        // 1.获取退款参数方式，通过工厂生成对应的策略组
        List<PayModeParam> payModeParams = refundModeParams.stream()
            .map(RefundModeParam::toPayModeParam)
            .collect(Collectors.toList());
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payModeParams);
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }

        // 3.执行退款
        this.doHandler(payment, paymentStrategyList, (strategyList, paymentObj) -> {
            // 发起支付成功进行的执行方法
            try {
                strategyList.forEach(AbsPayStrategy::doRefundHandler);
            }
            catch (Exception e) {
                // 记录退款失败的记录
                BigDecimal amount = refundModeParams.stream()
                    .map(RefundModeParam::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                SpringUtil.getBean(this.getClass()).saveRefund(payment, amount, refundModeParams);
                throw e;
            }
            // 处理支付单
            this.paymentHandler(paymentObj, refundModeParams);
        });
    }

    /**
     * 支付单处理
     */
    private void paymentHandler(Payment payment, List<RefundModeParam> refundModeParams) {
        BigDecimal amount = refundModeParams.stream()
            .map(RefundModeParam::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 剩余可退款余额
        BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);

        // 退款完成
        if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO) < 1) {
            payment.setPayStatus(PayStatusCode.TRADE_REFUNDED);
        }
        else {
            payment.setPayStatus(PayStatusCode.TRADE_REFUNDING);
        }

        payment.setRefundableBalance(refundableBalance);
        paymentManager.updateById(payment);
        // 记录退款成功的记录
        SpringUtil.getBean(this.getClass()).saveRefund(payment, amount, refundModeParams);
    }

    /**
     * 处理方法
     * @param payment 支付记录
     * @param strategyList 支付策略
     * @param successCallback 成功操作
     */
    private void doHandler(Payment payment, List<AbsPayStrategy> strategyList,
                           PayStrategyConsumer<List<AbsPayStrategy>, Payment> successCallback) {

        try {
            // 执行
            successCallback.accept(strategyList, payment);
        }
        catch (Exception e) {
            log.warn("退款失败");
            throw e;
        }
        finally {
            // 清除
            AsyncRefundLocal.clear();
        }
    }

    /**
     * 支付方式检查
     * @param refundModeParams 退款参数
     * @param refundableInfos 可退款信息
     */
    private void payModeCheck(List<RefundModeParam> refundModeParams, List<RefundableInfo> refundableInfos) {
        if (CollUtil.isEmpty(refundModeParams)) {
            throw new PayFailureException("传入的退款参数不合法");
        }
        Map<Integer, RefundableInfo> payModeMap = refundableInfos.stream()
            .collect(Collectors.toMap(RefundableInfo::getPayChannel, Function.identity()));
        for (RefundModeParam refundPayMode : refundModeParams) {
            this.payModeCheck(refundPayMode, payModeMap.get(refundPayMode.getPayChannel()));
        }
    }

    /**
     * 支付方式检查
     * @param refundModeParam 退款参数
     * @param refundableInfo 可退款对象
     */
    public void payModeCheck(RefundModeParam refundModeParam, RefundableInfo refundableInfo) {
        if (Objects.isNull(refundableInfo)) {
            throw new PayFailureException("退款参数非法");
        }
        // 退款金额为负数的
        if (BigDecimalUtil.compareTo(refundModeParam.getAmount(), BigDecimal.ZERO) < 1) {
            throw new PayAmountAbnormalException();
        }
        // 退款金额大于可退款金额
        if (BigDecimalUtil.compareTo(refundModeParam.getAmount(), refundableInfo.getAmount()) == 1) {
            throw new PayAmountAbnormalException("退款金额大于可退款金额");
        }

    }

    /**
     * 保存退款记录 成不成功都记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRefund(Payment payment, BigDecimal amount, List<RefundModeParam> refundModeParams) {
        List<RefundableInfo> refundableInfos = refundModeParams.stream()
            .map(RefundModeParam::toRefundableInfo)
            .collect(Collectors.toList());
        HttpServletRequest request = WebServletUtil.getRequest();
        String ip = ServletUtil.getClientIP(request);
        RefundRecord refundRecord = new RefundRecord().setRefundRequestNo(AsyncRefundLocal.get())
            .setRefundableInfo(refundableInfos)
            .setAmount(amount)
            .setRefundableBalance(payment.getRefundableBalance())
            .setClientIp(ip)
            .setPaymentId(payment.getId())
            .setBusinessId(payment.getBusinessId())
            .setUserId(SecurityUtil.getUserIdOrDefaultId())
            .setRefundTime(LocalDateTime.now())
            .setTitle(payment.getTitle())
            .setErrorMsg(AsyncRefundLocal.getErrorMsg())
            .setErrorCode(AsyncRefundLocal.getErrorCode())
            .setRefundStatus(Objects.isNull(AsyncRefundLocal.getErrorCode()) ? PayStatusCode.REFUND_PROCESS_SUCCESS
                    : PayStatusCode.REFUND_PROCESS_FAIL);
        refundRecordManager.save(refundRecord);
    }

}
