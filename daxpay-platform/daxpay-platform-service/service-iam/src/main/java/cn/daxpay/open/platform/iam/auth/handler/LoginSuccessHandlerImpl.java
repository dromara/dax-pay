package cn.daxpay.open.platform.iam.auth.handler;

import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogParam;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpToRegionService;
import cn.daxpay.open.platform.capability.audit.log.service.log.LoginLogService;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.handler.LoginSuccessHandler;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/// # 登录成功处理
///
@Component
@RequiredArgsConstructor
public class LoginSuccessHandlerImpl implements LoginSuccessHandler {

    private final LoginLogService loginLogService;

    private final IpToRegionService ipToRegionService;

    private final LoginRetryService loginRetryService;

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                               AuthInfoResult authInfoResult) {
        loginRetryService.onLoginSuccess(authInfoResult.getUserDetail().getId());
        var userAgent = UserAgentUtil.parse(request.getHeader(WebHeaderCode.USER_AGENT));
        // ip信息
        var ip = "未知";
        var location = "未知";
        Optional<String> ipOpt = Optional.ofNullable(WebServletUtil.getRequest()).map(JakartaServletUtil::getClientIP);
        if (ipOpt.isPresent()){
            ip = ipOpt.get();
            location = ipToRegionService.getRegionStrByIp(ip);
        }
        var loginLog = new LoginLogParam().setLogin(true)
                .setUserId(authInfoResult.getUserDetail().getId())
                .setClient(authInfoResult.getClient())
                .setLoginType(authInfoResult.getLoginType())
                .setAccount(authInfoResult.getUserDetail().getAccount())
                .setIp(ip)
                .setLoginLocation(location)
                .setOs(userAgent.getOs().getName())
                .setBrowser(userAgent.getBrowser().getName() + " " + userAgent.getVersion())
                .setLoginTime(OffsetDateTime.now(ZoneOffset.UTC));
        loginLogService.add(loginLog);
    }

}
