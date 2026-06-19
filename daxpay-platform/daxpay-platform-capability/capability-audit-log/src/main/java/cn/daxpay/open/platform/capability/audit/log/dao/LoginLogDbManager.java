package cn.daxpay.open.platform.capability.audit.log.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.capability.audit.log.entity.LoginLogDb;
import cn.daxpay.open.platform.capability.audit.log.param.LoginLogQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

/// # 登录日志
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class LoginLogDbManager extends BaseManager<LoginLogDbMapper, LoginLogDb> {

    public Page<LoginLogDb> page(PageParam pageParam, LoginLogQuery query) {
        var mpPage = MpUtil.getMpPage(pageParam, LoginLogDb.class);
        QueryWrapper<LoginLogDb> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    public void deleteByOffset(OffsetDateTime offset) {
        lambdaUpdate()
                .le(LoginLogDb::getLoginTime, offset)
                .remove();
    }
}
