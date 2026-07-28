package cn.daxpay.open.payment.wx.service.platform;

import cn.daxpay.open.payment.wx.dao.platform.WxPlatformAppAuthConfigManager;
import cn.daxpay.open.payment.wx.dao.platform.WxPlatformAppManager;
import cn.daxpay.open.payment.wx.entity.platform.WxPlatformAppAuthConfig;
import cn.daxpay.open.payment.wx.param.platform.WxPlatformAppAuthConfigParam;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 平台微信应用授权认证配置
///
/// 管理平台应用的授权认证配置；查询时不存在则创建默认记录；空 secret 表示不更新。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WxPlatformAppAuthConfigService {

    private final WxPlatformAppAuthConfigManager wxPlatformAppAuthConfigManager;
    private final WxPlatformAppManager wxPlatformAppManager;

    /// 根据平台应用ID查询授权认证配置，不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public WxPlatformAppAuthConfig findByWxPlatformAppId(Long wxPlatformAppId) {
        if (!wxPlatformAppManager.existedById(wxPlatformAppId)) {
            // 微信: 平台应用不存在
            throw new DataNotExistException("error.payment.wx.appNotFound");
        }
        var existing = wxPlatformAppAuthConfigManager.findByWxPlatformAppId(wxPlatformAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new WxPlatformAppAuthConfig()
                .setWxPlatformAppId(wxPlatformAppId);
        wxPlatformAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置（空 secret 表示不改）
    @Transactional(rollbackFor = Exception.class)
    public void save(WxPlatformAppAuthConfigParam param) {
        var config = this.findByWxPlatformAppId(param.getWxPlatformAppId());
        // 空 secret 表示不更新
        if (StrUtil.isNotBlank(param.getAppSecret())) {
            config.setAppSecret(param.getAppSecret());
        }
        wxPlatformAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByWxPlatformAppId(Long wxPlatformAppId) {
        wxPlatformAppAuthConfigManager.deleteByWxPlatformAppId(wxPlatformAppId);
    }
}
