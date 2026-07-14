package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 微信服务商通道商户应用 Manager
///
/// 服务商通道商户应用(子商户应用)数据访问管理器。方法按租户隔离边界分两类:
/// - 配置态(管理端 CRUD, 带租户隔离): [#listByMchNoAndChannelMchNo]、[#existsByChannelMchNoAndWxAppId]
/// - 运行态(认证/支付解析, 忽略租户, 方法名带 NotTenant): [#findByChannelMchNoAndWxAppIdNotTenant]；
///   按主键见基类 [BaseManager#findByIdNotTenant]
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

    /// 按通道商户号与wxAppId查询应用(channelAppId显式指定认证应用时使用, 校验该appId在系统中预配过, 忽略租户隔离)
    @IgnoreTenant
    public Optional<WechatIsvMchApp> findByChannelMchNoAndWxAppIdNotTenant(String channelMchNo, String wxAppId) {
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
