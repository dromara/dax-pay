package org.dromara.daxpay.service.dao.constant;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.constant.ApiConst;
import org.dromara.daxpay.service.param.constant.ApiConstQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ApiConstManager extends BaseManager<ApiConstMapper, ApiConst> {

    /**
     * 分页
     */
    public Page<ApiConst> page(PageParam pageParam, ApiConstQuery query) {
        Page<ApiConst> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<ApiConst> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }
}
