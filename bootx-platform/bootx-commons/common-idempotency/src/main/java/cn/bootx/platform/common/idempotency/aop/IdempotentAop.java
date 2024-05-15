package cn.bootx.platform.common.idempotency.aop;

import cn.bootx.platform.common.core.annotation.Idempotent;
import cn.bootx.platform.common.core.code.WebHeaderCode;
import cn.bootx.platform.common.core.exception.RepetitiveOperationException;
import cn.bootx.platform.common.headerholder.HeaderHolder;
import cn.bootx.platform.common.redis.RedisClient;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 幂等处理器切面
 *
 * @author xxm
 * @since 2021/08/20
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotentAop {

    private final RedisClient redisClient;

    private final List<String> METHODS = Arrays.asList(Method.GET.name(), Method.POST.name(), Method.PUT.name(),
            Method.DELETE.name());

    /**
     * 幂等切面
     */
    @Around("@annotation(idempotent)")
    public Object doAround(ProceedingJoinPoint pjp, Idempotent idempotent) throws Throwable {
        if (idempotent.enable()) {
            String method = WebServletUtil.getMethod();
            // 只处理四种经典的情况
            if (METHODS.contains(method.toUpperCase(Locale.ROOT))) {
                // 从请求头或者请求参数中获取幂等Token
                String idempotentToken = HeaderHolder.getIdempotentToken();
                if (StrUtil.isBlank(idempotentToken)) {
                    idempotentToken = WebServletUtil.getParameter((WebHeaderCode.IDEMPOTENT_TOKEN));
                }
                // 进行判断拦截
                if (StrUtil.isNotBlank(idempotentToken)) {
                    String key;
                    // 是否有自定义的命名空间
                    if (StrUtil.isNotBlank(idempotent.name())) {
                        key = WebHeaderCode.IDEMPOTENT_TOKEN + ":" + idempotent.name();
                    }
                    else {
                        // 没有的话添方法名为命名空间
                        key = WebHeaderCode.IDEMPOTENT_TOKEN + ":" + pjp.getStaticPart().getSignature().getName();
                    }
                    // 是否已经存在幂等token
                    Boolean flag = redisClient.setIfAbsent(key + ":" + idempotentToken, "", idempotent.timeout());
                    if (Boolean.FALSE.equals(flag)) {
                        throw new RepetitiveOperationException(idempotent.message());
                    }
                }
            }
        }
        return pjp.proceed();
    }

}
