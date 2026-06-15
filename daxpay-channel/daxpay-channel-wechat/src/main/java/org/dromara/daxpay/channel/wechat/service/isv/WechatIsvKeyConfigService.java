package org.dromara.daxpay.channel.wechat.service.isv;

import org.dromara.daxpay.channel.wechat.convert.isv.WechatIsvKeyConfigConvert;
import org.dromara.daxpay.channel.wechat.dao.isv.WechatIsvKeyConfigManager;
import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvKeyConfig;
import org.dromara.daxpay.channel.wechat.param.isv.WechatIsvKeyConfigParam;
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
