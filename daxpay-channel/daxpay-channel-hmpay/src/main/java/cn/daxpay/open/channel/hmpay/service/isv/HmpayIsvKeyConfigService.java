package cn.daxpay.open.channel.hmpay.service.isv;

import cn.daxpay.open.channel.hmpay.convert.isv.HmpayIsvKeyConfigConvert;
import cn.daxpay.open.channel.hmpay.dao.isv.HmpayIsvKeyConfigManager;
import cn.daxpay.open.channel.hmpay.entity.isv.HmpayIsvKeyConfig;
import cn.daxpay.open.channel.hmpay.param.isv.HmpayIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 河马付服务商密钥配置
///
/// 管理服务商密钥配置, 查询时不存在则创建默认记录(平台为唯一服务商, 密钥全局唯一)。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayIsvKeyConfigService {

    private final HmpayIsvKeyConfigManager hmpayIsvKeyConfigManager;

    /// 根据产品编码和沙箱标志查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public HmpayIsvKeyConfig findByProduct(String product, boolean sandbox) {
        var existing = hmpayIsvKeyConfigManager.findByProductAndSandbox(product, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new HmpayIsvKeyConfig()
                .setProduct(product)
                .setSandbox(sandbox);
        hmpayIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 记录不存在或关键字段(sandAppId/privateKey/publicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public HmpayIsvKeyConfig getByProductForPay(String product, boolean sandbox) {
        HmpayIsvKeyConfig config = hmpayIsvKeyConfigManager.findByProductAndSandbox(product, sandbox)
                // 河马付: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("channel.error.hmpayIsvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getSandAppId(), config.getPrivateKey(), config.getPublicKey())) {
            throw new BizInfoException("channel.error.hmpayIsvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(HmpayIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct(), Boolean.TRUE.equals(param.getSandbox()));
        HmpayIsvKeyConfigConvert.CONVERT.copy(param, config);
        hmpayIsvKeyConfigManager.updateById(config);
    }
}
