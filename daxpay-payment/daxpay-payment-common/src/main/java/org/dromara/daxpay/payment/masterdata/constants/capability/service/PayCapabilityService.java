package org.dromara.daxpay.payment.masterdata.constants.capability.service;

import org.dromara.daxpay.payment.masterdata.constants.capability.dao.PayCapabilityManager;
import org.dromara.daxpay.payment.masterdata.constants.capability.dao.PayProductCapabilityManager;
import org.dromara.daxpay.payment.masterdata.constants.capability.entity.PayCapability;
import org.dromara.daxpay.payment.masterdata.constants.capability.param.PayCapabilityQuery;
import org.dromara.daxpay.payment.masterdata.constants.capability.result.PayCapabilityResult;
import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.enums.pay.channel.PayCapabilityEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.dto.LabelValue;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 支付能力主数据
///
/// 供管理端维护与展示支付能力字典。
@Service
@RequiredArgsConstructor
public class PayCapabilityService {

    private final PayCapabilityManager payCapabilityManager;
    private final PayProductCapabilityManager payProductCapabilityManager;

    /// 分页查询
    public PageResult<PayCapabilityResult> page(PageParam pageParam, PayCapabilityQuery query, String nameKeyword) {
        PageResult<PayCapabilityResult> pageResult = MpUtil.toPageResult(payCapabilityManager.page(pageParam, query));
        List<PayCapabilityResult> records = pageResult.getRecords().stream()
                .map(this::fillDisplay)
                .filter(row -> matchNameKeyword(row, nameKeyword))
                .toList();
        pageResult.setRecords(records);
        return pageResult;
    }

    /// 按能力编码查详情，含已关联的支付产品
    public PayCapabilityResult findByCode(String code) {
        PayCapability row = payCapabilityManager.findByCode(code)
                .orElseThrow(() -> new DataNotExistException("error.payment.capability.notExist"));
        return fillDisplayWithProducts(row.toResult());
    }

    /// 补全支付能力 i18n 展示名
    private PayCapabilityResult fillDisplay(PayCapabilityResult result) {
        PayCapabilityEnum capabilityEnum = PayCapabilityEnum.findByCode(result.getCode());
        if (capabilityEnum != null) {
            result.setName(I18nUtil.getEnumName(capabilityEnum));
        }
        return result;
    }

    /// 补全展示名与已关联支付产品
    private PayCapabilityResult fillDisplayWithProducts(PayCapabilityResult result) {
        fillDisplay(result);
        List<LabelValue> products = payProductCapabilityManager.listByCapability(result.getCode()).stream()
                .map(rel -> {
                    ProductEnum productEnum = ProductEnum.findByCode(rel.getProductCode());
                    String label = productEnum != null
                            ? I18nUtil.getEnumName(productEnum)
                            : rel.getProductCode();
                    return new LabelValue(label, rel.getProductCode());
                })
                .toList();
        result.setProducts(products);
        return result;
    }

    /// 分页后在内存中按展示名过滤
    private boolean matchNameKeyword(PayCapabilityResult row, String nameKeyword) {
        if (StrUtil.isBlank(nameKeyword)) {
            return true;
        }
        String name = row.getName();
        return name != null && name.contains(nameKeyword);
    }
}
