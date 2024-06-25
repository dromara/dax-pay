package cn.daxpay.multi.service.dao.channel;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.multi.service.entity.channel.ChannelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通道配置
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelConfigManager extends BaseManager<ChannelConfigMapper, ChannelConfig> {

    /**
     * 根据应用号查询
     */
    public List<ChannelConfig> findByAppId(String appId) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(ChannelConfig::getAppId, appId)
                .list();
    }
    /**
     * 根据应用号和商户号查询
     */
    public List<ChannelConfig> findByMchNoAndAppId(String appId) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(ChannelConfig::getAppId, appId)
                .eq(ChannelConfig::getAppId, appId)
                .list();
    }

}
