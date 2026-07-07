package cn.daxpay.open.channel.vbill.service.isv;

import cn.daxpay.open.channel.vbill.convert.isv.VbillIsvKeyConfigConvert;
import cn.daxpay.open.channel.vbill.dao.isv.VbillIsvKeyConfigManager;
import cn.daxpay.open.channel.vbill.entity.isv.VbillIsvKeyConfig;
import cn.daxpay.open.channel.vbill.param.isv.VbillIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 随行付服务商密钥配置
///
/// 管理服务商密钥配置, 查询时不存在则创建默认记录(平台为唯一服务商, 密钥全局唯一)。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillIsvKeyConfigService {

    private final VbillIsvKeyConfigManager vbillIsvKeyConfigManager;

    /// 根据产品编码查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public VbillIsvKeyConfig findByProduct(String product) {
        var existing = vbillIsvKeyConfigManager.findByProduct(product);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new VbillIsvKeyConfig()
                .setProduct(product);
        vbillIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 记录不存在或关键字段(orgId/privateKey/publicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public VbillIsvKeyConfig getByProductForPay(String product) {
        VbillIsvKeyConfig config = vbillIsvKeyConfigManager.findByProduct(product)
                // 随行付: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.vbill.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getOrgId(), config.getPrivateKey(), config.getPublicKey())) {
            throw new BizInfoException("error.channel.vbill.isvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(VbillIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct());
        VbillIsvKeyConfigConvert.CONVERT.copy(param, config);
        vbillIsvKeyConfigManager.updateById(config);
    }
}
