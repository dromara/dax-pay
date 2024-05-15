package cn.bootx.platform.iam.core.user.service;

import cn.bootx.platform.common.websocket.entity.WsRes;
import cn.bootx.platform.common.websocket.service.UserWsNoticeService;
import cn.bootx.platform.iam.exception.user.UserInfoNotExistsException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.baseapi.core.captcha.service.CaptchaService;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户操作支撑服务
 *
 * @author xxm
 * @since 2022/6/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAssistService {

    private final UserInfoManager userInfoManager;

    private final CaptchaService captchaService;

    private final UserWsNoticeService userWsNoticeService;

    private final String changePhoneType = "change:phone";

    private final String changeEmailType = "change:email";

    private final String forgetPhoneType = "forget:phone";

    private final String forgetEmailType = "forget:email";

    /**
     * 给当前用户发送更改手机号验证码
     */
    public void sendCurrentPhoneChangeCaptcha() {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        this.sendPhoneChangeCaptcha(userInfo.getPhone());
    }

    /**
     * 验证当前用户发送更改手机号验证码
     */
    public boolean validateCurrentPhoneChangeCaptcha(String captcha) {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        return this.validatePhoneChangeCaptcha(userInfo.getPhone(), captcha);
    }

    /**
     * 发送更改手机号验证码
     */
    public void sendPhoneChangeCaptcha(String phone) {
        int captcha = captchaService.sendSmsCaptcha(phone, 15 * 60, changePhoneType);
        userWsNoticeService.sendMessageByUser(WsRes.notificationInfo("(开发模式)短信验证码 : " + captcha),
                SecurityUtil.getUserIdOrDefaultId());
    }

    /**
     * 验证改手机验证码
     */
    public boolean validatePhoneChangeCaptcha(String phone, String captcha) {
        return captchaService.validateSmsCaptcha(phone, captcha, changePhoneType);
    }

    /**
     * 删除手机验证码
     */
    public void deletePhoneChangeCaptcha(String phone) {
        captchaService.deleteSmsCaptcha(phone, changePhoneType);
    }

    /**
     * 给当前用户发送更改邮箱验证码
     */
    public void sendCurrentEmailChangeCaptcha() {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        this.sendEmailChangeCaptcha(userInfo.getEmail());
    }

    /**
     * 验证当前用户发送更改邮箱验证码
     */
    public boolean validateCurrentChangeEmailCaptcha(String captcha) {
        UserInfo userInfo = userInfoManager.findById(SecurityUtil.getUserId())
            .orElseThrow(UserInfoNotExistsException::new);
        return this.validateEmailChangeCaptcha(userInfo.getEmail(), captcha);
    }

    /**
     * 发送更改邮箱验证码
     */
    public void sendEmailChangeCaptcha(String email) {
        int captcha = captchaService.sendEmailCaptcha(email, 15 * 60, changeEmailType);
        userWsNoticeService.sendMessageByUser(WsRes.notificationInfo("(开发模式)邮件验证码 : " + captcha),
                SecurityUtil.getUserIdOrDefaultId());
    }

    /**
     * 验证改邮箱验证码
     */
    public boolean validateEmailChangeCaptcha(String email, String captcha) {
        return captchaService.validateEmailCaptcha(email, captcha, changeEmailType);
    }

    /**
     * 删除邮箱验证码
     */
    public void deleteEmailChangeCaptcha(String email) {
        captchaService.deleteEmailCaptcha(email, changeEmailType);
    }

    /**
     * 发送忘记密码手机验证码
     */
    public void sendPhoneForgetCaptcha(String phone) {
        UserInfo userInfo = userInfoManager.findByPhone(phone).orElseThrow(UserInfoNotExistsException::new);
        int captcha = captchaService.sendSmsCaptcha(userInfo.getPhone(), 15 * 60, forgetPhoneType);

    }

    /**
     * 验证忘记密码手机验证码
     */
    public boolean validatePhoneForgetCaptcha(String phone, String captcha) {
        return captchaService.validateSmsCaptcha(phone, captcha, forgetPhoneType);
    }

    /**
     * 删除手机验证码
     */
    public void deletePhoneForgetCaptcha(String phone) {
        captchaService.deleteSmsCaptcha(phone, forgetPhoneType);
    }

    /**
     * 发送忘记密码邮件验证码
     */
    public void sendEmailForgetCaptcha(String email) {
        UserInfo userInfo = userInfoManager.findByEmail(email).orElseThrow(UserInfoNotExistsException::new);
        int captcha = captchaService.sendEmailCaptcha(userInfo.getEmail(), 15 * 60, forgetEmailType);
        log.info("忘记密码邮件验证码: {}", captcha);
    }

    /**
     * 验证忘记密码邮箱验证码
     */
    public boolean validateEmailForgetCaptcha(String email, String captcha) {
        return captchaService.validateEmailCaptcha(email, captcha, forgetEmailType);
    }

    /**
     * 删除邮箱验证码
     */
    public void deleteEmailForgetCaptcha(String email) {
        captchaService.deleteEmailCaptcha(email, forgetEmailType);
    }

}
