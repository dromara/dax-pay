package cn.daxpay.open.payment.masterdata.constants.provider.service;

import cn.daxpay.open.payment.masterdata.constants.method.dao.PayMethodManager;
import cn.daxpay.open.payment.masterdata.constants.provider.dao.PayProviderManager;
import cn.daxpay.open.payment.masterdata.constants.provider.dao.PayProviderMethodManager;
import cn.daxpay.open.payment.masterdata.constants.method.entity.PayMethod;
import cn.daxpay.open.payment.masterdata.constants.provider.entity.PayProvider;
import cn.daxpay.open.payment.masterdata.constants.provider.entity.PayProviderMethod;
import cn.daxpay.open.payment.masterdata.constants.provider.result.PayProviderMethodResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.model.PayProviderMethodEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/// # 支付渠道与支付方式的配置与查询
///
/// 读取 `pay_md_provider`、`pay_md_method`、`pay_md_provider_method`，供下单校验和管理端「支付渠道」页面使用。
/// 内调约定：`providerCode` / `methodCode` 均为已解析的合法编码，不在本类重复判空。
@Service
@RequiredArgsConstructor
public class PayProviderMethodService {

    private final PayMethodManager payMethodManager;
    private final PayProviderManager payProviderManager;
    private final PayProviderMethodManager payProviderMethodManager;

    /// 预加载全部支付渠道、支付方式及二者关联
    public DirectoryLoadContext loadDirectoryContext() {
        Map<String, PayProvider> providerMap = payProviderManager.mapByCode();
        Map<String, PayMethod> methodMap = payMethodManager.mapByCode();
        Map<String, PayProviderMethod> relationMap = payProviderMethodManager.mapByPairKey();
        Map<String, List<PayProviderMethod>> realsByProvider = payProviderMethodManager.listAllOrdered().stream()
                .collect(Collectors.groupingBy(PayProviderMethod::getProvider));
        Map<String, List<MergedRelationRow>> relationsByProvider = new HashMap<>();
        for (Map.Entry<String, List<PayProviderMethod>> entry : realsByProvider.entrySet()) {
            relationsByProvider.put(entry.getKey(), mergeRelations(entry.getValue(), methodMap));
        }
        return new DirectoryLoadContext(providerMap, methodMap, relationMap, relationsByProvider);
    }

    /// 从预加载数据中取目录中的「渠道 + 支付方式」
    public List<PayProviderMethodEntry> listDirectoryEntriesFromContext(DirectoryLoadContext context) {
        List<PayProviderMethodEntry> entries = new ArrayList<>();
        for (PayProviderEnum providerEnum : PayProviderEnum.values()) {
            PayProvider dbProvider = context.providerMap().get(providerEnum.getCode());
            if (dbProvider != null && !dbProvider.isEnabled()) {
                continue;
            }
            for (MergedRelationRow row : context.relationsByProvider()
                    .getOrDefault(providerEnum.getCode(), List.of())) {
                entries.add(new PayProviderMethodEntry(providerEnum, row.methodEnum()));
            }
        }
        return entries;
    }

    /// 返回目录中全部「渠道 + 支付方式」
    public List<PayProviderMethodEntry> listDirectoryEntries() {
        DirectoryLoadContext context = loadDirectoryContext();
        return listDirectoryEntriesFromContext(context);
    }

    /// 判断该支付渠道下是否存在该支付方式（目录有效组合）
    public boolean contains(String providerCode, String methodCode) {
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        if (provider == null) {
            return false;
        }
        PayMethodEnum methodEnum = resolveMethodEnum(methodCode);
        if (methodEnum == null) {
            return false;
        }
        if (payMethodManager.findByCode(methodCode).isEmpty()) {
            return false;
        }
        return listMergedRelationsForProvider(providerCode).stream()
                .anyMatch(row -> Objects.equals(row.methodEnum().getCode(), methodCode));
    }

    /// 某支付渠道目录中的支付方式列表
    public List<PayMethodEnum> listMethodsForProvider(PayProviderEnum provider) {
        return listMergedRelationsForProvider(provider.getCode()).stream()
                .map(MergedRelationRow::methodEnum)
                .toList();
    }

    /// 同 {@link #listMethodsForProvider(PayProviderEnum)}
    public List<PayMethodEnum> listMethodsForBrand(PayProviderEnum brand) {
        return listMethodsForProvider(brand);
    }

    /// 平铺返回目录中的渠道与支付方式（不按渠道分组，不含支持的产品）
    public List<PayProviderMethodResult> listDirectoryFlat() {
        DirectoryLoadContext context = loadDirectoryContext();
        return listDirectoryEntriesFromContext(context).stream()
                .map(entry -> toFlatMethod(entry, context))
                .toList();
    }

    /// 管理端：某支付渠道下的全部支付方式
    public List<MergedRelationRow> listMergedRelationsForProvider(String providerCode) {
        Map<String, PayMethod> methodMap = payMethodManager.mapByCode();
        List<MergedRelationRow> rows = new ArrayList<>();
        int ordinal = 0;
        for (PayProviderMethod rel : payProviderMethodManager.listByProvider(providerCode)) {
            PayMethod methodRow = methodMap.get(rel.getMethod());
            if (methodRow == null) {
                continue;
            }
            PayMethodEnum methodEnum = resolveMethodEnum(rel.getMethod());
            if (methodEnum == null) {
                continue;
            }
            int sortNo = rel.getSortNo() != null ? rel.getSortNo() : ordinal;
            rows.add(new MergedRelationRow(methodEnum, sortNo, rel.getDescription()));
            ordinal++;
        }
        rows.sort(Comparator.comparingInt(MergedRelationRow::sortNo));
        return rows;
    }

    /// 转为平铺展示用的渠道+方式结果
    private PayProviderMethodResult toFlatMethod(PayProviderMethodEntry entry, DirectoryLoadContext context) {
        String pairKey = PayProviderMethodManager.pairKey(entry.getProviderCode(), entry.getMethodCode());
        PayProviderMethod relation = context.relationMap().get(pairKey);
        return new PayProviderMethodResult()
                .setProvider(entry.getProviderCode())
                .setMethod(entry.getMethodCode())
                .setMethodLabel(I18nUtil.getEnumName(entry.getMethod()))
                .setDescription(relation != null ? relation.getDescription() : null);
    }

    /// 合并关联表与支付方式主数据
    private List<MergedRelationRow> mergeRelations(List<PayProviderMethod> relations, Map<String, PayMethod> methodMap) {
        List<MergedRelationRow> rows = new ArrayList<>();
        int ordinal = 0;
        for (PayProviderMethod rel : relations) {
            PayMethod methodRow = methodMap.get(rel.getMethod());
            if (methodRow == null) {
                continue;
            }
            PayMethodEnum methodEnum = resolveMethodEnum(rel.getMethod());
            if (methodEnum == null) {
                continue;
            }
            int sortNo = rel.getSortNo() != null ? rel.getSortNo() : ordinal;
            rows.add(new MergedRelationRow(methodEnum, sortNo, rel.getDescription()));
            ordinal++;
        }
        rows.sort(Comparator.comparingInt(MergedRelationRow::sortNo));
        return rows;
    }

    /// 按编码解析支付方式枚举
    private PayMethodEnum resolveMethodEnum(String methodCode) {
        return Arrays.stream(PayMethodEnum.values())
                .filter(e -> Objects.equals(e.getCode(), methodCode))
                .findFirst()
                .orElse(null);
    }

    /// 预加载的渠道、方式、关联数据
    public record DirectoryLoadContext(
            Map<String, PayProvider> providerMap,
            Map<String, PayMethod> methodMap,
            Map<String, PayProviderMethod> relationMap,
            Map<String, List<MergedRelationRow>> relationsByProvider) {
    }

    /// 一条「渠道 + 支付方式」合并后的展示行
    public record MergedRelationRow(
            PayMethodEnum methodEnum,
            int sortNo,
            String description) {
    }
}