package cn.daxpay.open.payment.core.trade.service;

import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.payment.common.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.common.util.PayUtil;
import cn.hutool.core.util.StrUtil;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.trade.convert.PayTradeConvert;
import cn.daxpay.open.payment.core.trade.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.mq.NormalPayTimeoutMessage;
import cn.daxpay.open.payment.core.trade.mq.PayArtemisConstants;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

/// # 支付支持服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayAssistService {

    private final NormalPayOrderManager payNormalOrderManager;
    private final PayTradeManager payTradeManager;
    private final ArtemisTemplateService artemisTemplateService;

    /// 创建支付订单（容器 + 资金交易），填充到 context
    /// 调用方需保证 appId 和 product 已解析完毕
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(NormalPayParam payParam, PayStrategyContext context) {
        String appId = payParam.getAppId();
        OffsetDateTime expiredTime = this.getExpiredTime(payParam.getExpiredTime());
        Long amount = payParam.getAmount();
        // 从产品编码派生通道编码
        var productEnum = ProductEnum.findByCode(payParam.getProduct());
        var channel = productEnum.getChannel();
        // 终端信息
        String terminalNo = payParam.getTerminal() != null ? payParam.getTerminal().getTerminalNo() : null;
        // 创建容器 NormalPayOrder（含冗余字段，方便查询）
        NormalPayOrder normalOrder = new NormalPayOrder();
        normalOrder.setBizOrderNo(payParam.getBizOrderNo());
        normalOrder.setTitle(payParam.getTitle());
        normalOrder.setDescription(payParam.getDescription());
        normalOrder.setStatus(NormalPayOrderStatusEnum.WAIT_PAY.getCode());
        normalOrder.setNotifyUrl(payParam.getNotifyUrl());
        normalOrder.setReturnUrl(payParam.getReturnUrl());
        normalOrder.setAttach(payParam.getAttach());
        normalOrder.setExpiredTime(expiredTime);
        normalOrder.setAmount(amount);
        normalOrder.setCurrency(CurrencyEnum.CNY.getCode());
        normalOrder.setChannel(channel);
        normalOrder.setMethod(payParam.getMethod());
        normalOrder.setLimitPay(payParam.getLimitPay() != null
                ? String.join(",", payParam.getLimitPay()) : null);
        normalOrder.setOpenid(payParam.getOpenId());
        normalOrder.setBarCode(payParam.getAuthCode());
        normalOrder.setProduct(payParam.getProduct());
        normalOrder.setExtraParam(payParam.getExtraParam());
        normalOrder.setGoodsDetail(payParam.getGoodsDetail());
        normalOrder.setTerminalNo(terminalNo);
        // 客户端IP(支付入口已兜底, 作为单一事实源供退款/关单等后续流程取用)
        normalOrder.setClientIp(payParam.getClientIp());
        // 通道路由参数(同步时用于解析通道应用凭证)
        normalOrder.setChannelMchNo(payParam.getChannelMchNo());
        normalOrder.setCapability(payParam.getCapability());
        normalOrder.setAppId(appId);
        payNormalOrderManager.save(normalOrder);
        // 创建资金交易 PayTrade
        PayTrade trade = new PayTrade();
        trade.setAppId(appId);
        trade.setTradeNo(TradeNoGenerateUtil.pay());
        trade.setTradeType(PayTradeTypeEnum.NORMAL.getCode());
        trade.setContainerId(normalOrder.getId());
        trade.setAmount(amount);
        trade.setCurrency(CurrencyEnum.CNY.getCode());
        trade.setRefundableBalance(amount);
        trade.setStatus(PayFundStatusEnum.PROCESSING.getCode());
        trade.setSource(cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum.MCH_API.getCode());
        payTradeManager.save(trade);
        context.setTrade(trade);
        // 注册超时关单延时消息(按订单过期时间定时投递)
        this.registerTimeoutClose(trade.getTradeNo(), normalOrder.getBizOrderNo(), expiredTime);
    }

    /// 注册超时关单延时消息
    /// 按订单过期时间定时投递到 [PayArtemisConstants#NORMAL_TIMEOUT_QUEUE]。
    /// 发送失败不阻断下单流程, 由兜底定时任务 [cn.daxpay.open.payment.core.trade.job.NormalPayTimeoutJob] 补救。
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
    }

    /// 检查订单状态
    public void checkOrder(NormalPayOrder normalOrder, PayTrade trade) {
        // 容器状态检查
        String bizStatus = normalOrder.getStatus();
        if (Objects.equals(bizStatus, NormalPayOrderStatusEnum.PAID.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        if (Objects.equals(bizStatus, NormalPayOrderStatusEnum.CLOSED.getCode())
                || Objects.equals(bizStatus, NormalPayOrderStatusEnum.EXPIRED.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 资金状态检查
        String fundStatus = trade.getStatus();
        if (Objects.equals(fundStatus, PayFundStatusEnum.SUCCESS.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        if (Objects.equals(fundStatus, PayFundStatusEnum.CLOSE.getCode())
                || Objects.equals(fundStatus, PayFundStatusEnum.FAIL.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 超时检查
        if (Objects.nonNull(normalOrder.getExpiredTime())
                && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), normalOrder.getExpiredTime())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.timeoutRetry");
        }
    }

    /// 根据 PayTrade 构建支付结果
    public NormalPayResult buildResult(PayTrade trade) {
        return PayTradeConvert.CONVERT.toResult(trade);
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
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.expiredTimeError");
        }
    }
}
