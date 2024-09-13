package cn.daxpay.single.service.core.order.transfer.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.daxpay.single.core.code.TransferStatusEnum;
import cn.daxpay.single.core.result.order.TransferOrderResult;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.param.order.TransferOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author xxm
 * @since 2024/3/21
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TransferOrderManager extends BaseManager<TransferOrderMapper, TransferOrder> {


    /**
     * 分页
     */
    public Page<TransferOrder> page(PageParam pageParam, TransferOrderQuery query) {
        Page<TransferOrder> mpPage = MpUtil.getMpPage(pageParam, TransferOrder.class);
        QueryWrapper<TransferOrder> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /**
     * 根据转账号查询
     */
    public Optional<TransferOrder> findByTransferNo(String transferNo) {
        return findByField(TransferOrder::getTransferNo, transferNo);
    }

    /**
     * 根据商户转账号查询
     */
    public Optional<TransferOrder> findByBizTransferNo(String bizTransferNo) {
        return findByField(TransferOrder::getBizTransferNo, bizTransferNo);
    }

    /**
     * 查询汇总金额
     */
    public Integer getTalAmount(TransferOrderQuery query){
        QueryWrapper<TransferOrderQuery> generator = QueryGenerator.generator(query);
        generator.eq(MpUtil.getColumnName(TransferOrder::getStatus), TransferStatusEnum.SUCCESS.getCode());
        return baseMapper.getTalAmount(generator);
    }
}
