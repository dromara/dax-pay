package cn.bootx.platform.daxpay.service.core.payment.allocation.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.service.core.payment.allocation.entity.AllocationReceiver;
import cn.bootx.platform.daxpay.service.param.allocation.group.AllocationReceiverQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 分账接收方
 * @author xxm
 * @since 2024/3/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocationReceiverManager extends BaseManager<AllocationReceiverMapper,AllocationReceiver> {

    /**
     * 分页
     */
    public Page<AllocationReceiver> page(PageParam pageParam, AllocationReceiverQuery param){
        Page<AllocationReceiver> mpPage = MpUtil.getMpPage(pageParam, AllocationReceiver.class);
        QueryWrapper<AllocationReceiver> generator = QueryGenerator.generator(param);
        return this.page(mpPage, generator);
    }

}
