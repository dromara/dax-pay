package cn.daxpay.open.platform.iam.service.social;

import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.auth.service.TokenService;
import cn.daxpay.open.platform.iam.result.user.UserInfoResult;
import cn.daxpay.open.platform.iam.service.user.UserQueryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 社交登录处理器
///
/// 在 LOGIN 场景下, 绑定关系与状态检查已通过后, 走 [TokenService#completeAuthenticatedLogin]
/// 统一做双因素检查、创建登录态(超时/并发)与成功回调。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IamSocialLoginHandler {

    private final UserQueryService userQueryService;

    private final TokenService tokenService;

    private final PlatformStarterProperties platformStarterProperties;

    /// 使用已确认身份的 userId 完成登录, 返回 token
    ///
    /// @param userId 本地用户ID
    /// @param clientCode 终端编码
    /// @param source 三方平台编码(作为 loginType 记录到登录日志, 如 gitee/feishu)
    public String login(Long userId, String clientCode, String source,
                        HttpServletRequest request, HttpServletResponse response) {
        UserInfoResult userInfo = userQueryService.findById(userId);
        UserDetail userDetail = userInfo.toUserDetail();
        AuthInfoResult authInfoResult = new AuthInfoResult()
                .setId(userId)
                .setClient(clientCode)
                .setLoginType(source)
                .setUserDetail(userDetail);
        LoginAuthContext context = new LoginAuthContext()
                .setRequest(request)
                .setResponse(response)
                .setClientCode(clientCode)
                .setAuthLoginType(source)
                .setAuthProperties(platformStarterProperties.getAuth())
                .setUserDetail(userDetail);
        // 走统一登录收尾(含双因素检查)
        return tokenService.completeAuthenticatedLogin(authInfoResult, context);
    }
}
