package cn.daxpay.open.platform.iam.auth.handler;

import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.dao.user.UserExpandInfoManager;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogParam;
import cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpToRegionService;
import cn.daxpay.open.platform.capability.audit.log.service.log.LoginLogService;
import cn.daxpay.open.platform.capability.audit.log.util.UserAgentDisplayUtil;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.handler.LoginSuccessHandler;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/// # 登录成功处理
///
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandlerImpl implements LoginSuccessHandler {

    private final LoginLogService loginLogService;

    private final IpToRegionService ipToRegionService;

    private final LoginRetryService loginRetryService;

    private final UserExpandInfoManager userExpandInfoManager;

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                               AuthInfoResult authInfoResult) {
        var userId = authInfoResult.getUserDetail().getId();
        loginRetryService.onLoginSuccess(userId);
        var userAgent = UserAgentUtil.parse(request.getHeader(WebHeaderCode.USER_AGENT));
        // ip信息
        var ip = "未知";
        var location = "未知";
        String clientIp = null;
        Optional<String> ipOpt = Optional.ofNullable(WebServletUtil.getRequest()).map(JakartaServletUtil::getClientIP);
        if (ipOpt.isPresent()){
            clientIp = ipOpt.get();
            ip = clientIp;
            location = ipToRegionService.getRegionStrByIp(ip);
        }
        var loginTime = OffsetDateTime.now(ZoneOffset.UTC);
        var loginLog = new LoginLogParam().setLogin(true)
                .setUserId(userId)
                .setClient(authInfoResult.getClient())
                .setTerminal(authInfoResult.getTerminal())
                .setLoginType(authInfoResult.getLoginType())
                .setAccount(authInfoResult.getUserDetail().getAccount())
                .setIp(ip)
                .setLoginLocation(location)
                .setOs(UserAgentDisplayUtil.os(userAgent))
                .setBrowser(UserAgentDisplayUtil.browser(userAgent))
                .setLoginTime(loginTime);
        loginLogService.add(loginLog);
        // 回写登录轨迹: 用户详情的「最后登录时间/IP/登录次数」直接读扩展信息表, 此前无写入路径导致恒为空
        try {
            userExpandInfoManager.recordLoginTrace(userId, clientIp, loginTime);
        } catch (Exception e) {
            // 轨迹统计属旁路, 失败不影响登录本身
            log.warn("记录用户登录轨迹失败: userId={}", userId, e);
        }
    }

}
