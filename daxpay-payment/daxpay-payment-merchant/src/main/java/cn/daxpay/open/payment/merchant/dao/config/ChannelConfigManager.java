package cn.daxpay.open.payment.merchant.dao.config;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.payment.masterdata.config.entity.ChannelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 通道配置 constant
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelConfigManager extends BaseManager<ChannelConfigMapper, ChannelConfig> {

    /// 根据应用号查询
    public List<ChannelConfig> findByAppId(String appId) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(ChannelConfig::getAppId, appId)
                .list();
    }
    /// 根据应用号查询启用的配置
    public List<ChannelConfig> findEnableByAppId(String appId) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(ChannelConfig::isEnable, true)
                .eq(ChannelConfig::getAppId, appId)
                .list();
    }

    /// 根据应用号和通道查询
    public Optional<ChannelConfig> findByAppIdAndChannel(String appId, String channel) {
        return lambdaQuery()
                .eq(ChannelConfig::getAppId, appId)
                .eq(ChannelConfig::getChannel, channel)
                .oneOpt();
    }

    /// 判断是应用否存在指定的通道配置
    public boolean existsByAppIdAndChannel(String appId, String channel) {
        return lambdaQuery()
                .eq(ChannelConfig::getAppId, appId)
                .eq(ChannelConfig::getChannel, channel)
                .exists();
    }

}
