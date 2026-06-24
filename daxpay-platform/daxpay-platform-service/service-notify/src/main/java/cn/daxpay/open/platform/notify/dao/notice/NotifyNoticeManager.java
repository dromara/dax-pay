package cn.daxpay.open.platform.notify.dao.notice;

import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.notify.entity.notice.NotifyNotice;
import cn.daxpay.open.platform.notify.param.notice.NotifyNoticeQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
