package org.dromara.daxpay.service.service.config;

import cn.bootx.platform.core.exception.BizInfoException;
import org.dromara.daxpay.service.dao.config.PlatformConfigManager;
import org.dromara.daxpay.service.entity.config.PlatformConfig;
import org.dromara.daxpay.service.param.config.PlatformConfigParam;
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
    private final PlatformConfigManager platformConfigManager;

    /**
     * 获取平台配置
     */
    @Cacheable(value = "cache:PlatformConfig")
    public PlatformConfig getConfig(){
       return platformConfigManager.findById(1L).orElseThrow(() -> new BizInfoException("平台配置不存在"));
    }

    /**
     * 更新
     */
    @CacheEvict(value = "cache:PlatformConfig", allEntries = true)
    public void update(PlatformConfigParam param){
        PlatformConfig platformConfig = platformConfigManager.findById(1L)
                .orElseThrow(() -> new BizInfoException("平台配置不存在"));
        BeanUtil.copyProperties(param, platformConfig);
        platformConfigManager.updateById(platformConfig);
    }
}
