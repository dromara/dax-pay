package cn.daxpay.open.platform.capability.sensitiveword.dao;

import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordHitQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

/// # 敏感词命中 Manager
///
@Repository
public class SystemSensitiveWordHitManager extends BaseManager<SystemSensitiveWordHitMapper, SystemSensitiveWordHit> {

    /// 分页
    public Page<SystemSensitiveWordHit> page(PageParam pageParam, SystemSensitiveWordHitQuery query) {
        Page<SystemSensitiveWordHit> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<SystemSensitiveWordHit> wrapper = QueryGenerator.generator(query);
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }
}

