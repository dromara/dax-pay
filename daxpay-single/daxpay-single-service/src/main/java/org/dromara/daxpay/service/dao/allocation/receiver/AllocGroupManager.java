package org.dromara.daxpay.service.dao.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.allocation.receiver.AllocGroup;
import org.dromara.daxpay.service.param.allocation.group.AllocGroupQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/10/6
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocGroupManager extends BaseManager<AllocGroupMapper, AllocGroup> {

    /**
     * 分页
     */
    public Page<AllocGroup> page(PageParam pageParam, AllocGroupQuery query) {
        Page<AllocGroup> mpPage = MpUtil.getMpPage(pageParam, AllocGroup.class);
        QueryWrapper<AllocGroup> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /**
     * 根据分账组编号查询
     */
    public Optional<AllocGroup> findByGroupNo(String groupNo, String appId) {
        return this.lambdaQuery()
                .eq(AllocGroup::getGroupNo,groupNo)
                .eq(AllocGroup::getAppId,appId)
                .oneOpt();
    }

    /**
     * 清除默认分账组
     */
    public void clearDefault(String channel, String appId) {
        lambdaUpdate()
                .set(AllocGroup::isDefaultGroup,false)
                .eq(AllocGroup::getChannel,channel)
                .eq(AllocGroup::getAppId,appId)
                .update();
    }

    /**
     * 获取默认分账组
     */
    public Optional<AllocGroup> findDefaultGroup(String channel, String appId) {
        return this.lambdaQuery()
                .eq(AllocGroup::getChannel,channel)
                .eq(AllocGroup::isDefaultGroup,true)
                .eq(AllocGroup::getAppId,appId)
                .oneOpt();
    }

    /**
     * 分账组编号是否存在
     */
    public boolean existedByGroupNo(String groupNo, String appId) {
        return this.lambdaQuery()
                .eq(AllocGroup::getGroupNo,groupNo)
                .eq(AllocGroup::getAppId,appId)
                .exists();
    }
}
