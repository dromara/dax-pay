package org.dromara.daxpay.service.merchant.service.config;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import org.dromara.daxpay.service.merchant.cache.MchAppCacheService;
import org.dromara.daxpay.service.merchant.dao.config.MerchantNotifyConfigManager;
import org.dromara.daxpay.service.pay.dao.constant.MerchantNotifyConstManager;
import org.dromara.daxpay.service.merchant.entity.config.MerchantNotifyConfig;
import org.dromara.daxpay.service.pay.entity.constant.MerchantNotifyConst;
import org.dromara.daxpay.service.merchant.entity.app.MchApp;
import org.dromara.daxpay.service.merchant.result.config.MerchantNotifyConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商户应用消息通知配置服务
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNotifyConfigService {

    private final MerchantNotifyConstManager constManagerAll;

    private final MerchantNotifyConfigManager configManager;

    private final MchAppCacheService mchAppCacheService;

    /**
     * 显示列表
     */
    public List<MerchantNotifyConfigResult> findAllByAppId(String appId) {
        var map = configManager.findAllByAppId(appId)
                .stream()
                .collect(Collectors.toMap(MerchantNotifyConfig::getCode, Function.identity(), (v1, v2) -> v1));
        List<MerchantNotifyConst> costs = constManagerAll.lambdaQuery().orderByAsc(MpIdEntity::getId).list();
        MchApp mchApp = mchAppCacheService.get(appId);

        return costs.stream()
                .map(o->{
                    Boolean subscribe = Optional.ofNullable(map.get(o.getCode()))
                            .map(MerchantNotifyConfig::isSubscribe)
                            .orElse(false);
                    var result = new MerchantNotifyConfigResult()
                            .setCode(o.getCode())
                            .setName(o.getName())
                            .setDescription(o.getDescription())
                            .setSubscribe(subscribe);
                    result.setAppId(appId)
                            .setMchNo(mchApp.getMchNo());
                    return result;
                }).toList();
    }



    /**
     * 查询商户应用消息通知配置
     */
    @Cacheable(value = "cache:notifyConfig", key = "#appId + ':' + #notifyType")
    public boolean getSubscribeByAppIdAndType(String appId, String notifyType) {
        return configManager.lambdaQuery()
                .eq(MerchantNotifyConfig::getAppId, appId)
                .eq(MerchantNotifyConfig::getCode, notifyType)
                .oneOpt()
                .map(MerchantNotifyConfig::isSubscribe)
                .orElse(false);
    }

    /**
     * 开启或关闭订阅
     */
    public void subscribe(String appId, String notifyType, boolean subscribe){
        // 查询应用
        var mchApp = mchAppCacheService.get(appId);

        // 判断是否存在配置
        var notifyConfigOpt = configManager.findByAppIdAndType(appId, notifyType);

        if (notifyConfigOpt.isPresent()) {
            notifyConfigOpt.get().setSubscribe(subscribe);
            configManager.updateById(notifyConfigOpt.get());
        } else {
            MerchantNotifyConfig merchantNotifyConfig = new MerchantNotifyConfig();
            merchantNotifyConfig.setAppId(appId);
            merchantNotifyConfig.setMchNo(mchApp.getMchNo());
            merchantNotifyConfig.setCode(notifyType);
            merchantNotifyConfig.setSubscribe(subscribe);
            configManager.save(merchantNotifyConfig);
        }
    }

}
