package cn.daxpay.open.payment.wx.dao;

import cn.daxpay.open.payment.wx.entity.WxMchApp;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 商户微信应用
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class WxMchAppManager extends BaseManager<WxMchAppMapper, WxMchApp> {

    /// 按商户号查询应用列表（按创建时间升序）
    public List<WxMchApp> listByMchNo(String mchNo) {
        return lambdaQuery()
                .eq(WxMchApp::getMchNo, mchNo)
                .orderByAsc(WxMchApp::getCreateTime)
                .orderByAsc(WxMchApp::getId)
                .list();
    }

    /// 按商户号与 wxAppId 查询应用
    public Optional<WxMchApp> findByMchNoAndWxAppId(String mchNo, String wxAppId) {
        return firstOpt(q -> q
                .eq(WxMchApp::getMchNo, mchNo)
                .eq(WxMchApp::getWxAppId, wxAppId));
    }

    /// 校验同商户下 wxAppId 是否已存在(排除自身)
    public boolean existsByMchNoAndWxAppId(String mchNo, String wxAppId, Long excludeId) {
        return lambdaQuery()
                .eq(WxMchApp::getMchNo, mchNo)
                .eq(WxMchApp::getWxAppId, wxAppId)
                .ne(excludeId != null, WxMchApp::getId, excludeId)
                .exists();
    }

    /// 按商户号与应用类型查询首个应用
    public Optional<WxMchApp> findFirstByMchNoAndAppType(String mchNo, String appType) {
        return firstOpt(q -> q
                .eq(WxMchApp::getMchNo, mchNo)
                .eq(WxMchApp::getAppType, appType)
                .orderByAsc(WxMchApp::getCreateTime)
                .orderByAsc(WxMchApp::getId));
    }
}
