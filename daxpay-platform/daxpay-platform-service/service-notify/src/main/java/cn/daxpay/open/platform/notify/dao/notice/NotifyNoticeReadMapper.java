package cn.daxpay.open.platform.notify.dao.notice;

import cn.daxpay.open.platform.notify.entity.notice.NotifyNoticeRead;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

/// 公告已读记录
@Mapper
public interface NotifyNoticeReadMapper extends MPJBaseMapper<NotifyNoticeRead> {

    /// 插入或更新阅读记录(单条已读/忽略用)
    ///
    /// 依赖表既有唯一约束 uk_notify_notice_read (user_id, notice_id): 无记录则插入, 已有则刷新阅读时间与忽略标记;
    /// 取代"先查后插"的写法, 同用户多端并发标记已读天然幂等(旧写法并发方会撞唯一约束报错).
    /// 主键由调用方生成(雪花 id), 手写 SQL 不经过 MyBatis-Plus 的主键填充.
    @Insert("""
        INSERT INTO notify_notice_read (id, user_id, notice_id, read_time, is_ignored, create_time)
        VALUES (#{id}, #{userId}, #{noticeId}, #{readTime}, #{isIgnored}, #{readTime})
        ON CONFLICT (user_id, notice_id)
        DO UPDATE SET read_time = EXCLUDED.read_time, is_ignored = EXCLUDED.is_ignored
        """)
    void upsertRead(@Param("id") Long id, @Param("userId") Long userId, @Param("noticeId") Long noticeId,
                    @Param("readTime") OffsetDateTime readTime, @Param("isIgnored") boolean isIgnored);

    /// 批量插入阅读记录, 已存在的静默跳过(全部已读用)
    ///
    /// 一次性补齐所有可见未读公告的阅读记录, 依赖唯一约束 DO NOTHING 保证幂等,
    /// 已有记录(含已忽略)不覆盖其原始阅读时间.
    @Insert("""
        <script>
        INSERT INTO notify_notice_read (id, user_id, notice_id, read_time, is_ignored, create_time)
        VALUES
        <foreach collection="records" item="r" separator=",">
            (#{r.id}, #{r.userId}, #{r.noticeId}, #{r.readTime}, #{r.isIgnored}, #{r.readTime})
        </foreach>
        ON CONFLICT (user_id, notice_id) DO NOTHING
        </script>
        """)
    void insertIgnoreConflict(@Param("records") List<NotifyNoticeRead> records);
}
