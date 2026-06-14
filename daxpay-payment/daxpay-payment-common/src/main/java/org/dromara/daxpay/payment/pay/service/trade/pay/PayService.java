package org.dromara.daxpay.payment.pay.service.trade.pay;

import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.platform.core.exception.system.DataErrorException;
import org.dromara.daxpay.platform.core.exception.PayFailureException;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.pay.bo.trade.PayResultBo;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderExpandManager;
import org.dromara.daxpay.payment.pay.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayMethodEnum;
import org.dromara.daxpay.platform.core.enums.pay.pay.PayStatusEnum;
import org.dromara.daxpay.payment.pay.exception.TradeProcessingException;
import org.dromara.daxpay.payment.pay.service.trade.TradeUniHandleService;
import org.dromara.daxpay.payment.pay.service.masterdata.product.PayProductCapabilityService;
import org.dromara.daxpay.payment.pay.strategy.AbsPayStrategy;
import org.dromara.daxpay.platform.core.enums.unipay.PayBodyTypeEnum;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/// # 支付服务类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

    private final PayAssistService payAssistService;
    private final LockTemplate lockTemplate;
    private final PayOrderManager payOrderManager;
    private final PayOrderExpandManager payOrderExpandManager;
    private final TradeUniHandleService tradeUniHandleService;

    /// 支付入口
    public PayResult pay(PayParam payParam){
        // 校验超时时间, 不可早于当前
        payAssistService.validationExpiredTime(payParam.getExpiredTime());
        // 获取商户订单号
        String bizOrderNo = payParam.getBizOrderNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + bizOrderNo,10000,200);
        if (Objects.isNull(lock)){
            log.warn("正在支付中，请勿重复支付");
            // 正在支付中，请勿重复支付
            throw new TradeProcessingException();
        }
        try {
            // 查询并检查订单
            var payOrder = payAssistService.getOrderAndCheck(payParam.getBizOrderNo(), payParam.getAppId());
            // 调用支付流程
            return this.payHandle(payParam,payOrder);
        } catch (Exception e) {
            log.error("支付异常",e);
            throw e;
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 支付操作 无事务
    /// 拆分为多阶段，1. 保存订单记录信息 2. 调起支付 3. 支付成功后操作
    public PayResult payHandle(PayParam payParam, PayOrder payOrder) {
        // 获取支付策略类
        var payStrategy = PaymentStrategyFactory.createByProduct(payParam.getProduct(), AbsPayStrategy.class);
        // 初始化支付的参数
        payStrategy.setPayParam(payParam);
        // 检测支付能力是否支持（产品已挂载能力覆盖该支付方式）
        var methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        if (!SpringUtil.getBean(PayProductCapabilityService.class).productSupportsMethod(
                payParam.getProduct(), methodEnum.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.unsupportedPayMethod",
                    I18nUtil.getEnumName(methodEnum) + "(" + methodEnum.getCode() + ")");
        }
        // 其他支付方式检查
        if (methodEnum == PayMethodEnum.OTHER){
            if (StrUtil.isBlank(payParam.getOtherMethod())) {
                // 其他支付方法不能为空
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.otherMethodRequired");
            }
        }
        // 执行支付前处理动作, 进行各种校验, 校验通过才会进行下面的操作
        payStrategy.doBeforePayHandler();
        // 订单不存在执行支付前的保存动作, 保存支付订单默认状态为支付中
        if (Objects.isNull(payOrder)){
            payOrder = payAssistService.createPayOrder(payParam);
        } else {
            // 如果订单存在判断是都一
            var orderExpand = payOrderExpandManager.findById(payOrder.getId()).orElseThrow(() -> new DataErrorException("error.payment.order.payOrderExtNotExist"));
            // 判断是否已经拉起了支付，如果拉起返回保存的支付参数
            if (StrUtil.isNotBlank(orderExpand.getPayBody())){
                return payAssistService.buildResult(payOrder,orderExpand);
            }
        }
        // 订单待支付状态, 设置支付方式和对应状态
        if (Objects.equals(payOrder.getStatus(), PayStatusEnum.WAIT.getCode())){
            payAssistService.updatePayOrder(payParam,payOrder);
        }
        payStrategy.setOrder(payOrder);
        PayResultBo result;
        try {
            // 支付操作
            result = payStrategy.doPayHandler();
        } catch (Exception e) {
            log.error("支付出现异常",e);
            payOrder.setStatus(PayStatusEnum.FAIL.getCode());
            if (e instanceof PayFailureException) {
                payOrder.setErrorMsg(e.getMessage());
            } else {
                payOrder.setErrorMsg("支付出现异常: "+ e.getMessage());
            }
            // 这个方法没有事务, 所以可以正常更新
            payOrderManager.updateById(payOrder);
            throw e;
        }
        // 支付调起成功后操作, 使用事务来保证数据一致性
        return SpringUtil.getBean(this.getClass()).paySuccess(payOrder, result);
    }

    /// 支付调用成功后操作, 更新订单信息
    @Transactional(rollbackFor = Exception.class)
    public PayResult paySuccess(PayOrder payOrder, PayResultBo result){
        // 如果支付完成, 进行订单完成处理, 同时发送回调消息
        if (result.isComplete()) {
            payOrder.setStatus(PayStatusEnum.SUCCESS.getCode())
                    .setPayTime(result.getFinishTime());
        }
        // 更新订单信息
        payOrder.setOutOrderNo(result.getOutOrderNo())
                .setErrorMsg(null)
                .setTransOrderNo(result.getTransOrderNo())
                .setRelationOrderNo(result.getRelationOrderNo());
        // 更新订单扩展信息
        var orderExpand = payOrderExpandManager.findById(payOrder.getId()).orElseThrow(() -> new DataErrorException("error.payment.order.payOrderExtNotExist"));
        orderExpand.setTradeProduct(result.getTradeProduct())
                .setBankType(result.getBankType())
                .setTradeWay(result.getTradeWay())
                .setPromotionType(result.getPromotionType())
                .setRealAmount(Opt.ofNullable(result.getRealAmount()).orElse(payOrder.getAmount()))
                .setBuyerId(result.getBuyerId())
                .setUserId(result.getUserId())
                .setPayBody(result.getPayBody())
                .setPayBodyType(Optional.ofNullable(result.getPayBodyType()).map(PayBodyTypeEnum::getCode).orElse(null));
        // 统一处理
        tradeUniHandleService.payAfterHandel(payOrder,orderExpand);
        return payAssistService.buildResult(payOrder,orderExpand);
    }
}

