package cn.daxpay.open.channel.sheng.service.config;

import cn.daxpay.open.channel.sheng.convert.ShengIsvKeyConfigConvert;
import cn.daxpay.open.channel.sheng.dao.ShengIsvKeyConfigManager;
import cn.daxpay.open.channel.sheng.entity.ShengIsvKeyConfig;
import cn.daxpay.open.channel.sheng.param.ShengIsvKeyConfigParam;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 盛付通服务商密钥配置
///
/// 管理产品级全局的盛付通服务商密钥配置(sheng_isv), 查询时不存在则创建默认记录,
/// 保存时合并敏感字段(空值不覆盖)。平台为唯一服务商, 每产品仅一条配置。
/// 盛付通不提供集测接口, 无沙箱双环境。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengIsvKeyConfigService {

    private final ShengIsvKeyConfigManager shengIsvKeyConfigManager;

    /// 根据产品编码查询密钥配置, 不存在则创建默认记录
    ///
    /// 服务商密钥不挂商户维度([cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity]),
    /// 无需回填 mchNo。
    @Transactional(rollbackFor = Exception.class)
    public ShengIsvKeyConfig findByProduct(String product) {
        var existing = shengIsvKeyConfigManager.findByProduct(product);
        if (existing.isPresent()) {
            return existing.get();
        }
        var config = new ShengIsvKeyConfig()
                .setProduct(product);
        shengIsvKeyConfigManager.save(config);
        return config;
    }

    /// 支付场景查询服务商密钥(必填校验, 不创建记录)
    ///
    /// 只读不写: 记录不存在或关键字段(shengMchId/merchantPrivateKey/shengpayPublicKey)任一为空时 fail-fast,
    /// 避免空凭证下发到子应用后子应用才发现问题。
    public ShengIsvKeyConfig getByProductForPay(String product) {
        ShengIsvKeyConfig config = shengIsvKeyConfigManager.findByProduct(product)
                // 盛付通: 服务商密钥未配置
                .orElseThrow(() -> new BizInfoException("error.channel.sheng.isvKeyNotConfigured"));
        if (StrUtil.hasBlank(config.getShengMchId(), config.getMerchantPrivateKey(), config.getShengpayPublicKey())) {
            throw new BizInfoException("error.channel.sheng.isvKeyNotConfigured");
        }
        return config;
    }

    /// 保存服务商密钥配置(更新)
    ///
    /// 以 product 定位记录, 仅更新可编辑字段;
    /// product 为不可变身份字段, 由 findByProduct 从 DB 加载后保持不变。
    @Transactional(rollbackFor = Exception.class)
    public void save(ShengIsvKeyConfigParam param) {
        var config = this.findByProduct(param.getProduct());
        ShengIsvKeyConfigConvert.CONVERT.copy(param, config);
        shengIsvKeyConfigManager.updateById(config);
    }
}
