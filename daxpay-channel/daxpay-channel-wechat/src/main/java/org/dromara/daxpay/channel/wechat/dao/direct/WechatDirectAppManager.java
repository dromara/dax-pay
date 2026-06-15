package org.dromara.daxpay.channel.wechat.dao.direct;

import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectApp;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 微信直连商户应用
///
/// 直连商户应用数据访问管理器，提供按商户号和通道商户号查询列表、同一通道下应用ID唯一性校验等方法。
///
@Repository
public class WechatDirectAppManager extends BaseManager<WechatDirectAppMapper, WechatDirectApp> {

    /// 根据商户号和通道商户号查询应用列表（按创建时间升序）
    public List<WechatDirectApp> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        return lambdaQuery()
                .eq(WechatDirectApp::getMchNo, mchNo)
                .eq(WechatDirectApp::getChannelMchNo, channelMchNo)
                .orderByAsc(WechatDirectApp::getCreateTime)
                .orderByAsc(WechatDirectApp::getId)
                .list();
    }

    /// 校验同一通道商户下wxAppId是否已存在(排除自身)
    public boolean existsByChannelMchNoAndWxAppId(String mchNo, String channelMchNo, String wxAppId, Long excludeId) {
        return lambdaQuery()
                .eq(WechatDirectApp::getMchNo, mchNo)
                .eq(WechatDirectApp::getChannelMchNo, channelMchNo)
                .eq(WechatDirectApp::getWxAppId, wxAppId)
                .ne(excludeId != null, WechatDirectApp::getId, excludeId)
                .exists();
    }
}
