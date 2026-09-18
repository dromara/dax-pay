package cn.daxpay.open.platform.notify.dao.message;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.notify.entity.message.NotifyMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/// 个人消息(预留)
@Repository
@AllArgsConstructor
public class NotifyMessageManager extends BaseManager<NotifyMessageMapper, NotifyMessage> {

    /// 统计用户未读个人消息数(SQL 端 count, 不拉全量行到内存取 size)
    public long countByUserAndUnread(Long userId) {
        return lambdaQuery()
            .eq(NotifyMessage::getUserId, userId)
            .eq(NotifyMessage::getIsRead, false)
            .count();
    }
}
