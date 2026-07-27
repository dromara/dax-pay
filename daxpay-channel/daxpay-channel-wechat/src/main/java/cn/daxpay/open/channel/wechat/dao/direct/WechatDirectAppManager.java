package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 微信直连商户应用
///
/// 方法按租户隔离边界分三类:
/// - 配置态 CRUD: [#listByMchNoAndChannelMchNo]、[#existsByChannelMchNoAndWxAppId]
/// - 支付/回调（已装载 mchNo）: [#findFirstByChannelMchNo]、[#findFirstByChannelMchNoAndAppType] 等租户内方法
/// - 认证引导（无上下文）: 方法名带 NotTenant
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

    /// 按通道商户号查询首个应用（支付/回调，租户内）
    public Optional<WechatDirectApp> findFirstByChannelMchNo(String channelMchNo) {
        return firstOpt(q -> q
                .eq(WechatDirectApp::getChannelMchNo, channelMchNo)
                .orderByAsc(WechatDirectApp::getCreateTime)
                .orderByAsc(WechatDirectApp::getId));
    }

    /// 按通道商户号与应用类型查询首个应用（支付/回调，租户内）
    public Optional<WechatDirectApp> findFirstByChannelMchNoAndAppType(String channelMchNo, String appType) {
        return firstOpt(q -> q
                .eq(WechatDirectApp::getChannelMchNo, channelMchNo)
                .eq(WechatDirectApp::getAppType, appType)
                .orderByAsc(WechatDirectApp::getCreateTime)
                .orderByAsc(WechatDirectApp::getId));
    }

    /// 按通道商户号与应用类型查询全部应用，供应用解析做唯一性判断
    public List<WechatDirectApp> listByChannelMchNoAndAppType(String channelMchNo, String appType) {
        return lambdaQuery()
                .eq(WechatDirectApp::getChannelMchNo, channelMchNo)
                .eq(WechatDirectApp::getAppType, appType)
                .orderByAsc(WechatDirectApp::getCreateTime)
                .orderByAsc(WechatDirectApp::getId)
                .list();
    }

    /// 按通道商户号与wxAppId查询应用（支付/回调，租户内）
    public Optional<WechatDirectApp> findByChannelMchNoAndWxAppId(String channelMchNo, String wxAppId) {
        return firstOpt(q -> q
                .eq(WechatDirectApp::getChannelMchNo, channelMchNo)
                .eq(WechatDirectApp::getWxAppId, wxAppId));
    }

    /// 按通道商户号与wxAppId查询应用（认证引导，忽略租户）
    @IgnoreTenant
    public Optional<WechatDirectApp> findByChannelMchNoAndWxAppIdNotTenant(String channelMchNo, String wxAppId) {
        return findByChannelMchNoAndWxAppId(channelMchNo, wxAppId);
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
