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

/// # 支付宝直连通道配置组装器
///
/// 从进件商户对象([AlipayDirectApp] + [AlipayDirectAppKeyConfig])读取密钥/证书,
/// 并补充主应用回调地址(notifyUrl)与沙箱标志(sandbox), 组装为下发给子应用的 config Map。
///
/// 供支付策略([cn.daxpay.open.channel.alipay.strategy.direct.AlipayDirectPayStrategy])
/// 与回调验签([cn.daxpay.open.channel.alipay.service.callback.AlipayCallbackService])复用, 避免重复读取配置。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectConfigAssembler {

    private final AlipayDirectAppManager alipayDirectAppManager;
    private final AlipayDirectAppKeyConfigService alipayDirectAppKeyConfigService;

    /// 组装直连商户的通道调用配置(下发给子应用)
    ///
    /// @param mchNo 商户号
    /// @return 通道配置 Map, 字段对齐子应用 `AlipayConfigDto`
    public Map<String, Object> buildConfig(String mchNo) {
        AlipayDirectApp app = alipayDirectAppManager.findFirstByMchNo(mchNo)
                // 支付宝: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
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
}
