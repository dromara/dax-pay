package cn.bootx.platform.common.websocket.service;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.common.websocket.entity.WsResult;
import cn.bootx.platform.common.websocket.notice.UserNoticeWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户websocket方式发送通知消息
 *
 * @author xxm
 * @since 2022/6/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserWsNoticeService {

    private final UserNoticeWebSocketHandler noticeWebSocketHandler;

    /**
     * 服务端发送消息给客户端(指定用户)
     */
    public <T> void sendMessageByUser(WsResult<T> resResult, Long userId) {
        noticeWebSocketHandler.sendMessageByUser(JacksonUtil.toJson(resResult), userId);
    }

    /**
     * 服务端发送消息给客户端(指定用户组)
     */
    public <T> void sendMessageByUsers(WsResult<T> resResult, List<Long> userIds) {
        noticeWebSocketHandler.sendMessageByUsers(JacksonUtil.toJson(resResult), userIds);
    }

    /**
     * 服务端发送消息给客户端(全体发送)
     */
    public <T> void sendMessageByAll(WsResult<T> resResult) {
        noticeWebSocketHandler.sendMessageByAll(JacksonUtil.toJson(resResult));
    }

}
