package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.capability.auth.exception.CaptchaErrorException;
import cn.daxpay.open.platform.capability.auth.exception.CaptchaRequiredException;
import cn.daxpay.open.platform.iam.result.captcha.CaptchaDataResult;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/// # 图形验证码服务
///
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String CAPTCHA_PREFIX = "captcha:";

    private static final int CAPTCHA_WIDTH = 120;

    private static final int CAPTCHA_HEIGHT = 40;

    private static final int CAPTCHA_LENGTH = 4;

    private static final Duration CAPTCHA_EXPIRE = Duration.ofMinutes(5);

    /// 生成图形验证码
    public CaptchaDataResult generateCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, CAPTCHA_LENGTH, 20);
        String captchaKey = UUID.fastUUID().toString(true);
        String code = captcha.getCode();
        stringRedisTemplate.opsForValue().set(CAPTCHA_PREFIX + captchaKey, code, CAPTCHA_EXPIRE);
        return new CaptchaDataResult()
                .setCaptchaKey(captchaKey)
                .setCaptchaData(captcha.getImageBase64Data());
    }

    /// 验证验证码
    ///
    /// @param captchaKey   验证码key
    /// @param captchaCode  验证码
    /// @param ignoreCase   是否忽略大小写
    /// @return 验证成功返回true，验证码不存在返回false
    public boolean validateCaptcha(String captchaKey, String captchaCode, boolean ignoreCase) {
        String key = CAPTCHA_PREFIX + captchaKey;
        String storedCode = stringRedisTemplate.opsForValue().get(key);
        if (storedCode == null) {
            return false;
        }
        stringRedisTemplate.delete(key);
        if (ignoreCase) {
            return storedCode.equalsIgnoreCase(captchaCode);
        }
        return storedCode.equals(captchaCode);
    }

    /// 验证验证码，验证失败抛出异常
    ///
    /// @param captchaKey   验证码key
    /// @param captchaCode  验证码
    /// @param ignoreCase   是否忽略大小写
    public void validateCaptchaOrThrow(String captchaKey, String captchaCode, boolean ignoreCase) {
        if (!validateCaptcha(captchaKey, captchaCode, ignoreCase)) {
            throw new CaptchaErrorException();
        }
    }

    /// 检查是否需要验证码，如果需要则抛出异常
    ///
    /// @param errorCount 当前错误次数
    /// @param triggerAttempts 触发验证码的错误次数阈值
    public void checkCaptchaRequired(int errorCount, int triggerAttempts) {
        if (errorCount >= triggerAttempts) {
            String captchaKey = UUID.fastUUID().toString(true);
            throw new CaptchaRequiredException(captchaKey);
        }
    }

    /// 检查验证码，如果需要验证码但未提供则抛出异常，如果提供了则验证
    ///
    /// @param errorCount 当前错误次数
    /// @param triggerAttempts 触发验证码的错误次数阈值
    /// @param captchaKey 验证码key（可为空）
    /// @param captchaCode 验证码（可为空）
    public void checkOrValidateCaptcha(int errorCount, int triggerAttempts, String captchaKey, String captchaCode) {
        if (errorCount < triggerAttempts) {
            return;
        }
        if (captchaKey == null || captchaKey.isBlank() || captchaCode == null || captchaCode.isBlank()) {
            String newCaptchaKey = UUID.fastUUID().toString(true);
            throw new CaptchaRequiredException(newCaptchaKey);
        }
        validateCaptchaOrThrow(captchaKey, captchaCode, true);
    }
}


