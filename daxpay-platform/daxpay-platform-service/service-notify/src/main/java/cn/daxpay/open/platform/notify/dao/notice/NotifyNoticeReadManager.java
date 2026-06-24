package cn.daxpay.open.platform.notify.dao.notice;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.notify.entity.notice.NotifyNoticeRead;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// 公告已读记录
@Repository
@AllArgsConstructor
public class NotifyNoticeReadManager extends BaseManager<NotifyNoticeReadMapper, NotifyNoticeRead> {

    /// 查询用户对某条公告的已读记录
    public Optional<NotifyNoticeRead> findByUserAndNotice(Long userId, Long noticeId) {
        return lambdaQuery()
            .eq(NotifyNoticeRead::getUserId, userId)
            .eq(NotifyNoticeRead::getNoticeId, noticeId)
            .oneOpt();
    }

    /// 查询用户所有未忽略的已读记录
    public List<NotifyNoticeRead> findAllByUserAndNotIgnored(Long userId) {
        return lambdaQuery()
            .eq(NotifyNoticeRead::getUserId, userId)
            .eq(NotifyNoticeRead::getIsIgnored, false)
            .list();
    }

    /// 查询用户所有已读记录(含忽略)
    public List<NotifyNoticeRead> findAllByUser(Long userId) {
        return lambdaQuery()
            .eq(NotifyNoticeRead::getUserId, userId)
            .list();
    }
}
