package org.dromara.daxpay.platform.capability.audit.log.handler;

import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import org.dromara.daxpay.platform.common.request.context.RequestContextHolder;
import org.dromara.daxpay.platform.common.spring.util.AopUtil;
import org.dromara.daxpay.platform.common.spring.util.WebServletUtil;
import org.dromara.daxpay.platform.core.annotation.OperateLog;
import org.dromara.daxpay.platform.core.annotation.OperateLogs;
import org.dromara.daxpay.platform.core.code.WebHeaderCode;
import org.dromara.daxpay.platform.core.entity.UserDetail;
import org.dromara.daxpay.platform.capability.audit.log.param.OperateLogParam;
import org.dromara.daxpay.platform.capability.audit.log.service.ip2region.IpToRegionService;
import org.dromara.daxpay.platform.capability.audit.log.service.log.OperateLogService;
import org.dromara.daxpay.platform.capability.auth.util.SecurityUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/// # 操作日志切面处理
///
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperateLogAspectHandler {

    private final OperateLogService operateLogService;
    private final IpToRegionService ipToRegionService;

    /// 配置织入点
    @Pointcut("@annotation(org.dromara.daxpay.platform.core.annotation.OperateLog) || @annotation(org.dromara.daxpay.platform.core.annotation.OperateLogs)")
    public void logPointCut() {
    }

    /// 处理完请求后执行
    @AfterReturning(pointcut = "logPointCut()", returning = "o")
    public void doAfterReturning(JoinPoint joinPoint, Object o) {
        handleLog(joinPoint, null, o);
    }

    /// 拦截异常操作
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    /// 操作log处理
    public void handleLog(JoinPoint joinPoint, Exception e, Object o) {
        List<OperateLog> logs = getMethodAnnotation(joinPoint);
        if (CollUtil.isEmpty(logs)) {
            return;
        }
        // ip信息
        var ip = "未知";
        String location = "未知";
        Optional<String> ipOpt = Optional.ofNullable(WebServletUtil.getRequest()).map(JakartaServletUtil::getClientIP);
        if (ipOpt.isPresent()){
            ip = ipOpt.get();
            location = ipToRegionService.getRegionStrByIp(ip);
        }
        // 获取终端
        String clientCode = RequestContextHolder.getClientCode();

        var userAgent = UserAgentUtil.parse(RequestContextHolder.getHeader(WebHeaderCode.USER_AGENT));
        // 登录用户
        Optional<UserDetail> currentUser = SecurityUtil.getCurrentUser();
        // 设置方法名称
        var className = joinPoint.getTarget().getClass().getName();
        var methodName = joinPoint.getSignature().getName();

        for (OperateLog log : logs) {
            OperateLogParam operateLog = new OperateLogParam()
                    .setTitle(log.title())
                    .setOperateId(currentUser.map(UserDetail::getId).orElse(DesensitizedUtil.userId()))
                    .setAccount(currentUser.map(UserDetail::getAccount).orElse("未知"))
                    .setBusinessType(log.businessType().name().toLowerCase(Locale.ROOT))
                    .setOperateUrl(RequestContextHolder.getRequestUri())
                    .setMethod(className + "#" + methodName)
                    .setRequestMethod(RequestContextHolder.getMethod())
                    .setClient(clientCode)
                    .setOs(userAgent.getOs().getName())
                    .setBrowser(userAgent.getBrowser().getName() + " " + userAgent.getVersion())
                    .setSuccess(true)
                    .setOperateIp(ip)
                    .setOperateLocation(location)
                    .setOperateTime(LocalDateTime.now())
                    // 注解配置字段
                    .setSaveParam(log.saveParam())
                    .setSaverReturn(log.saverReturn())
                    .setMaskParam(log.maskParam())
                    .setMaskReturn(log.maskReturn())
                    .setFullMaskKeys(log.fullMaskKeys())
                    .setPartialMaskRules(log.partialMaskRules())
                    .setPayloadMaxLength(log.payloadMaxLength());
            // 异常流
            if (Objects.nonNull(e)) {
                operateLog.setSuccess(false).setErrorMsg(e.getMessage());
            }

            // 参数
            if (log.saveParam() && Objects.nonNull(joinPoint.getArgs())) {
                Object[] args = joinPoint.getArgs();
                operateLog.setOperateParam(JacksonUtil.toJson(args));
            }

            // 返回值
            if (log.saverReturn() && Objects.nonNull(o)) {
                operateLog.setOperateReturn(JacksonUtil.toJson(o));
            }
            // 通过服务层写入缓冲队列
            operateLogService.add(operateLog);
        }
    }

    /// 获取注解
    private List<OperateLog> getMethodAnnotation(JoinPoint joinPoint) {
        List<OperateLog> operateLogs = Optional.ofNullable(AopUtil.getMethodAnnotation(joinPoint, OperateLogs.class))
                .map(OperateLogs::value)
                .map(ListUtil::of)
                .orElse(null);
        if (CollUtil.isEmpty(operateLogs)) {
            operateLogs = ListUtil.of(AopUtil.getMethodAnnotation(joinPoint, OperateLog.class));
        }
        return operateLogs;
    }

}
