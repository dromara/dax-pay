package cn.daxpay.open.payment.douyin.dao.platform;

import cn.daxpay.open.payment.douyin.entity.platform.DyPlatformApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 平台抖音应用
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class DyPlatformAppManager extends BaseManager<DyPlatformAppMapper, DyPlatformApp> {

    /// 查询全部应用（按创建时间升序）
    public List<DyPlatformApp> listAll() {
        return lambdaQuery()
                .orderByAsc(DyPlatformApp::getCreateTime)
                .orderByAsc(DyPlatformApp::getId)
                .list();
    }

    /// 按 douyinAppId 查询应用
    public Optional<DyPlatformApp> findByDouyinAppId(String douyinAppId) {
        return firstOpt(q -> q.eq(DyPlatformApp::getDouyinAppId, douyinAppId));
    }

    /// 校验抖音应用AppId是否已存在(排除自身)
    public boolean existsByDouyinAppId(String douyinAppId, Long excludeId) {
        return lambdaQuery()
                .eq(DyPlatformApp::getDouyinAppId, douyinAppId)
                .ne(excludeId != null, DyPlatformApp::getId, excludeId)
                .exists();
    }

    /// 按应用类型查询首个应用
    public Optional<DyPlatformApp> findFirstByAppType(String appType) {
        return firstOpt(q -> q
                .eq(DyPlatformApp::getAppType, appType)
                .orderByAsc(DyPlatformApp::getCreateTime)
                .orderByAsc(DyPlatformApp::getId));
    }

    /// 按应用类型查询全部应用（按创建时间升序），供应用解析做唯一性判断
    public List<DyPlatformApp> listByAppType(String appType) {
        return lambdaQuery()
                .eq(DyPlatformApp::getAppType, appType)
                .orderByAsc(DyPlatformApp::getCreateTime)
                .orderByAsc(DyPlatformApp::getId)
                .list();
    }
}
