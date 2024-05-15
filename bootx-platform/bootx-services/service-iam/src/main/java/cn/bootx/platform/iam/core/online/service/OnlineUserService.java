package cn.bootx.platform.iam.core.online.service;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.websocket.manager.SpringWebSocketSessionManager;
import cn.bootx.platform.common.websocket.notice.UserNoticeWebSocketHandler;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.iam.dto.online.OnlineUserInfoDto;
import cn.bootx.platform.iam.dto.online.OnlineUserSessionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * websocket会话管理
 * @author xxm
 * @since 2023/10/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineUserService {
    private final UserInfoManager userInfoManager;
    /**
     * 在线用户分页
     */
    public PageResult<OnlineUserInfoDto> page(PageParam pageParam){
        SpringWebSocketSessionManager wsManager = UserNoticeWebSocketHandler.getWsManager();
        // 获取用户ids
        Map<String, List<String>> uid2sid = wsManager.getUid2sid();
            // 组装用户信息
        Set<String> idSet = uid2sid.keySet();
        if (CollUtil.isNotEmpty(idSet)){
            List<Long> ids = CollUtil.sub(idSet, pageParam.start(), pageParam.end())
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            Map<Long, UserInfo> userMap = userInfoManager.findAllByIds(ids)
                    .stream()
                    .collect(Collectors.toMap(UserInfo::getId, Function.identity()));
            List<OnlineUserInfoDto> onlineUserInfos = ids.stream()
                    .map(userMap::get)
                    .filter(Objects::nonNull)
                    .map(UserInfo::toOnline)
                    .collect(Collectors.toList());
            return new PageResult<OnlineUserInfoDto>()
                    .setRecords(onlineUserInfos)
                    .setSize(pageParam.getSize())
                    .setTotal(idSet.size())
                    .setCurrent(pageParam.getCurrent());
        }
        return new PageResult<>();
    }

    /**
     * 获取用户链接信息
     */
    public List<OnlineUserSessionDto> getSessionByUserId(String userId){
        SpringWebSocketSessionManager wsManager = UserNoticeWebSocketHandler.getWsManager();
        List<String> sessionIds = wsManager.getUid2sid()
                .get(userId);
        Map<String, WebSocketSession> sessionPool = wsManager.getSessionPool();
        return sessionIds.stream()
                .map(sessionPool::get)
                .map(session -> new OnlineUserSessionDto()
                        .setSessionId(session.getId())
                        .setIp(session.getRemoteAddress().getAddress().getHostAddress())
                        .setUri(session.getUri().toString())
                )
                .collect(Collectors.toList());
    }

}
