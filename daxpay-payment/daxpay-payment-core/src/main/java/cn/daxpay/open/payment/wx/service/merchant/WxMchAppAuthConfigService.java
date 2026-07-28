package cn.daxpay.open.payment.wx.service.merchant;

import cn.daxpay.open.payment.wx.dao.merchant.WxMchAppAuthConfigManager;
import cn.daxpay.open.payment.wx.dao.merchant.WxMchAppManager;
import cn.daxpay.open.payment.wx.entity.merchant.WxMchAppAuthConfig;
import cn.daxpay.open.payment.wx.param.merchant.WxMchAppAuthConfigParam;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 商户微信应用授权认证配置
///
/// 管理商户应用的授权认证配置；查询时不存在则创建默认记录；空 secret 表示不更新。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WxMchAppAuthConfigService {

    private final WxMchAppAuthConfigManager wxMchAppAuthConfigManager;
    private final WxMchAppManager wxMchAppManager;

    /// 根据商户应用ID查询授权认证配置，不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public WxMchAppAuthConfig findByWxMchAppId(Long wxMchAppId) {
        var app = wxMchAppManager.findById(wxMchAppId)
                // 微信: 商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.wx.mchAppNotFound"));
        var existing = wxMchAppAuthConfigManager.findByWxMchAppId(wxMchAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new WxMchAppAuthConfig()
                .setWxMchAppId(wxMchAppId);
        // 与主表商户号一致
        config.setMchNo(app.getMchNo());
        wxMchAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置（空 secret 表示不改）
    @Transactional(rollbackFor = Exception.class)
    public void save(WxMchAppAuthConfigParam param) {
        var config = this.findByWxMchAppId(param.getWxMchAppId());
        // 空 secret 表示不更新
        if (StrUtil.isNotBlank(param.getAppSecret())) {
            config.setAppSecret(param.getAppSecret());
        }
        wxMchAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByWxMchAppId(Long wxMchAppId) {
        wxMchAppAuthConfigManager.deleteByWxMchAppId(wxMchAppId);
    }
}
