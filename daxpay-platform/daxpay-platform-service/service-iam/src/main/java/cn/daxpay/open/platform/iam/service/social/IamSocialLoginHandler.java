package cn.daxpay.open.platform.iam.service.social;

import cn.daxpay.open.platform.capability.social.login.SocialLoginHandler;
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

/// # 社交登录处理器实现
///
/// 在 LOGIN 场景下, 通过绑定关系确认用户身份后, 委托本类完成 Sa-Token 登录签发与 session 填充
///
@Slf4j
@Service
@RequiredArgsConstructor
public class IamSocialLoginHandler implements SocialLoginHandler {

    private final UserQueryService userQueryService;

    @Override
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
