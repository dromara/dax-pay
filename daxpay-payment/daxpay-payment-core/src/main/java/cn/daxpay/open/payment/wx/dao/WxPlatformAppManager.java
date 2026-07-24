package cn.daxpay.open.payment.wx.dao;

import cn.daxpay.open.payment.wx.entity.WxPlatformApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 平台微信应用
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class WxPlatformAppManager extends BaseManager<WxPlatformAppMapper, WxPlatformApp> {

    /// 查询全部应用（按创建时间升序）
    public List<WxPlatformApp> listAll() {
        return lambdaQuery()
                .orderByAsc(WxPlatformApp::getCreateTime)
                .orderByAsc(WxPlatformApp::getId)
                .list();
    }

    /// 按 wxAppId 查询应用
    public Optional<WxPlatformApp> findByWxAppId(String wxAppId) {
        return firstOpt(q -> q.eq(WxPlatformApp::getWxAppId, wxAppId));
    }

    /// 校验微信应用AppId是否已存在(排除自身)
    public boolean existsByWxAppId(String wxAppId, Long excludeId) {
        return lambdaQuery()
                .eq(WxPlatformApp::getWxAppId, wxAppId)
                .ne(excludeId != null, WxPlatformApp::getId, excludeId)
                .exists();
    }

    /// 按应用类型查询首个应用
    public Optional<WxPlatformApp> findFirstByAppType(String appType) {
        return firstOpt(q -> q
                .eq(WxPlatformApp::getAppType, appType)
                .orderByAsc(WxPlatformApp::getCreateTime)
                .orderByAsc(WxPlatformApp::getId));
    }
}
