package org.dromara.daxpay.service.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.common.context.MchAppLocal;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.entity.config.ChannelCashierConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelCashierConfigManage extends BaseManager<ChannelCashierConfigMapper, ChannelCashierConfig> {

    /**
     * 根据AppId查询列表
     */
    public List<ChannelCashierConfig> findAllByAppId(String appId) {
        return this.lambdaQuery()
                .eq(ChannelCashierConfig::getAppId, appId)
                .list();
    }

    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, String appId) {
        return this.lambdaQuery()
                .eq(ChannelCashierConfig::getCashierType, type)
                .eq(MchAppBaseEntity::getAppId,appId)
                .exists();
    }
    /**
     * 判断类型是否存在
     */
    public boolean existsByType(String type, String appId, Long id) {
        return this.lambdaQuery()
                .eq(ChannelCashierConfig::getCashierType, type)
                .eq(MchAppBaseEntity::getAppId,appId)
                .ne(ChannelCashierConfig::getId,id)
                .exists();
    }

    /**
     * 根据类型查询
     */
    public Optional<ChannelCashierConfig> findByCashierType(String cashierType) {
        MchAppLocal mchAppInfo = PaymentContextLocal.get().getMchAppInfo();
        return lambdaQuery().eq(ChannelCashierConfig::getCashierType, cashierType)
                .eq(ChannelCashierConfig::getAppId, mchAppInfo.getAppId())
                .oneOpt();
    }
}
