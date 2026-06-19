package cn.daxpay.open.platform.capability.wechat.token.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/// # 微信Token管理服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final LockTemplate lockTemplate;

    /// Token缓存Key前缀
    private static final String TOKEN_CACHE_KEY = "wechat:token:";
    /// Token过期时间Key前缀
    private static final String TOKEN_EXPIRE_KEY = "wechat:token:expire:";
    /// Token刷新锁Key前缀
    private static final String TOKEN_LOCK_KEY = "wechat:token:lock:";
    /// Token默认过期时间（秒），微信AccessToken有效期为7200秒，提前5分钟刷新
    private static final long TOKEN_EXPIRE_TIME = 7200 - 300;

    /// 获取AccessToken（自动刷新，支持多副本部署）
    /// @param wxAppId 微信AppId
    /// @param appSecret AppSecret
    /// @return AccessToken
    public String getAccessToken(String wxAppId, String appSecret) {
        String cacheKey = TOKEN_CACHE_KEY + wxAppId;
        
        // 从缓存获取Token
        String token = redisTemplate.opsForValue().get(cacheKey);
        
        // 如果Token存在且未即将过期，直接返回
        if (StrUtil.isNotBlank(token) && !isTokenExpiringSoon(wxAppId)) {
            log.debug("从缓存获取AccessToken成功，wxAppId: {}", wxAppId);
            return token;
        }
        
        // Token不存在或即将过期，需要刷新
        return refreshAccessToken(wxAppId, appSecret);
    }

    /// 手动刷新AccessToken（使用lock4j分布式锁）
    /// @param appId AppId
    /// @param appSecret AppSecret
    /// @return 新的AccessToken
    public String refreshAccessToken(String wxAppId, String appSecret) {
        String lockKey = TOKEN_LOCK_KEY + wxAppId;
        String cacheKey = TOKEN_CACHE_KEY + wxAppId;
        String expireKey = TOKEN_EXPIRE_KEY + wxAppId;
        
        // 使用lock4j获取分布式锁
        LockInfo lockInfo = lockTemplate.lock(lockKey, 30000L, 5000L);
        if (lockInfo == null) {
            log.warn("获取Token刷新锁失败，wxAppId: {}", wxAppId);
            // 获取锁失败，尝试从缓存获取
            String token = redisTemplate.opsForValue().get(cacheKey);
            if (StrUtil.isNotBlank(token)) {
                return token;
            }
            throw new RuntimeException("获取AccessToken失败：无法获取分布式锁");
        }
        
        try {
            // 双重检查，避免重复刷新
            String token = redisTemplate.opsForValue().get(cacheKey);
            if (StrUtil.isNotBlank(token) && !isTokenExpiringSoon(wxAppId)) {
                log.debug("其他实例已刷新Token，直接返回，wxAppId: {}", wxAppId);
                return token;
            }
            
            // 调用微信API获取新Token
            log.info("开始刷新AccessToken，wxAppId: {}", wxAppId);
            WxMpService wxMpService = createWxMpService(wxAppId, appSecret);
            String newToken;
            try {
                newToken = wxMpService.getAccessToken();
            } catch (WxErrorException e) {
                log.error("调用微信API获取AccessToken失败，wxAppId: {}, 错误: {}", wxAppId, e.getMessage());
                throw new RuntimeException("刷新AccessToken失败: " + e.getMessage(), e);
            }
            
            if (StrUtil.isBlank(newToken)) {
                throw new RuntimeException("刷新AccessToken失败：返回的Token为空");
            }
            
            // 缓存新Token
            redisTemplate.opsForValue().set(cacheKey, newToken, TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
            // 记录过期时间
            long expireTime = System.currentTimeMillis() + TOKEN_EXPIRE_TIME * 1000;
            redisTemplate.opsForValue().set(expireKey, String.valueOf(expireTime), TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
            
            log.info("刷新AccessToken成功，wxAppId: {}", wxAppId);
            return newToken;
            
        } finally {
            // 释放锁
            lockTemplate.releaseLock(lockInfo);
        }
    }

    /// 检查Token是否即将过期（过期前5分钟）
    /// @param wxAppId 微信AppId
    /// @return 是否即将过期
    public boolean isTokenExpiringSoon(String wxAppId) {
        String expireKey = TOKEN_EXPIRE_KEY + wxAppId;
        String expireTimeStr = redisTemplate.opsForValue().get(expireKey);
        
        if (StrUtil.isBlank(expireTimeStr)) {
            return true;
        }
        
        try {
            long expireTime = Long.parseLong(expireTimeStr);
            long currentTime = System.currentTimeMillis();
            // 提前5分钟判断为即将过期
            return (expireTime - currentTime) < 300000;
        } catch (NumberFormatException e) {
            log.warn("解析Token过期时间失败，wxAppId: {}", wxAppId);
            return true;
        }
    }

    /// 创建微信公众号Service
    private WxMpService createWxMpService(String wxAppId, String appSecret) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(wxAppId);
        config.setSecret(appSecret);
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }
}

