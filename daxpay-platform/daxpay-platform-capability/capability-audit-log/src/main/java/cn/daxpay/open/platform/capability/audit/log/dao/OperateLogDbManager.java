package cn.daxpay.open.platform.capability.audit.log.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.capability.audit.log.entity.OperateLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.OperateLogQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/// # 操作日志
///
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogDbManager extends BaseManager<OperateLogDbMapper, OperateLogDb> {

    /// 分页
    public Page<OperateLogDb> page(PageParam pageParam, OperateLogQuery query) {
        QueryWrapper<OperateLogDb> generator = QueryGenerator.generator(query);
        Page<OperateLogDb> mpPage = MpUtil.getMpPage(pageParam);
        return this.page(mpPage,generator);
    }

    /// 删除 小于指定日期的日志(物理删除)
    ///
    /// 不能再用 lambdaUpdate().remove(): 实体的 `deleted` 标了 @TableLogic, MP 会把 remove() 改写成逻辑删除 UPDATE,
    /// 日志表将不再回收空间且行数只增不减。@TableLogic 只影响 MP 内置方法, 物理清理走 Mapper 自定义 @Delete。
    public void deleteByOffset(OffsetDateTime offset) {
        baseMapper.deleteByOperateTimeBefore(offset);
    }

}
