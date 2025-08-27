package cn.bootx.platform.starter.audit.log.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.entity.OperateLogDb;
import cn.bootx.platform.starter.audit.log.param.OperateLogQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author xxm
 * @since 2021/8/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogDbManager extends BaseManager<OperateLogDbMapper, OperateLogDb> {

    /**
     * 分页
     */
    public Page<OperateLogDb> page(PageParam pageParam, OperateLogQuery query) {
        QueryWrapper<OperateLogDb> generator = QueryGenerator.generator(query);
        Page<OperateLogDb> mpPage = MpUtil.getMpPage(pageParam);
        return this.page(mpPage,generator);
    }

    /**
     * 删除 小于指定日期的日志
     */
    public void deleteByOffset(LocalDateTime offset){
        lambdaUpdate()
                .le(OperateLogDb::getOperateTime, offset)
                .remove();

    }

}
