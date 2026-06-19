package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAppManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvAppAuthConfigManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAppAuthConfig;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvAppAuthConfigParam;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信服务商应用授权认证配置
///
/// 管理服务商应用的授权认证配置，查询时不存在则创建默认记录。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAppAuthConfigService {

    private final WechatIsvAppAuthConfigManager wechatIsvAppAuthConfigManager;
    private final WechatIsvAppManager wechatIsvAppManager;

    /// 根据应用ID查询授权认证配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public WechatIsvAppAuthConfig findByWechatIsvAppId(Long wechatIsvAppId) {
        if (!wechatIsvAppManager.existedById(wechatIsvAppId)) {
            throw new DataNotExistException("error.channel.wechat.appNotFound");
        }
        var existing = wechatIsvAppAuthConfigManager.findByWechatIsvAppId(wechatIsvAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new WechatIsvAppAuthConfig()
                .setWechatIsvAppId(wechatIsvAppId);
        wechatIsvAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置
    @Transactional(rollbackFor = Exception.class)
    public void save(WechatIsvAppAuthConfigParam param) {
        var config = this.findByWechatIsvAppId(param.getWechatIsvAppId());
        config.setAppSecret(param.getAppSecret());
        config.setAuthCallbackUrl(param.getAuthCallbackUrl());
        wechatIsvAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByWechatIsvAppId(Long wechatIsvAppId) {
        wechatIsvAppAuthConfigManager.deleteByWechatIsvAppId(wechatIsvAppId);
    }
}
