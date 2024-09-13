package cn.bootx.platform.baseapi.core.captcha.service;

import cn.bootx.platform.baseapi.dto.captcha.CaptchaDataResult;
import cn.bootx.platform.common.redis.RedisClient;
import cn.hutool.core.util.RandomUtil;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 验证码服务
 *
 * @author xxm
 * @since 2021/8/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    /** redis key前缀 */
    private final String imgCaptchaPrefix = "login:captcha:img";

    /** 手机验证码前缀 */
    private final String smsCaptchaPrefix = "phone:captcha:";

    /** 邮箱验证码前缀 */
    private final String emailCaptchaPrefix = "email:captcha:";

    private final RedisClient redisClient;

    /**
     * 获取图片验证码
     */
    public CaptchaDataResult imgCaptcha() {
        String key = RandomUtil.randomString(16);
        // 几位数运算，默认是两位
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(105, 35);
        // 获取运算的结果
        String text = captcha.text();
        log.info("获取图片验证码: {}", text);
        redisClient.setWithTimeout(imgCaptchaPrefix + key, text, 5 * 60 * 1000);
        return new CaptchaDataResult().setCaptchaKey(key).setCaptchaData(captcha.toBase64());
    }

    /**
     * 校验图片验证码
     * @param key 验证码Key
     */
    public boolean validateImgCaptcha(String key, String captcha) {
        // 比较验证码是否正确
        String captchaByRedis = redisClient.get(imgCaptchaPrefix + key);
        return Objects.equals(captcha, captchaByRedis);
    }

    /**
     * 将图片验证码设置为失效
     * @param key 验证码Key
     */
    public void deleteImgCaptcha(String key) {
        redisClient.deleteKey(imgCaptchaPrefix + key);
    }

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @param timeoutSec 超时时间
     * @param type 业务类型, 用来区分不同业务的短信验证码
     * @return 验证码
     */
    public int sendSmsCaptcha(String phone, long timeoutSec, String type) {
        int captcha = RandomUtil.randomInt(100000, 1000000);
        log.info("短信验证码: {}", captcha);
        redisClient.setWithTimeout(getSmsCaptchaPrefix(type) + phone, String.valueOf(captcha), timeoutSec * 1000);
        return captcha;
    }

    /**
     * 验证手机发送的验证码是否还在有效时间内
     * @param phone 手机号
     * @param type 业务类型, 用来区分不同业务的短信验证码
     */
    public boolean existsSmsCaptcha(String phone, String type) {
        return redisClient.exists(getSmsCaptchaPrefix(type) + phone);
    }

    /**
     * 校验手机验证码
     */
    public boolean validateSmsCaptcha(String phone, String captcha, String type) {
        // 比较验证码是否正确
        String captchaByRedis = redisClient.get(getSmsCaptchaPrefix(type) + phone);
        return Objects.equals(captcha, captchaByRedis);
    }

    /**
     * 将手机验证码验证码设置为失效
     */
    public void deleteSmsCaptcha(String phone, String type) {
        redisClient.deleteKey(getSmsCaptchaPrefix(type) + phone);
    }

    /**
     * 获取手机验证码前缀
     */
    private String getSmsCaptchaPrefix(String type) {
        return smsCaptchaPrefix + type + ":";
    }

    /**
     * 发送邮箱验证码
     */
    public int sendEmailCaptcha(String email, long timeoutSec, String type) {
        int captcha = RandomUtil.randomInt(100000, 1000000);
        log.info("邮箱验证码: {}", captcha);
        redisClient.setWithTimeout(getEmailCaptchaPrefix(type) + email, String.valueOf(captcha), timeoutSec * 1000);
        return captcha;
    }

    /**
     * 邮箱发送的验证码是否还有效
     */
    public boolean existsEmailCaptcha(String email, String type) {
        return redisClient.exists(getEmailCaptchaPrefix(type) + email);
    }

    /**
     * 校验邮箱验证码
     */
    public boolean validateEmailCaptcha(String email, String captcha, String type) {
        // 比较验证码是否正确
        String captchaByRedis = redisClient.get(getEmailCaptchaPrefix(type) + email);
        return Objects.equals(captcha, captchaByRedis);
    }

    /**
     * 失效邮箱验证码
     */
    public void deleteEmailCaptcha(String email, String type) {
        redisClient.deleteKey(getEmailCaptchaPrefix(type) + email);
    }

    /**
     * 获取邮箱验证码前缀
     */
    private String getEmailCaptchaPrefix(String type) {
        return emailCaptchaPrefix + type + ":";
    }

}
