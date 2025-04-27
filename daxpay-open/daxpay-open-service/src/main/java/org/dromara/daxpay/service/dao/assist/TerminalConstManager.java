package org.dromara.daxpay.service.dao.assist;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import org.dromara.daxpay.service.entity.constant.TerminalConst;
import org.dromara.daxpay.service.param.constant.TerminalConstQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通道终端报送类型
 * @author xxm
 * @since 2025/3/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TerminalConstManager extends BaseManager<TerminalConstMapper, TerminalConst> {

    /**
     * 分页
     */
    public Page<TerminalConst> page(PageParam pageParam, TerminalConstQuery query) {
        Page<TerminalConst> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<TerminalConst> wrapper = QueryGenerator.generator(query);
        wrapper.orderByAsc(MpIdEntity.Id.id);
        return this.page(mpPage, wrapper);
    }

    /**
     * 查询启用的类型
     */
    public List<TerminalConst> findAllEnable() {
        return lambdaQuery()
                .eq(TerminalConst::isEnable, true)
                .orderByAsc(MpIdEntity::getId)
                .list();
    }

    /**
     * 根据通道查询启用的类型
     */
    public List<TerminalConst> findAllByChannel(String channel) {
        return lambdaQuery()
                .eq(TerminalConst::getChannel, channel)
                .orderByAsc(MpIdEntity::getId)
                .list();
    }

}
