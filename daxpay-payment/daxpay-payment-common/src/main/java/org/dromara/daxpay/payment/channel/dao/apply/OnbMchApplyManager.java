package org.dromara.daxpay.payment.channel.dao.apply;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpIdEntity;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.payment.channel.entity.apply.OnbMchApply;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户入驻申请
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class OnbMchApplyManager extends BaseManager<OnbMchApplyMapper, OnbMchApply> {

    /// 分页
    public Page<OnbMchApply> page(PageParam pageParam, OnbMchApplyQuery query) {
        Page<OnbMchApply> mpPage = MpUtil.getMpPage(pageParam, OnbMchApply.class);
        QueryWrapper<OnbMchApply> generator = QueryGenerator.generator(query);
        // 过滤掉大字段
        generator.select(this.getEntityClass (), MpUtil::excludeBigField);
        return this.page(mpPage,generator);
    }

    /// 查询简单对象
    public Optional<OnbMchApply> findSimpleById(Long id) {
        return lambdaQuery()
                .select(this.getEntityClass (), MpUtil::excludeBigField)
                .eq(MpIdEntity::getId, id)
                .oneOpt();
    }
}
