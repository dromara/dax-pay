package cn.daxpay.open.channel.sheng.service.config;

import cn.daxpay.open.channel.sheng.convert.ShengIsvKeyConfigConvert;
import cn.daxpay.open.channel.sheng.dao.ShengIsvKeyConfigManager;
import cn.daxpay.open.channel.sheng.entity.ShengIsvKeyConfig;
import cn.daxpay.open.channel.sheng.param.ShengIsvKeyConfigParam;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 盛付通服务商密钥配置
///
/// 管理产品级全局的盛付通服务商密钥配置(sheng_isv), 查询只读回显(不存在不落库),
/// 保存时加载或创建并合并敏感字段(空值不覆盖)。平台为唯一服务商, 每产品仅一条配置。
/// 盛付通不提供集测接口, 无沙箱双环境。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengIsvKeyConfigService {

    private final ShengIsvKeyConfigManager shengIsvKeyConfigManager;

    /// 根据产品编码查询密钥配置(只读回显, 不写库)
    ///
    /// 记录不存在时返回未持久化的空配置(仅 product) —— 避免 VIEW 权限的 GET 产生写副作用,
    /// 新记录由 [save] 以 MANAGE 权限创建。
    /// 服务商密钥不挂商户维度([cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity]),
    /// 无需回填 mchNo。
    public ShengIsvKeyConfig findByProduct(String product) {
        validateProduct(product);
        return shengIsvKeyConfigManager.findByProduct(product)
                .orElseGet(() -> new ShengIsvKeyConfig().setProduct(product));
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

    /// 保存服务商密钥配置(不存在则创建)
    ///
    /// 以 product 定位记录, 仅更新可编辑字段; product 为不可变身份字段;
    /// 记录不存在时立即落库(保存接口以 MANAGE 权限为前提)。
    @Transactional(rollbackFor = Exception.class)
    public void save(ShengIsvKeyConfigParam param) {
        validateProduct(param.getProduct());
        var config = shengIsvKeyConfigManager.findByProduct(param.getProduct())
                .orElseGet(() -> {
                    var created = new ShengIsvKeyConfig()
                            .setProduct(param.getProduct());
                    shengIsvKeyConfigManager.save(created);
                    return created;
                });
        ShengIsvKeyConfigConvert.CONVERT.copy(param, config);
        shengIsvKeyConfigManager.updateById(config);
    }

    /// 产品白名单校验: 服务商密钥仅服务于盛付通服务商产品(sheng_isv)
    ///
    /// product 为外部入参, 不校验会为任意字符串(如其他产品码/乱值)建立全局密钥记录。
    private void validateProduct(String product) {
        if (!ProductEnum.SHENG_ISV.getCode().equals(product)) {
            // 盛付通: 该接口仅支持服务商产品(sheng_isv)
            throw new BizInfoException("error.channel.sheng.invalidProduct");
        }
    }
}
