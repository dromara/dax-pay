package cn.daxpay.open.payment.trade.transfer.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.trade.transfer.entity.WechatTransferOrder;
import cn.daxpay.open.payment.trade.transfer.param.WechatTransferOrderQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/// # 微信转账单管理器
///
@Repository
public class WechatTransferOrderManager
        extends BaseManager<WechatTransferOrderMapper, WechatTransferOrder> {

    /// 根据平台转账单号查询
    public Optional<WechatTransferOrder> findByTransferNo(String transferNo) {
        return findByField(WechatTransferOrder::getTransferNo, transferNo);
    }

    /// 根据商户转账号和应用号查询(幂等查重主路径)
    public Optional<WechatTransferOrder> findByBizTransferNo(String bizTransferNo, String appId) {
        return lambdaQuery()
                .eq(WechatTransferOrder::getBizTransferNo, bizTransferNo)
                .eq(WechatTransferOrder::getAppId, appId)
                .oneOpt();
    }

    /// 根据主键查询
    public Optional<WechatTransferOrder> findById(Long id) {
        return super.findById(id);
    }

    /// 分页查询(管理端), 默认按创建时间倒序
    public Page<WechatTransferOrder> page(PageParam pageParam, WechatTransferOrderQuery query) {
        Page<WechatTransferOrder> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<WechatTransferOrder> wrapper = QueryGenerator.generator(query);
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");
        return this.page(mpPage, wrapper);
    }

    /// CAS 式状态更新：仅当当前状态在 expectFrom 集合内时才更新，保证原子性
    ///
    /// SQL 语义：`UPDATE pay_transfer_order_wechat SET status=?, ... WHERE id=? AND status IN (...)`。
    /// 微信特有字段 [transferBody] 随状态一并回写(拉起确认参数, 处理中时写入)。
    public boolean casUpdateStatus(WechatTransferOrder order, Set<String> expectFrom) {
        return lambdaUpdate()
                .eq(WechatTransferOrder::getId, order.getId())
                .in(WechatTransferOrder::getStatus, expectFrom)
                .set(WechatTransferOrder::getStatus, order.getStatus())
                .set(WechatTransferOrder::getFinishTime, order.getFinishTime())
                .set(WechatTransferOrder::getOutTransferNo, order.getOutTransferNo())
                .set(WechatTransferOrder::getTransferBody, order.getTransferBody())
                .set(WechatTransferOrder::getErrorMsg, order.getErrorMsg())
                .update();
    }
}
