package cn.daxpay.open.platform.capability.auth.handler;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.capability.auth.exception.NotLoginException;
import cn.daxpay.open.platform.capability.auth.exception.RouterCheckException;
import cn.dev33.satoken.exception.SaTokenContextException;
import cn.dev33.satoken.exception.SaTokenException;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/// # 过滤SaTokenException,需要运行在 RestExceptionHandler 之前
///
/// ## SSE / 异步派发
/// SSE 端点(如 `/notify/user/sse/connect`)基于 `SseEmitter`, 完成/超时/断连时容器会 ASYNC/ERROR 重派发.
/// 此时 Sa-Token ThreadLocal 上下文常为空, 可能抛 [SaTokenContextException].
/// 与 [cn.daxpay.open.platform.system.handler.exception.RestExceptionHandler] 对齐:
/// SSE 或非 REQUEST 派发时不写 JSON body, 避免 EventSource 把 `code:1` 当致命错误狂重连.
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Slf4j
@RestControllerAdvice
public class SaExceptionHandler {

    /// 判断当前响应是否为 SSE 事件流(Content-Type 已锁定为 text/event-stream)
    private boolean isSseStream(HttpServletResponse response) {
        String contentType = response.getContentType();
        return contentType != null && contentType.contains(MediaType.TEXT_EVENT_STREAM_VALUE);
    }

    /// SSE 流或非 REQUEST 派发(ASYNC/ERROR 等): 上下文/鉴权异常按常态降级
    private boolean isSseOrNonRequest(HttpServletRequest request, HttpServletResponse response) {
        if (request.getDispatcherType() != DispatcherType.REQUEST) {
            return true;
        }
        return isSseStream(response);
    }

    /// 未登录返回401
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Result<Void>> handleNotLoginException(NotLoginException ex){
        // 日志固定输出 messageKey 与中文翻译, 不受请求语言影响
        String key = ex.resolveMessageKey();
        log.info("鉴权异常 消息={}, key={}", I18nUtil.get(key, Locale.CHINA, ex.getArgs()), key, ex);
        Result<Void> result = Res.response(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    /// 处理 Sa-Token 抛出的未登录异常(被踢下线/顶下线/过期等)
    ///
    /// Sa-Token 自身的 [cn.dev33.satoken.exception.NotLoginException] 与本项目自定义的
    /// [NotLoginException] 同名但不同包, 互无继承关系; 前者继承自 [SaTokenException],
    /// 若不在此单独处理, 会落入下方 [SaTokenException] 兜底而返回 500, 前端 401 拦截无法感知。
    /// 按异常 type 映射不同 i18n 文案, 返回 401 以便前端识别并跳转登录页。
    @ExceptionHandler(cn.dev33.satoken.exception.NotLoginException.class)
    public ResponseEntity<Result<Void>> handleSaTokenNotLoginException(cn.dev33.satoken.exception.NotLoginException ex) {
        String type = ex.getType();
        // 按未登录原因映射 i18n 消息 key
        String messageKey = switch (type) {
            case cn.dev33.satoken.exception.NotLoginException.NOT_TOKEN      -> "error.auth.notToken";
            case cn.dev33.satoken.exception.NotLoginException.INVALID_TOKEN  -> "error.auth.invalidToken";
            case cn.dev33.satoken.exception.NotLoginException.TOKEN_TIMEOUT  -> "error.auth.tokenTimeout";
            case cn.dev33.satoken.exception.NotLoginException.BE_REPLACED    -> "error.auth.beReplaced";
            case cn.dev33.satoken.exception.NotLoginException.KICK_OUT       -> "error.auth.kickOut";
            case cn.dev33.satoken.exception.NotLoginException.TOKEN_FREEZE   -> "error.auth.tokenFreeze";
            default -> "error.auth.notLogin";
        };
        String message = I18nUtil.get(messageKey);
        log.info("Sa-Token 未登录 type={}, key={}", type, messageKey);
        Result<Void> result = Res.response(CommonErrorCode.AUTHENTICATION_FAIL, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    /// 路径无权访问
    @ExceptionHandler(RouterCheckException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(RouterCheckException ex) {
        // 日志固定输出 messageKey 与中文翻译, 不受请求语言影响
        String key = ex.resolveMessageKey();
        log.info("鉴权异常 消息={}, key={}", I18nUtil.get(key, Locale.CHINA, ex.getArgs()), key, ex);
        Result<Void> result = Res.response(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    /// Sa-Token 请求上下文未初始化
    ///
    /// SSE/异步派发下属常态噪声; REQUEST 派发仍出现则多为 ContextFilter 未生效, 按认证失败返回 401。
    @ExceptionHandler(SaTokenContextException.class)
    public ResponseEntity<?> handleSaTokenContextException(SaTokenContextException ex,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {
        // SSE 流 / 非 REQUEST: 无 JSON body, 避免 EventSource 狂重连
        if (isSseOrNonRequest(request, response)) {
            log.info("SaToken 上下文未初始化(SSE/异步派发), dispatcher={}, msg={}",
                    request.getDispatcherType(), ex.getMessage());
            return ResponseEntity.ok().build();
        }
        // REQUEST 派发仍无上下文: 需排查 SaTokenContextFilter 装配
        log.warn("SaToken 上下文未初始化(REQUEST 派发, 需排查 ContextFilter), path={}, msg={}",
                request.getRequestURI(), ex.getMessage());
        // 认证上下文未初始化，请重新登录或刷新页面
        String message = I18nUtil.get("error.auth.contextNotInit");
        Result<Void> result = Res.response(CommonErrorCode.AUTHENTICATION_FAIL, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    /// sa鉴权业务异常
    ///
    /// 注意: SSE 等异步端点产生的事件流响应 Content-Type 已锁定为 text/event-stream,
    /// 无 JSON 消息转换器, 此时返回 Result 会抛 HttpMessageNotWritableException, 故仅返回无 body.
    @ExceptionHandler(SaTokenException.class)
    public ResponseEntity<?> handleBusinessException(SaTokenException ex,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {
        // SSE / 非 REQUEST: 与 RestExceptionHandler 对齐, 无 body 200, 不打完整堆栈
        if (isSseOrNonRequest(request, response)) {
            log.info("Sa-Token 异常(SSE/异步派发), 类型={}, 消息={}",
                    ex.getClass().getSimpleName(), ex.getMessage());
            return ResponseEntity.ok().build();
        }
        log.info("Sa-Token 异常 类型={}, 消息={}", ex.getClass().getSimpleName(), ex.getMessage());
        log.debug("Sa-Token 异常堆栈", ex);
        Result<Void> result = Res.response(CommonCode.FAIL_CODE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

}
