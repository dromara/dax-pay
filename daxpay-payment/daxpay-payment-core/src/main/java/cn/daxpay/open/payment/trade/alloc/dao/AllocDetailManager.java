package cn.daxpay.open.payment.trade.alloc.dao;

import cn.daxpay.open.payment.trade.alloc.entity.AllocDetail;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/// # 分账明细管理器
///
/// 明细表继承 [MpBaseEntity], 不参与 mch 行级隔离, 通过 [allocNo] 关联主表。
/// 同步/回调更新明细时用 CAS 保证并发安全。
@Repository
public class AllocDetailManager extends BaseManager<AllocDetailMapper, AllocDetail> {

    /// 根据分账单号查询全部明细(按创建时间正序)
    public List<AllocDetail> findAllByAllocNo(String allocNo) {
        return lambdaQuery()
                .eq(AllocDetail::getAllocNo, allocNo)
                .orderByAsc(AllocDetail::getCreateTime)
                .list();
    }

    /// 根据分账单号查询全部明细(忽略租户, 定时任务引导读用)
    public List<AllocDetail> findAllByAllocNoNotTenant(String allocNo) {
        return lambdaQuery()
                .eq(AllocDetail::getAllocNo, allocNo)
                .orderByAsc(AllocDetail::getCreateTime)
                .list();
    }

    /// 根据主键查询
    public Optional<AllocDetail> findById(Long id) {
        return super.findById(id);
    }

    /// CAS 式明细结果更新：仅当明细当前结果在 expectFrom 集合内时才更新
    ///
    /// SQL 语义：`UPDATE pay_alloc_detail SET result=?, ... WHERE id=? AND result IN (...)`。
    ///
    /// @param detail     已设置目标结果与关联字段的实体（从数据库加载后修改）
    /// @param expectFrom 合法的前置结果编码集合(如 pending)
    /// @return true=更新成功；false=结果已被其他线程改变
    public boolean casUpdateResult(AllocDetail detail, Set<String> expectFrom) {
        return lambdaUpdate()
                .eq(AllocDetail::getId, detail.getId())
                .in(AllocDetail::getResult, expectFrom)
                .set(AllocDetail::getResult, detail.getResult())
                .set(AllocDetail::getOutDetailId, detail.getOutDetailId())
                .set(AllocDetail::getErrorCode, detail.getErrorCode())
                .set(AllocDetail::getErrorMsg, detail.getErrorMsg())
                .set(AllocDetail::getFinishTime, detail.getFinishTime())
                .update();
    }

    /// 根据接收方账号查单条明细(同步/回调按 account 匹配明细用)
    public Optional<AllocDetail> findByAllocNoAndReceiverAccount(String allocNo, String receiverAccount) {
        return lambdaQuery()
                .eq(AllocDetail::getAllocNo, allocNo)
                .eq(AllocDetail::getReceiverAccount, receiverAccount)
                .oneOpt();
    }
}
