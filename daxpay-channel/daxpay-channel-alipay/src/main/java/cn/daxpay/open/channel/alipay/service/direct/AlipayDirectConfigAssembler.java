package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppKeyConfig;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/// # 支付宝直连通道配置组装器
///
/// 从进件商户对象([AlipayDirectApp] + [AlipayDirectAppKeyConfig])读取密钥/证书,
/// 并补充主应用回调地址(notifyUrl)与沙箱标志(sandbox), 组装为下发给子应用的 config Map。
///
/// 应用解析优先级：能力关联(显式配置 > appType自动推导) > 通道商户首个应用 > 商户号首个应用(兜底)。
///
/// 供支付策略([cn.daxpay.open.channel.alipay.strategy.direct.AlipayDirectPayStrategy])组装通道调用配置。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectConfigAssembler {

    private final AlipayDirectAppManager alipayDirectAppManager;
    private final AlipayDirectAppKeyConfigService alipayDirectAppKeyConfigService;
    private final AlipayDirectAppCapabilityService alipayDirectAppCapabilityService;

    /// 组装直连商户的通道调用配置(下发给子应用)
    ///
    /// @param mchNo        商户号(最终兜底定位应用)
    /// @param channelMchNo 通道商户号(路由回填)
    /// @param capability   支付能力编码(路由回填,用于选择匹配的应用)
    /// @return 通道配置 Map, 字段对齐子应用 `AlipayConfigDto`
    public Map<String, Object> buildConfig(String mchNo, String channelMchNo, String capability) {
        AlipayDirectApp app = resolveApp(mchNo, channelMchNo, capability);
        AlipayDirectAppKeyConfig keyConfig = alipayDirectAppKeyConfigService.findByAlipayDirectAppId(app.getId());

        Map<String, Object> config = new HashMap<>();
        config.put("aliAppId", app.getAliAppId());
        config.put("privateKey", keyConfig.getPrivateKey());
        config.put("alipayPublicKey", keyConfig.getAlipayPublicKey());
        config.put("authType", keyConfig.getAuthType());
        config.put("appCert", keyConfig.getAppCert());
        config.put("alipayCert", keyConfig.getAlipayCert());
        config.put("alipayRootCert", keyConfig.getAlipayRootCert());
        // TODO notifyUrl/sandbox 后续按业务要求手动组装(来源已迁移至进件配置, 待确认组装方式)
        return config;
    }

    /// 解析支付使用的应用，优先级：能力关联 > 通道商户首个 > 商户号首个兜底
    private AlipayDirectApp resolveApp(String mchNo, String channelMchNo, String capability) {
        // 1. 能力关联解析(显式配置 > appType自动推导)
        Optional<AlipayDirectApp> resolved = alipayDirectAppCapabilityService.resolveApp(channelMchNo, capability);
        if (resolved.isPresent()) {
            return resolved.get();
        }
        // 2. 兜底:按通道商户号取首个应用
        Optional<AlipayDirectApp> byChannel = alipayDirectAppManager.findFirstByChannelMchNo(channelMchNo);
        if (byChannel.isPresent()) {
            return byChannel.get();
        }
        // 3. 最终兜底:按商户号取首个应用(兼容单应用旧场景)
        // 支付宝: 直连商户应用不存在
        return alipayDirectAppManager.findFirstByMchNo(mchNo)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
    }
}
