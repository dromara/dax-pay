package cn.daxpay.open.payment.trade.runtime.service.pay.common;

import cn.daxpay.open.payment.strategy.pay.AbsNormalPayStrategy;
import cn.daxpay.open.payment.strategy.risk.PayRiskCheckContext;
import cn.daxpay.open.payment.strategy.risk.PayRiskChecker;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformPaySecurityConfig;
import cn.daxpay.open.platform.system.service.config.security.PlatformSecurityConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 支付风控辅助
///
/// 统一封装事前/事后黑名单检查入口，供普通支付、网关支付、同步查单、异步回调复用。
/// 插件缺失或总开关关闭时视为放行。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRiskAssistService {

    private final ObjectProvider<PayRiskChecker> payRiskCheckerProvider;
    private final PlatformSecurityConfigService platformSecurityConfigService;
    private final NormalPayOrderManager normalPayOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;

    /// 支付前检查（须在 [AbsNormalPayStrategy#doBeforePay] 之后调用，保证微信 channelAppId 已回填）
    ///
    /// `riskBlockBeforePay=false` 时仍执行检查并落命中，但不抛异常阻断下单。
    public void checkBeforePay(NormalPayParam payParam, String scene) {
        PayRiskChecker checker = payRiskCheckerProvider.getIfAvailable();
        if (checker == null || payParam == null) {
            return;
        }
        var config = platformSecurityConfigService.getPaySecurityConfig();
        if (!Boolean.TRUE.equals(config.getRiskEnabled())) {
            return;
        }
        PayRiskCheckContext ctx = buildContextFromParam(payParam, scene);
        // false=仅记录不拦截；缺省/true=命中拒绝下单
        ctx.setBlockOnHit(!Boolean.FALSE.equals(config.getRiskBlockBeforePay()));
        checker.checkBeforePay(ctx);
    }

    /// 支付成功后补录（同步返回 / 查单 / 回调统一入口）；异常只告警，不阻断资金态
    ///
    /// 仅使用付款用户标识 [buyerId]（微信 openid / 支付宝 user_id），不读取通道内部 userId。
    public void checkAfterPay(PayTrade trade, String buyerId) {
        PayRiskChecker checker = payRiskCheckerProvider.getIfAvailable();
        if (checker == null || trade == null) {
            return;
        }
        PlatformPaySecurityConfig config = platformSecurityConfigService.getPaySecurityConfig();
        if (!Boolean.TRUE.equals(config.getRiskEnabled())
                || !Boolean.TRUE.equals(config.getRiskCheckAfterPay())) {
            return;
        }
        PayRiskCheckContext ctx = buildContextFromTrade(trade);
        if (StrUtil.isNotBlank(buyerId)) {
            ctx.setBuyerId(buyerId);
        }
        // 下单未带 openId 时，用通道回写的 buyerId 补用户标识比对
        if (StrUtil.isBlank(ctx.getOpenId()) && StrUtil.isNotBlank(ctx.getBuyerId())) {
            ctx.setOpenId(ctx.getBuyerId());
        }
        try {
            checker.checkAfterPay(ctx);
        } catch (Exception e) {
            log.warn("支付后风控补录失败 tradeNo={}: {}", trade.getTradeNo(), e.getMessage());
        }
    }

    /// 从支付参数构建风控检查上下文
    private PayRiskCheckContext buildContextFromParam(NormalPayParam payParam, String scene) {
        PayRiskCheckContext ctx = new PayRiskCheckContext()
                .setScene(StrUtil.blankToDefault(scene, resolveSceneFromSource(payParam.getSource())))
                .setMchNo(payParam.getMchNo())
                .setAppId(payParam.getAppId())
                .setClientIp(payParam.getClientIp())
                .setOpenId(payParam.getOpenId())
                .setMethod(payParam.getMethod())
                .setProduct(payParam.getProduct())
                .setChannelAppId(payParam.getChannelAppId())
                .setBizOrderNo(payParam.getBizOrderNo());
        fillChannelByProduct(ctx, payParam.getProduct());
        return ctx;
    }

    /// 从资金凭证构建风控检查上下文(含容器回查)
    private PayRiskCheckContext buildContextFromTrade(PayTrade trade) {
        PayRiskCheckContext ctx = new PayRiskCheckContext()
                .setTradeNo(trade.getTradeNo())
                .setTradeType(trade.getTradeType())
                .setMchNo(trade.getMchNo())
                .setAppId(trade.getAppId());
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                ctx.setScene("gateway")
                        .setClientIp(order.getClientIp())
                        .setOpenId(order.getOpenid())
                        .setBuyerId(order.getBuyerId())
                        .setMethod(order.getMethod())
                        .setProduct(order.getProduct())
                        .setChannel(order.getChannel())
                        .setChannelAppId(order.getChannelAppId())
                        .setOrderNo(order.getOrderNo())
                        .setBizOrderNo(order.getBizOrderNo());
            }
        } else {
            NormalPayOrder order = normalPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                ctx.setScene(resolveSceneFromSource(order.getSource()))
                        .setClientIp(order.getClientIp())
                        .setOpenId(order.getOpenid())
                        .setBuyerId(order.getBuyerId())
                        .setMethod(order.getMethod())
                        .setProduct(order.getProduct())
                        .setChannel(order.getChannel())
                        .setChannelAppId(order.getChannelAppId())
                        .setOrderNo(order.getOrderNo())
                        .setBizOrderNo(order.getBizOrderNo());
            }
        }
        if (StrUtil.isBlank(ctx.getChannel())) {
            fillChannelByProduct(ctx, ctx.getProduct());
        }
        return ctx;
    }

    /// 从产品编码反推通道编码填入风控上下文
    private static void fillChannelByProduct(PayRiskCheckContext ctx, String product) {
        if (StrUtil.isBlank(product)) {
            return;
        }
        try {
            ctx.setChannel(ProductEnum.findByCode(product).getChannel());
        } catch (Exception ignored) {
            // 反推失败忽略, channel 留空
        }
    }

    /// 交易来源 → 风控场景编码: 码牌→code, 其余→api
    private static String resolveSceneFromSource(String source) {
        if (StrUtil.isNotBlank(source) && TradeSourceEnum.CASHIER_CODE.getCode().equals(source)) {
            return "code";
        }
        return "api";
    }
}
