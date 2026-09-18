package cn.daxpay.open.platform.notify.dao.notice;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.notify.entity.notice.NotifyNoticeRead;
import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
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

    /// 查询用户对一批公告的已读记录(铃铛列表按当批可见公告 in 查询, 不再拉用户全量历史回执)
    public List<NotifyNoticeRead> findAllByUserAndNoticeIds(Long userId, Collection<Long> noticeIds) {
        if (noticeIds == null || noticeIds.isEmpty()) {
            return List.of();
        }
        return lambdaQuery()
            .eq(NotifyNoticeRead::getUserId, userId)
            .in(NotifyNoticeRead::getNoticeId, noticeIds)
            .list();
    }

    /// 插入或更新阅读记录(唯一约束兜底, 同用户多端并发标记已读幂等)
    public void upsertRead(Long userId, Long noticeId, OffsetDateTime readTime, boolean isIgnored) {
        // 手写 SQL 不经过 MyBatis-Plus 主键填充, 雪花 id 在此生成(与实体 IdType.ASSIGN_ID 同源算法)
        baseMapper.upsertRead(IdUtil.getSnowflakeNextId(), userId, noticeId, readTime, isIgnored);
    }

    /// 批量插入阅读记录, 已存在的静默跳过(全部已读补录用, 幂等且不覆盖已有阅读时间)
    public void saveAllIgnoreConflict(List<NotifyNoticeRead> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        for (NotifyNoticeRead read : records) {
            read.setId(IdUtil.getSnowflakeNextId());
        }
        baseMapper.insertIgnoreConflict(records);
    }
}
