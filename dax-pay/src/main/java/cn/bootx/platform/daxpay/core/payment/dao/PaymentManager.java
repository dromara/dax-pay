package cn.bootx.platform.daxpay.core.payment.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.entity.QueryOrder;
import cn.bootx.platform.common.query.entity.QueryParams;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.core.payment.entity.Payment;
import cn.bootx.platform.daxpay.param.payment.PaymentQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

import static cn.bootx.platform.daxpay.code.CachingCode.PAYMENT_BUSINESS_ID;
import static cn.bootx.platform.daxpay.code.CachingCode.PAYMENT_ID;

@Repository
@RequiredArgsConstructor
public class PaymentManager extends BaseManager<PaymentMapper, Payment> {

    @Override
    @Caching(evict = { @CacheEvict(value = PAYMENT_ID, key = "#payment.id"),
            @CacheEvict(value = PAYMENT_BUSINESS_ID, key = "#payment.businessId") })
    public Payment updateById(Payment payment) {
        return super.updateById(payment);
    }

    @Override
    @Cacheable(value = { PAYMENT_ID }, key = "#id")
    public Optional<Payment> findById(Serializable id) {
        return super.findById(id);
    }

    /**
     * 根据BusinessId查询
     */
    @Cacheable(value = { PAYMENT_BUSINESS_ID }, key = "#businessId")
    public Optional<Payment> findByBusinessId(String businessId) {
        return findByField(Payment::getBusinessId, businessId);
    }

    /**
     * 分页查询
     */
    public Page<Payment> page(PageParam pageParam, PaymentQuery param, QueryOrder queryOrder) {
        Page<Payment> mpPage = MpUtil.getMpPage(pageParam, Payment.class);
        QueryWrapper<Payment> wrapper = QueryGenerator.generator(param, queryOrder);
        return this.page(mpPage,wrapper);
    }

    /**
     * 超级分页查询
     */
    public Page<Payment> superPage(PageParam pageParam, QueryParams queryParams) {
        QueryWrapper<Payment> wrapper = QueryGenerator.generator(queryParams);
        Page<Payment> mpPage = MpUtil.getMpPage(pageParam, Payment.class);
        return this.page(mpPage, wrapper);
    }

}
