package org.dromara.daxpay.service.dao.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.constant.ChannelConst;
import org.dromara.daxpay.service.param.constant.ChannelConstQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/6/25
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ChannelConstManager extends BaseManager<ChannelConstMapper, ChannelConst> {

    /**
     * 分页
     */
    public Page<ChannelConst> page(PageParam pageParam, ChannelConstQuery query) {
        Page<ChannelConst> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<ChannelConst> wrapper = QueryGenerator.generator(query);
        return this.page(mpPage, wrapper);
    }

    /**
     * 查询全部启用的通道
     */
    public List<ChannelConst> findAllByEnable() {
        return lambdaQuery()
                .eq(ChannelConst::isEnable, true)
                .orderByAsc(MpIdEntity::getId)
                .list();
    }
}
