package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectKeyConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 微信直连密钥配置
///
/// 直连密钥配置数据访问管理器，提供按通道商户号查询和删除密钥配置的方法。
///
@Repository
public class WechatDirectKeyConfigManager extends BaseManager<WechatDirectKeyConfigMapper, WechatDirectKeyConfig> {

    /// 根据通道商户号查询密钥配置
    public Optional<WechatDirectKeyConfig> findByChannelMchNo(String channelMchNo) {
        return lambdaQuery()
                .eq(WechatDirectKeyConfig::getChannelMchNo, channelMchNo)
                .oneOpt();
    }

    /// 根据通道商户号删除密钥配置
    public void deleteByChannelMchNo(String channelMchNo) {
        lambdaUpdate()
                .eq(WechatDirectKeyConfig::getChannelMchNo, channelMchNo)
                .remove();
    }
}
