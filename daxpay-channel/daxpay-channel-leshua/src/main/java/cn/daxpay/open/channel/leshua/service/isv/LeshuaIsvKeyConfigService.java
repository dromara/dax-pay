package cn.daxpay.open.channel.leshua.service.isv;

import cn.daxpay.open.channel.leshua.convert.isv.LeshuaIsvKeyConfigConvert;
import cn.daxpay.open.channel.leshua.dao.isv.LeshuaIsvKeyConfigManager;
import cn.daxpay.open.channel.leshua.entity.isv.LeshuaIsvKeyConfig;
import cn.daxpay.open.channel.leshua.param.isv.LeshuaIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 乐刷服务商密钥配置
///
/// 管理服务商密钥配置, 查询时不存在则创建默认记录(平台为唯一服务商, 密钥全局唯一)。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaIsvKeyConfigService {

    private final LeshuaIsvKeyConfigManager leshuaIsvKeyConfigManager;

    /// 根据产品编码和沙箱标志查询密钥配置, 不存在则创建默认记录
    @Transactional(rollbackFor = Exception.class)
    public LeshuaIsvKeyConfig findByProduct(String product, boolean sandbox) {
        var existing = leshuaIsvKeyConfigManager.findByProductAndSandbox(product, sandbox);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new LeshuaIsvKeyConfig()
                .setProduct(product)
                .setSandbox(sandbox);
        leshuaIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 与 [#findByProduct] 的 upsert 语义不同, 此方法只读不写:
    /// 记录不存在或关键字段(lsMchNo/tradeKey/signType)任一为空时 fail-fast。
    public LeshuaIsvKeyConfig getByProductForPay(String product, boolean sandbox) {
        LeshuaIsvKeyConfig config = leshuaIsvKeyConfigManager.findByProductAndSandbox(product, sandbox)
                // 乐刷: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.leshua.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getLsMchNo(), config.getTradeKey(), config.getSignType())) {
            throw new BizInfoException("error.channel.leshua.isvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(LeshuaIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct(), Boolean.TRUE.equals(param.getSandbox()));
        LeshuaIsvKeyConfigConvert.CONVERT.copy(param, config);
        leshuaIsvKeyConfigManager.updateById(config);
    }
}
