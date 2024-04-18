package cn.bootx.platform.daxpay.service.core.payment.allocation.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationGroup;
import cn.bootx.platform.daxpay.service.param.allocation.group.AllocationGroupParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/3/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocationGroupManager extends BaseManager<AllocationGroupMapper,AllocationGroup> {

    public Page<AllocationGroup> page(PageParam pageParam, AllocationGroupParam query) {
        Page<AllocationGroup> mpPage = MpUtil.getMpPage(pageParam, AllocationGroup.class);
        QueryWrapper<AllocationGroup> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /**
     * 清除默认分账组
     */
    public void clearDefault(String channel) {
        lambdaUpdate()
                .set(AllocationGroup::isDefaultGroup,false)
                .eq(AllocationGroup::getChannel,channel)
                .update();
    }

    /**
     * 获取默认分账组
     */
    public Optional<AllocationGroup> findDefaultGroup(String asyncChannel) {
       return findByField(AllocationGroup::getChannel,asyncChannel);
    }
}
