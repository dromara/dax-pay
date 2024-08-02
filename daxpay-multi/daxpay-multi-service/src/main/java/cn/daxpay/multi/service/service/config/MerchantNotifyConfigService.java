package cn.daxpay.multi.service.service.config;

import cn.daxpay.multi.service.dao.config.MerchantNotifyConfigManager;
import cn.daxpay.multi.service.entity.config.MerchantNotifyConfig;
import cn.daxpay.multi.service.entity.constant.MerchantNotifyConst;
import cn.daxpay.multi.service.result.config.MerchantNotifyConfigResult;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户应用消息通知配置服务
 * @author xxm
 * @since 2024/7/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNotifyConfigService {

    private final MerchantNotifyConfigManager merchantNotifyConfigManager;

    /**
     * 显示列表
     */
    public List<MerchantNotifyConfigResult> findAllByAppId(String appId) {
        var wrapper = new MPJLambdaWrapper<MerchantNotifyConfig>()
                .select(MerchantNotifyConfig::isSubscribe)
                .selectAll(MerchantNotifyConst.class)
                .rightJoin(MerchantNotifyConst.class, MerchantNotifyConst::getCode, MerchantNotifyConfig::getNotifyType)
                .eq(MerchantNotifyConst::isEnable, true)
                .eq(MerchantNotifyConfig::getAppId, appId)
                .orderByAsc(MerchantNotifyConst::getId);
        return merchantNotifyConfigManager.selectJoinList(MerchantNotifyConfigResult.class, wrapper);
    }



    /**
     * 查询商户应用消息通知配置
     */
    @Cacheable(value = "cache:notifyConfig", key = "#appId + ':' + #notifyType")
    public boolean getSubscribeByAppIdAndType(String appId, String notifyType) {
        return merchantNotifyConfigManager.lambdaQuery()
                .eq(MerchantNotifyConfig::getAppId, appId)
                .eq(MerchantNotifyConfig::getNotifyType, notifyType)
                .oneOpt()
                .map(MerchantNotifyConfig::isSubscribe)
                .orElse(false);
    }

    /**
     * 开启或关闭订阅
     */
    public void subscribe(String appId, String notifyType, boolean subscribe){
        // 判断是否存在配置
        var notifyConfigOpt = merchantNotifyConfigManager.findByAppIdAndType(appId, notifyType);

        if (notifyConfigOpt.isPresent()) {
            notifyConfigOpt.get().setSubscribe(subscribe);
            merchantNotifyConfigManager.updateById(notifyConfigOpt.get());
        } else {
            MerchantNotifyConfig merchantNotifyConfig = new MerchantNotifyConfig();
            merchantNotifyConfig.setAppId(appId);
            merchantNotifyConfig.setNotifyType(notifyType);
            merchantNotifyConfig.setSubscribe(subscribe);
            merchantNotifyConfigManager.save(merchantNotifyConfig);
        }
    }

}
