package cn.daxpay.open.channel.wechat.service.direct;

import cn.daxpay.open.channel.wechat.convert.direct.WechatDirectAppAuthConfigConvert;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppManager;
import cn.daxpay.open.channel.wechat.dao.direct.WechatDirectAppAuthConfigManager;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectApp;
import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppAuthConfig;
import cn.daxpay.open.channel.wechat.param.direct.WechatDirectAppAuthConfigParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信直连商户应用授权认证配置
///
/// 管理直连商户应用的授权认证配置，查询时不存在则创建默认记录，保存时校验应用归属关系。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDirectAppAuthConfigService {

    private final WechatDirectAppAuthConfigManager wechatDirectAppAuthConfigManager;
    private final WechatDirectAppManager wechatDirectAppManager;

    /// 根据应用ID查询授权认证配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public WechatDirectAppAuthConfig findByWechatDirectAppId(Long wechatDirectAppId) {
        var existing = wechatDirectAppAuthConfigManager.findByWechatDirectAppId(wechatDirectAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var app = wechatDirectAppManager.findById(wechatDirectAppId)
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        var config = new WechatDirectAppAuthConfig()
                .setChannelMchNo(app.getChannelMchNo())
                .setWechatDirectAppId(wechatDirectAppId);
        config.setMchNo(app.getMchNo());
        wechatDirectAppAuthConfigManager.save(config);
        return config;
    }

    /// 根据应用ID查询授权认证配置(运行态认证使用, 忽略租户隔离)
    ///
    /// 与 [#findByWechatDirectAppId] 的区别: 走 NotTenant 查询路径, 供认证策略(网关端无登录态)调用;
    /// [#save] 等配置态仍调原方法(保留租户隔离)。
    @IgnoreTenant
    @Transactional(rollbackFor = Exception.class)
    public WechatDirectAppAuthConfig findByWechatDirectAppIdForAuth(Long wechatDirectAppId) {
        var existing = wechatDirectAppAuthConfigManager.findByWechatDirectAppIdNotTenant(wechatDirectAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var app = wechatDirectAppManager.findByIdNotTenant(wechatDirectAppId)
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        var config = new WechatDirectAppAuthConfig()
                .setChannelMchNo(app.getChannelMchNo())
                .setWechatDirectAppId(wechatDirectAppId);
        config.setMchNo(app.getMchNo());
        wechatDirectAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(WechatDirectAppAuthConfigParam param) {
        var app = wechatDirectAppManager.findById(param.getWechatDirectAppId())
                // 微信: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            // 微信: 直连商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.mchAppNotFound");
        }
        var config = this.findByWechatDirectAppId(param.getWechatDirectAppId());
        WechatDirectAppAuthConfigConvert.CONVERT.copy(param, config);
        wechatDirectAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByWechatDirectAppId(Long wechatDirectAppId) {
        wechatDirectAppAuthConfigManager.deleteByWechatDirectAppId(wechatDirectAppId);
    }
}
