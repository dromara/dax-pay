package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 微信服务商通道商户应用 Manager
///
/// 服务商通道商户应用(子商户应用)数据访问管理器,提供按商户号和通道商户号查询列表、
/// 首个应用查询、应用类型推导查询及同一通道下应用AppId唯一性校验等方法。
///
@Repository
public class WechatIsvMchAppManager extends BaseManager<WechatIsvMchAppMapper, WechatIsvMchApp> {

    /// 根据商户号和通道商户号查询应用列表(按创建时间升序)
    public List<WechatIsvMchApp> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        return lambdaQuery()
                .eq(WechatIsvMchApp::getMchNo, mchNo)
                .eq(WechatIsvMchApp::getChannelMchNo, channelMchNo)
                .orderByAsc(WechatIsvMchApp::getCreateTime)
                .orderByAsc(WechatIsvMchApp::getId)
                .list();
    }

    /// 按通道商户号查询首个应用(能力解析兜底时使用)
    public Optional<WechatIsvMchApp> findFirstByChannelMchNo(String channelMchNo) {
        return firstOpt(q -> q
                .eq(WechatIsvMchApp::getChannelMchNo, channelMchNo)
                .orderByAsc(WechatIsvMchApp::getCreateTime)
                .orderByAsc(WechatIsvMchApp::getId));
    }

    /// 按通道商户号与应用类型查询首个应用(能力→应用类型推导时使用)
    public Optional<WechatIsvMchApp> findFirstByChannelMchNoAndAppType(String channelMchNo, String appType) {
        return firstOpt(q -> q
                .eq(WechatIsvMchApp::getChannelMchNo, channelMchNo)
                .eq(WechatIsvMchApp::getAppType, appType)
                .orderByAsc(WechatIsvMchApp::getCreateTime)
                .orderByAsc(WechatIsvMchApp::getId));
    }

    /// 按通道商户号与wxAppId查询应用(opAppId显式指定认证应用时使用, 校验该appId在系统中预配过)
    public Optional<WechatIsvMchApp> findByChannelMchNoAndWxAppId(String channelMchNo, String wxAppId) {
        return firstOpt(q -> q
                .eq(WechatIsvMchApp::getChannelMchNo, channelMchNo)
                .eq(WechatIsvMchApp::getWxAppId, wxAppId));
    }

    /// 校验同一通道商户下wxAppId是否已存在(排除自身)
    public boolean existsByChannelMchNoAndWxAppId(String mchNo, String channelMchNo, String wxAppId, Long excludeId) {
        return lambdaQuery()
                .eq(WechatIsvMchApp::getMchNo, mchNo)
                .eq(WechatIsvMchApp::getChannelMchNo, channelMchNo)
                .eq(WechatIsvMchApp::getWxAppId, wxAppId)
                .ne(excludeId != null, WechatIsvMchApp::getId, excludeId)
                .exists();
    }
}
