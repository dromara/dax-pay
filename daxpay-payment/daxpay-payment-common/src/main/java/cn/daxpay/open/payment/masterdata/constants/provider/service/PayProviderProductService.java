package cn.daxpay.open.payment.masterdata.constants.provider.service;

import cn.daxpay.open.payment.masterdata.constants.provider.dao.PayProviderMethodManager;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductResult;
import cn.daxpay.open.payment.masterdata.constants.provider.result.PayProviderProductResult;
import cn.daxpay.open.payment.masterdata.constants.product.service.PayProductCapabilityService;
import cn.daxpay.open.payment.masterdata.constants.product.service.PayProductService;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum;
import cn.daxpay.open.platform.core.model.PayProviderMethodEntry;
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