package cn.daxpay.open.platform.iam.service.social;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.entity.UserDetail;
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
/// 在 LOGIN 场景下, 通过绑定关系确认用户身份后, 完成本地 Sa-Token 登录签发与 session 填充.
/// 被 SocialEndpoint 直接注入使用(同模块, 无需 SPI 抽象).
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IamSocialLoginHandler {

    private final UserQueryService userQueryService;

    /// 使用已确认身份的 userId 完成登录(含 session 填充), 返回 token
    /// @param userId 本地用户ID
    /// @param clientCode 终端编码
    public String login(Long userId, String clientCode, HttpServletRequest request, HttpServletResponse response) {
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
        return StpUtil.getTokenValue();
    }
}
