package cn.daxpay.open.payment.unipay.gateway.service;

import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.enums.GatewayPayTypeEnum;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.runtime.service.pay.PayAssistService;
import cn.daxpay.open.payment.trade.runtime.mq.GatewayTimeoutMessage;
import cn.daxpay.open.payment.trade.runtime.mq.PayArtemisConstants;
import cn.daxpay.open.payment.unipay.gateway.param.GatewayPrePayParam;
import cn.daxpay.open.payment.unipay.gateway.result.GatewayPrePayResult;
import cn.daxpay.open.platform.common.artemis.service.ArtemisTemplateService;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformUrlConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/// # 网关支付支撑服务
///
/// 预下单建容器、URL 生成、状态校验、超时消息注册。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayAssistService {

    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final MerchantContextLoader merchantContextLoader;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final ArtemisTemplateService artemisTemplateService;
    private final LockTemplate lockTemplate;
    private final PayAssistService payAssistService;

    /// 预下单: 仅创建容器, 返回落地页 URL
    public GatewayPrePayResult prePay(GatewayPrePayParam param) {
        if (StrUtil.isBlank(param.getClientIp())) {
            param.setClientIp(WebServletUtil.getClientIp());
        }
        payAssistService.validationExpiredTime(param.getExpiredTime());
        GatewayPayTypeEnum typeEnum = GatewayPayTypeEnum.findByCode(param.getGatewayPayType());
        // 本期仅实现聚合扫码, 收银台类型允许预留但提示
        if (typeEnum == GatewayPayTypeEnum.CASHIER) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.gateway.cashierNotReady");
        }

        var mchApp = merchantContextLoader.resolveApp(param.getMchNo(), param.getAppId());
        param.setAppId(mchApp.getAppId());
        merchantContextLoader.initMch(param.getMchNo());

        LockInfo lock = lockTemplate.lock(
                "payment:gateway:pre:" + param.getAppId() + ":" + param.getBizOrderNo(), 10000, 200);
        if (Objects.isNull(lock)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.processing");
        }
        try {
            return cn.hutool.extra.spring.SpringUtil.getBean(this.getClass()).doPrePay(param, typeEnum);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public GatewayPrePayResult doPrePay(GatewayPrePayParam param, GatewayPayTypeEnum typeEnum) {
        // 幂等: 已有未终态单则返回原 URL
        var existing = gatewayPayOrderManager.findByBizOrderNo(param.getBizOrderNo(), param.getAppId());
        if (existing.isPresent()) {
            GatewayPayOrder order = existing.get();
            String status = order.getStatus();
            if (Objects.equals(status, GatewayOrderStatusEnum.PAID.getCode())) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
            }
            // failed 与 closed/expired 同为终态, 不允许再返回原 URL 假装可付
            if (List.of(GatewayOrderStatusEnum.CLOSED.getCode(), GatewayOrderStatusEnum.EXPIRED.getCode(),
                    GatewayOrderStatusEnum.FAILED.getCode()).contains(status)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
            }
            return this.buildPrePayResult(order);
        }

        OffsetDateTime expiredTime = payAssistService.getExpiredTime(param.getExpiredTime());

        GatewayPayOrder order = new GatewayPayOrder();
        order.setAppId(param.getAppId());
        // 容器业务单号用 order() 号段(ORD…), 与资金 tradeNo 的 pay() 号段(PAY…)身份分离
        order.setOrderNo(TradeNoGenerateUtil.order());
        order.setBizOrderNo(param.getBizOrderNo());
        order.setGatewayType(typeEnum.getCode());
        order.setTitle(param.getTitle());
        order.setDescription(param.getDescription());
        order.setStatus(GatewayOrderStatusEnum.WAIT_PAY.getCode());
        order.setNotifyUrl(param.getNotifyUrl());
        order.setReturnUrl(param.getReturnUrl());
        order.setAttach(param.getAttach());
        order.setExtraParam(param.getExtraParam());
        order.setExpiredTime(expiredTime);
        order.setAmount(param.getAmount());
        order.setCurrency(CurrencyEnum.CNY.getCode());
        order.setClientIp(param.getClientIp());
        order.setGoodsDetail(param.getGoodsDetail());
        gatewayPayOrderManager.save(order);

        this.registerTimeout(order.getOrderNo(), order.getBizOrderNo(), expiredTime);
        return this.buildPrePayResult(order);
    }

    /// 按 orderNo 加载并校验可支付
    ///
    /// 引导读用 NotTenant 定位订单后 **立即** initMch，后续聚合配置/交易均走租户过滤。
    public GatewayPayOrder getOrderAndCheck(String orderNo) {
        GatewayPayOrder order = gatewayPayOrderManager.findByOrderNoNotTenant(orderNo)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.error.payOrderNotExist"));
        // 立即装载商户上下文，防止后续 MchBaseEntity 查询无 mchNo
        merchantContextLoader.initMch(order.getMchNo());
        this.checkPayable(order);
        return order;
    }

    /// 校验容器可继续支付
    public void checkPayable(GatewayPayOrder order) {
        String status = order.getStatus();
        if (Objects.equals(status, GatewayOrderStatusEnum.PAID.getCode())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.alreadySuccess");
        }
        // 与 NormalPayOrder 终态校验对齐: failed / closed / expired 均不可继续支付
        if (List.of(GatewayOrderStatusEnum.CLOSED.getCode(), GatewayOrderStatusEnum.EXPIRED.getCode(),
                GatewayOrderStatusEnum.FAILED.getCode()).contains(status)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.failedOrClosed");
        }
        if (Objects.nonNull(order.getExpiredTime())
                && DateTimeUtil.ge(OffsetDateTime.now(ZoneOffset.UTC), order.getExpiredTime())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.timeoutRetry");
        }
    }

    /// 生成落地页 URL
    public String buildGatewayUrl(GatewayPayOrder order) {
        PlatformUrlConfig urlConfig = platformUrlConfigService.getUrlConfig();
        String gatewayBase = urlConfig.getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(gatewayBase)) {
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.common.gatewayUrlNotConfigured");
        }
        gatewayBase = StrUtil.removeSuffix(gatewayBase, "/");
        if (Objects.equals(order.getGatewayType(), GatewayPayTypeEnum.AGGREGATE.getCode())) {
            return gatewayBase + "/aggregate/" + order.getOrderNo();
        }
        return gatewayBase + "/cashier/" + order.getOrderNo();
    }

    public GatewayPrePayResult buildPrePayResult(GatewayPayOrder order) {
        return new GatewayPrePayResult()
                .setOrderNo(order.getOrderNo())
                .setBizOrderNo(order.getBizOrderNo())
                .setStatus(order.getStatus())
                .setUrl(this.buildGatewayUrl(order))
                .setExpiredTime(order.getExpiredTime());
    }

    private void registerTimeout(String orderNo, String bizOrderNo, OffsetDateTime expiredTime) {
        GatewayTimeoutMessage message = new GatewayTimeoutMessage()
                .setOrderNo(orderNo)
                .setBizOrderNo(bizOrderNo);
        String json = JacksonUtil.toJson(message);
        try {
            artemisTemplateService.sendDelayAt(PayArtemisConstants.GATEWAY_TIMEOUT_QUEUE, json, expiredTime);
        } catch (Exception e) {
            log.warn("注册网关超时关单延时消息失败, 由定时任务兜底, orderNo={}", orderNo, e);
        }
    }
}
