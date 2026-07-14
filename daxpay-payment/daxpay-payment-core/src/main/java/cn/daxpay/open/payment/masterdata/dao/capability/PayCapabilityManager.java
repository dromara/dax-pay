package cn.daxpay.open.payment.masterdata.dao.capability;

import cn.daxpay.open.payment.masterdata.entity.capability.PayCapability;
import cn.daxpay.open.payment.masterdata.param.capability.PayCapabilityQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/// # 支付能力
@Repository
@RequiredArgsConstructor
public class PayCapabilityManager extends BaseManager<PayCapabilityMapper, PayCapability> {

    /// 按编码查询
    public Optional<PayCapability> findByCode(String code) {
        return lambdaQuery().eq(PayCapability::getCode, code).oneOpt();
    }

    /// 分页（默认 sort_no、id 升序）
    public Page<PayCapability> page(PageParam pageParam, PayCapabilityQuery query) {
        Page<PayCapability> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayCapability> wrapper = QueryGenerator.generator(query);
        if (StrUtil.isBlank(query.getSortField())) {
            wrapper.orderByAsc(MpUtil.getColumnName(PayCapability::getSortNo))
                    .orderByAsc(MpUtil.getColumnName(PayCapability::getId));
        }
        return page(mpPage, wrapper);
    }

    /// 全部未删除，有序
    public List<PayCapability> listAllOrdered() {
        return lambdaQuery()
                .orderByAsc(PayCapability::getSortNo)
                .orderByAsc(PayCapability::getId)
                .list();
    }

    /// code -> 实体
    public Map<String, PayCapability> mapByCode() {
        return listAllOrdered().stream()
                .collect(Collectors.toMap(PayCapability::getCode, e -> e, (a, b) -> a));
    }

    /// 按编码批量查询
    public List<PayCapability> listByCodes(Collection<String> codes) {
        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(PayCapability::getCode, codes).list();
    }
}