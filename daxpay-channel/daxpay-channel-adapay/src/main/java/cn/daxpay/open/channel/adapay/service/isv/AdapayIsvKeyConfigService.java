package cn.daxpay.open.channel.adapay.service.isv;

import cn.daxpay.open.channel.adapay.convert.isv.AdapayIsvKeyConfigConvert;
import cn.daxpay.open.channel.adapay.dao.isv.AdapayIsvKeyConfigManager;
import cn.daxpay.open.channel.adapay.entity.isv.AdapayIsvKeyConfig;
import cn.daxpay.open.channel.adapay.param.isv.AdapayIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # Adapay 服务商密钥配置
///
/// 管理服务商密钥配置, 查询时不存在则创建默认记录(平台为唯一服务商, 同一环境仅一条)。
/// 服务商配置与通道商户直连密钥配置([cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig])并存,
/// 本配置承载平台服务商主体身份(服务商号/交易密钥/签名密钥), 供服务商台账与进件等场景使用。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayIsvKeyConfigService {

    private final AdapayIsvKeyConfigManager adapayIsvKeyConfigManager;

    /// 根据沙箱环境查询密钥配置, 不存在则创建默认记录
    ///
    /// @param sandbox 沙箱标志(生产/沙箱双环境并存, 按环境分别存一份服务商配置)
    @Transactional(rollbackFor = Exception.class)
    public AdapayIsvKeyConfig findBySandbox(boolean sandbox) {
        var existing = adapayIsvKeyConfigManager.findBySandbox(sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new AdapayIsvKeyConfig().setSandbox(sandbox);
        adapayIsvKeyConfigManager.save(config);
        return config;
    }

    /// 保存服务商密钥配置(更新, 空值不覆盖)
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(AdapayIsvKeyConfigParam param) {
        boolean sandbox = Boolean.TRUE.equals(param.getSandbox());
        var config = this.findBySandbox(sandbox);
        AdapayIsvKeyConfigConvert.CONVERT.copy(param, config);
        adapayIsvKeyConfigManager.updateById(config);
    }

    /// 台账/进件场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 记录不存在或关键字段(isvNo/apiKey/privateKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public AdapayIsvKeyConfig getForPay(boolean sandbox) {
        AdapayIsvKeyConfig config = adapayIsvKeyConfigManager.findBySandbox(sandbox)
                // Adapay: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.adapay.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getIsvNo(), config.getApiKey(), config.getPrivateKey())) {
            throw new BizInfoException("error.channel.adapay.isvKeyNotConfigured");
        }
        return config;
    }
}
