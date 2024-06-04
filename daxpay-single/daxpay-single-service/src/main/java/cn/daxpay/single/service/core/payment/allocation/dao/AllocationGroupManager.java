package cn.daxpay.single.service.core.payment.allocation.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocationGroup;
import cn.daxpay.single.service.param.allocation.group.AllocationGroupParam;
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
public class AllocationGroupManager extends BaseManager<AllocationGroupMapper, AllocationGroup> {

    /**
     * 分页
     */
    public Page<AllocationGroup> page(PageParam pageParam, AllocationGroupParam query) {
        Page<AllocationGroup> mpPage = MpUtil.getMpPage(pageParam, AllocationGroup.class);
        QueryWrapper<AllocationGroup> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /**
     * 根据分账组编号查询
     */
    public Optional<AllocationGroup> findByGroupNo(String groupNo) {
        return this.lambdaQuery()
                .eq(AllocationGroup::getGroupNo,groupNo)
                .oneOpt();
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
    public Optional<AllocationGroup> findDefaultGroup(String channel) {
       return this.lambdaQuery()
               .eq(AllocationGroup::getChannel,channel)
               .eq(AllocationGroup::isDefaultGroup,true)
               .oneOpt();
    }

    /**
     * 分账组编号是否存在
     */
    public boolean existedByGroupNo(String groupNo) {
        return existedByField(AllocationGroup::getGroupNo,groupNo);
    }
}
