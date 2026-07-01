package cn.daxpay.open.platform.system.handler.exception;

import cn.daxpay.open.platform.common.config.properties.PlatformCommonProperties;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizErrorException;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.BizWarnException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/// # 项目异常处理
///
/// ## SSE 异步端点说明
/// SSE 事件流端点(如 `/sse/connect`)基于 `SseEmitter`, 在客户端断连/超时/发送失败时,
/// Servlet 容器会做 ASYNC 重派发并抛出非 `SaTokenException` 的 `Throwable`(常为 `ClientAbortException`),
/// 最终落入本兜底处理. 此时响应 `Content-Type` 已锁为 `text/event-stream`, 无 JSON 转换器,
/// 写 body 会抛 `HttpMessageNotWritableException`. 故所有 handler 在 SSE 场景统一返回无 body 的 200 响应,
/// 并降级为 info 日志(多为客户端主动断连, 属常态).
@Slf4j
@RestControllerAdvice
@EnableConfigurationProperties(PlatformCommonProperties.class)
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final PlatformCommonProperties properties;

    /// 判断当前响应是否为 SSE 事件流(Content-Type 已锁定为 text/event-stream)
    private boolean isSseStream(HttpServletResponse response) {
        String contentType = response.getContentType();
        return contentType != null && contentType.contains(MediaType.TEXT_EVENT_STREAM_VALUE);
    }

    /// SSE 场景统一日志: 降级为 info, 仅记录异常类型与消息, 不打印完整堆栈
    private void logSse(Throwable ex) {
        log.info("SSE 端点异常(通常为客户端断连), 类型={}, 消息={}", ex.getClass().getSimpleName(), ex.getMessage());
    }

    /// 获取异常消息，支持国际化(按请求 locale, 用于响应)
    private String getMessage(BizException ex) {
        String messageKey = ex.resolveMessageKey();
        if (messageKey != null) {
            return I18nUtil.get(messageKey, ex.getArgs());
        }
        return ex.getMessage();
    }

    /// 获取异常的固定中文消息(用于日志, 不受请求语言影响)
    private String zhMessage(BizException ex) {
        String messageKey = ex.resolveMessageKey();
        return messageKey != null ? I18nUtil.get(messageKey, Locale.CHINA, ex.getArgs()) : ex.getMessage();
    }

    /// 业务异常统一处理: SSE 降级 + 日志(由 logger 决定级别与文案) + 国际化响应
    private Object handleBiz(BizException ex, HttpServletResponse response, Consumer<BizException> logger) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        logger.accept(ex);
        return Res.response(ex.getCode(), getMessage(ex), MDC.get(CommonCode.TRACE_ID));
    }

    /// 系统异常统一响应: 按配置决定是否透传原始消息(运行时异常/Throwable 兜底共用)
    private Object systemErrorResponse(Throwable ex) {
        if (properties.getException().isShowFullMessage()) {
            return Res.response(CommonErrorCode.SYSTEM_ERROR, ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
        }
        log.error("系统错误 {}", ex.getMessage(), ex);
        return Res.response(CommonErrorCode.SYSTEM_ERROR, I18nUtil.get("error.common.system"), MDC.get(CommonCode.TRACE_ID));
    }

    /// 普通业务异常, 不进行堆栈跟踪(debug 级别才跟踪)
    @ExceptionHandler(BizInfoException.class)
    public Object handleBizInfoException(BizInfoException ex, HttpServletResponse response) {
        return handleBiz(ex, response, e -> {
            log.info("业务异常 key={}, 消息={}", e.resolveMessageKey(), zhMessage(e));
            log.debug(e.resolveMessageKey(), e);
        });
    }

    /// 警告业务异常, 量多需关注
    @ExceptionHandler(BizWarnException.class)
    public Object handleBizWarnException(BizWarnException ex, HttpServletResponse response) {
        return handleBiz(ex, response, e -> log.warn("业务警告 key={}, 消息={}", e.resolveMessageKey(), zhMessage(e), e));
    }

    /// 致命业务异常, 需立即排查
    @ExceptionHandler(BizErrorException.class)
    public Object handleBizErrorException(BizErrorException ex, HttpServletResponse response) {
        return handleBiz(ex, response, e -> log.error("业务错误 key={}, 消息={}", e.resolveMessageKey(), zhMessage(e), e));
    }

    /// 业务异常兜底
    @ExceptionHandler(BizException.class)
    public Object handleBusinessException(BizException ex, HttpServletResponse response) {
        return handleBiz(ex, response, e -> log.info("业务异常 key={}, 消息={}", e.resolveMessageKey(), zhMessage(e), e));
    }

    /// 请求参数校验未通过
    @ExceptionHandler({ ConstraintViolationException.class })
    public Object handleBusinessException(ConstraintViolationException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.info(ex.getMessage(), ex);
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        return Res.response(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, message, MDC.get(CommonCode.TRACE_ID));
    }

    /// 请求参数校验未通过
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Object handleBusinessException(MethodArgumentNotValidException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.info(ex.getMessage(), ex);
        String message = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return Res.response(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, message, MDC.get(CommonCode.TRACE_ID));
    }

    /// 不支持 HTTP 请求方法异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        String message = I18nUtil.get("error.common.unsupportedOperate");
        log.info(message, ex);
        return Res.error(message);
    }

    /// 请求参数校验未通过
    @ExceptionHandler({ ValidationException.class })
    public Object handleBusinessException(ValidationException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.info(ex.getMessage(), ex);
        return Res.response(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /// 页面或资源不存在
    @ExceptionHandler({ NoResourceFoundException.class })
    public ResponseEntity<?> handleBusinessException(NoResourceFoundException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.info(ex.getMessage(), ex);
        String message = I18nUtil.get("error.common.notFound");
        Result<Void> result = Res.response(CommonErrorCode.SOURCES_NOT_EXIST, message, MDC.get(CommonCode.TRACE_ID));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    /// 处理 MissingServletRequestParameterException ( 缺少 必填的request param )
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.info(ex.getMessage(), ex);
        String message = I18nUtil.get("error.common.parseParams");
        return Res.response(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, message, MDC.get(CommonCode.TRACE_ID));
    }

    /// 处理 HttpMessageConversionException
    @ExceptionHandler(HttpMessageConversionException.class)
    public Object handleHttpMessageConversionException(HttpMessageConversionException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.info(ex.getMessage(), ex);
        return Res.response(CommonErrorCode.PARSE_PARAMETERS_ERROR, ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /// 处理 HttpMessageConversionException
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.info("参数绑定失败 ", ex);
        return Res.response(CommonErrorCode.PARSE_PARAMETERS_ERROR, ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /// 空指针异常
    @ExceptionHandler(NullPointerException.class)
    public Object handleNullPointerException(NullPointerException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.warn("空指针 ", ex);
        String message = I18nUtil.get("error.common.system");
        return Res.response(CommonErrorCode.SYSTEM_ERROR, message, MDC.get(CommonCode.TRACE_ID));
    }

    /// 处理运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.error(ex.getMessage(), ex);
        return systemErrorResponse(ex);
    }

    /// 处理 OutOfMemoryError
    @ExceptionHandler(OutOfMemoryError.class)
    public Object handleOomException(OutOfMemoryError ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        log.error("内存不足错误 {}", ex.getMessage(), ex);
        String message = I18nUtil.get("error.common.system");
        return Res.response(CommonErrorCode.SYSTEM_ERROR, message, MDC.get(CommonCode.TRACE_ID));
    }

    /// 处理 Throwable 异常
    @ExceptionHandler(Throwable.class)
    public Object handleThrowable(Throwable ex, HttpServletResponse response) {
        if (isSseStream(response)) {
            logSse(ex);
            return ResponseEntity.ok().build();
        }
        return systemErrorResponse(ex);
    }

}
