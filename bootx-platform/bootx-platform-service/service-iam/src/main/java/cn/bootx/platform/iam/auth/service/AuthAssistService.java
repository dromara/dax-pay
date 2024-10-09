package cn.bootx.platform.iam.auth.service;

import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.iam.code.UserStatusEnum;
import cn.bootx.platform.iam.dao.user.UserInfoManager;
import cn.bootx.platform.iam.entity.user.UserInfo;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.iam.param.auth.LoginContentParam;
import cn.bootx.platform.iam.result.auth.LoginContentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 认证支撑服务
 *
 * @author xxm
 * @since 2021/9/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthAssistService {

    private final UserInfoManager userInfoManager;

    private final String smsCaptchaType = "login";

    /**
     * 发送短信验证码
     */
    public void sendSmsCaptcha(String phone) {
        // 判断用户是否存在
        UserInfo userInfo = userInfoManager.findByPhone(phone).orElseThrow(UserInfoNotExistsException::new);
        if (!Objects.equals(userInfo.getStatus(), UserStatusEnum.NORMAL.getCode())) {
            throw new BizException("用户状态异常");
        }
    }

    /**
     * 登录⻚上下⽂信息
     */
    public LoginContentResult getLoginContent(LoginContentParam param){
        return null;
    }

    /**
     * 二次校验信息
     */
    public void getSecondCheck(){

    }

}
