package cn.daxpay.multi.service.service.config;

import cn.daxpay.multi.service.entity.config.PlatformConfig;
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

    /**
     * 获取平台配置
     */
    public PlatformConfig getConfig(){
       return new PlatformConfig();
    }
}
