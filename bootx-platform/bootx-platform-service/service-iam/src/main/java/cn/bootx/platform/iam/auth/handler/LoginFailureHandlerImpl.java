package cn.bootx.platform.iam.auth.handler;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.code.WebHeaderCode;
import cn.bootx.platform.starter.audit.log.service.ip2region.IpToRegionService;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.bootx.platform.starter.audit.log.service.log.LoginLogService;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.starter.auth.handler.LoginFailureHandler;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 登录失败
 *
 * @author xxm
 * @since 2021/8/13
 */
@Component
@RequiredArgsConstructor
public class LoginFailureHandlerImpl implements LoginFailureHandler {

    private final LoginLogService loginLogService;

    private final IpToRegionService ipToRegionService;

    @Override
    public void onLoginFailure(HttpServletRequest request, HttpServletResponse response, LoginFailureException e) {

        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(WebHeaderCode.USER_AGENT));
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
        LoginLogParam loginLog = new LoginLogParam().setAccount(e.getAccount())
                .setLogin(false)
                .setClient(client)
                .setLoginType(loginType)
                .setMsg(e.getMessage())
                .setIp(ip)
                .setLoginLocation(location)
                .setOs(userAgent.getOs().getName())
                .setBrowser(userAgent.getBrowser().getName() + " " + userAgent.getVersion())
                .setLoginTime(LocalDateTime.now());
        loginLogService.add(loginLog);
    }

}
