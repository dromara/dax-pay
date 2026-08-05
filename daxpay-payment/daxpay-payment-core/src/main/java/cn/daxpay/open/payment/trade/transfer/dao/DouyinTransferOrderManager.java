package cn.daxpay.open.payment.trade.transfer.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.trade.transfer.entity.DouyinTransferOrder;
import cn.daxpay.open.payment.trade.transfer.param.DouyinTransferOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/// # 抖音转账单管理器
///
@Repository
public class DouyinTransferOrderManager
        extends BaseManager<DouyinTransferOrderMapper, DouyinTransferOrder> {

    /// 根据平台转账单号查询
    public Optional<DouyinTransferOrder> findByTransferNo(String transferNo) {
        return findByField(DouyinTransferOrder::getTransferNo, transferNo);
    }

    /// 根据商户转账号和应用号查询(幂等查重主路径)
    public Optional<DouyinTransferOrder> findByBizTransferNo(String bizTransferNo, String appId) {
        return lambdaQuery()
                .eq(DouyinTransferOrder::getBizTransferNo, bizTransferNo)
                .eq(DouyinTransferOrder::getAppId, appId)
                .oneOpt();
    }

    /// 根据主键查询
    public Optional<DouyinTransferOrder> findById(Long id) {
        return super.findById(id);
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<DouyinTransferOrder> page(PageParam pageParam, DouyinTransferOrderQuery query) {
        Page<DouyinTransferOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<DouyinTransferOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// CAS 式状态更新：仅当当前状态在 expectFrom 集合内时才更新，保证原子性
    ///
    /// SQL 语义：`UPDATE pay_transfer_order_douyin SET status=?, ... WHERE id=? AND status IN (...)`。
    public boolean casUpdateStatus(DouyinTransferOrder order, Set<String> expectFrom) {
        return lambdaUpdate()
                .eq(DouyinTransferOrder::getId, order.getId())
                .in(DouyinTransferOrder::getStatus, expectFrom)
                .set(DouyinTransferOrder::getStatus, order.getStatus())
                .set(DouyinTransferOrder::getFinishTime, order.getFinishTime())
                .set(DouyinTransferOrder::getOutTransferNo, order.getOutTransferNo())
                .set(DouyinTransferOrder::getErrorMsg, order.getErrorMsg())
                .update();
    }
}
