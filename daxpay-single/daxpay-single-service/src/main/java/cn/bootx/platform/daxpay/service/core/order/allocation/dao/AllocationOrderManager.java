package cn.bootx.platform.daxpay.service.core.order.allocation.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.order.allocation.entity.AllocationOrder;
import cn.bootx.platform.daxpay.service.param.order.AllocationOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocationOrderManager extends BaseManager<AllocationOrderMapper, AllocationOrder> {

    /**
     * 根据分账单号查询
     */
    public Optional<AllocationOrder> findByAllocationNo(String allocationNo){
        return findByField(AllocationOrder::getAllocationNo, allocationNo);
    }

    /**
     * 分页
     */
    public Page<AllocationOrder> page(PageParam pageParam, AllocationOrderQuery param){
            Page<AllocationOrder> mpPage = MpUtil.getMpPage(pageParam, AllocationOrder.class);
            QueryWrapper<AllocationOrder> generator = QueryGenerator.generator(param);
            return this.page(mpPage, generator);
    }
}
