package org.dromara.daxpay.channel.douyin.dao.direct;

import org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 抖音直连密钥配置
///
/// 直连密钥配置数据访问管理器，提供按通道商户号查询和删除密钥配置的方法。
///
@Repository
public class DouyinDirectKeyConfigManager extends BaseManager<DouyinDirectKeyConfigMapper, DouyinDirectKeyConfig> {

    /// 根据通道商户号查询密钥配置
    public Optional<DouyinDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(DouyinDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(DouyinDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
