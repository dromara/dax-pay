package cn.daxpay.single.service.core.payment.allocation.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.service.core.payment.allocation.entity.AllocationReceiver;
import cn.daxpay.single.service.param.allocation.receiver.AllocationReceiverQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * 根据接收方编号查询
     */
    public Optional<AllocationReceiver> findByReceiverNo(String receiverNo) {
        return findByField(AllocationReceiver::getReceiverNo, receiverNo);
    }

    /**
     * 根据分账方编号查询列表
     */
    public List<AllocationReceiver> findAllByReceiverNos(List<String> receiverNos) {
        return findAllByFields(AllocationReceiver::getReceiverNo, receiverNos);
    }

    /**
     * 根据所属通道查询
     */
    public List<AllocationReceiver> findAllByChannel(String channel) {
        return findAllByField(AllocationReceiver::getChannel, channel);
    }
}
