package cn.bootx.platform.starter.quartz.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.starter.quartz.code.QuartzJobCode;
import cn.bootx.platform.starter.quartz.entity.QuartzJob;
import cn.bootx.platform.starter.quartz.param.QuartzJobParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        QueryWrapper<QuartzJob> wrapper = QueryGenerator.generator(param);
        Page<QuartzJob> mpPage = MpUtil.getMpPage(pageParam);
        return page(mpPage,wrapper);
    }

    /**
     * 查询在执行中的定时任务配置
     */
    public List<QuartzJob> findRunning() {
        return findAllByField(QuartzJob::getState, QuartzJobCode.RUNNING);
    }

}
