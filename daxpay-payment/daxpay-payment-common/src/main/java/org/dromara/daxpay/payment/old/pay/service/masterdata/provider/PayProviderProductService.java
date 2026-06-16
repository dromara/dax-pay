package org.dromara.daxpay.payment.old.pay.service.masterdata.provider;

import org.dromara.daxpay.payment.old.pay.dao.masterdata.provider.PayProviderMethodManager;
import org.dromara.daxpay.payment.old.pay.result.masterdata.product.PayProductResult;
import org.dromara.daxpay.payment.old.pay.result.masterdata.provider.PayProviderProductResult;
import org.dromara.daxpay.payment.old.pay.service.masterdata.product.PayProductCapabilityService;
import org.dromara.daxpay.payment.old.pay.service.masterdata.product.PayProductService;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.model.PayProviderMethodEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/// # 支付渠道与支付方式对应的产品
///
/// 计算每种「支付渠道 + 支付方式」可用的支付产品列表。
@Service
@RequiredArgsConstructor
public class PayProviderProductService {

    private final PayProductService payProductService;
    private final PayProductCapabilityService payProductCapabilityService;

    /// 构建「渠道+方式 → 产品列表」索引，供管理端批量展示
    public Map<String, List<PayProviderProductResult>> buildSupportedProductsIndex(
            List<PayProviderMethodEntry> directoryEntries) {
        Map<String, List<PayProviderProductResult>> index = new HashMap<>();
        if (directoryEntries.isEmpty()) {
            return index;
        }
        Map<String, Set<String>> capabilityCodesByProduct = payProductCapabilityService.loadCapabilityCodesByProduct();
        for (PayProductResult product : payProductService.listAll()) {
            PayProviderProductResult productItem = new PayProviderProductResult()
                    .setLabel(product.getName())
                    .setValue(product.getCode())
                    .setChannel(product.getChannel())
                    .setChannelName(product.getChannelName() != null
                            ? product.getChannelName()
                            : I18nUtil.getEnumName(ChannelEnum.findByCode(product.getChannel())));
            for (PayProviderMethodEntry entry : directoryEntries) {
                if (!payProductCapabilityService.supportsMethod(
                        capabilityCodesByProduct, product.getCode(), entry.getMethodCode())) {
                    continue;
                }
                String pairKey = PayProviderMethodManager.pairKey(entry.getProviderCode(), entry.getMethodCode());
                index.computeIfAbsent(pairKey, key -> new ArrayList<>()).add(productItem);
            }
        }
        return index;
    }
}