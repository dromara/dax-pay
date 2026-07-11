package cn.daxpay.open.platform.system.dao.mobile;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.system.entity.mobile.MobileApp;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 移动端应用配置管理器
@Repository
public class MobileAppManager extends BaseManager<MobileAppMapper, MobileApp> {

    /// 按端类型 + 移动平台查询(唯一键)
    public Optional<MobileApp> findByAppTypeAndPlatform(String appType, String platform) {
        return lambdaQuery()
                .eq(MobileApp::getAppType, appType)
                .eq(MobileApp::getPlatform, platform)
                .oneOpt();
    }

    /// 按端类型查询全部平台配置
    public List<MobileApp> findAllByAppType(String appType) {
        return lambdaQuery()
                .eq(MobileApp::getAppType, appType)
                .list();
    }
}
