package org.dromara.daxpay.payment.masterdata.constants.product.service;

import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.masterdata.constants.product.dao.PayProductManager;
import org.dromara.daxpay.payment.masterdata.constants.product.entity.PayProduct;
import org.dromara.daxpay.payment.masterdata.constants.product.param.PayProductQuery;
import org.dromara.daxpay.payment.masterdata.constants.product.result.PayProductResult;
import org.dromara.daxpay.payment.strategy.product.AbsProductStrategy;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// # 支付产品主数据
///
/// 名称、支付通道、是否进件/终端/沙箱等。数据来自产品枚举、产品策略与 `pay_product` 表。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayProductService {

    private final PayProductManager payProductManager;
    private final PayProductCapabilityService payProductCapabilityService;

    /// 切换支付产品启停
    @Transactional(rollbackFor = Exception.class)
    public void switchEnabled(String code, boolean enabled) {
        PayProduct product = payProductManager.findByCode(code)
                .orElseThrow(() -> new DataNotExistException("error.payment.product.notExist"));
        product.setEnabled(enabled);
        payProductManager.saveOrUpdate(product);
    }

    /// 分页查询支付产品
    public PageResult<PayProductResult> page(PageParam pageParam, PayProductQuery query, String nameKeyword) {
        PageResult<PayProductResult> pageResult = MpUtil.toPageResult(payProductManager.page(pageParam, query));
        pageResult.setRecords(pageResult.getRecords().stream()
                .map(this::fillProductFeatures)
                .filter(row -> matchNameKeyword(row, nameKeyword))
                .toList());
        return pageResult;
    }

    /// 根据产品编码查询
    public PayProductResult findByCode(String code) {
        PayProduct payProduct = payProductManager.findByCode(code)
                .orElseThrow(() -> new DataNotExistException("error.payment.product.notExist"));
        return enrich(payProduct.toResult());
    }

    /// 查询全部支付产品，按 sortNo 升序排列
    public List<PayProduct> listSortedProducts() {
        return payProductManager.lambdaQuery()
                .orderByAsc(PayProduct::getSortNo)
                .list();
    }

    /// 按支付通道编码查询所属支付产品
    public List<PayProductResult> listByChannel(String channelCode) {
        return payProductManager.listByChannel(channelCode).stream()
                .map(PayProduct::toResult)
                .map(this::fillProductFeatures)
                .toList();
    }

    /// 返回全部支付产品，按 sortNo、id 排序
    /// 策略存在时补全特性字段，不存在时仅返回枚举与数据库基础信息
    public List<PayProductResult> listAll() {
        Map<String, PayProduct> dbMap = payProductManager.lambdaQuery().list().stream()
                .collect(Collectors.toMap(PayProduct::getCode, p -> p, (a, b) -> a));

        return Arrays.stream(ProductEnum.values())
                .map(e -> toProductResult(e, dbMap))
                .sorted(Comparator.comparing(PayProductResult::getSortNo,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(PayProductResult::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    /// 支付产品下拉选项
    public List<LabelValue> dropdown() {
        return listAll().stream()
                .map(e -> new LabelValue(e.getName(), e.getCode()))
                .toList();
    }

    /// 枚举与库表合并为产品结果
    private PayProductResult toProductResult(ProductEnum product, Map<String, PayProduct> dbMap) {
        PayProductResult result = new PayProductResult()
                .setCode(product.getCode())
                .setChannel(product.getChannel())
                .setName(I18nUtil.getEnumName(product))
                .setChannelName(I18nUtil.getEnumName(ChannelEnum.findByCode(product.getChannel())));

        PayProduct dbRow = dbMap.get(product.getCode());
        if (dbRow != null) {
            result.setId(dbRow.getId());
            result.setEnabled(dbRow.isEnabled());
            result.setSortNo(dbRow.getSortNo());
            result.setDescription(dbRow.getDescription());
            result.setIcon(dbRow.getIcon());
            result.setSettlePeriods(dbRow.getSettlePeriods());
            if (dbRow.getSandbox() != null) {
                result.setSandbox(dbRow.getSandbox());
            }
        } else {
            result.setEnabled(true);
            result.setSortNo(0);
        }
        AbsProductStrategy strategy = resolveStrategy(product.getCode());
        if (strategy != null) {
            applyStrategyFields(strategy, result);
        }
        return result;
    }

    /// 补全支付能力与展示字段
    private PayProductResult enrich(PayProductResult result) {
        payProductCapabilityService.fillCapabilities(result);
        return fillProductFeatures(result);
    }

    /// 补全名称、通道名与策略特性
    private PayProductResult fillProductFeatures(PayProductResult result) {
        String code = result.getCode();
        ProductEnum productEnum = ProductEnum.findByCode(code);
        if (productEnum != null) {
            result.setName(I18nUtil.getEnumName(productEnum));
        }
        result.setChannelName(I18nUtil.getEnumName(ChannelEnum.findByCode(result.getChannel())));
        AbsProductStrategy strategy = resolveStrategy(code);
        if (strategy != null) {
            applyStrategyFields(strategy, result);
        }
        return result;
    }

    /// 按产品编码获取策略，不存在则返回 null
    private AbsProductStrategy resolveStrategy(String productCode) {
        if (!PaymentStrategyFactory.existsByProduct(productCode, AbsProductStrategy.class)) {
            return null;
        }
        return PaymentStrategyFactory.createByProduct(productCode, AbsProductStrategy.class);
    }

    /// 写入策略层产品特性字段
    private void applyStrategyFields(AbsProductStrategy strategy, PayProductResult target) {
        target.setIsv(strategy.isIsv())
                .setTerminal(strategy.isTerminal())
                .setSandbox(strategy.isSandbox())
                .setApiCallMode(strategy.getApiCallMode().getCode())
                .setPayIdType(strategy.getPayIdType().getCode());
    }

    /// 按产品名称关键字过滤（分页后在内存中匹配 i18n 展示名）
    private boolean matchNameKeyword(PayProductResult row, String nameKeyword) {
        if (StrUtil.isBlank(nameKeyword)) {
            return true;
        }
        String name = row.getName();
        return name != null && name.contains(nameKeyword);
    }
}
