package cn.daxpay.open.payment.pay.service;

import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.payment.common.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.merchant.service.appinfo.MchAppInfoService;
import cn.daxpay.open.payment.common.util.PayUtil;
import cn.hutool.core.util.StrUtil;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.pay.convert.PayTradeConvert;
import cn.daxpay.open.payment.pay.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.pay.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.pay.order.dao.PayTradeManager;
import cn.daxpay.open.payment.pay.order.entity.PayTrade;
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
    private final MchAppInfoService mchAppInfoService;

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
        normalOrder.setProduct(payParam.getProduct());
        normalOrder.setExtraParam(payParam.getExtraParam());
        normalOrder.setGoodsDetail(payParam.getGoodsDetail());
        normalOrder.setTerminalNo(terminalNo);
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
        trade.setProduct(payParam.getProduct());
        trade.setChannel(channel);
        trade.setMethod(payParam.getMethod());
        trade.setLimitPay(payParam.getLimitPay() != null
                ? String.join(",", payParam.getLimitPay()) : null);
        trade.setAmount(amount);
        trade.setCurrency(CurrencyEnum.CNY.getCode());
        trade.setRefundableBalance(amount);
        trade.setStatus(PayFundStatusEnum.PROCESSING.getCode());
        trade.setExpiredTime(expiredTime);
        trade.setSource(cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum.MCH_API.getCode());
        trade.setBarCode(payParam.getAuthCode());
        trade.setOpenid(payParam.getOpenId());
        payTradeManager.save(trade);
        context.setContainer(normalOrder).setTrade(trade);
    }

    /// 查询已有订单并校验，结果填充到 context
    /// 订单不存在则 context 保持不变（调用方据此判断是否新建）
    public void findAndCheckOrder(String bizOrderNo, PayStrategyContext context) {
        Optional<NormalPayOrder> normalOrderOpt = payNormalOrderManager.findByBizOrderNo(bizOrderNo);
        if (normalOrderOpt.isEmpty()) {
            return;
        }
        NormalPayOrder normalOrder = normalOrderOpt.get();
        PayTrade trade = payTradeManager.findByContainerId(normalOrder.getId()).orElse(null);
        if (trade == null) {
            return;
        }
        this.checkOrder(normalOrder, trade);
        context.setContainer(normalOrder).setTrade(trade);
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
        if (Objects.nonNull(trade.getExpiredTime())
                && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), trade.getExpiredTime())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.timeoutRetry");
        }
    }

    /// 根据 PayTrade 构建支付结果
    public NormalPayResult buildResult(PayTrade trade) {
        NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
        return PayTradeConvert.CONVERT.toResult(trade, normalOrder);
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
