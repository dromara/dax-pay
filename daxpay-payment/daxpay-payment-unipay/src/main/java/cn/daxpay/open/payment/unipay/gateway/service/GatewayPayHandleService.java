package cn.daxpay.open.payment.unipay.gateway.service;

import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.runtime.service.pay.PayUniHandleService;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 网关支付统一内核
///
/// 在 product/method 已解析后: 懒创建 Trade → 调通道策略 → 回写容器。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayHandleService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PayRouteService payRouteService;
    private final MerchantContextLoader merchantContextLoader;
    private final PayUniHandleService payUniHandleService;
    private final GatewayPayAssistService gatewayPayAssistService;
    private final LockExecutor lockExecutor;

    /// 发起网关支付
    ///
    /// @param channelMchNo 通道商户号(DIRECT 模式传入跳过路由, 为空走路由解析)
    /// @param capability   支付能力(DIRECT 模式必填)
    public NormalPayResult handle(GatewayPayOrder order, String product, String method,
                                  String channelMchNo, String capability,
                                  String openId, String clientEnv, String device, String clientIp) {
        return lockExecutor.execute(
                "payment:gateway:pay:" + order.getOrderNo(),
                () -> {
                    // 重新加载最新状态
                    GatewayPayOrder current = gatewayPayOrderManager.findById(order.getId())
                            .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                                    "pay.error.payOrderNotExist"));
                    gatewayPayAssistService.checkPayable(current);
                    merchantContextLoader.initMch(current.getMchNo());

                    // 已有 Trade: 幂等 / 锁定规则
                    PayTrade existing = payTradeManager.findByContainerId(current.getId(), PayTradeTypeEnum.GATEWAY.getCode())
                            .orElse(null);
                    if (existing != null) {
                        if (Objects.equals(existing.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
                            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
                        }
                        if (Objects.equals(existing.getStatus(), PayFundStatusEnum.PROCESSING.getCode())
                                || Objects.equals(existing.getStatus(), PayFundStatusEnum.INIT.getCode())) {
                            // 通道/方式不一致则拒绝换端(product/method 在容器上)
                            if (!Objects.equals(current.getProduct(), product)
                                    || !Objects.equals(current.getMethod(), method)) {
                                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                                        "pay.error.gateway.channelLocked");
                            }
                            // payBody 仅在容器, 已拉起则幂等返回
                            if (StrUtil.isNotBlank(current.getPayBody())) {
                                return this.buildResult(current, existing);
                            }
                        } else {
                            // fail/close 等终态: 首期不允许换方式重试
                            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
                        }
                    }

                    // 组装路由用参数
                    NormalPayParam payParam = this.buildPayParam(current, product, method, channelMchNo, capability, openId, clientIp);
                    payRouteService.resolve(payParam);
                    var payStrategy = PaymentStrategyFactory.createByProduct(payParam.getProduct(), AbsNormalPayStrategy.class);
                    var context = new PayStrategyContext().setPayParam(payParam);
                    payStrategy.doBeforePay(context);

                    // 建 Trade(若无) + 回填容器
                    if (existing == null) {
                        existing = SpringUtil.getBean(this.getClass())
                                .createTrade(current, payParam, clientEnv, device);
                    } else {
                        // 回填路由结果到容器
                        this.fillRouteOnOrder(current, payParam, clientEnv, device);
                        gatewayPayOrderManager.updateById(current);
                    }
                    context.setTrade(existing);

                    PayTradeResultBo result;
                    try {
                        result = payStrategy.doPay(context);
                    } catch (Exception e) {
                        log.error("网关支付出现异常", e);
                        String errMsg = (e instanceof PayFailureException)
                                ? e.getMessage() : "支付出现异常: " + e.getMessage();
                        payUniHandleService.payFail(existing, errMsg);
                        if (e instanceof RuntimeException re) {
                            throw re;
                        }
                        throw new PayFailureException(errMsg);
                    }
                    return SpringUtil.getBean(this.getClass()).paySuccess(current, existing, result);
                },
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.processing")
        );
    }

    /// 创建资金凭证并更新容器为 paying
    @Transactional(rollbackFor = Exception.class)
    public PayTrade createTrade(GatewayPayOrder order, NormalPayParam payParam, String clientEnv, String device) {
        var productEnum = ProductEnum.findByCode(payParam.getProduct());
        String channel = productEnum.getChannel();

        PayTrade trade = new PayTrade();
        trade.setAppId(order.getAppId());
        trade.setTradeNo(TradeNoGenerateUtil.pay());
        trade.setTradeType(PayTradeTypeEnum.GATEWAY.getCode());
        trade.setContainerId(order.getId());
        trade.setAmount(order.getAmount());
        trade.setCurrency(CurrencyEnum.CNY.getCode());
        // 入账金额: 未成功前为 0, 成功后由 PayUniHandleService 按规则回写
        trade.setPostedAmount(0L);
        trade.setRefundableBalance(order.getAmount());
        trade.setStatus(PayFundStatusEnum.PROCESSING.getCode());
        // 默认上送网关业务单号; 特殊通道返回后可覆盖
        trade.setRelationOrderNo(order.getOrderNo());
        trade.setSource(TradeSourceEnum.AGGRESS_PAY.getCode());
        payTradeManager.save(trade);

        this.fillRouteOnOrder(order, payParam, clientEnv, device);
        order.setStatus(GatewayOrderStatusEnum.PAYING.getCode());
        order.setChannel(channel);
        order.setMethod(payParam.getMethod());
        order.setLimitPay(payParam.getLimitPay() != null
                ? String.join(",", payParam.getLimitPay()) : null);
        order.setOpenid(payParam.getOpenId());
        order.setProduct(payParam.getProduct());
        // 与 Normal 对齐: 容器冗余 relationOrderNo, 供 PayTradeContainerFields / 管理展示
        order.setRelationOrderNo(order.getOrderNo());
        gatewayPayOrderManager.updateById(order);
        return trade;
    }

    @Transactional(rollbackFor = Exception.class)
    public NormalPayResult paySuccess(GatewayPayOrder order, PayTrade trade, PayTradeResultBo result) {
        if (result.isComplete()) {
            trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
            trade.setPayTime(result.getFinishTime());
        }
        trade.setOutOrderNo(result.getOutOrderNo());
        // 回执与 payBody 写容器, 由 payAfterHandel 统一处理(内部 reload 容器)
        payUniHandleService.payAfterHandel(trade, result);
        // 必须 reload: payAfterHandel 写的是另一份实体, 入参 order 无 payBody
        GatewayPayOrder latest = gatewayPayOrderManager.findById(order.getId()).orElse(order);
        return this.buildResult(latest, trade);
    }

    private void fillRouteOnOrder(GatewayPayOrder order, NormalPayParam payParam, String clientEnv, String device) {
        order.setChannelMchNo(payParam.getChannelMchNo());
        order.setCapability(payParam.getCapability());
        // doBeforePay 后 payParam.channelAppId 为实际解析值
        order.setChannelAppId(payParam.getChannelAppId());
        if (StrUtil.isNotBlank(clientEnv)) {
            order.setClientEnv(clientEnv);
        }
        if (StrUtil.isNotBlank(device)) {
            order.setDevice(device);
        }
        if (StrUtil.isNotBlank(payParam.getClientIp())) {
            order.setClientIp(payParam.getClientIp());
        }
    }

    private NormalPayParam buildPayParam(GatewayPayOrder order, String product, String method,
                                         String channelMchNo, String capability,
                                         String openId, String clientIp) {
        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(order.getMchNo());
        payParam.setAppId(order.getAppId());
        payParam.setBizOrderNo(order.getBizOrderNo());
        payParam.setTitle(order.getTitle());
        payParam.setDescription(order.getDescription());
        payParam.setAmount(order.getAmount());
        payParam.setProduct(product);
        payParam.setMethod(method);
        payParam.setChannelMchNo(channelMchNo);
        payParam.setCapability(capability);
        // 已支付过则带上订单快照的通道应用
        payParam.setChannelAppId(order.getChannelAppId());
        payParam.setOpenId(openId);
        payParam.setNotifyUrl(order.getNotifyUrl());
        payParam.setReturnUrl(order.getReturnUrl());
        payParam.setAttach(order.getAttach());
        // 预下单写入的通道扩展参数, 与 Normal 容器透传一致
        payParam.setExtraParam(order.getExtraParam());
        payParam.setExpiredTime(order.getExpiredTime());
        payParam.setClientIp(StrUtil.blankToDefault(clientIp, order.getClientIp()));
        payParam.setGoodsDetail(order.getGoodsDetail());
        return payParam;
    }

    private NormalPayResult buildResult(GatewayPayOrder order, PayTrade trade) {
        return new NormalPayResult()
                .setOrderId(order.getId())
                .setBizOrderNo(order.getBizOrderNo())
                // 网关业务单号 vs 资金交易号分离
                .setOrderNo(order.getOrderNo())
                .setTradeNo(trade.getTradeNo())
                .setStatus(trade.getStatus())
                .setPayBody(order.getPayBody())
                .setPayBodyType(order.getPayBodyType());
    }
}
