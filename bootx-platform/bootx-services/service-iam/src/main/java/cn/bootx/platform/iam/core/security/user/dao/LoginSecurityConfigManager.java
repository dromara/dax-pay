package cn.bootx.platform.iam.core.security.user.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.iam.core.security.user.entity.LoginSecurityConfig;
import cn.bootx.platform.iam.param.security.LoginSecurityConfigParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 登录安全策略
 * @author xxm
 * @since 2023-09-19
 */
@Repository
@RequiredArgsConstructor
public class LoginSecurityConfigManager extends BaseManager<LoginSecurityConfigMapper, LoginSecurityConfig> {

    /**
    * 分页
    */
    public Page<LoginSecurityConfig> page(PageParam pageParam, LoginSecurityConfigParam param) {
        Page<LoginSecurityConfig> mpPage = MpUtil.getMpPage(pageParam, LoginSecurityConfig.class);
        QueryWrapper<LoginSecurityConfig> wrapper = QueryGenerator.generator(param, this.getEntityClass());
        wrapper.select(this.getEntityClass(),MpUtil::excludeBigField)
                .orderByDesc(MpUtil.getColumnName(LoginSecurityConfig::getId));
        return this.page(mpPage,wrapper);
    }
}
