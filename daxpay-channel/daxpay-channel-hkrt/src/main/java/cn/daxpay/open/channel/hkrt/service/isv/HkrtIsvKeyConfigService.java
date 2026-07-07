package cn.daxpay.open.channel.hkrt.service.isv;

import cn.daxpay.open.channel.hkrt.convert.isv.HkrtIsvKeyConfigConvert;
import cn.daxpay.open.channel.hkrt.dao.isv.HkrtIsvKeyConfigManager;
import cn.daxpay.open.channel.hkrt.entity.isv.HkrtIsvKeyConfig;
import cn.daxpay.open.channel.hkrt.param.isv.HkrtIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 海科融通服务商密钥配置
///
/// 管理服务商密钥配置, 查询时不存在则创建默认记录(平台为唯一服务商, 密钥全局唯一)。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtIsvKeyConfigService {

    private final HkrtIsvKeyConfigManager hkrtIsvKeyConfigManager;

    /// 根据产品编码查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public HkrtIsvKeyConfig findByProduct(String product) {
        var existing = hkrtIsvKeyConfigManager.findByProduct(product);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new HkrtIsvKeyConfig()
                .setProduct(product);
        hkrtIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 与 [findByProduct] 的 upsert 语义不同, 此方法只读不写:
    /// 记录不存在或关键字段(agentNo/accessId/accessKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public HkrtIsvKeyConfig getByProductForPay(String product) {
        HkrtIsvKeyConfig config = hkrtIsvKeyConfigManager.findByProduct(product)
                // 海科融通: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("channel.error.hkrtIsvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getAgentNo(), config.getAccessId(), config.getAccessKey())) {
            throw new BizInfoException("channel.error.hkrtIsvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(HkrtIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct());
        HkrtIsvKeyConfigConvert.CONVERT.copy(param, config);
        hkrtIsvKeyConfigManager.updateById(config);
    }
}
