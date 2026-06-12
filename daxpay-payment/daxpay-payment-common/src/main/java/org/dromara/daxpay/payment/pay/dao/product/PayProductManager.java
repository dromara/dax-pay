package org.dromara.daxpay.payment.pay.dao.product;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.pay.entity.masterdata.product.PayProduct;
import org.dromara.daxpay.payment.pay.param.masterdata.product.PayProductQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 支付产品
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayProductManager extends BaseManager<PayProductMapper, PayProduct> {

    /// 根据产品编码查询
    public Optional<PayProduct> findByCode(String code) {
        return lambdaQuery()
                .eq(PayProduct::getCode, code)
                .oneOpt();
    }

    /// 按通道编码查询
    public List<PayProduct> listByChannel(String channel) {
        return lambdaQuery()
                .eq(PayProduct::getChannel, channel)
                .orderByAsc(PayProduct::getSortNo)
                .orderByAsc(PayProduct::getId)
                .list();
    }

    /// 分页
    public Page<PayProduct> page(PageParam pageParam, PayProductQuery query) {
        Page<PayProduct> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayProduct> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }
}
