package cn.daxpay.open.payment.trade.runtime.service.pay;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.assist.MerchantContextLoader;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.hutool.core.util.StrUtil;
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
    private final PayRouteService payRouteService;
    private final MerchantContextLoader merchantContextLoader;

    /// 支付入口
    public NormalPayResult pay(NormalPayParam payParam) {
        // 客户端IP兜底: 商户未传时从当前HTTP请求提取, 供通道 location_info 等场景使用
        if (StrUtil.isBlank(payParam.getClientIp())) {
            payParam.setClientIp(WebServletUtil.getClientIp());
        }
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
    /// 拆分为多阶段: 1.应用解析与校验 2.通道路由 3.查询已有订单 4.新建订单 5.发起支付 6.支付成功后处理
    public NormalPayResult payHandle(NormalPayParam payParam) {
        // 应用解析: 空则取商户默认应用, 校验启用与归属, 回填到 payParam
        var mchApp = merchantContextLoader.resolveApp(payParam.getMchNo(), payParam.getAppId());
        payParam.setAppId(mchApp.getAppId());
        // 路由解析：直定模式(已传 channelMchNo)直接解析，否则按 appId+method 策略匹配
        payRouteService.resolve(payParam);
        var payStrategy = PaymentStrategyFactory.createByProduct(payParam.getProduct(), AbsNormalPayStrategy.class);
        // 支付前处理: 校验与通道配置组装(只依赖请求参数), 失败直接抛出不持久化(订单尚未创建)
        var context = new PayStrategyContext().setPayParam(payParam);
        payStrategy.doBeforePay(context);
        // 查询已有订单并校验，结果填充到 context
        payAssistService.findAndCheckOrder(payParam.getBizOrderNo(), context);
        // 已拉起支付则返回缓存的支付参数
        if (Objects.nonNull(context.getTrade()) && StrUtil.isNotBlank(context.getTrade().getPayBody())) {
            return payAssistService.buildResult(context.getTrade());
        }
        // 订单不存在则新建（填充 context）
        if (Objects.isNull(context.getTrade())) {
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
            String errMsg = (e instanceof PayFailureException)
                    ? e.getMessage() : "支付出现异常: " + e.getMessage();
            payUniHandleService.payFail(trade, errMsg);
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
        // payBody/payBodyType 留 trade(已拉起缓存标记)
        trade.setPayBody(result.getPayBody());
        trade.setPayBodyType(Objects.nonNull(result.getPayBodyType())
                ? result.getPayBodyType().getCode() : null);
        // 回执字段写容器, 由 payAfterHandel 统一处理
        payUniHandleService.payAfterHandel(trade, result);
        return payAssistService.buildResult(trade);
    }
}
