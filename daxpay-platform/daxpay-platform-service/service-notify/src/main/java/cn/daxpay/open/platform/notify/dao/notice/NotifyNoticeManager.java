package cn.daxpay.open.platform.notify.dao.notice;

import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.notify.entity.notice.NotifyNotice;
import cn.daxpay.open.platform.notify.enums.NotifyStatusEnum;
import cn.daxpay.open.platform.notify.param.notice.NotifyNoticeQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

/// 公告
@Repository
@AllArgsConstructor
public class NotifyNoticeManager extends BaseManager<NotifyNoticeMapper, NotifyNotice> {

    /// 管理端分页查询
    public Page<NotifyNotice> page(PageParam pageParam, NotifyNoticeQuery query) {
        Page<NotifyNotice> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery()
            .like(StrUtil.isNotBlank(query.getTitle()), NotifyNotice::getTitle, query.getTitle())
            .eq(StrUtil.isNotBlank(query.getStatus()), NotifyNotice::getStatus, query.getStatus())
            .eq(StrUtil.isNotBlank(query.getSeverity()), NotifyNotice::getSeverity, query.getSeverity())
            .orderByDesc(NotifyNotice::getIsTop)
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    /// 商户端分页查询(仅当前时间在生效/过期窗口内的已发布公告)
    public Page<NotifyNotice> pagePublished(PageParam pageParam) {
        Page<NotifyNotice> mpPage = MpUtil.getMpPage(pageParam);
        return visibleScope()
            .orderByDesc(NotifyNotice::getIsTop)
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    /// 商户端详情查询(仅当前时间在生效/过期窗口内的已发布公告, 草稿/下线/过期返回空)
    public Optional<NotifyNotice> findVisibleById(Long id) {
        return visibleScope()
            .eq(MpIdEntity::getId, id)
            .oneOpt();
    }

    /// 商户端可见范围条件: 已发布 且 当前时间在 [生效时间, 过期时间) 窗口内(为空表示不限制)
    private LambdaQueryChainWrapper<NotifyNotice> visibleScope() {
        OffsetDateTime now = OffsetDateTime.now();
        return lambdaQuery()
            .eq(NotifyNotice::getStatus, NotifyStatusEnum.published.getCode())
            .and(w -> w.isNull(NotifyNotice::getEffectiveTime)
                .or()
                .le(NotifyNotice::getEffectiveTime, now))
            .and(w -> w.isNull(NotifyNotice::getExpireTime)
                .or()
                .gt(NotifyNotice::getExpireTime, now));
    }
}
