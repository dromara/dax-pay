package org.dromara.daxpay.payment.old.pay.service.masterdata.provider;

import org.dromara.daxpay.payment.old.pay.dao.masterdata.provider.PayProviderMethodManager;
import org.dromara.daxpay.payment.old.pay.entity.masterdata.provider.PayProvider;
import org.dromara.daxpay.payment.old.pay.result.masterdata.provider.PayProviderGroupResult;
import org.dromara.daxpay.payment.old.pay.result.masterdata.provider.PayProviderMethodResult;
import org.dromara.daxpay.payment.old.pay.result.masterdata.provider.PayProviderProductResult;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayProviderEnum;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.model.PayProviderMethodEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// # 管理端支付渠道配置
///
/// 按支付渠道分组展示支付方式，并附带每种方式支持的产品列表。
@Service
@RequiredArgsConstructor
public class PayProviderService {

    private final PayProviderMethodService payProviderMethodService;
    private final PayProviderProductService payProviderProductService;

    /// 按支付渠道分组，返回各渠道下的支付方式
    public List<PayProviderGroupResult> listByProvider() {
        return buildGroups(loadAdminDirectoryContext());
    }

    /// 按支付渠道编码与支付方式编码查一条配置详情（含支持的产品列表）
    public PayProviderMethodResult get(String providerCode, String methodCode) {
        if (PayProviderEnum.findByCode(providerCode) == null) {
            throw new DataNotExistException("error.payment.capability.invalidProvider");
        }
        AdminDirectoryContext adminContext = loadAdminDirectoryContext();
        PayProviderMethodService.MergedRelationRow row = adminContext.directoryContext().relationsByProvider()
                .getOrDefault(providerCode, List.of()).stream()
                .filter(r -> Objects.equals(r.methodEnum().getCode(), methodCode))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.payment.capability.methodNotInDirectory"));
        return toAdminMethod(providerCode, row, adminContext);
    }

    /// 预加载管理端展示所需数据
    private AdminDirectoryContext loadAdminDirectoryContext() {
        PayProviderMethodService.DirectoryLoadContext directoryContext =
                payProviderMethodService.loadDirectoryContext();
        List<PayProviderMethodEntry> directoryEntries =
                payProviderMethodService.listDirectoryEntriesFromContext(directoryContext);
        Map<String, List<PayProviderProductResult>> supportedProductsIndex =
                payProviderProductService.buildSupportedProductsIndex(directoryEntries);
        return new AdminDirectoryContext(directoryContext, supportedProductsIndex);
    }

    /// 按支付渠道分组组装列表
    private List<PayProviderGroupResult> buildGroups(AdminDirectoryContext adminContext) {
        PayProviderMethodService.DirectoryLoadContext directoryContext = adminContext.directoryContext();
        Map<String, PayProvider> providerMap = directoryContext.providerMap();
        List<PayProviderGroupResult> groups = new ArrayList<>();
        int brandOrdinal = 0;
        for (PayProviderEnum brandEnum : PayProviderEnum.values()) {
            PayProvider dbBrand = providerMap.get(brandEnum.getCode());
            PayProviderGroupResult group = new PayProviderGroupResult()
                    .setProvider(brandEnum.getCode())
                    .setProviderLabel(I18nUtil.getEnumName(brandEnum))
                    .setIcon(dbBrand != null ? dbBrand.getIcon() : null)
                    .setSortNo(resolveSortNo(dbBrand, brandOrdinal));
            List<PayProviderMethodResult> methods = directoryContext.relationsByProvider()
                    .getOrDefault(brandEnum.getCode(), List.of()).stream()
                    .map(row -> toAdminMethod(brandEnum.getCode(), row, adminContext))
                    .toList();
            group.setMethods(methods);
            groups.add(group);
            brandOrdinal++;
        }
        groups.sort(Comparator.comparingInt(g -> g.getSortNo() != null ? g.getSortNo() : 0));
        return groups;
    }

    /// 组装管理端单条渠道+方式详情
    private PayProviderMethodResult toAdminMethod(
            String providerCode,
            PayProviderMethodService.MergedRelationRow row,
            AdminDirectoryContext adminContext) {
        String methodCode = row.methodEnum().getCode();
        String pairKey = PayProviderMethodManager.pairKey(providerCode, methodCode);
        List<PayProviderProductResult> supportedProducts =
                adminContext.supportedProductsIndex().getOrDefault(pairKey, List.of());
        return new PayProviderMethodResult()
                .setProvider(providerCode)
                .setMethod(methodCode)
                .setMethodLabel(I18nUtil.getEnumName(row.methodEnum()))
                .setSortNo(row.sortNo())
                .setDescription(row.description())
                .setSupportedProducts(supportedProducts);
    }

    /// 取渠道排序号，无则使用枚举顺序
    private static int resolveSortNo(PayProvider dbBrand, int fallback) {
        return dbBrand != null && dbBrand.getSortNo() != null ? dbBrand.getSortNo() : fallback;
    }

    /// 管理端预加载：渠道方式配置与支持的产品索引
    private record AdminDirectoryContext(
            PayProviderMethodService.DirectoryLoadContext directoryContext,
            Map<String, List<PayProviderProductResult>> supportedProductsIndex) {
    }
}
