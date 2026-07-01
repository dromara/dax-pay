package cn.daxpay.open.payment.old.pay.service.trade.pay;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.platform.core.exception.system.DataErrorException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.payment.common.context.PayContext;
import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.old.pay.bo.trade.PayResultBo;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderExpandManager;
import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderManager;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum;
import cn.daxpay.open.payment.old.pay.exception.TradeProcessingException;
import cn.daxpay.open.payment.old.pay.service.trade.TradeUniHandleService;
import cn.daxpay.open.payment.masterdata.constants.product.service.PayProductCapabilityService;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.strategy.pay.AbsPayStrategy;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
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
@Service("oldPayService")
@RequiredArgsConstructor
public class PayService {

    private final PayAssistService payAssistService;
    private final LockTemplate lockTemplate;
    private final PayOrderManager payOrderManager;
    private final PayOrderExpandManager payOrderExpandManager;
    private final TradeUniHandleService tradeUniHandleService;

    /// 支付入口
    public NormalPayResult pay(NormalPayParam payParam){
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
    public NormalPayResult payHandle(NormalPayParam payParam, PayOrder payOrder) {
        // 获取支付策略类
        var payStrategy = PaymentStrategyFactory.createByProduct(payParam.getProduct(), AbsPayStrategy.class);
        PayContext context = new PayContext(payParam);
        // 检测支付能力是否支持（产品已挂载能力覆盖该支付方式）
        var methodEnum = PayMethodEnum.findByCode(payParam.getMethod());
        if (!SpringUtil.getBean(PayProductCapabilityService.class).productSupportsMethod(
                payParam.getProduct(), methodEnum.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.unsupportedPayMethod",
                    I18nUtil.getEnumName(methodEnum) + "(" + methodEnum.getCode() + ")");
        }
        // 执行支付前处理动作, 进行各种校验, 校验通过才会进行下面的操作
        payStrategy.doBeforePay(context);
        // 订单不存在执行支付前的保存动作, 保存支付订单默认状态为支付中
        if (Objects.isNull(payOrder)){
            payOrder = payAssistService.createPayOrder(payParam);
        } else {
            // 订单: 支付订单扩展信息不存在
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
        PayTrade trade = new PayTrade();
        trade.setId(payOrder.getId());
        trade.setProduct(payOrder.getProduct());
        trade.setChannel(payOrder.getChannel());
        trade.setMethod(payOrder.getMethod());
        trade.setAmount(payOrder.getAmount());
        context.setTrade(trade);
        PayResultBo result;
        try {
            // 支付操作
            var newResult = payStrategy.doPay(context);
            result = new PayResultBo();
            result.setOutOrderNo(newResult.getOutOrderNo());
            result.setComplete(newResult.isComplete());
            result.setRealAmount(newResult.getRealAmount());
            result.setFinishTime(newResult.getFinishTime());
            result.setPayBody(newResult.getPayBody());
            result.setPayBodyType(newResult.getPayBodyType());
            result.setBuyerId(newResult.getBuyerId());
            result.setUserId(newResult.getUserId());
            result.setTradeProduct(newResult.getTradeProduct());
            result.setTradeWay(newResult.getTradeWay());
            result.setBankType(newResult.getBankType());
            result.setTransOrderNo(newResult.getTransOrderNo());
            result.setRelationOrderNo(newResult.getRelationOrderNo());
            result.setPromotionType(newResult.getPromotionType());
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
    public NormalPayResult paySuccess(PayOrder payOrder, PayResultBo result){
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

