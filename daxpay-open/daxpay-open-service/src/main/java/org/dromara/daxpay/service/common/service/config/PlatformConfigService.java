package org.dromara.daxpay.service.common.service.config;

import org.dromara.daxpay.core.exception.ConfigNotExistException;
import org.dromara.daxpay.service.common.dao.config.PlatformBasicConfigManager;
import org.dromara.daxpay.service.common.dao.config.PlatformUrlConfigManager;
import org.dromara.daxpay.service.common.dao.config.PlatformWebsiteConfigManage;
import org.dromara.daxpay.service.common.entity.config.PlatformBasicConfig;
import org.dromara.daxpay.service.common.entity.config.PlatformUrlConfig;
import org.dromara.daxpay.service.common.entity.config.PlatformWebsiteConfig;
import org.dromara.daxpay.service.common.param.config.PlatformBasicConfigParam;
import org.dromara.daxpay.service.common.param.config.PlatformUrlConfigParam;
import org.dromara.daxpay.service.common.param.config.PlatformWebsiteConfigPram;
import org.dromara.daxpay.service.common.result.config.PlatformBasicConfigResult;
import org.dromara.daxpay.service.common.result.config.PlatformUrlConfigResult;
import org.dromara.daxpay.service.common.result.config.PlatformWebsiteConfigResult;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 平台配置
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformConfigService {
    private final PlatformBasicConfigManager basicConfigManager;
    private final PlatformUrlConfigManager urlConfigManager;
    private final PlatformWebsiteConfigManage websiteConfigManager;

    /**
     * 获取基础配置
     */
    @Cacheable(value = "cache:config:platform:basic")
    public PlatformBasicConfig getBasicConfig(){
        return this.getOrCreateBasicConfig();
    }

    /**
     * 获取基础配置
     */
    public PlatformBasicConfigResult findBasicConfig(){
        return this.getOrCreateBasicConfig().toResult();
    }

    /**
     * 获取基础配置, 如果不存在进行创建
     */
    public PlatformBasicConfig getOrCreateBasicConfig(){
        var platformConfig = basicConfigManager.findById(1L);
        if (platformConfig.isEmpty()){
            var config = new PlatformBasicConfig();
            config.setId(1L);
            basicConfigManager.save(config);
            return config;
        }
        return platformConfig.get();
    }

    /**
     * 更新基础配置
     */
    @CacheEvict(value = "cache:config:platform:basic", allEntries = true)
    public void updateBasic(PlatformBasicConfigParam param){
        var config = basicConfigManager.findById(1L)
                .orElseThrow(() -> new ConfigNotExistException("平台配置不存在"));
            BeanUtil.copyProperties(param, config);
            basicConfigManager.updateById(config);
    }

    /**
     * 获取平台访问地址配置
     */
    @Cacheable(value = "cache:config:platform:url")
    public PlatformUrlConfig getUrlConfig(){
        return this.getOrCreateUrlConfig();
    }

    /**
     * 获取平台访问地址配置
     */
    public PlatformUrlConfigResult findUrlConfig(){
        return this.getOrCreateUrlConfig().toResult();
    }

    /**
     * 获取平台访问地址配置, 如果不存在进行创建
     */
    public PlatformUrlConfig getOrCreateUrlConfig(){
        var urlConfig = urlConfigManager.findById(1L);
        if (urlConfig.isEmpty()){
            var config = new PlatformUrlConfig();
            config.setId(1L);
            urlConfigManager.save(config);
            return config;
        }
        return urlConfig.get();
    }

    /**
     * 更新平台访问地址配置
     */
    @CacheEvict(value = "cache:config:platform:url", allEntries = true)
    public void updateUrl(PlatformUrlConfigParam param){
        var config = urlConfigManager.findById(1L)
                .orElseThrow(() -> new ConfigNotExistException("平台配置不存在"));
        BeanUtil.copyProperties(param, config);
        urlConfigManager.updateById(config);
    }

    /**
     * 获取平台站点配置
     */
    @Cacheable(value = "cache:config:platform:website")
    public PlatformWebsiteConfig getWebsiteConfig(){
        return this.getOrCreateWebsiteConfig();
    }

    /**
     * 获取平台站点配置
     */
    public PlatformWebsiteConfigResult findWebsiteConfig(){
        return this.getOrCreateWebsiteConfig().toResult();
    }

    /**
     * 获取平台站点配置, 如果不存在进行创建
     */
    public PlatformWebsiteConfig getOrCreateWebsiteConfig(){
        var WebsiteConfig = websiteConfigManager.findById(1L);
        if (WebsiteConfig.isEmpty()){
            var config = new PlatformWebsiteConfig();
            config.setId(1L);
            websiteConfigManager.save(config);
            return config;
        }
        return WebsiteConfig.get();
    }

    /**
     * 更新平台站点配置
     */
    @CacheEvict(value = "cache:config:platform:website", allEntries = true)
    public void updateWebsite(PlatformWebsiteConfigPram param){
        var config = websiteConfigManager.findById(1L)
                .orElseThrow(() -> new ConfigNotExistException("平台站点配置不存在"));
        BeanUtil.copyProperties(param, config);
        websiteConfigManager.updateById(config);
    }

}
