package cn.daxpay.open.channel.dougong.service.isv;

import cn.daxpay.open.channel.dougong.convert.isv.DougongIsvKeyConfigConvert;
import cn.daxpay.open.channel.dougong.dao.isv.DougongIsvKeyConfigManager;
import cn.daxpay.open.channel.dougong.entity.isv.DougongIsvKeyConfig;
import cn.daxpay.open.channel.dougong.param.isv.DougongIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 斗拱服务商密钥配置
///
/// 管理服务商密钥配置, 查询时不存在则创建默认记录(平台为唯一服务商, 密钥全局唯一)。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongIsvKeyConfigService {

    private final DougongIsvKeyConfigManager dougongIsvKeyConfigManager;

    /// 根据产品编码查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public DougongIsvKeyConfig findByProduct(String product) {
        var existing = dougongIsvKeyConfigManager.findByProduct(product);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new DougongIsvKeyConfig()
                .setProduct(product);
        dougongIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 记录不存在或关键字段(sysId/productId/privateKey/dgPublicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public DougongIsvKeyConfig getByProductForPay(String product) {
        DougongIsvKeyConfig config = dougongIsvKeyConfigManager.findByProduct(product)
                // 斗拱: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.dougong.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getSysId(), config.getProductId(), config.getPrivateKey(), config.getDgPublicKey())) {
            throw new BizInfoException("error.channel.dougong.isvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(DougongIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct());
        DougongIsvKeyConfigConvert.CONVERT.copy(param, config);
        dougongIsvKeyConfigManager.updateById(config);
    }
}
