package cn.bootx.platform.daxpay.core.system.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.daxpay.core.system.dao.PlatformConfigManager;
import cn.bootx.platform.daxpay.core.system.entity.PlatformConfig;
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


}
