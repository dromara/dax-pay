package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvMchAppAuthConfigConvert;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppAuthConfigManager;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvMchAppManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchApp;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppAuthConfig;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvMchAppAuthConfigParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信服务商通道商户应用授权认证配置
///
/// 管理服务商通道商户应用(子商户应用)的授权认证配置,查询时不存在则创建默认记录,保存时校验应用归属关系。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvMchAppAuthConfigService {

    private final WechatIsvMchAppAuthConfigManager wechatIsvMchAppAuthConfigManager;
    private final WechatIsvMchAppManager wechatIsvMchAppManager;

    /// 根据应用ID查询授权认证配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public WechatIsvMchAppAuthConfig findByWechatIsvMchAppId(Long wechatIsvMchAppId) {
        var existing = wechatIsvMchAppAuthConfigManager.findByWechatIsvMchAppId(wechatIsvMchAppId);
        if (existing.isPresent()) {
            return existing.get();
        }
        var app = wechatIsvMchAppManager.findById(wechatIsvMchAppId)
                // 微信: 服务商通道商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        var config = new WechatIsvMchAppAuthConfig()
                .setChannelMchNo(app.getChannelMchNo())
                .setWechatIsvMchAppId(wechatIsvMchAppId);
        config.setMchNo(app.getMchNo());
        wechatIsvMchAppAuthConfigManager.save(config);
        return config;
    }

    /// 保存应用授权认证配置(更新)
    @Transactional(rollbackFor = Exception.class)
    public void save(WechatIsvMchAppAuthConfigParam param) {
        var app = wechatIsvMchAppManager.findById(param.getWechatIsvMchAppId())
                // 微信: 服务商通道商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.mchAppNotFound"));
        if (!app.getMchNo().equals(param.getMchNo()) || !app.getChannelMchNo().equals(param.getChannelMchNo())) {
            // 微信: 服务商通道商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.mchAppNotFound");
        }
        var config = this.findByWechatIsvMchAppId(param.getWechatIsvMchAppId());
        WechatIsvMchAppAuthConfigConvert.CONVERT.copy(param, config);
        wechatIsvMchAppAuthConfigManager.updateById(config);
    }

    /// 删除应用授权认证配置
    public void deleteByWechatIsvMchAppId(Long wechatIsvMchAppId) {
        wechatIsvMchAppAuthConfigManager.deleteByWechatIsvMchAppId(wechatIsvMchAppId);
    }
}
