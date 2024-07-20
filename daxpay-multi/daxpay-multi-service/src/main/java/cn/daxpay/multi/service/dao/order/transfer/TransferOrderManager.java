package cn.daxpay.multi.service.dao.order.transfer;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.daxpay.multi.core.enums.TransferStatusEnum;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.param.order.transfer.TransferOrderQuery;
import cn.daxpay.multi.service.result.order.transfer.TransferOrderResult;
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
        generator.eq(MpUtil.getColumnName(TransferOrderResult::getStatus), TransferStatusEnum.SUCCESS.getCode());
        return baseMapper.getTalAmount(generator);
    }
}
