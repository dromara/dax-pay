package cn.daxpay.open.payment.trade.notice.dao;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.param.MchNoticeTaskQuery;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Repository;

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
                                                       String bizNo, String protocol, String source) {
        return lambdaQuery()
                .eq(MchNoticeTask::getMchNo, mchNo)
                .eq(MchNoticeTask::getAppId, appId)
                .eq(MchNoticeTask::getEvent, event)
                .eq(MchNoticeTask::getBizNo, bizNo)
                .eq(MchNoticeTask::getProtocol, protocol)
                .eq(MchNoticeTask::getSource, source)
                .oneOpt();
    }
}
