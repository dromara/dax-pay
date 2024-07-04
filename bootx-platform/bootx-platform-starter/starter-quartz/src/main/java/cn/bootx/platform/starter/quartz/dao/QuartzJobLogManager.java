package cn.bootx.platform.starter.quartz.dao;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.starter.quartz.entity.QuartzJobLog;
import cn.bootx.platform.starter.quartz.param.QuartzJobLogQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author xxm
 * @since 2022/5/1
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class QuartzJobLogManager extends BaseManager<QuartzJobLogMapper, QuartzJobLog> {

    /**
     * 分页
     */
    public Page<QuartzJobLog> page(PageParam pageParam, QuartzJobLogQuery query) {
        Page<QuartzJobLog> mpPage = MpUtil.getMpPage(pageParam);

        return this.lambdaQuery()
            .eq(QuartzJobLog::getClassName, query.getClassName())
            .eq(Objects.nonNull(query.getSuccess()), QuartzJobLog::getSuccess, query.getSuccess())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

}
