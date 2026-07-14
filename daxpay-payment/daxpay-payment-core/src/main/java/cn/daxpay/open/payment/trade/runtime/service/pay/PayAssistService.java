package cn.daxpay.open.payment.trade.runtime.service.pay;

import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.payment.trade.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.common.util.PayUtil;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.mq.NormalPayTimeoutMessage;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
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
    /// orderNo 与 tradeNo 独立生成; 普通通道实际上送串 relationOrderNo 默认=orderNo
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
        // 双号独立生成
        String orderNo = TradeNoGenerateUtil.order();
        String tradeNo = TradeNoGenerateUtil.pay();
        // 创建容器 NormalPayOrder（含冗余字段，方便查询）
        NormalPayOrder normalOrder = new NormalPayOrder();
        normalOrder.setOrderNo(orderNo);
        // 普通通道默认上送 orderNo, 特殊通道下单后再覆盖 relation
        normalOrder.setRelationOrderNo(orderNo);
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
        // 通道应用 AppId: doBeforePay 已将解析后的实际值回填到 payParam
        normalOrder.setChannelAppId(payParam.getChannelAppId());
        normalOrder.setAppId(appId);
        payNormalOrderManager.save(normalOrder);
        // 创建资金交易 PayTrade
        PayTrade trade = new PayTrade();
        trade.setAppId(appId);
        trade.setTradeNo(tradeNo);
        trade.setTradeType(PayTradeTypeEnum.NORMAL.getCode());
        trade.setContainerId(normalOrder.getId());
        trade.setAmount(amount);
        trade.setCurrency(CurrencyEnum.CNY.getCode());
        // 入账金额: 未成功前为 0, 成功后由 PayUniHandleService 按规则回写
        trade.setPostedAmount(0L);
        trade.setRefundableBalance(amount);
        trade.setStatus(PayFundStatusEnum.PROCESSING.getCode());
        // 实际上送串权威: 默认=orderNo, 特殊通道支付返回后可覆盖
        trade.setRelationOrderNo(orderNo);
        trade.setSource(cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum.MCH_API.getCode());
        payTradeManager.save(trade);
        context.setTrade(trade);
        context.setNormalOrder(normalOrder);
        // 注册超时关单延时消息(按订单过期时间定时投递)
        this.registerTimeoutClose(trade.getTradeNo(), normalOrder.getBizOrderNo(), expiredTime);
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
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        if (Objects.equals(bizStatus, NormalPayOrderStatusEnum.CLOSED.getCode())
                || Objects.equals(bizStatus, NormalPayOrderStatusEnum.EXPIRED.getCode())
                || Objects.equals(bizStatus, NormalPayOrderStatusEnum.FAILED.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 资金状态检查
        String fundStatus = trade.getStatus();
        if (Objects.equals(fundStatus, PayFundStatusEnum.SUCCESS.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        if (Objects.equals(fundStatus, PayFundStatusEnum.CLOSE.getCode())
                || Objects.equals(fundStatus, PayFundStatusEnum.FAIL.getCode())
                || Objects.equals(fundStatus, PayFundStatusEnum.CANCEL.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        // 超时检查
        if (Objects.nonNull(normalOrder.getExpiredTime())
                && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), normalOrder.getExpiredTime())) {
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
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.expiredTimeError");
        }
    }
}
