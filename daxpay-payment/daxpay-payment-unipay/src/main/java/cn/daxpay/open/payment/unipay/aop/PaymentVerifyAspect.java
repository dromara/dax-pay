package cn.daxpay.open.payment.unipay.aop;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.common.request.context.RequestContextHolder;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.event.UnipayApiAccessLogEvent;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.util.ValidationUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/// # 开放支付验签切面（[PaymentVerify]）
///
/// 执行顺序: 过滤器 -> 拦截器 -> 切面 -> 方法。
/// 切面负责**签名 API 身份源**：校验参数后 `initMch` + 验签；不负责应用解析。
/// 装载后的 mchNo 供 TenantLine / 自动填充使用。
///
/// 同时在 finally 中发布 [UnipayApiAccessLogEvent]，失败吞异常，不影响支付主流程。
/// 审计业务索引：`mchNo` + `reqId`。
@Aspect
@Slf4j
@Component
@Order
@RequiredArgsConstructor
public class PaymentVerifyAspect {
    private final PaymentSignService paymentSignService;
    private final PaymentReplayProtectService paymentReplayProtectService;
    private final MerchantContextLoader merchantContextLoader;
    private final ApplicationEventPublisher eventPublisher;

    /// 处理方法上的@PaymentVerify注解
    @Around("@annotation(paymentVerify)")
    public Object methodPointAnnotation(ProceedingJoinPoint pjp, PaymentVerify paymentVerify) throws Throwable {
        return doVerify(pjp);
    }

    /// 处理类上的@PaymentVerify注解（排除方法上也有的情况，避免重复匹配）
    @Around("@within(paymentVerify) && !@annotation(cn.daxpay.open.payment.unipay.aop.PaymentVerify)")
    public Object methodPointWithin(ProceedingJoinPoint pjp, PaymentVerify paymentVerify) throws Throwable {
        return doVerify(pjp);
    }

    /// 支付签名校验逻辑 + 接口审计发布
    private Object doVerify(ProceedingJoinPoint pjp) throws Throwable {
        long startNs = System.nanoTime();
        Object result = null;
        Throwable error = null;
        String reqId = null;
        try {
            Object[] args = pjp.getArgs();
            if (args.length == 0) {
                // 支付方法至少有一个参数
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.verify.methodParamRequired");
            }
            Object param = args[0];
            if (param instanceof MerchantPaymentCommonParam paymentParam) {
                reqId = paymentParam.getReqId();
                // 参数校验
                ValidationUtil.validateParam(paymentParam);
                // 商户身份初始化(含状态校验), 使 mchNo 进入线程上下文供签名校验与自动填充
                merchantContextLoader.initMch(paymentParam.getMchNo());
                // 参数签名校验
                paymentSignService.signVerify(paymentParam);
                // 防重放校验（Nonce一次性消费 + 请求时间窗口，受平台API安全配置开关控制）
                paymentReplayProtectService.verify(paymentParam, paymentParam.getMchNo());
            } else {
                // 参数需要继承MerchantPaymentCommonParam
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.verify.paramExtendRequired");
            }
            Object proceed;
            try {
                proceed = pjp.proceed();
            } catch (BizException ex) {
                // DaxResult.msg 按 Accept-Language 解析 messageKey 为本地化消息
                // 否则商户和日志看到的都是 "error.channel.alipay.payFailed" 这种原始 key, 无法阅读
                DaxResult<Void> daxResult = new DaxResult<>(ex.getCode(), resolveResponseMessage(ex));
                enrichDaxResult(daxResult, reqId);
                paymentSignService.sign(daxResult);
                result = daxResult;
                return daxResult;
            }
            // 对返回值添加响应时间、reqId 并进行签名(traceId 走响应头, 不进 body)
            if (proceed instanceof DaxResult<?> daxResult) {
                daxResult.setResTime(OffsetDateTime.now(ZoneOffset.UTC));
                enrichDaxResult(daxResult, reqId);
                paymentSignService.sign(daxResult);
                result = daxResult;
            } else {
                // 支付方法返回类型需要为 DaxResult
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.verify.returnTypeRequired");
            }
            return proceed;
        } catch (Throwable t) {
            error = t;
            throw t;
        } finally {
            // 审计发布：任何异常均吞掉，绝不影响支付
            try {
                publishAudit(pjp, startNs, result, error, reqId);
            } catch (Exception e) {
                log.warn("支付接口审计事件发布失败: {}", e.getMessage());
            }
        }
    }

    /// 回写请求ID
    ///
    /// traceId 不再写入响应体, 由 [cn.daxpay.open.platform.common.request.context.filter.TraceIdFilter]
    /// 统一通过 `x-trace-id` 响应头返回, 供客户端关联排障。
    private void enrichDaxResult(DaxResult<?> daxResult, String reqId) {
        if (StrUtil.isNotBlank(reqId)) {
            daxResult.setReqId(reqId);
        }
    }

    /// 解析异常消息用于响应(按请求 Accept-Language 国际化)
    ///
    /// [BizException#getMessage] 返回的是 messageKey 原文(如 "error.channel.alipay.payFailed"),
    /// 需通过 [I18nUtil] 解析为本地化消息(如 "支付宝支付异常: 余额不足"),
    /// 与 [cn.daxpay.open.platform.system.handler.exception.RestExceptionHandler] 的响应消息保持一致语义。
    private String resolveResponseMessage(BizException ex) {
        String messageKey = ex.resolveMessageKey();
        if (messageKey != null) {
            return I18nUtil.get(messageKey, ex.getArgs());
        }
        return ex.getMessage();
    }

    /// 组装并发布审计事件
    private void publishAudit(ProceedingJoinPoint pjp, long startNs, Object result, Throwable error, String reqId) {
        long durationMs = (System.nanoTime() - startNs) / 1_000_000L;
        String apiPath = RequestContextHolder.getRequestUri();
        String requestMethod = RequestContextHolder.getMethod();

        UnipayApiAccessLogEvent event = new UnipayApiAccessLogEvent()
                .setApiPath(apiPath)
                .setApiTitle(resolveApiTitle(apiPath))
                .setRequestMethod(requestMethod)
                .setReqId(reqId)
                .setDurationMs(durationMs)
                .setOperateTime(OffsetDateTime.now(ZoneOffset.UTC))
                .setTraceId(resolveTraceId());

        // 真实接入 IP
        Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .ifPresent(event::setRequestIp);

        // 请求参数
        Object[] args = pjp.getArgs();
        Object param = (args != null && args.length > 0) ? args[0] : null;
        if (param instanceof MerchantPaymentCommonParam paymentParam) {
            event.setMchNo(paymentParam.getMchNo());
            event.setClientIp(paymentParam.getClientIp());
            if (StrUtil.isBlank(event.getReqId())) {
                event.setReqId(paymentParam.getReqId());
            }
            try {
                event.setReqParam(JacksonUtil.toJson(param));
            } catch (Exception e) {
                event.setReqParam(null);
            }
        } else if (param != null) {
            try {
                event.setReqParam(JacksonUtil.toJson(param));
            } catch (Exception ignored) {
                // ignore
            }
        }

        // 响应结果
        if (result instanceof DaxResult<?> daxResult) {
            boolean success = daxResult.getCode() == CommonCode.SUCCESS_CODE;
            event.setSuccess(success);
            event.setErrorCode(daxResult.getCode());
            if (!success) {
                event.setErrorMsg(daxResult.getMsg());
            }
            if (StrUtil.isBlank(event.getReqId()) && StrUtil.isNotBlank(daxResult.getReqId())) {
                event.setReqId(daxResult.getReqId());
            }
            try {
                event.setResBody(JacksonUtil.toJson(daxResult));
            } catch (Exception e) {
                event.setResBody(null);
            }
        } else if (error != null) {
            event.setSuccess(false);
            event.setErrorMsg(StrUtil.sub(error.getMessage(), 0, 512));
            try {
                Map<String, Object> errBody = new LinkedHashMap<>(2);
                errBody.put("exception", error.getClass().getSimpleName());
                errBody.put("message", error.getMessage());
                event.setResBody(JacksonUtil.toJson(errBody));
            } catch (Exception ignored) {
                // ignore
            }
        } else {
            event.setSuccess(true);
        }

        eventPublisher.publishEvent(event);
    }

    /// 解析链路 ID：优先 MDC，其次请求头
    private String resolveTraceId() {
        String fromMdc = MDC.get(CommonCode.TRACE_ID);
        if (StrUtil.isNotBlank(fromMdc)) {
            return fromMdc;
        }
        return RequestContextHolder.getTraceId();
    }

    /// 按路径映射可读标题
    private String resolveApiTitle(String path) {
        if (StrUtil.isBlank(path)) {
            return "统一支付接口";
        }
        if (path.contains("/sync/") && path.endsWith("/pay")) {
            return "支付订单同步";
        }
        if (path.endsWith("/close")) {
            return "关单/撤销";
        }
        if (path.contains("/pay-order")) {
            return "支付订单查询";
        }
        if (path.contains("/gateway/pre-pay")) {
            return "网关预下单";
        }
        if (path.contains("/gateway/query")) {
            return "网关订单查询";
        }
        if (path.contains("/generate-auth-url")) {
            return "生成通道授权链接";
        }
        if (path.contains("/assist/channel/auth")) {
            return "通道授权";
        }
        if (path.endsWith("/pay")) {
            return "支付下单";
        }
        return path;
    }
}
