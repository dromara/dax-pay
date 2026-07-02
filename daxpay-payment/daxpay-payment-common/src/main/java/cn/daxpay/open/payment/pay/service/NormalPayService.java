package cn.daxpay.open.payment.pay.service;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.payment.common.context.NormalPayContext;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.util.PaymentStrategyFactory;
import cn.daxpay.open.payment.pay.bo.PayTradeResultBo;
import cn.daxpay.open.payment.pay.order.dao.PayTradeManager;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
import cn.daxpay.open.payment.pay.service.route.PayRouteFacade;
import cn.hutool.core.util.StrUtil;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 普通支付服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class NormalPayService {

    private final PayAssistService payAssistService;
    private final PayUniHandleService payUniHandleService;
    private final LockTemplate lockTemplate;
    private final PayTradeManager payTradeManager;
    private final PayRouteFacade payRouteFacade;

    /// 支付入口
    public NormalPayResult pay(NormalPayParam payParam) {
        payAssistService.validationExpiredTime(payParam.getExpiredTime());
        String bizOrderNo = payParam.getBizOrderNo();
        LockInfo lock = lockTemplate.lock("payment:pay:" + bizOrderNo, 10000, 200);
        if (Objects.isNull(lock)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.processing");
        }
        try {
            return this.payHandle(payParam);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 支付操作
    /// 拆分为多阶段: 1.校验与配置组装 2.查询已有订单 3.新建订单 4.发起支付 5.支付成功后处理
    public NormalPayResult payHandle(NormalPayParam payParam) {
        // 解析应用号：空则取商户默认应用
        payAssistService.resolveApp(payParam);
        // 路由解析：直定模式(已传 channelMchNo)直接解析，否则按 appId+method 策略匹配
        payRouteFacade.resolve(payParam);
        var payStrategy = PaymentStrategyFactory.createByProduct(payParam.getProduct(), AbsNormalPayStrategy.class);
        // 支付前处理: 校验与通道配置组装(只依赖请求参数), 失败直接抛出不持久化(订单尚未创建)
        NormalPayContext context = new NormalPayContext().setPayParam(payParam);
        payStrategy.doBeforePay(context);
        // 查询已有订单并校验，结果填充到 context
        payAssistService.findAndCheckOrder(payParam.getBizOrderNo(), context);
        // 已拉起支付则返回缓存的支付参数
        if (Objects.nonNull(context.getTrade()) && StrUtil.isNotBlank(context.getTrade().getPayBody())) {
            return payAssistService.buildResult(context.getTrade());
        }
        // 订单不存在则新建（填充 context）
        if (Objects.isNull(context.getContainer())) {
            payAssistService.createOrder(payParam, context);
        }
        PayTrade trade = context.getTrade();
        // 支付操作, 失败标记 trade 为 FAIL 并持久化
        PayTradeResultBo result;
        try {
            result = payStrategy.doPay(context);
        } catch (Exception e) {
            log.error("支付出现异常", e);
            trade.setStatus(PayFundStatusEnum.FAIL.getCode());
            if (e instanceof PayFailureException) {
                trade.setErrorMsg(e.getMessage());
            } else {
                trade.setErrorMsg("支付出现异常: " + e.getMessage());
            }
            payTradeManager.updateById(trade);
            throw e;
        }
        return SpringUtil.getBean(this.getClass()).paySuccess(trade, result);
    }

    /// 支付成功后操作
    @Transactional(rollbackFor = Exception.class)
    public NormalPayResult paySuccess(PayTrade trade, PayTradeResultBo result) {
        if (result.isComplete()) {
            trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
            trade.setPayTime(result.getFinishTime());
        }
        // trade.status 在 complete=false 时保持 PROCESSING(createOrder 时已设)
        trade.setOutOrderNo(result.getOutOrderNo());
        trade.setTransOrderNo(result.getTransOrderNo());
        trade.setRelationOrderNo(result.getRelationOrderNo());
        trade.setBuyerId(result.getBuyerId());
        trade.setBuyerLogonId(result.getBuyerLogonId());
        trade.setTradeProduct(result.getTradeProduct());
        trade.setTradeWay(result.getTradeWay());
        trade.setBankType(result.getBankType());
        trade.setPromotionType(result.getPromotionType());
        trade.setPayBody(result.getPayBody());
        trade.setPayBodyType(Objects.nonNull(result.getPayBodyType())
                ? result.getPayBodyType().getCode() : null);
        trade.setErrorMsg(null);
        // 参考商业版: 不论是否完成都更新交易单, 仅 SUCCESS 时同步容器
        payUniHandleService.payAfterHandel(trade);
        return payAssistService.buildResult(trade);
    }
}
