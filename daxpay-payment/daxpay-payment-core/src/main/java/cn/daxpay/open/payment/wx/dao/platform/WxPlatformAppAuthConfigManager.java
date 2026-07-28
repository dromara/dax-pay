package cn.daxpay.open.payment.wx.dao.platform;

import cn.daxpay.open.payment.wx.entity.platform.WxPlatformAppAuthConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 平台微信应用授权认证配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class WxPlatformAppAuthConfigManager extends BaseManager<WxPlatformAppAuthConfigMapper, WxPlatformAppAuthConfig> {

    /// 根据平台应用ID查询授权认证配置
    public Optional<WxPlatformAppAuthConfig> findByWxPlatformAppId(Long wxPlatformAppId) {
        return lambdaQuery()
                .eq(WxPlatformAppAuthConfig::getWxPlatformAppId, wxPlatformAppId)
                .oneOpt();
    }

    /// 根据平台应用ID删除授权认证配置
    public void deleteByWxPlatformAppId(Long wxPlatformAppId) {
        lambdaUpdate()
                .eq(WxPlatformAppAuthConfig::getWxPlatformAppId, wxPlatformAppId)
                .remove();
    }
}
