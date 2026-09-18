package cn.daxpay.open.platform.notify.dao.notice;

import cn.daxpay.open.platform.notify.entity.notice.NotifyNotice;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.OffsetDateTime;
import java.util.List;

/// 公告
@Mapper
public interface NotifyNoticeMapper extends MPJBaseMapper<NotifyNotice> {

    /// 统计用户未读的可见公告数
    ///
    /// 可见条件(已发布 + 当前时间在生效/过期窗口内)与 [NotifyUserNoticeService] 的列表判定一致;
    /// 阅读记录含忽略(忽略视同已读), 通过 NOT EXISTS 反连接走 notify_notice_read(user_id, notice_id) 索引,
    /// 在 SQL 端 count 完成, 避免拉取公告正文与用户全量回执到内存做差集.
    @Select("""
        SELECT COUNT(*)
        FROM notify_notice n
        WHERE n.deleted = false
          AND n.status = 'published'
          AND (n.effective_time IS NULL OR n.effective_time <= #{now})
          AND (n.expire_time IS NULL OR n.expire_time > #{now})
          AND NOT EXISTS (
              SELECT 1 FROM notify_notice_read r
              WHERE r.user_id = #{userId} AND r.notice_id = n.id
          )
        """)
    long countVisibleUnread(@Param("userId") Long userId, @Param("now") OffsetDateTime now);

    /// 查询当前可见公告的摘要列表(铃铛列表/全部已读补录用)
    ///
    /// 只取铃铛展示所需列, 正文截取前 120 字符作为内容摘要; Markdown 渲染全文的职责已收敛到详情接口,
    /// 列表不再返回全文, 避免公告量增大后每次铃铛刷新全量拉取正文;
    /// 截断结果仍映射到实体 content 字段, 由 [NotifyNoticeConvert#toBrief] 转为内容摘要 message.
    @Select("""
        SELECT n.id, n.title, left(n.content, 120) AS content, n.severity, n.is_top, n.create_time
        FROM notify_notice n
        WHERE n.deleted = false
          AND n.status = 'published'
          AND (n.effective_time IS NULL OR n.effective_time <= #{now})
          AND (n.expire_time IS NULL OR n.expire_time > #{now})
        ORDER BY n.is_top DESC, n.id DESC
        """)
    List<NotifyNotice> listVisibleSummaries(@Param("now") OffsetDateTime now);
}
