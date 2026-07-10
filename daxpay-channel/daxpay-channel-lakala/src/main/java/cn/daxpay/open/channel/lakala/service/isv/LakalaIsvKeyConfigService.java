package cn.daxpay.open.channel.lakala.service.isv;

import cn.daxpay.open.channel.lakala.convert.isv.LakalaIsvKeyConfigConvert;
import cn.daxpay.open.channel.lakala.dao.isv.LakalaIsvKeyConfigManager;
import cn.daxpay.open.channel.lakala.entity.isv.LakalaIsvKeyConfig;
import cn.daxpay.open.channel.lakala.param.isv.LakalaIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 拉卡拉服务商密钥配置
///
/// 管理服务商密钥配置, 查询时不存在则创建默认记录(平台为唯一服务商, 密钥全局唯一)。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaIsvKeyConfigService {

    private final LakalaIsvKeyConfigManager lakalaIsvKeyConfigManager;

    /// 根据产品编码和沙箱标志查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public LakalaIsvKeyConfig findByProduct(String product, boolean sandbox) {
        var existing = lakalaIsvKeyConfigManager.findByProductAndSandbox(product, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new LakalaIsvKeyConfig()
                .setProduct(product)
                .setSandbox(sandbox);
        lakalaIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 与 [#findByProduct] 的 upsert 语义不同, 此方法只读不写:
    /// 记录不存在或关键字段(lklAppId/privateKey/publicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public LakalaIsvKeyConfig getByProductForPay(String product, boolean sandbox) {
        LakalaIsvKeyConfig config = lakalaIsvKeyConfigManager.findByProductAndSandbox(product, sandbox)
                // 拉卡拉: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.lakala.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getLklAppId(), config.getPrivateKey(), config.getPublicKey())) {
            throw new BizInfoException("error.channel.lakala.isvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(LakalaIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct(), Boolean.TRUE.equals(param.getSandbox()));
        LakalaIsvKeyConfigConvert.CONVERT.copy(param, config);
        lakalaIsvKeyConfigManager.updateById(config);
    }
}
