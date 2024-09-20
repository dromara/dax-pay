package cn.daxpay.multi.service.service.config;

import cn.bootx.platform.core.exception.BizInfoException;
import cn.daxpay.multi.service.dao.config.PlatformConfigManager;
import cn.daxpay.multi.service.entity.config.PlatformConfig;
import cn.daxpay.multi.service.param.config.PlatformConfigParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public PlatformConfig getConfig(){
       return platformConfigManager.findById(1L).orElseThrow(() -> new BizInfoException("平台配置不存在"));
    }


    /**
     * 更新
     */
    public void update(PlatformConfigParam param){
        PlatformConfig platformConfig = platformConfigManager.findById(1L)
                .orElseThrow(() -> new BizInfoException("平台配置不存在"));
        BeanUtil.copyProperties(param, platformConfig);
        platformConfigManager.updateById(platformConfig);
    }
}
