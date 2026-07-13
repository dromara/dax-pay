package cn.daxpay.open.payment.masterdata.constants.product.service;

import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayProductCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.capability.entity.PayCapability;
import cn.daxpay.open.payment.masterdata.constants.product.entity.PayProductCapability;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductCapabilityResult;
import cn.daxpay.open.payment.masterdata.constants.product.result.PayProductResult;
import cn.daxpay.open.payment.strategy.product.AbsProductStrategy;
import cn.daxpay.open.payment.strategy.ProductStrategySupport;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/// # 支付产品与支付能力关联
///
/// 查询产品挂载了哪些支付能力，并判断某支付产品是否支持某种支付方式。
/// 方式→能力以各产品策略 {@link AbsProductStrategy#methodCapabilityMapping()} 为准，再与 `pay_md_product_capability` 求交。
/// 内调约定：`productCode` / `methodCode` 均为已解析的合法编码，不在本类重复判空。
@Service
@RequiredArgsConstructor
public class PayProductCapabilityService {

    private final PayProductCapabilityManager payProductCapabilityManager;
    private final PayCapabilityManager payCapabilityManager;


    /// 预加载「支付产品 → 能力编码」，供批量判断使用
    public Map<String, Set<String>> loadCapabilityCodesByProduct() {
        Map<String, PayCapability> capabilityMap = payCapabilityManager.mapByCode();
        Map<String, Set<String>> capabilityCodesByProduct = new HashMap<>();
        for (PayProductCapability rel : payProductCapabilityManager.listAllOrdered()) {
            PayCapability cap = capabilityMap.get(rel.getCapabilityCode());
            if (cap == null) {
                continue;
            }
            capabilityCodesByProduct
                    .computeIfAbsent(rel.getProductCode(), key -> new HashSet<>())
                    .add(rel.getCapabilityCode());
        }
        return capabilityCodesByProduct;
    }

    /// 用预加载结果判断产品是否支持该支付方式（策略声明能力 ∩ 已挂载能力，不再查库）
    public boolean supportsMethod(Map<String, Set<String>> capabilityCodesByProduct, String productCode, String methodCode) {
        Set<String> mountedCodes = capabilityCodesByProduct.getOrDefault(productCode, Set.of());
        return strategyCapabilitiesForMethod(productCode, methodCode).stream()
                .map(PayCapabilityEnum::getCode)
                .anyMatch(mountedCodes::contains);
    }

    /// 判断产品是否支持该支付方式（策略声明能力 ∩ 已挂载能力，按产品查库）
    public boolean productSupportsMethod(String productCode, String methodCode) {
        List<PayProductCapability> rels = payProductCapabilityManager.listByProduct(productCode);
        if (rels.isEmpty()) {
            return false;
        }
        // 批量获取能力主数据（替代逐个 findByCode）
        Set<String> capabilityCodes = rels.stream()
                .map(PayProductCapability::getCapabilityCode)
                .collect(Collectors.toSet());
        Set<String> validCodes = payCapabilityManager.listByCodes(capabilityCodes).stream()
                .map(PayCapability::getCode)
                .collect(Collectors.toSet());
        Set<String> mountedCodes = rels.stream()
                .map(PayProductCapability::getCapabilityCode)
                .filter(validCodes::contains)
                .collect(Collectors.toSet());
        return strategyCapabilitiesForMethod(productCode, methodCode).stream()
                .map(PayCapabilityEnum::getCode)
                .anyMatch(mountedCodes::contains);
    }

    /// 填充单个产品的支付能力列表
    public void fillCapabilities(PayProductResult result) {
        if (StrUtil.isBlank(result.getCode())) {
            return;
        }
        fillCapabilitiesBatch(List.of(result));
    }

    /// 批量填充产品的支付能力列表
    public void fillCapabilitiesBatch(List<PayProductResult> products) {
        if (CollUtil.isEmpty(products)) {
            return;
        }
        Set<String> productCodes = products.stream()
                .map(PayProductResult::getCode)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        if (productCodes.isEmpty()) {
            return;
        }
        List<PayProductCapability> allRels = payProductCapabilityManager.listByProductCodes(productCodes);
        Map<String, List<PayProductCapability>> relsByProduct = allRels.stream()
                .collect(Collectors.groupingBy(PayProductCapability::getProductCode));
        Set<String> capabilityCodes = allRels.stream()
                .map(PayProductCapability::getCapabilityCode)
                .collect(Collectors.toSet());
        Map<String, PayCapability> capabilityMap = payCapabilityManager.listByCodes(capabilityCodes).stream()
                .collect(Collectors.toMap(PayCapability::getCode, e -> e, (a, b) -> a));

        for (PayProductResult product : products) {
            if (StrUtil.isBlank(product.getCode())) {
                product.setCapabilities(List.of());
                continue;
            }
            List<PayProductCapability> rels = relsByProduct.getOrDefault(product.getCode(), List.of());
            List<PayProductCapabilityResult> items = new ArrayList<>();
            for (PayProductCapability rel : rels) {
                PayCapability cap = capabilityMap.get(rel.getCapabilityCode());
                if (cap == null) {
                    continue;
                }
                items.add(toItem(rel, cap));
            }
            product.setCapabilities(items);
        }
    }

    /// 关联行转为能力展示项
    private PayProductCapabilityResult toItem(PayProductCapability rel, PayCapability cap) {
        var item = new PayProductCapabilityResult()
                .setCode(cap.getCode())
                .setSortNo(rel.getSortNo());
        PayCapabilityEnum capabilityEnum = PayCapabilityEnum.findByCode(cap.getCode());
        if (capabilityEnum != null) {
            item.setName(I18nUtil.getEnumName(capabilityEnum));
        } else {
            item.setName(cap.getCode());
        }
        return item;
    }

    /// 产品策略在该支付方式下声明的支付能力列表
    private List<PayCapabilityEnum> strategyCapabilitiesForMethod(String productCode, String methodCode) {
        if (!PaymentStrategyFactory.existsByProduct(productCode, AbsProductStrategy.class)) {
            return List.of();
        }
        PayMethodEnum method = findMethodByCode(methodCode);
        if (method == null) {
            return List.of();
        }
        AbsProductStrategy strategy = PaymentStrategyFactory.createByProduct(productCode, AbsProductStrategy.class);
        return ProductStrategySupport.capabilitiesForMethod(strategy, method);
    }

    /// 按编码解析支付方式枚举（未知编码返回 null，不抛异常）
    private static PayMethodEnum findMethodByCode(String code) {
        return Arrays.stream(PayMethodEnum.values())
                .filter(method -> Objects.equals(method.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}