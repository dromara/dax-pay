package cn.bootx.platform.starter.audit.log.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.starter.audit.log.entity.LoginLogDb;
import cn.bootx.platform.starter.audit.log.param.LoginLogParam;
import cn.hutool.core.util.StrUtil;
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

    public Page<LoginLogDb> page(PageParam pageParam, LoginLogParam loginLogParam) {
        Page<LoginLogDb> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery().orderByDesc(LoginLogDb::getId)
            .like(StrUtil.isNotBlank(loginLogParam.getAccount()), LoginLogDb::getAccount, loginLogParam.getAccount())
            .like(StrUtil.isNotBlank(loginLogParam.getClient()), LoginLogDb::getClient, loginLogParam.getClient())
            .like(StrUtil.isNotBlank(loginLogParam.getLoginType()), LoginLogDb::getLoginType,
                    loginLogParam.getLoginType())
            .page(mpPage);
    }

    public void deleteByOffset(LocalDateTime offset) {
        lambdaUpdate()
                .le(LoginLogDb::getLoginTime, offset)
                .remove();
    }
}
