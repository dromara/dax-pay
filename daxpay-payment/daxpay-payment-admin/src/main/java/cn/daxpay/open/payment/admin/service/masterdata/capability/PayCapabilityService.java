package cn.daxpay.open.payment.admin.service.masterdata.capability;

import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.capability.dao.PayProductCapabilityManager;
import cn.daxpay.open.payment.masterdata.constants.capability.entity.PayCapability;
import cn.daxpay.open.payment.masterdata.constants.capability.param.PayCapabilityQuery;
import cn.daxpay.open.payment.masterdata.constants.capability.result.PayCapabilityResult;
import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.dto.LabelValue;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
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
                    String label = I18nUtil.getEnumName(ProductEnum.findByCode(rel.getProductCode()));
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
