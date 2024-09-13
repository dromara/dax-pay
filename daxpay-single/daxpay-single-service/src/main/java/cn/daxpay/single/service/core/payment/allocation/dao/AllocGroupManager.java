package cn.daxpay.single.service.core.payment.allocation.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocGroup;
import cn.daxpay.single.service.param.allocation.group.AllocGroupParam;
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
public class AllocGroupManager extends BaseManager<AllocGroupMapper, AllocGroup> {

    /**
     * 分页
     */
    public Page<AllocGroup> page(PageParam pageParam, AllocGroupParam query) {
        Page<AllocGroup> mpPage = MpUtil.getMpPage(pageParam, AllocGroup.class);
        QueryWrapper<AllocGroup> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /**
     * 根据分账组编号查询
     */
    public Optional<AllocGroup> findByGroupNo(String groupNo) {
        return this.lambdaQuery()
                .eq(AllocGroup::getGroupNo,groupNo)
                .oneOpt();
    }

    /**
     * 清除默认分账组
     */
    public void clearDefault(String channel) {
        lambdaUpdate()
                .set(AllocGroup::isDefaultGroup,false)
                .eq(AllocGroup::getChannel,channel)
                .update();
    }

    /**
     * 获取默认分账组
     */
    public Optional<AllocGroup> findDefaultGroup(String channel) {
       return this.lambdaQuery()
               .eq(AllocGroup::getChannel,channel)
               .eq(AllocGroup::isDefaultGroup,true)
               .oneOpt();
    }

    /**
     * 分账组编号是否存在
     */
    public boolean existedByGroupNo(String groupNo) {
        return existedByField(AllocGroup::getGroupNo,groupNo);
    }
}
