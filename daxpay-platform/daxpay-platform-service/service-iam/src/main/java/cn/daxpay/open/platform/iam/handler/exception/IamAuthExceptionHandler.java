package cn.daxpay.open.platform.iam.handler.exception;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.platform.iam.exception.auth.TwoFactorRequiredException;
import cn.daxpay.open.platform.iam.result.auth.TwoFactorChallengeResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

/// # IAM 认证异常处理
///
/// 集中处理双因素认证挑战等 IAM 认证流程异常, 返回前端可识别的结构化结果。
/// 2FA 属 IAM 业务, 其异常处理内聚于本模块, 避免下层 service-system 反向依赖 service-iam。
///
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class IamAuthExceptionHandler {

    /// 双因素认证: 密码通过但需二次验证, 返回预认证令牌, 不计入登录失败
    @ExceptionHandler(TwoFactorRequiredException.class)
    public Result<TwoFactorChallengeResult> handleTwoFactorRequired(TwoFactorRequiredException ex) {
        String key = ex.resolveMessageKey();
        log.info("双因素认证 key={}, 消息={}", key, I18nUtil.get(key, Locale.CHINA, ex.getArgs()));
        String message = I18nUtil.get(key, ex.getArgs());
        return Res.response(TwoFactorRequiredException.CODE, message, new TwoFactorChallengeResult(ex.getPreAuthToken()));
    }

}
