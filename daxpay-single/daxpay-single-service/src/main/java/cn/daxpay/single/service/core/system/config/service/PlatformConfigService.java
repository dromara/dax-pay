package cn.daxpay.single.service.core.system.config.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.daxpay.single.service.common.context.PlatformLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.system.config.dao.PlatformConfigManager;
import cn.daxpay.single.service.core.system.config.entity.PlatformConfig;
import cn.daxpay.single.service.param.system.config.PlatformConfigParam;
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

    /**
     * 初始化平台配置上下文
     */
    public void initPlatform(){
        PlatformConfig config = this.getConfig();
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        platform.setSignType(config.getSignType());
        platform.setSignSecret(config.getSignSecret());
        platform.setOrderTimeout(config.getOrderTimeout());
        platform.setLimitAmount(config.getLimitAmount());
        platform.setWebsiteUrl(config.getWebsiteUrl());
        platform.setNoticeType(config.getNotifyType());
        platform.setNoticeUrl(config.getNotifyUrl());
    }
}
