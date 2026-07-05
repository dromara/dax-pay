package cn.daxpay.open.channel.wechat.service.isv;

import cn.daxpay.open.channel.wechat.convert.isv.WechatIsvKeyConfigConvert;
import cn.daxpay.open.channel.wechat.dao.isv.WechatIsvKeyConfigManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvKeyConfig;
import cn.daxpay.open.channel.wechat.param.isv.WechatIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 微信服务商密钥配置
///
/// 管理服务商密钥配置，查询时不存在则创建默认记录（平台为唯一服务商，密钥全局唯一）。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvKeyConfigService {

    private final WechatIsvKeyConfigManager wechatIsvKeyConfigManager;

    /// 根据产品编码查询密钥配置, 不存在则创建默认记录
    /// 注意: 微信服务商模式不支持沙箱环境
    @Transactional(rollbackFor = Exception.class)
    public WechatIsvKeyConfig findByProduct(String product) {
        var existing = wechatIsvKeyConfigManager.findByProduct(product);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new WechatIsvKeyConfig()
                .setProduct(product);
        wechatIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 与 [findByProduct] 的 upsert 语义不同, 此方法只读不写:
    /// 记录不存在或关键字段(wxMchId/apiKeyV3/privateKey/certSerialNo)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public WechatIsvKeyConfig getByProductForPay(String product) {
        WechatIsvKeyConfig config = wechatIsvKeyConfigManager.findByProduct(product)
                .orElseThrow(() -> new BizInfoException("error.channel.wechat.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getWxMchId(), config.getApiKeyV3(),
                config.getPrivateKey(), config.getCertSerialNo())) {
            throw new BizInfoException("error.channel.wechat.isvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置
    /// 注意: 微信服务商模式不支持沙箱环境
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(WechatIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct());
        config.setWxMchId(param.getWxMchId());
        WechatIsvKeyConfigConvert.CONVERT.copy(param, config);
        wechatIsvKeyConfigManager.updateById(config);
    }
}
