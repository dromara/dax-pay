package cn.daxpay.open.platform.iam.auth.handler;

import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogParam;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpToRegionService;
import cn.daxpay.open.platform.capability.audit.log.service.log.LoginLogService;
import cn.daxpay.open.platform.capability.audit.log.util.UserAgentDisplayUtil;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.capability.auth.handler.LoginFailureHandler;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/// # 登录失败
///
@Component
@RequiredArgsConstructor
public class LoginFailureHandlerImpl implements LoginFailureHandler {

    private final LoginLogService loginLogService;

    private final IpToRegionService ipToRegionService;

    private final LoginRetryService loginRetryService;

    @Override
    public void onLoginFailure(HttpServletRequest request, HttpServletResponse response, LoginFailureException e) {
        loginRetryService.onLoginFailure(e.getUserId(), e.getAccount());

        var userAgent = UserAgentUtil.parse(request.getHeader(WebHeaderCode.USER_AGENT));
        // ip信息
        String ip = "未知";
        String location = "未知";
        Optional<String> ipOpt = Optional.ofNullable(WebServletUtil.getRequest()).map(JakartaServletUtil::getClientIP);
        if (ipOpt.isPresent()){
            ip = ipOpt.get();
            location = ipToRegionService.getRegionStrByIp(ip);
        }

        String loginType = SecurityUtil.getLoginType(request);
        String client = SecurityUtil.getClient(request);
        // 请求终端白名单归一, 与登录成功路径口径一致
        String terminal = WebHeaderCode.normalizeTerminal(request.getHeader(WebHeaderCode.X_TERMINAL));
        LoginLogParam loginLog = new LoginLogParam().setAccount(e.getAccount())
                .setLogin(false)
                .setClient(client)
                .setTerminal(terminal)
                .setLoginType(loginType)
                .setMsg(e.getMessage())
                .setIp(ip)
                .setLoginLocation(location)
                .setOs(UserAgentDisplayUtil.os(userAgent))
                .setBrowser(UserAgentDisplayUtil.browser(userAgent))
                .setLoginTime(OffsetDateTime.now(ZoneOffset.UTC));
        loginLogService.add(loginLog);
    }

}
