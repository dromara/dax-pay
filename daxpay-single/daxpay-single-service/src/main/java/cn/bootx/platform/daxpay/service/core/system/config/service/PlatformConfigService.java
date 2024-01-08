package cn.bootx.platform.daxpay.service.core.system.config.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.service.core.system.config.entity.PlatformConfig;
import cn.bootx.platform.daxpay.service.core.system.config.dao.PlatformConfigManager;
import cn.bootx.platform.daxpay.service.param.system.config.PlatformConfigParam;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 平台配置
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformConfigService {
    /** 默认配置ID */
    private static final long ID = 0L;

    private final PlatformConfigManager platformConfigManager;

    /**
     * 获取支付平台配置
     */
    public PlatformConfig getConfig(){
        return platformConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("平台配置不存在"));
    }

    /**
     * 更新配置
     */
    public void update(PlatformConfigParam param){
        PlatformConfig config = platformConfigManager.findById(ID).orElseThrow(() -> new DataNotExistException("平台配置不存在"));
        BeanUtil.copyProperties(param,config);
        platformConfigManager.updateById(config);
    }
}
