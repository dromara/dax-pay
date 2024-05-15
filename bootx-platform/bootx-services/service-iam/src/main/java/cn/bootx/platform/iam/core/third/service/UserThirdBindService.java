package cn.bootx.platform.iam.core.third.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.starter.auth.authentication.OpenIdAuthentication;
import cn.bootx.platform.starter.auth.code.AuthLoginTypeCode;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.iam.core.third.dao.UserThirdInfoManager;
import cn.bootx.platform.iam.core.third.dao.UserThirdManager;
import cn.bootx.platform.iam.core.third.entity.UserThird;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 三方账号绑定
 *
 * @author xxm
 * @since 2021/8/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserThirdBindService {

    private final UserThirdManager userThirdManager;

    private final UserThirdInfoManager userThirdInfoManager;

    private final List<OpenIdAuthentication> openIdAuthentications;

    /**
     * 绑定账号
     */
    @Transactional(rollbackFor = Exception.class)
    public void bind(String authCode, String loginType, String state) {
        OpenIdAuthentication openIdAuthentication = this.getOpenIdAuthentication(loginType);
        openIdAuthentication.bindUser(authCode, state);
    }

    /**
     * 解除绑定
     */
    @Transactional
    public void unbind(String loginType) {
        Long userId = SecurityUtil.getUserId();
        if (!userThirdManager.existsByUserId(userId)) {
            throw new DataNotExistException("用户绑定关系不存");
        }
        userThirdInfoManager.deleteByUserAndClientCode(userId, loginType);
        switch (loginType) {
            case AuthLoginTypeCode.WE_CHAT: {
                userThirdManager.unbind(userId, UserThird::getWeChatId);
                break;
            }
            case AuthLoginTypeCode.WE_COM: {
                userThirdManager.unbind(userId, UserThird::getWeComId);
                break;
            }
            case AuthLoginTypeCode.DING_TALK: {
                userThirdManager.unbind(userId, UserThird::getDingTalkId);
                break;
            }
            default: {
                throw new BizException("未被支持的三方登录方式");
            }
        }
    }

    /**
     * 获取 openId登录认证器
     */
    private OpenIdAuthentication getOpenIdAuthentication(String clientCode) {
        return openIdAuthentications.stream()
                .filter(o -> o.adaptation(clientCode))
                .findFirst()
                .orElseThrow(() -> new LoginFailureException("未找到对应的终端认证器"));
    }

}
