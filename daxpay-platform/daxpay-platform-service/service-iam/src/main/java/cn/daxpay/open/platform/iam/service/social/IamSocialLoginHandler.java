package cn.daxpay.open.platform.iam.service.social;

import java.util.List;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.handler.LoginSuccessHandler;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 社交登录处理器
///
/// 在 LOGIN 场景下, 通过绑定关系确认用户身份后, 完成本地 Sa-Token 登录签发与 session 填充,
/// 并触发 [LoginSuccessHandler] 链以记录登录日志(IP/UA/地域/登录方式等).
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IamSocialLoginHandler {

    private final UserQueryService userQueryService;

    private final List<LoginSuccessHandler> loginSuccessHandlers;

    /// 使用已确认身份的 userId 完成登录(含 session 填充), 返回 token
    /// @param userId 本地用户ID
    /// @param clientCode 终端编码
    /// @param source 三方平台编码(作为 loginType 记录到登录日志, 如 gitee/feishu)
    public String login(Long userId, String clientCode, String source,
                        HttpServletRequest request, HttpServletResponse response) {
        // 加载用户信息并构建会话对象
        UserInfoResult userInfo = userQueryService.findById(userId);
        UserDetail userDetail = userInfo.toUserDetail();
        // 签发 Sa-Token
        var saLoginModel = new SaLoginParameter()
            .setDeviceType(clientCode)
            .setIsLastingCookie(true);
        StpUtil.login(userId, saLoginModel);
        // 填充 session
        SaSession session = StpUtil.getSession();
        session.set(CommonCode.USER, userDetail);
        // 构建认证结果, loginType 使用三方平台编码
        AuthInfoResult authInfoResult = new AuthInfoResult()
            .setId(userId)
            .setClient(clientCode)
            .setLoginType(source)
            .setUserDetail(userDetail);
        // 触发登录成功处理器链(记录登录日志等)
        for (LoginSuccessHandler handler : loginSuccessHandlers) {
            try {
                handler.onLoginSuccess(request, response, authInfoResult);
            } catch (Exception e) {
                log.error("社交登录成功处理出现异常: {}", e.getMessage(), e);
            }
        }
        return StpUtil.getTokenValue();
    }
}
