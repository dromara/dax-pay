package cn.daxpay.open.payment.merchant.service.config;

import cn.daxpay.open.payment.merchant.convert.config.MchRiskConfigConvert;
import cn.daxpay.open.payment.merchant.dao.config.MchRiskConfigManager;
import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.config.MchRiskConfig;
import cn.daxpay.open.payment.merchant.param.config.MchRiskConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchRiskConfigResult;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/// # 商户风控配置服务
///
/// 商户级风控配置(1:1 商户)的读写。支付链路通过 [#getConfigForPayment] 高频读取(带 Caffeine 缓存),
/// 运营端通过 [#saveOrUpdate] 更新并失效缓存。
///
/// 围栏两级门控: 平台总闸 + 商户 geoFenceEnabled; 围栏策略为平台全局配置, 非商户级。
@Slf4j
@Service
@RequiredArgsConstructor
public class MchRiskConfigService {

    private final MchRiskConfigManager mchRiskConfigManager;
    private final MerchantInfoManager merchantInfoManager;

    /// 商户风控配置支付链路读取缓存(短 TTL, 配置改动可接受短延迟)
    private final Cache<String, MchRiskConfig> paymentReadCache = Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .maximumSize(10_000)
            .build();

    /// 运营端: 根据商户号查询风控配置, 无记录返回默认对象(不落库)
    public MchRiskConfigResult findByMchNo(String mchNo) {
        return mchRiskConfigManager.findByMchNo(mchNo)
                .map(MchRiskConfig::toResult)
                .orElseGet(() -> defaultResult(mchNo));
    }

    /// 支付链路: 读取商户风控配置(带缓存), 无记录返回默认对象(不落库)
    public MchRiskConfig getConfigForPayment(String mchNo) {
        if (StrUtil.isBlank(mchNo)) {
            return defaultConfig(null);
        }
        return paymentReadCache.get(mchNo, k -> mchRiskConfigManager.findByMchNo(k)
                .orElseGet(() -> defaultConfig(k)));
    }

    /// 运营端: 保存或更新(按商户号 upsert), 失效支付链路缓存
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(MchRiskConfigParam param) {
        // 校验商户存在
        merchantInfoManager.findByMchNo(param.getMchNo())
                // 商户: 商户不存在
                .orElseThrow(() -> new DataNotExistException("error.payment.merchant.merchantNotExist"));
        var existing = mchRiskConfigManager.findByMchNo(param.getMchNo());
        if (existing.isPresent()) {
            MchRiskConfig entity = existing.get();
            MchRiskConfigConvert.CONVERT.copy(param, entity);
            mchRiskConfigManager.updateById(entity);
        } else {
            MchRiskConfig entity = MchRiskConfigConvert.CONVERT.toEntity(param);
            // 运营端写 MchBaseEntity 必须显式 setMchNo(不装载商户 PaymentContext), 勿链式
            entity.setMchNo(param.getMchNo());
            mchRiskConfigManager.save(entity);
        }
        // 失效支付链路缓存
        paymentReadCache.invalidate(param.getMchNo());
    }

    private MchRiskConfig defaultConfig(String mchNo) {
        // MchRiskConfig 字段已有默认值(geoFenceEnabled=false), 仅补 mchNo
        // setMchNo 返回父类类型, 单独赋值勿链式
        MchRiskConfig config = new MchRiskConfig();
        config.setMchNo(mchNo);
        return config;
    }

    private MchRiskConfigResult defaultResult(String mchNo) {
        // setMchNo 返回父类类型, 单独赋值勿链式
        MchRiskConfigResult result = new MchRiskConfigResult();
        result.setMchNo(mchNo);
        result.setGeoFenceEnabled(false);
        return result;
    }
}
