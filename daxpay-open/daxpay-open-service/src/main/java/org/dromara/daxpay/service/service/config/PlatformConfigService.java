package org.dromara.daxpay.service.service.config;

import cn.bootx.platform.core.exception.BizInfoException;
import org.dromara.daxpay.service.dao.config.PlatformConfigManager;
import org.dromara.daxpay.service.entity.config.PlatformConfig;
import org.dromara.daxpay.service.param.config.PlatformConfigParam;
import org.dromara.daxpay.service.result.config.PlatformConfigResult;
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
     * 获取平台配置
     */
    public PlatformConfigResult findConfig(){
        return platformConfigManager.findById(1L).map(PlatformConfig::toResult)
                .orElse(new PlatformConfigResult());
    }

    /**
     * 新增
     */
    @CacheEvict(value = "cache:PlatformConfig", allEntries = true)
    public void update(PlatformConfigParam param){
        var platformConfig = platformConfigManager.findById(1L);
        if (platformConfig.isEmpty()){
            PlatformConfig config = new PlatformConfig();
            BeanUtil.copyProperties(param, config);
            config.setId(1L);
            platformConfigManager.save(config);
        } else{
            var config = platformConfig.get();
            BeanUtil.copyProperties(param, config);
            platformConfigManager.updateById(config);
        }
    }
}
