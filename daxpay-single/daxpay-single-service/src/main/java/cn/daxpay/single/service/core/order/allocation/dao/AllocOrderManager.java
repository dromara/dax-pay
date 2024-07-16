package cn.daxpay.single.service.core.order.allocation.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.core.code.AllocOrderStatusEnum;
import cn.daxpay.single.service.core.order.allocation.entity.AllocOrder;
import cn.daxpay.single.service.param.order.AllocOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/4/7
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocOrderManager extends BaseManager<AllocOrderMapper, AllocOrder> {

    /**
     * 根据分账单号查询
     */
    public Optional<AllocOrder> findByAllocNo(String allocNo){
        return findByField(AllocOrder::getAllocNo, allocNo);
    }

    /**
     * 根据商户分账号查询
     */
    public Optional<AllocOrder> findByBizAllocNo(String bizAllocNo){
        return findByField(AllocOrder::getBizAllocNo, bizAllocNo);
    }

    /**
     * 分页
     */
    public Page<AllocOrder> page(PageParam pageParam, AllocOrderQuery param){
            Page<AllocOrder> mpPage = MpUtil.getMpPage(pageParam, AllocOrder.class);
            QueryWrapper<AllocOrder> generator = QueryGenerator.generator(param);
            return this.page(mpPage, generator);
    }

    /**
     * 查询待同步的分账单
     */
    public List<AllocOrder> findSyncOrder(){
        List<String> statusList = Arrays.asList(AllocOrderStatusEnum.ALLOCATION_PROCESSING.getCode(), AllocOrderStatusEnum.ALLOCATION_END.getCode());
        return lambdaQuery()
                .in(AllocOrder::getStatus, statusList)
                .list();
    }
}
