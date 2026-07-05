package cn.daxpay.open.payment.app.mobile.service;

import cn.daxpay.open.payment.app.mobile.convert.MobileAppConvert;
import cn.daxpay.open.payment.app.mobile.dao.MobileAppManager;
import cn.daxpay.open.payment.app.mobile.entity.MobileApp;
import cn.daxpay.open.payment.app.mobile.param.MobileAppParam;
import cn.daxpay.open.payment.app.mobile.result.MobileAppResult;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// # 移动端应用配置服务
///
/// 平台级配置, 按端类型(appType)+移动平台(platform)维度管理。
@Slf4j
@Service
@RequiredArgsConstructor
public class MobileAppService {

    private final MobileAppManager manager;

    /// 查询全部(前端按端类型分组展示卡片)
    public List<MobileAppResult> findAll() {
        return manager.findAll().stream()
                .map(MobileApp::toResult)
                .toList();
    }

    /// 按端类型查询所有平台配置(端详情页Tab列表)
    public List<MobileAppResult> findAllByAppType(String appType) {
        return manager.findAllByField(MobileApp::getAppType, appType).stream()
                .map(MobileApp::toResult)
                .toList();
    }

    /// 查询单条
    public MobileAppResult findById(Long id) {
        // 通用: 移动端应用配置不存在
        return manager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.mobile_app.notExist"))
                .toResult();
    }

    /// 保存(按端类型+平台组合 upsert)
    @Transactional(rollbackFor = Exception.class)
    public MobileAppResult save(MobileAppParam param) {
        var existing = manager.lambdaQuery()
                .eq(MobileApp::getAppType, param.getAppType())
                .eq(MobileApp::getPlatform, param.getPlatform())
                .oneOpt();
        if (existing.isPresent()) {
            var entity = existing.get();
            MobileAppConvert.CONVERT.copy(param, entity);
            manager.updateById(entity);
            return entity.toResult();
        }
        var entity = MobileAppConvert.CONVERT.toEntity(param);
        manager.save(entity);
        return entity.toResult();
    }

    /// 更新启用状态
    @Transactional(rollbackFor = Exception.class)
    public void updateEnabled(Long id, Boolean enabled) {
        var entity = manager.findById(id)
                // 通用: 移动端应用配置不存在
                .orElseThrow(() -> new DataNotExistException("error.mobile_app.notExist"));
        entity.setEnabled(enabled);
        manager.updateById(entity);
    }
}
