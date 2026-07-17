package cn.daxpay.open.platform.capability.audit.log.dao;

import cn.daxpay.open.platform.capability.audit.log.entity.UnipayApiLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.UnipayApiLogQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/// # 统一支付接口审计日志 Manager
///
@Slf4j
@Service
@RequiredArgsConstructor
public class UnipayApiLogDbManager extends BaseManager<UnipayApiLogDbMapper, UnipayApiLogDb> {

    /// 分页
    public Page<UnipayApiLogDb> page(PageParam pageParam, UnipayApiLogQuery query) {
        QueryWrapper<UnipayApiLogDb> generator = QueryGenerator.generator(query);
        Page<UnipayApiLogDb> mpPage = MpUtil.getMpPage(pageParam);
        return this.page(mpPage, generator);
    }

    /// 删除小于指定时间的日志
    public void deleteByOffset(OffsetDateTime offset) {
        lambdaUpdate()
                .le(UnipayApiLogDb::getOperateTime, offset)
                .remove();
    }
}
