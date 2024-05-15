package cn.bootx.platform.starter.quartz.core.dao;

import cn.bootx.platform.starter.quartz.core.entity.QuartzJob;
import cn.bootx.platform.starter.quartz.param.QuartzJobParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.quartz.code.QuartzJobCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 定时任务
 *
 * @author xxm
 * @since 2021/11/2
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class QuartzJobManager extends BaseManager<QuartzJobMapper, QuartzJob> {

    /**
     * 分页
     */
    public Page<QuartzJob> page(PageParam pageParam, QuartzJobParam param) {
        Page<QuartzJob> mpPage = MpUtil.getMpPage(pageParam, QuartzJob.class);
        return lambdaQuery().orderByDesc(QuartzJob::getId).page(mpPage);
    }

    /**
     * 查询在执行中的定时任务配置
     */
    public List<QuartzJob> findRunning() {
        return findAllByField(QuartzJob::getState, QuartzJobCode.RUNNING);
    }

}
