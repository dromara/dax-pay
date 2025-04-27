package org.dromara.daxpay.service.dao.constant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.constant.PayMethodConst;
import org.dromara.daxpay.service.param.constant.MethodConstQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/6/26
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MethodConstManager extends BaseManager<MethodConstMapper, PayMethodConst> {

    /**
     * 分页
     */
    public Page<PayMethodConst> page(PageParam pageParam, MethodConstQuery query) {
        Page<PayMethodConst> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayMethodConst> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }
}
