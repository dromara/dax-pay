package cn.bootx.platform.starter.audit.log.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.entity.LoginLogDb;
import cn.bootx.platform.starter.audit.log.param.LoginLogQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 登录日志
 *
 * @author xxm
 * @since 2021/8/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class LoginLogDbManager extends BaseManager<LoginLogDbMapper, LoginLogDb> {

    public Page<LoginLogDb> page(PageParam pageParam, LoginLogQuery query) {
        var mpPage = MpUtil.getMpPage(pageParam, LoginLogDb.class);
        QueryWrapper<LoginLogDb> generator = QueryGenerator.generator(query);
        return this.page(mpPage, generator);
    }

    public void deleteByOffset(LocalDateTime offset) {
        lambdaUpdate()
                .le(LoginLogDb::getLoginTime, offset)
                .remove();
    }
}
