package cn.daxpay.single.service.core.task.notice.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.task.notice.entity.ClientNoticeTask;
import cn.daxpay.single.service.param.record.ClientNoticeTaskQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxm
 * @since 2024/2/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeTaskManager extends BaseManager<ClientNoticeTaskMapper, ClientNoticeTask> {


    /**
     * 分页
     */
    public Page<ClientNoticeTask> page(PageParam pageParam, ClientNoticeTaskQuery query){
        QueryWrapper<ClientNoticeTask> generator = QueryGenerator.generator(query);
        Page<ClientNoticeTask> mpPage = MpUtil.getMpPage(pageParam, ClientNoticeTask.class);

        return this.page(mpPage, generator);
    }
}
