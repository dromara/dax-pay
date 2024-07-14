package cn.bootx.platform.common.exceptionhandler.handler;

import cn.bootx.platform.core.code.CommonCode;
import cn.bootx.platform.core.code.CommonErrorCode;
import cn.bootx.platform.core.exception.BizErrorException;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.BizInfoException;
import cn.bootx.platform.core.exception.BizWarnException;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Web 项目异常处理
 *
 * @author xxm
 * @since 2020/5/8 15:30
 */
@Slf4j
@RestControllerAdvice
@EnableConfigurationProperties(ExceptionHandlerProperties.class)
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final ExceptionHandlerProperties properties;

    /**
     * 普通业务异常, 不需要进行堆栈跟踪
     */
    @ExceptionHandler(BizInfoException.class)
    public Result<Void> handleBizInfoException(BizInfoException ex) {
        log.info(ex.getMessage());
        return Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 警告业务异常, 如果量多需要关注
     */
    @ExceptionHandler(BizWarnException.class)
    public Result<Void> handleBizWarnException(BizWarnException ex) {
        log.warn(ex.getMessage(), ex);
        return Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 致命警告业务异常, 需要进行立即进入排查
     */
    @ExceptionHandler(BizErrorException.class)
    public Result<Void> handleBizErrorException(BizErrorException ex) {
        log.error(ex.getMessage(), ex);
        return Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBusinessException(BizException ex) {
        log.info(ex.getMessage(), ex);
        return Res.response(ex.getCode(), ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 请求参数校验未通过
     */
    @ExceptionHandler({ ConstraintViolationException.class })
    public Result<Void> handleBusinessException(ConstraintViolationException ex) {
        log.info(ex.getMessage(), ex);
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            message.append(violation.getMessage()).append(System.lineSeparator());
        }
        return Res.response(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, message.toString(), MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 不支持 HTTP 请求方法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("不支持");
        sb.append(e.getMethod());
        sb.append("请求方法，");
        sb.append("支持以下");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            sb.append(String.join("、", methods));
        }
        log.info(sb.toString(), e);
        return Res.error(sb.toString());
    }

    /**
     * 请求参数校验未通过
     */
    @ExceptionHandler({ ValidationException.class })
    public Result<Void> handleBusinessException(ValidationException ex) {
        log.info(ex.getMessage(), ex);
        return Res.response(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 请求参数校验未通过
     */
    @ExceptionHandler({ NoResourceFoundException.class })
    public Result<Void> handleBusinessException(NoResourceFoundException ex) {
        log.info(ex.getMessage(), ex);
        return Res.response(CommonErrorCode.SOURCES_NOT_EXIST, "页面或资源不存在", MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 处理 MissingServletRequestParameterException ( 缺少 必填的request param )
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.info(ex.getMessage(), ex);
        return Res.response(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "参数处理失败", MDC.get(CommonCode.TRACE_ID));
    }


    /**
     * 处理 HttpMessageConversionException
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public Result<Void> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        log.info(ex.getMessage(), ex);
        return Res.response(CommonErrorCode.PARSE_PARAMETERS_ERROR, ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }
    /**
     * 处理 HttpMessageConversionException
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException ex) {
        log.info("参数绑定失败 ", ex);
        return Res.response(CommonErrorCode.PARSE_PARAMETERS_ERROR, ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException ex) {
        log.warn("空指针 ", ex);
        return Res.response(CommonErrorCode.SYSTEM_ERROR, "数据错误", MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        if (properties.isShowFullMessage()) {
            return Res.response(CommonErrorCode.SYSTEM_ERROR, ex.getMessage(), MDC.get(CommonCode.TRACE_ID));
        }
        else {
            return Res.response(CommonErrorCode.SYSTEM_ERROR, "系统错误", MDC.get(CommonCode.TRACE_ID));
        }
    }

    /**
     * 处理 OutOfMemoryError
     */
    @ExceptionHandler(OutOfMemoryError.class)
    public Result<Void> handleOomException(OutOfMemoryError ex) {
        log.error("内存不足错误 {}", ex.getMessage(), ex);
        return Res.response(CommonErrorCode.SYSTEM_ERROR, "系统错误", MDC.get(CommonCode.TRACE_ID));
    }

    /**
     * 处理 Throwable 异常
     */
    @ExceptionHandler(Throwable.class)
    public Result<Void> handleThrowable(Throwable ex) {
        log.error("系统错误 {}", ex.getMessage(), ex);
        return Res.response(CommonErrorCode.SYSTEM_ERROR, "系统错误", MDC.get(CommonCode.TRACE_ID));
    }

}
