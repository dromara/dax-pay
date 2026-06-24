package cn.daxpay.open.platform.notify.dao.message;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.notify.entity.message.NotifyMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/// 个人消息(预留)
@Repository
@AllArgsConstructor
public class NotifyMessageManager extends BaseManager<NotifyMessageMapper, NotifyMessage> {

    /// 查询用户未读个人消息
    public List<NotifyMessage> findAllByUserAndUnread(Long userId) {
        return lambdaQuery()
            .eq(NotifyMessage::getUserId, userId)
            .eq(NotifyMessage::getIsRead, false)
            .list();
    }
}
