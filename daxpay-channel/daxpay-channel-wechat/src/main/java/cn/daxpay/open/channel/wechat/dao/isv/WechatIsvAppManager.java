package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 微信服务商应用
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAppManager extends BaseManager<WechatIsvAppMapper, WechatIsvApp> {

    /// 查询全部应用（按创建时间升序，先创建的在前）
    public List<WechatIsvApp> listAll() {
        return lambdaQuery()
                .orderByAsc(WechatIsvApp::getCreateTime)
                .orderByAsc(WechatIsvApp::getId)
                .list();
    }

    /// 查询第一个应用（运行时默认使用）
    public Optional<WechatIsvApp> findFirst() {
        return firstOpt(q -> q
                .orderByAsc(WechatIsvApp::getCreateTime)
                .orderByAsc(WechatIsvApp::getId));
    }

    /// 按应用类型查询首个应用（能力→应用类型推导时使用）
    public Optional<WechatIsvApp> findFirstByAppType(String appType) {
        return firstOpt(q -> q
                .eq(WechatIsvApp::getAppType, appType)
                .orderByAsc(WechatIsvApp::getCreateTime)
                .orderByAsc(WechatIsvApp::getId));
    }

    /// 按 wxAppId 查询应用(opAppId显式指定认证应用时使用, 校验该appId在系统中预配过)
    public Optional<WechatIsvApp> findByWxAppId(String wxAppId) {
        return firstOpt(q -> q.eq(WechatIsvApp::getWxAppId, wxAppId));
    }

    /// 校验微信应用AppId是否已存在(排除自身)
    public boolean existsByWxAppId(String wxAppId, Long excludeId) {
        return lambdaQuery()
                .eq(WechatIsvApp::getWxAppId, wxAppId)
                .ne(excludeId != null, WechatIsvApp::getId, excludeId)
                .exists();
    }
}
