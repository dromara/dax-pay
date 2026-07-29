package cn.daxpay.open.payment.douyin.dao.merchant;

import cn.daxpay.open.payment.douyin.entity.merchant.DyMchApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 商户抖音应用
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class DyMchAppManager extends BaseManager<DyMchAppMapper, DyMchApp> {

    /// 按商户号查询应用列表（按创建时间升序）
    public List<DyMchApp> listByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(DyMchApp::getMchNo, mchNo)
                .orderByAsc(DyMchApp::getCreateTime)
                .orderByAsc(DyMchApp::getId)
                .list();
    }

    /// 按商户号与 douyinAppId 查询应用
    public Optional<DyMchApp> findByMchNoAndDouyinAppId(String mchNo, String douyinAppId) {
        return firstOpt(q -> q
                .eq(DyMchApp::getMchNo, mchNo)
                .eq(DyMchApp::getDouyinAppId, douyinAppId));
    }

    /// 校验同商户下 douyinAppId 是否已存在(排除自身)
    public boolean existsByMchNoAndDouyinAppId(String mchNo, String douyinAppId, Long excludeId) {
        return lambdaQuery()
                .eq(DyMchApp::getMchNo, mchNo)
                .eq(DyMchApp::getDouyinAppId, douyinAppId)
                .ne(excludeId != null, DyMchApp::getId, excludeId)
                .exists();
    }

    /// 按商户号与应用类型查询首个应用
    public Optional<DyMchApp> findFirstByMchNoAndAppType(String mchNo, String appType) {
        return firstOpt(q -> q
                .eq(DyMchApp::getMchNo, mchNo)
                .eq(DyMchApp::getAppType, appType)
                .orderByAsc(DyMchApp::getCreateTime)
                .orderByAsc(DyMchApp::getId));
    }
}
