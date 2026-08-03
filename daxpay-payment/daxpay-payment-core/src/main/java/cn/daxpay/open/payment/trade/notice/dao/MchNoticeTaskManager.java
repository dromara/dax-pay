package cn.daxpay.open.payment.trade.notice.dao;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeTaskQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/// # 商户出站通知任务管理
///
@Repository
public class MchNoticeTaskManager extends BaseManager<MchNoticeTaskMapper, MchNoticeTask> {

    /// 分页查询
    public Page<MchNoticeTask> page(PageParam pageParam, MchNoticeTaskQuery query) {
        Page<MchNoticeTask> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<MchNoticeTask> wrapper = QueryGenerator.generator(query);
        wrapper.lambda().orderByDesc(MchNoticeTask::getId);
        return this.page(mpPage, wrapper);
    }

    /// 按幂等键查询已存在任务
    public Optional<MchNoticeTask> findByIdempotentKey(String mchNo, String appId, String event,
                                                       String bizNo, String transport, String format, String source) {
        return lambdaQuery()
                .eq(MchNoticeTask::getMchNo, mchNo)
                .eq(MchNoticeTask::getAppId, appId)
                .eq(MchNoticeTask::getEvent, event)
                .eq(MchNoticeTask::getBizNo, bizNo)
                .eq(MchNoticeTask::getTransport, transport)
                .eq(MchNoticeTask::getFormat, format)
                .eq(MchNoticeTask::getSource, source)
                .oneOpt();
    }

    /// 扫描未成功且(nextTime 为空 或 nextTime <= now)的孤儿任务(MQ 投递失败兜底)
    ///
    /// 覆盖 [cn.daxpay.open.payment.trade.notice.service.NoticeTaskScheduleService#scheduleImmediateAfterCommit]
    /// 投递 MQ 失败导致任务卡在 success=false、nextTime=null 的场景(全仓无其他扫描入口)。
    /// 跨租户扫描(定时任务无 HTTP 上下文), 单次上限 limit 防积压爆量。
    @IgnoreTenant
    public List<MchNoticeTask> findStaleUnsent(int limit) {
        return listLimit(limit, q -> q
                .eq(MchNoticeTask::isSuccess, false)
                .and(w -> w.isNull(MchNoticeTask::getNextTime)
                        .or().le(MchNoticeTask::getNextTime, OffsetDateTime.now(ZoneOffset.UTC)))
                .orderByAsc(MchNoticeTask::getCreateTime));
    }
}
