package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.runtime.service.pay.common.PayUniHandleService;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPaySecurityConfig;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import cn.daxpay.open.payment.trade.util.PayTradeInitUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 网关支付统一内核
///
/// 在 product/method 已解析后: 懒创建 Trade → 调通道策略 → 回写容器。
@Slf4j
@Service
public class GatewayPayHandleService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayTradeManager payTradeManager;
    private final PayRouteService payRouteService;
    private final MerchantContextLoader merchantContextLoader;
    private final PayUniHandleService payUniHandleService;
    private final GatewayPayAssistService gatewayPayAssistService;
    private final LockExecutor lockExecutor;
    /// 风控检查器（可选 SPI：插件 daxpay-plugin-risk 在 classpath 时生效，无 Bean 视为放行）
    private final ObjectProvider<PayRiskChecker> payRiskCheckerProvider;
    private final PlatformSecurityConfigService platformSecurityConfigService;

    /// 自注入，保证 [GatewayPayHandleService#createTrade] / [GatewayPayHandleService#paySuccess] 走 Spring 事务代理
    private final GatewayPayHandleService self;

    public GatewayPayHandleService(GatewayPayOrderManager gatewayPayOrderManager,
                                   PayTradeManager payTradeManager,
                                   PayRouteService payRouteService,
                                   MerchantContextLoader merchantContextLoader,
                                   PayUniHandleService payUniHandleService,
                                   GatewayPayAssistService gatewayPayAssistService,
                                   LockExecutor lockExecutor,
                                   ObjectProvider<PayRiskChecker> payRiskCheckerProvider,
                                   PlatformSecurityConfigService platformSecurityConfigService,
                                   @Lazy GatewayPayHandleService self) {
        this.gatewayPayOrderManager = gatewayPayOrderManager;
        this.payTradeManager = payTradeManager;
        this.payRouteService = payRouteService;
        this.merchantContextLoader = merchantContextLoader;
        this.payUniHandleService = payUniHandleService;
        this.gatewayPayAssistService = gatewayPayAssistService;
        this.lockExecutor = lockExecutor;
        this.payRiskCheckerProvider = payRiskCheckerProvider;
        this.platformSecurityConfigService = platformSecurityConfigService;
        this.self = self;
    }

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
                            // 聚合支付 product 传 null(由路由解析填充) 是合法设计, 用容器已持久化值补位避免误判切换支付方式
                            String effectiveProduct = StrUtil.blankToDefault(product, current.getProduct());
                            // 收银台 DIRECT 模式 item.method 可能为空(由路由反推), 容器上的 method 是首次支付时路由反推后的值;
                            // 这里先反推对齐, 避免与容器已持久化值口径不一致而误判切换支付方式
                            String effectiveMethod = method;
                            if (StrUtil.isBlank(effectiveMethod) && StrUtil.isNotBlank(channelMchNo)) {
                                effectiveMethod = payRouteService.inferMethodForCapability(channelMchNo, capability);
                            }
                            if (!Objects.equals(current.getProduct(), effectiveProduct)
                                    || !Objects.equals(current.getMethod(), effectiveMethod)) {
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
                    // 风控前置检查: 命中黑名单(IP/openId)抛异常拒绝下单; openId 缺失时降级为仅 IP 校验 + 事后补录
                    this.runRiskBeforePay(current, payParam);
                    var payStrategy = PaymentStrategyFactory.createByProduct(payParam.getProduct(), AbsNormalPayStrategy.class);
                    var context = new PayStrategyContext().setPayParam(payParam);
                    payStrategy.doBeforePay(context);

                    // 建 Trade(若无) + 回填容器
                    if (existing == null) {
                        existing = self.createTrade(current, payParam, clientEnv, device);
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
                    NormalPayResult payResult = self.paySuccess(current, existing, result);
                    // 风控事后补录: 用通道回写 buyerId 补充命中检查, 仅记录不阻断资金态
                    this.runRiskAfterPay(current, payParam, existing, result);
                    return payResult;
                },
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.processing")
        );
    }

    /// 创建资金凭证并更新容器为 paying
    @Transactional(rollbackFor = Exception.class)
    public PayTrade createTrade(GatewayPayOrder order, NormalPayParam payParam, String clientEnv, String device) {
        var productEnum = ProductEnum.findByCode(payParam.getProduct());
        String channel = productEnum.getChannel();

        // 来源: 冗余自容器(预下单权威); 历史单无 source 时按 gatewayType 派生
        String source = StrUtil.isNotBlank(order.getSource())
                ? order.getSource()
                : GatewayPayAssistService.resolveSourceByGatewayTypeCode(order.getGatewayType());
        // 历史预下单可能未写容器 source, 建 trade 时回填
        if (StrUtil.isBlank(order.getSource())) {
            order.setSource(source);
        }

        // 默认上送网关业务单号; 特殊通道返回后可覆盖
        // channelMchNo 取自路由后的 payParam; storeNo 冗余自预下单容器
        PayTrade trade = PayTradeInitUtil.initProcessing(
                order.getAppId(),
                TradeNoGenerateUtil.pay(),
                PayTradeTypeEnum.GATEWAY.getCode(),
                order.getId(),
                order.getAmount(),
                order.getOrderNo(),
                source,
                payParam.getChannelMchNo(),
                order.getStoreNo(),
                order.getTitle());
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

    /// 支付前风控检查：命中黑名单抛异常拒绝下单；插件缺失或开关关闭视为放行
    private void runRiskBeforePay(GatewayPayOrder order, NormalPayParam payParam) {
        PayRiskChecker checker = payRiskCheckerProvider.getIfAvailable();
        if (checker == null) {
            return;
        }
        PlatformPaySecurityConfig config = platformSecurityConfigService.getPaySecurityConfig();
        if (!Boolean.TRUE.equals(config.getRiskEnabled())
                || !Boolean.TRUE.equals(config.getRiskBlockBeforePay())) {
            return;
        }
        PayRiskCheckContext ctx = this.buildRiskContext(order, payParam);
        checker.checkBeforePay(ctx);
    }

    /// 支付成功后风控补录：用通道回写 buyerId 补充命中检查，仅记录不阻断资金态
    private void runRiskAfterPay(GatewayPayOrder order, NormalPayParam payParam, PayTrade trade, PayTradeResultBo result) {
        PayRiskChecker checker = payRiskCheckerProvider.getIfAvailable();
        if (checker == null) {
            return;
        }
        PlatformPaySecurityConfig config = platformSecurityConfigService.getPaySecurityConfig();
        if (!Boolean.TRUE.equals(config.getRiskEnabled())
                || !Boolean.TRUE.equals(config.getRiskCheckAfterPay())) {
            return;
        }
        PayRiskCheckContext ctx = this.buildRiskContext(order, payParam);
        ctx.setTradeNo(trade.getTradeNo());
        ctx.setTradeType(PayTradeTypeEnum.GATEWAY.getCode());
        if (result != null) {
            ctx.setBuyerId(result.getBuyerId());
            if (StrUtil.isBlank(ctx.getOpenId())) {
                ctx.setOpenId(result.getUserId());
            }
        }
        try {
            checker.checkAfterPay(ctx);
        } catch (Exception e) {
            log.warn("网关支付后风控补录失败 tradeNo={}: {}", trade.getTradeNo(), e.getMessage());
        }
    }

    /// 构建风控上下文（路由解析后调用, 此时 product/method/channelAppId 已回填）
    private PayRiskCheckContext buildRiskContext(GatewayPayOrder order, NormalPayParam payParam) {
        PayRiskCheckContext ctx = new PayRiskCheckContext()
                // 网关统一标 gateway, 命中记录可按场景筛选运营处置
                .setScene("gateway")
                .setMchNo(payParam.getMchNo())
                .setAppId(payParam.getAppId())
                .setClientIp(payParam.getClientIp())
                .setOpenId(payParam.getOpenId())
                .setMethod(payParam.getMethod())
                .setProduct(payParam.getProduct())
                .setChannelAppId(payParam.getChannelAppId())
                .setOrderNo(order.getOrderNo())
                .setBizOrderNo(payParam.getBizOrderNo());
        // 由支付产品反推通道族, 供 openId 名单精确匹配
        if (StrUtil.isNotBlank(payParam.getProduct())) {
            try {
                ctx.setChannel(ProductEnum.findByCode(payParam.getProduct()).getChannel());
            } catch (Exception e) {
                // 反推失败忽略, channel 留空走宽匹配
            }
        }
        return ctx;
    }

}
