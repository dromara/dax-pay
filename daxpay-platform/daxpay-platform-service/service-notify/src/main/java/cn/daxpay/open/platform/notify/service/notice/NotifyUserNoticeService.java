package cn.daxpay.open.platform.notify.service.notice;

import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.notify.convert.notice.NotifyNoticeConvert;
import cn.daxpay.open.platform.notify.dao.message.NotifyMessageManager;
import cn.daxpay.open.platform.notify.dao.notice.NotifyNoticeManager;
import cn.daxpay.open.platform.notify.dao.notice.NotifyNoticeReadManager;
import cn.daxpay.open.platform.notify.entity.message.NotifyMessage;
import cn.daxpay.open.platform.notify.entity.notice.NotifyNotice;
import cn.daxpay.open.platform.notify.entity.notice.NotifyNoticeRead;
import cn.daxpay.open.platform.notify.enums.NotifyStatusEnum;
import cn.daxpay.open.platform.notify.enums.NotifyTypeEnum;
import cn.daxpay.open.platform.notify.param.notice.NotifyUserNoticeQuery;
import cn.daxpay.open.platform.notify.result.notice.NotifyNoticeBriefResult;
import cn.daxpay.open.platform.notify.result.notice.NotifyUnreadCountResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/// 用户端通知服务(未读数/列表/已读/清空/忽略)
///
/// 公告采用"广播 + 已读表"模型: 一条公告对全员可见, 单独维护用户阅读/忽略状态;
/// 个人消息采用"定向"模型, 直接带 user_id. 两类在铃铛中聚合展示.
@Service
@AllArgsConstructor
public class NotifyUserNoticeService {

    private final NotifyNoticeManager noticeManager;

    private final NotifyNoticeReadManager readManager;

    private final NotifyMessageManager messageManager;

    /// 未读数(公告 + 个人消息)
    public NotifyUnreadCountResult unreadCount() {
        Long userId = SecurityUtil.getUserId();
        // 可见公告
        List<NotifyNotice> visibleNotices = findVisibleNotices();
        // 用户已读(且未忽略)的公告id集合
        Set<Long> readNoticeIds = readManager.findAllByUserAndNotIgnored(userId).stream()
            .map(NotifyNoticeRead::getNoticeId)
            .collect(Collectors.toSet());
        int noticeUnread = (int) visibleNotices.stream()
            .filter(n -> !readNoticeIds.contains(n.getId()))
            .count();
        // 个人消息未读数(预留, 当前表无数据返回0)
        int messageUnread = messageManager.findAllByUserAndUnread(userId).size();
        return new NotifyUnreadCountResult()
            .setNoticeCount(noticeUnread)
            .setMessageCount(messageUnread)
            .setTotal(noticeUnread + messageUnread);
    }

    /// 铃铛列表(可见公告 + 个人消息聚合, 排除被忽略的公告)
    public List<NotifyNoticeBriefResult> list(NotifyUserNoticeQuery query) {
        Long userId = SecurityUtil.getUserId();

        // 可见公告
        List<NotifyNotice> visibleNotices = findVisibleNotices();
        // 用户阅读记录
        List<NotifyNoticeRead> reads = readManager.findAllByUser(userId);
        Map<Long, NotifyNoticeRead> readMap = reads.stream()
            .collect(Collectors.toMap(NotifyNoticeRead::getNoticeId, Function.identity(), (a, b) -> a));
        Set<Long> ignoredIds = reads.stream()
            .filter(r -> Boolean.TRUE.equals(r.getIsIgnored()))
            .map(NotifyNoticeRead::getNoticeId)
            .collect(Collectors.toSet());

        List<NotifyNoticeBriefResult> list = new ArrayList<>();
        // 组装公告(排除被忽略的)
        for (NotifyNotice notice : visibleNotices) {
            if (ignoredIds.contains(notice.getId())) {
                continue;
            }
            NotifyNoticeBriefResult brief = NotifyNoticeConvert.CONVERT.toBrief(notice);
            NotifyNoticeRead read = readMap.get(notice.getId());
            // 有阅读记录视为已读
            brief.setIsRead(read != null);
            list.add(brief);
        }

        // 个人消息(未删除的全部展示, 已读后前端可删除)
        List<NotifyMessage> messages = messageManager.lambdaQuery()
            .eq(NotifyMessage::getUserId, userId)
            .orderByDesc(MpIdEntity::getId)
            .list();
        for (NotifyMessage message : messages) {
            list.add(NotifyNoticeConvert.CONVERT.convert(message));
        }

        // 只看未读
        if (Boolean.TRUE.equals(query.getOnlyUnread())) {
            list = list.stream()
                .filter(b -> !Boolean.TRUE.equals(b.getIsRead()))
                .collect(Collectors.toList());
        }
        return list;
    }

    /// 标记单条已读
    @Transactional(rollbackFor = Exception.class)
    public void markRead(String type, Long id) {
        if (NotifyTypeEnum.notice.getCode().equals(type)) {
            markNoticeRead(id, false);
        }
        // 个人消息标记已读预留(暂不接入业务)
    }

    /// 清空(全部标记已读): 为所有可见未读公告补阅读记录
    @Transactional(rollbackFor = Exception.class)
    public void readAll() {
        Long userId = SecurityUtil.getUserId();
        OffsetDateTime now = OffsetDateTime.now();
        List<NotifyNotice> visibleNotices = findVisibleNotices();
        Set<Long> readNoticeIds = readManager.findAllByUser(userId).stream()
            .map(NotifyNoticeRead::getNoticeId)
            .collect(Collectors.toSet());
        for (NotifyNotice notice : visibleNotices) {
            if (readNoticeIds.contains(notice.getId())) {
                continue;
            }
            NotifyNoticeRead read = new NotifyNoticeRead();
            read.setUserId(userId);
            read.setNoticeId(notice.getId());
            read.setReadTime(now);
            read.setIsIgnored(false);
            readManager.save(read);
        }
    }

    /// 忽略(用户主动隐藏)
    @Transactional(rollbackFor = Exception.class)
    public void ignore(String type, Long id) {
        Long userId = SecurityUtil.getUserId();
        if (NotifyTypeEnum.notice.getCode().equals(type)) {
            markNoticeRead(id, true);
        } else if (NotifyTypeEnum.message.getCode().equals(type)) {
            // 个人消息忽略=逻辑删除(预留)
            NotifyMessage message = messageManager.findById(id).orElse(null);
            if (message != null && message.getUserId().equals(userId)) {
                messageManager.deleteById(id);
            }
        }
    }

    /// 标记公告已读/忽略(无记录则新增)
    private void markNoticeRead(Long noticeId, boolean ignored) {
        Long userId = SecurityUtil.getUserId();
        OffsetDateTime now = OffsetDateTime.now();
        Optional<NotifyNoticeRead> existing = readManager.findByUserAndNotice(userId, noticeId);
        if (existing.isPresent()) {
            NotifyNoticeRead read = existing.get();
            read.setReadTime(now);
            read.setIsIgnored(ignored);
            readManager.updateById(read);
        } else {
            NotifyNoticeRead read = new NotifyNoticeRead();
            read.setUserId(userId);
            read.setNoticeId(noticeId);
            read.setReadTime(now);
            read.setIsIgnored(ignored);
            readManager.save(read);
        }
    }

    /// 查询当前生效的可见公告(已发布 + 生效期内, 置顶/时间倒序)
    private List<NotifyNotice> findVisibleNotices() {
        OffsetDateTime now = OffsetDateTime.now();
        return noticeManager.lambdaQuery()
            .eq(NotifyNotice::getStatus, NotifyStatusEnum.published.getCode())
            .and(w -> w.isNull(NotifyNotice::getEffectiveTime).or().le(NotifyNotice::getEffectiveTime, now))
            .and(w -> w.isNull(NotifyNotice::getExpireTime).or().gt(NotifyNotice::getExpireTime, now))
            .orderByDesc(NotifyNotice::getIsTop)
            .orderByDesc(MpIdEntity::getId)
            .list();
    }
}
