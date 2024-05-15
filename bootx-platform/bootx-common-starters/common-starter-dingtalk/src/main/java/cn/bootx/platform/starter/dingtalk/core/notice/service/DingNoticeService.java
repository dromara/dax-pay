package cn.bootx.platform.starter.dingtalk.core.notice.service;

import cn.bootx.platform.starter.dingtalk.param.notice.ChatNotice;
import cn.bootx.platform.starter.dingtalk.param.notice.CorpNotice;
import cn.bootx.platform.starter.dingtalk.param.notice.RecallCorpNotice;
import cn.bootx.platform.starter.dingtalk.param.notice.UpdateCorpNotice;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.starter.dingtalk.core.base.service.DingAccessService;
import cn.bootx.platform.starter.dingtalk.core.base.result.DingTalkResult;
import cn.bootx.platform.starter.dingtalk.core.notice.result.ChatNoticeResult;
import cn.bootx.platform.starter.dingtalk.core.notice.result.CorpNoticeResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.bootx.platform.starter.dingtalk.code.DingTalkCode.*;

/**
 * 钉钉通知发送服务
 *
 * @author xxm
 * @since 2022/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DingNoticeService {

    private final DingAccessService dingAccessService;

    /**
     * 发送普通消息, 支持普通群和个人会话
     *
     * <a href=
     * "https://open.dingtalk.com/document/orgapp-server/send-normal-messages">...</a>
     */
    public void sendNotice() {
    }

    /**
     * 发送企业群消息
     *
     * <a href=
     * "https://open.dingtalk.com/document/orgapp-server/enterprise-group-message-overview">...</a>
     */
    public ChatNoticeResult sendChatNotice(ChatNotice param) {
        String accessToken = dingAccessService.getAccessToken();
        String responseBody = HttpUtil.createPost(StrUtil.format(NOTICE_CHAT_URL, accessToken))
            .body(param.toParam())
            .execute()
            .body();
        return JacksonUtil.toBean(responseBody, ChatNoticeResult.class);
    }

    /**
     * 发送工作通知
     *
     * <a href=
     * "https://open.dingtalk.com/document/orgapp-server/asynchronous-sending-of-enterprise-session-messages">...</a>
     */
    public CorpNoticeResult sendCorpNotice(CorpNotice param) {
        String accessToken = dingAccessService.getAccessToken();
        String responseBody = HttpUtil.createPost(StrUtil.format(NOTICE_CORP_SEND_URL, accessToken))
            .body(param.toParam())
            .execute()
            .body();
        return JacksonUtil.toBean(responseBody, CorpNoticeResult.class);
    }

    /**
     * 更新工作通知状态栏
     * @url <a href=
     * "https://open.dingtalk.com/document/orgapp-server/update-work-notification-status-bar">...</a>
     */
    public CorpNoticeResult updateCorpNotice(UpdateCorpNotice param) {
        String accessToken = dingAccessService.getAccessToken();
        String responseBody = HttpUtil.createPost(StrUtil.format(NOTICE_CORP_UPDATE_URL, accessToken))
            .body(param.toParam())
            .execute()
            .body();
        return JacksonUtil.toBean(responseBody, CorpNoticeResult.class);
    }

    /**
     * 撤回工作通知消息
     * @url <a href=
     * "https://open.dingtalk.com/document/orgapp-server/notification-of-work-withdrawal">...</a>
     */
    public DingTalkResult<?> recallCorpNotice(RecallCorpNotice param) {

        String accessToken = dingAccessService.getAccessToken();
        String responseBody = HttpUtil.createPost(StrUtil.format(NOTICE_CORP_RECALL_URL, accessToken))
            .body(param.toParam())
            .execute()
            .body();
        return JacksonUtil.toBean(responseBody, DingTalkResult.class);
    }

}
