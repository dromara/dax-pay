package org.dromara.daxpay.payment.old.pay.dao.order.transfer;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpCreateEntity;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.common.mybatisplus.query.generator.QueryGenerator;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import org.dromara.daxpay.payment.old.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.old.pay.param.order.transfer.TransferOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
@RequiredArgsConstructor
public class TransferOrderManager extends BaseManager<TransferOrderMapper, TransferOrder> {

    /// 分页
    public Page<TransferOrder> page(PageParam pageParam, TransferOrderQuery query) {
        Page<TransferOrder> mpPage = MpUtil.getMpPage(pageParam, TransferOrder.class);
        QueryWrapper<TransferOrder> generator = QueryGenerator.generator(query);
        return page(mpPage,generator);
    }

    /// 查询指定时间的转账中的订单
    @IgnoreTenant
    public List<TransferOrder> findAllByProgress(OffsetDateTime startTime, OffsetDateTime endTime) {
        return lambdaQuery()
                .eq(TransferOrder::getStatus, TransferStatusEnum.PROGRESS.getCode())
                .between(MpCreateEntity::getCreateTime, startTime,  endTime)
                .list();
    }

    /// 查询指定时间前的转账中的订单
    @IgnoreTenant
    public List<TransferOrder> findAllByBeforeProgress(OffsetDateTime dateTime) {
        return lambdaQuery()
                .eq(TransferOrder::getStatus, TransferStatusEnum.PROGRESS.getCode())
                .le(MpCreateEntity::getCreateTime, dateTime)
                .list();
    }

    /// 根据转账号查询
    public Optional<TransferOrder> findByTransferNo(String transferNo) {
        return findByField(TransferOrder::getTransferNo, transferNo);
    }

    /// 根据转账号查询
    @IgnoreTenant
    public Optional<TransferOrder> findByTransferNoNotTenant(String transferNo) {
        return findByField(TransferOrder::getTransferNo, transferNo);
    }

    /// 根据商户转账号查询
    public Optional<TransferOrder> findByBizTransferNo(String bizTransferNo, String appId) {
        return lambdaQuery()
                .eq(TransferOrder::getBizTransferNo, bizTransferNo)
                .eq(TransferOrder::getAppId, appId)
                .oneOpt();
    }

    /// 查询汇总金额
    public BigDecimal getTotalAmount(TransferOrderQuery query){
        QueryWrapper<TransferOrderQuery> generator = QueryGenerator.generator(query);
        generator.eq(MpUtil.getColumnName(TransferOrder::getStatus), TransferStatusEnum.SUCCESS.getCode());
        return baseMapper.getTotalAmount(generator);
    }

    /// 查询订单, 不过滤租户
    public Optional<TransferOrder> findByIdNotTenant(Long id) {
        return findById(id);
    }
}
