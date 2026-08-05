package cn.daxpay.open.payment.trade.runtime.service.pay.normal;

import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.common.util.PayUtil;
import cn.daxpay.open.payment.merchant.service.store.MchStoreInfoService;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.mq.NormalPayTimeoutMessage;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.payment.trade.util.PayTradeInitUtil;
import cn.daxpay.open.payment.trade.util.PayTradeProviderUtil;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

/// # 常规支付支持服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class NormalPayAssistService {

    private final NormalPayOrderManager payNormalOrderManager;
    private final PayTradeManager payTradeManager;
    private final ArtemisTemplateService artemisTemplateService;
    private final MchStoreInfoService mchStoreInfoService;
    private final PaymentContext paymentContext;

    /// 创建支付订单（容器 + 资金交易），填充到 context
    /// 调用方需保证 appId 和 product 已解析完毕
    /// orderNo 与 tradeNo 独立生成; 普通通道实际上送串 relationOrderNo 默认=orderNo
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(NormalPayParam payParam, PayStrategyContext context) {
        OffsetDateTime expiredTime = this.getExpiredTime(payParam.getExpiredTime());
        // 门店号: 显式优先, 空则回落商户默认门店; 有值则校验存在/归属/启用
        String storeNo = payParam.getTerminal() != null ? payParam.getTerminal().getStoreNo() : null;
        storeNo = mchStoreInfoService.resolveStoreNo(paymentContext.getMchNo(), storeNo);
        mchStoreInfoService.validateStoreForPay(storeNo, paymentContext.getMchNo());
        // 双号独立生成
        String orderNo = TradeNoGenerateUtil.order();
        String tradeNo = TradeNoGenerateUtil.pay();
        String source = resolveSource(payParam);

        NormalPayOrder normalOrder = buildNormalOrder(payParam, orderNo, expiredTime, source, storeNo);
        payNormalOrderManager.save(normalOrder);

        PayTrade trade = buildPayTrade(payParam, normalOrder, tradeNo, orderNo, source);
        payTradeManager.save(trade);

        context.setTrade(trade);
        context.setNormalOrder(normalOrder);
        // 注册超时关单延时消息(按订单过期时间定时投递)
        this.registerTimeoutClose(trade.getTradeNo(), normalOrder.getBizOrderNo(), expiredTime);
    }

    /// 来源: 容器权威; 协议层(易支付/码牌)可显式传入, 默认商户 API
    private String resolveSource(NormalPayParam payParam) {
        return StrUtil.isNotBlank(payParam.getSource())
                ? payParam.getSource()
                : TradeSourceEnum.MCH_API.getCode();
    }

    /// 组装普通支付业务容器（未落库）
    private NormalPayOrder buildNormalOrder(NormalPayParam payParam, String orderNo,
                                            OffsetDateTime expiredTime, String source, String storeNo) {
        // 从产品编码派生通道编码
        var productEnum = ProductEnum.findByCode(payParam.getProduct());
        String channel = productEnum.getChannel();
        // 终端信息
        String terminalNo = payParam.getTerminal() != null ? payParam.getTerminal().getTerminalNo() : null;

        NormalPayOrder normalOrder = new NormalPayOrder();
        // --- 业务身份 ---
        normalOrder.setOrderNo(orderNo);
        // 普通通道默认上送 orderNo, 特殊通道下单后再覆盖 relation
        normalOrder.setRelationOrderNo(orderNo);
        normalOrder.setBizOrderNo(payParam.getBizOrderNo());
        normalOrder.setTitle(payParam.getTitle());
        normalOrder.setDescription(payParam.getDescription());
        normalOrder.setSource(source);
        // --- 状态与金额 ---
        normalOrder.setStatus(NormalPayOrderStatusEnum.WAIT_PAY.getCode());
        normalOrder.setExpiredTime(expiredTime);
        normalOrder.setAmount(payParam.getAmount());
        // 币种: 显式透传优先, 缺省 cny(向后兼容国内通道)
        normalOrder.setCurrency(StrUtil.isNotBlank(payParam.getCurrency())
                ? CurrencyEnum.findByCode(payParam.getCurrency()).getCode()
                : CurrencyEnum.CNY.getCode());
        // --- 支付路由 ---
        normalOrder.setChannel(channel);
        normalOrder.setMethod(payParam.getMethod());
        // 支付渠道: 由 method 派生, 渠道分布报表/详情展示用
        normalOrder.setProvider(PayTradeProviderUtil.resolveProviderByMethod(payParam.getMethod()));
        normalOrder.setLimitPay(payParam.getLimitPay() != null
                ? String.join(",", payParam.getLimitPay()) : null);
        normalOrder.setOpenid(payParam.getOpenId());
        normalOrder.setAuthCode(payParam.getAuthCode());
        normalOrder.setProduct(payParam.getProduct());
        // 通道路由参数(同步时用于解析通道应用凭证)
        normalOrder.setChannelMchNo(payParam.getChannelMchNo());
        normalOrder.setCapability(payParam.getCapability());
        // 通道应用 AppId: doBeforePay 已将解析后的实际值回填到 payParam
        normalOrder.setChannelAppId(payParam.getChannelAppId());
        // --- 终端 / 门店 / 客户端 ---
        normalOrder.setTerminalNo(terminalNo);
        // 门店号: 线下经营归属权威落容器
        normalOrder.setStoreNo(storeNo);
        // 客户端IP(支付入口已兜底, 作为单一事实源供退款/关单等后续流程取用)
        normalOrder.setClientIp(payParam.getClientIp());
        // --- 回调与扩展 ---
        normalOrder.setNotifyUrl(payParam.getNotifyUrl());
        normalOrder.setReturnUrl(payParam.getReturnUrl());
        normalOrder.setAttach(payParam.getAttach());
        normalOrder.setExtraParam(payParam.getExtraParam());
        normalOrder.setGoodsDetail(payParam.getGoodsDetail());
        normalOrder.setAppId(payParam.getAppId());
        return normalOrder;
    }

    /// 组装资金交易（未落库）; relationOrderNo 默认=orderNo, 特殊通道支付返回后可覆盖
    /// channel / channelMchNo / storeNo / provider 冗余自容器，资金列表与通道/渠道报表免 JOIN
    private PayTrade buildPayTrade(NormalPayParam payParam, NormalPayOrder normalOrder,
                                   String tradeNo, String orderNo, String source) {
        return PayTradeInitUtil.initProcessing(
                payParam.getAppId(),
                tradeNo,
                PayTradeTypeEnum.NORMAL.getCode(),
                normalOrder.getId(),
                payParam.getAmount(),
                normalOrder.getCurrency(),
                orderNo,
                source,
                normalOrder.getChannelMchNo(),
                normalOrder.getStoreNo(),
                normalOrder.getTitle(),
                normalOrder.getProvider(),
                normalOrder.getChannel());
    }

    /// 注册超时关单延时消息
    /// 按订单过期时间定时投递到 [PayArtemisConstants#NORMAL_TIMEOUT_QUEUE]。
    /// 发送失败不阻断下单流程, 由兜底定时任务 [NormalPayTimeoutJob] 补救。
    private void registerTimeoutClose(String tradeNo, String bizOrderNo, OffsetDateTime expiredTime) {
        NormalPayTimeoutMessage message = new NormalPayTimeoutMessage()
                .setTradeNo(tradeNo)
                .setBizOrderNo(bizOrderNo);
        String json = JacksonUtil.toJson(message);
        try {
            artemisTemplateService.sendDelayAt(PayArtemisConstants.NORMAL_TIMEOUT_QUEUE, json, expiredTime);
        } catch (Exception e) {
            // broker 不可用等情况, 不阻断下单, 由定时任务兜底
            log.warn("注册超时关单延时消息失败, 由定时任务兜底, tradeNo={}", tradeNo, e);
        }
    }

    /// 记录通道错误信息到容器(不改状态/不通知), 用于通道结果未知等需后续同步纠正的场景
    ///
    /// 与 [NormalPayService] 的 FAIL 路径不同: 本方法保持资金态 PROCESSING, 仅把错误摘要写入容器
    /// 供商户查询/排查, 订单由定时同步任务查通道真实状态后纠正。
    public void recordPayError(PayStrategyContext context, String errMsg) {
        if (context.getNormalOrder() == null) {
            return;
        }
        NormalPayOrder order = context.getNormalOrder();
        order.setErrorMsg(StrUtil.maxLength(errMsg, 500));
        payNormalOrderManager.updateById(order);
    }

    /// 查询已有订单并校验，结果填充到 context
    /// 订单不存在则 context 保持不变（调用方据此判断是否新建）
    public void findAndCheckOrder(String bizOrderNo, PayStrategyContext context) {
        Optional<NormalPayOrder> normalOrderOpt = payNormalOrderManager.findByBizOrderNo(bizOrderNo);
        if (normalOrderOpt.isEmpty()) {
            return;
        }
        NormalPayOrder normalOrder = normalOrderOpt.get();
        PayTrade trade = payTradeManager.findByContainerId(normalOrder.getId(), PayTradeTypeEnum.NORMAL.getCode()).orElse(null);
        if (trade == null) {
            return;
        }
        this.checkOrder(normalOrder, trade);
        context.setTrade(trade);
        context.setNormalOrder(normalOrder);
    }

    /// 检查订单状态
    public void checkOrder(NormalPayOrder normalOrder, PayTrade trade) {
        // 容器状态检查
        String bizStatus = normalOrder.getStatus();
        if (Objects.equals(bizStatus, NormalPayOrderStatusEnum.PAID.getCode())) {
            // 支付: 已经支付成功, 请勿重新支付
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        if (Objects.equals(bizStatus, NormalPayOrderStatusEnum.CLOSED.getCode())
                || Objects.equals(bizStatus, NormalPayOrderStatusEnum.EXPIRED.getCode())
                || Objects.equals(bizStatus, NormalPayOrderStatusEnum.FAILED.getCode())) {
            // 支付: 该订单支付失败或已经被关闭
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 资金状态检查
        String fundStatus = trade.getStatus();
        if (Objects.equals(fundStatus, PayFundStatusEnum.SUCCESS.getCode())) {
            // 支付: 已经支付成功, 请勿重新支付
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        if (Objects.equals(fundStatus, PayFundStatusEnum.CLOSE.getCode())
                || Objects.equals(fundStatus, PayFundStatusEnum.FAIL.getCode())
                || Objects.equals(fundStatus, PayFundStatusEnum.CANCEL.getCode())) {
            // 支付: 该订单支付失败或已经被关闭
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 超时检查
        if (Objects.nonNull(normalOrder.getExpiredTime())
                && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), normalOrder.getExpiredTime())) {
            // 支付: 支付已超时, 请重新确认支付状态
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.timeoutRetry");
        }
    }

    /// 根据 PayTrade + 容器构建支付结果（orderNo/payBody 取自容器）
    public NormalPayResult buildResult(PayTrade trade) {
        NormalPayOrder order = null;
        if (trade.getContainerId() != null) {
            order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        }
        return buildResult(trade, order);
    }

    /// 根据资金凭证与业务容器构建支付结果
    public NormalPayResult buildResult(PayTrade trade, NormalPayOrder order) {
        NormalPayResult result = new NormalPayResult();
        result.setOrderId(order != null ? order.getId() : trade.getContainerId());
        result.setBizOrderNo(order != null ? order.getBizOrderNo() : null);
        // 业务单号与资金交易号分离暴露
        result.setOrderNo(order != null ? order.getOrderNo() : null);
        result.setTradeNo(trade.getTradeNo());
        result.setStatus(trade.getStatus());
        if (order != null) {
            result.setPayBody(order.getPayBody());
            result.setPayBodyType(order.getPayBodyType());
        }
        return result;
    }

    /// 获取支付超时时间
    public OffsetDateTime getExpiredTime(OffsetDateTime expiredTime) {
        if (Objects.nonNull(expiredTime)) {
            return expiredTime;
        }
        return PayUtil.getPaymentExpiredTime(30);
    }

    /// 校验超时时间
    public void validationExpiredTime(OffsetDateTime expiredTime) {
        if (Objects.nonNull(expiredTime)
                && DateTimeUtil.lt(expiredTime, OffsetDateTime.now(ZoneOffset.UTC))) {
            // 支付: 支付超时时间设置有误, 请检查
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.expiredTimeError");
        }
    }
}
