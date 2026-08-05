package cn.daxpay.open.payment.trade.transfer.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.trade.transfer.entity.AlipayTransferOrder;
import cn.daxpay.open.payment.trade.transfer.param.AlipayTransferOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/// # 支付宝转账单管理器
///
@Repository
public class AlipayTransferOrderManager
        extends BaseManager<AlipayTransferOrderMapper, AlipayTransferOrder> {

    /// 根据平台转账单号查询
    public Optional<AlipayTransferOrder> findByTransferNo(String transferNo) {
        return findByField(AlipayTransferOrder::getTransferNo, transferNo);
    }

    /// 根据商户转账号和应用号查询(幂等查重主路径)
    public Optional<AlipayTransferOrder> findByBizTransferNo(String bizTransferNo, String appId) {
        return lambdaQuery()
                .eq(AlipayTransferOrder::getBizTransferNo, bizTransferNo)
                .eq(AlipayTransferOrder::getAppId, appId)
                .oneOpt();
    }

    /// 根据主键查询
    public Optional<AlipayTransferOrder> findById(Long id) {
        return super.findById(id);
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<AlipayTransferOrder> page(PageParam pageParam, AlipayTransferOrderQuery query) {
        Page<AlipayTransferOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<AlipayTransferOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// CAS 式状态更新：仅当当前状态在 expectFrom 集合内时才更新，保证原子性
    ///
    /// SQL 语义：`UPDATE pay_transfer_order_alipay SET status=?, ... WHERE id=? AND status IN (...)`。
    public boolean casUpdateStatus(AlipayTransferOrder order, Set<String> expectFrom) {
        return lambdaUpdate()
                .eq(AlipayTransferOrder::getId, order.getId())
                .in(AlipayTransferOrder::getStatus, expectFrom)
                .set(AlipayTransferOrder::getStatus, order.getStatus())
                .set(AlipayTransferOrder::getFinishTime, order.getFinishTime())
                .set(AlipayTransferOrder::getOutTransferNo, order.getOutTransferNo())
                .set(AlipayTransferOrder::getErrorMsg, order.getErrorMsg())
                .update();
    }
}
