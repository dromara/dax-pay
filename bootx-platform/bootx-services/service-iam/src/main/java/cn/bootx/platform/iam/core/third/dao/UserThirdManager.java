package cn.bootx.platform.iam.core.third.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.third.entity.UserThird;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 三方登录
 *
 * @author xxm
 * @since 2021/8/2
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserThirdManager extends BaseManager<UserThirdMapper, UserThird> {

    public Optional<UserThird> findByUserId(Long userId) {
        return findByField(UserThird::getUserId, userId);
    }

    public boolean existsByUserId(Long userId) {
        return existedByField(UserThird::getUserId, userId);
    }

    /**
     * 解绑
     */
    public void unbind(Long userId, SFunction<UserThird, String> function) {
        this.lambdaUpdate().set(function, null).eq(UserThird::getUserId, userId).update();
    }

    public Page<UserThird> page(PageParam pageParam) {
        Page<UserThird> mpPage = MpUtil.getMpPage(pageParam, UserThird.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId).page(mpPage);
    }

}
