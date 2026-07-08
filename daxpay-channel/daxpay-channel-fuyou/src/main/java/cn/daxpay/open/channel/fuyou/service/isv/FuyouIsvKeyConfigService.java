package cn.daxpay.open.channel.fuyou.service.isv;

import cn.daxpay.open.channel.fuyou.convert.isv.FuyouIsvKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.dao.isv.FuyouIsvKeyConfigManager;
import cn.daxpay.open.channel.fuyou.entity.isv.FuyouIsvKeyConfig;
import cn.daxpay.open.channel.fuyou.param.isv.FuyouIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 富友服务商密钥配置
///
/// 管理服务商密钥配置, 查询时不存在则创建默认记录(平台为唯一服务商, 密钥全局唯一)。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouIsvKeyConfigService {

    private final FuyouIsvKeyConfigManager fuyouIsvKeyConfigManager;

    /// 根据产品编码和沙箱标志查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public FuyouIsvKeyConfig findByProduct(String product, boolean sandbox) {
        var existing = fuyouIsvKeyConfigManager.findByProductAndSandbox(product, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new FuyouIsvKeyConfig()
                .setProduct(product)
                .setSandbox(sandbox);
        fuyouIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 记录不存在或关键字段(fyAppId/privateKey/publicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public FuyouIsvKeyConfig getByProductForPay(String product, boolean sandbox) {
        FuyouIsvKeyConfig config = fuyouIsvKeyConfigManager.findByProductAndSandbox(product, sandbox)
                // 富友: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.fuyou.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getFyAppId(), config.getPrivateKey(), config.getPublicKey())) {
            throw new BizInfoException("error.channel.fuyou.isvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(FuyouIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct(), Boolean.TRUE.equals(param.getSandbox()));
        FuyouIsvKeyConfigConvert.CONVERT.copy(param, config);
        fuyouIsvKeyConfigManager.updateById(config);
    }
}
