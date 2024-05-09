package cn.bootx.platform.starter.dingtalk.core.base.service;

import cn.bootx.platform.starter.dingtalk.configuration.DingTalkProperties;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.spring.exception.RetryableException;
import cn.bootx.platform.starter.dingtalk.core.base.result.AccessTokenResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static cn.bootx.platform.starter.dingtalk.code.DingTalkCode.*;

/**
 * 钉钉访问凭证
 *
 * @author xxm
 * @since 2022/4/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DingAccessService {

    private final DingTalkProperties dingTalkProperties;

    private long expiresTime;

    private String accessToken;

    /**
     * 企业内部应用的access_token
     */
    public String getAccessToken() {
        if (System.currentTimeMillis() < expiresTime) {
            return accessToken;
        }
        Lock lock = new ReentrantLock();
        boolean locked = false;
        try {
            do {
                locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (System.currentTimeMillis() < expiresTime) {
                    return accessToken;
                }
            }
            while (!locked);
            SpringUtil.getBean(getClass())
                .getAppAccessToken(dingTalkProperties.getAppKey(), dingTalkProperties.getAppSecret());
        }
        catch (InterruptedException e) {
            throw new BizException("获取钉钉应用AccessToken失败");
        }
        finally {
            if (locked) {
                lock.unlock();
            }
        }
        return accessToken;
    }

    /**
     * 企业内部应用的access_token
     */
    @Retryable(value = RetryableException.class)
    public void getAppAccessToken(String appKey, String appSecret) {
        // 获取accessToken
        Map<String, String> map = new HashMap<>();
        map.put(APP_KEY, appKey);
        map.put(APP_SECRET, appSecret);
        try {
            String responseBody = HttpUtil.createGet(StrUtil.format(APP_ACCESS_TOKEN_URL, map)).execute().body();
            AccessTokenResult<?> dingTalkResult = JacksonUtil.toBean(responseBody, AccessTokenResult.class);
            if (StrUtil.isBlank(dingTalkResult.getAccessToken())) {
                throw new RetryableException();
            }
            this.accessToken = dingTalkResult.getAccessToken();
            // 设置超时时间
            this.expiresTime = System.currentTimeMillis() + (dingTalkResult.getExpiresIn() - 200) * 1000L;
        }
        catch (HttpException e) {
            throw new RetryableException();
        }
    }

}
