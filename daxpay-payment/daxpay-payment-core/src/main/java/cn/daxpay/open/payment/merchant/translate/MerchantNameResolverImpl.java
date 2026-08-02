package cn.daxpay.open.payment.merchant.translate;

import cn.daxpay.open.payment.merchant.dao.info.MerchantInfoManager;
import cn.daxpay.open.payment.merchant.entity.info.MerchantInfo;
import cn.daxpay.open.platform.core.spi.MerchantNameResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/// # 商户名称解析器实现
///
/// 由 payment 模块提供, 供 platform 模块(敏感词命中/统一支付日志等)翻译 mchNo -> mchName.
/// 忽略租户, 运营端跨租户查看
@Component
@RequiredArgsConstructor
public class MerchantNameResolverImpl implements MerchantNameResolver {

    private final MerchantInfoManager merchantInfoManager;

    @Override
    public Map<String, String> resolveNames(Collection<String> mchNos) {
        if (mchNos == null || mchNos.isEmpty()) {
            return Collections.emptyMap();
        }
        return merchantInfoManager.findAllByMchNosNotTenant(mchNos).stream()
                .filter(m -> m.getMchNo() != null && m.getMchName() != null)
                .collect(Collectors.toMap(MerchantInfo::getMchNo, MerchantInfo::getMchName, (a, b) -> a));
    }
}
