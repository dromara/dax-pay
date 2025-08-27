package org.dromara.daxpay.service.merchant.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.pay.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.merchant.entity.config.MerchantNotifyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商户应用消息通知配置
 * @author xxm
 * @since 2024/8/2
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantNotifyConfigManager extends BaseManager<MerchantNotifyConfigMapper, MerchantNotifyConfig> {

    /**
     * 根据id进行更新
     */
    @Override
    @CacheEvict(value = "cache:notifyConfig", key = "#merchantNotifyConfig.appId + ':' + #merchantNotifyConfig.code")
    public int updateById(MerchantNotifyConfig merchantNotifyConfig) {
        return super.updateById(merchantNotifyConfig);
    }

    /**
     * 根据AppId和类型查询
     */
    public Optional<MerchantNotifyConfig> findByAppIdAndType(String appId, String notifyType) {
        return lambdaQuery()
                .eq(MerchantNotifyConfig::getAppId, appId)
                .eq(MerchantNotifyConfig::getCode, notifyType)
                .oneOpt();
    }

     /**
     * 根据appId查询
     */
    public List<MerchantNotifyConfig> findAllByAppId(String appId) {
        return findAllByField(MchAppBaseEntity::getAppId, appId);
    }
}
