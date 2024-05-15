package cn.bootx.platform.notice.core.dingtalk.service;

import cn.bootx.platform.notice.service.DingRobotNoticeSender;
import cn.bootx.platform.starter.dingtalk.core.robot.service.DingRobotSendService;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.LinkMsg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.MarkdownMsg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.TextMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钉钉机器人消息
 *
 * @author xxm
 * @since 2022/7/19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DingRobotNoticeSenderImpl implements DingRobotNoticeSender {

    private final DingRobotSendService dingRobotSendService;

    @Override
    public void sendSimpleText(String code, String msg) {
        TextMsg dingTalkTextNotice = new TextMsg(msg);
        dingRobotSendService.sendNotice(code, dingTalkTextNotice);
    }

    @Override
    public void sendText(String code, TextMsg dingTalkTextNotice) {
        dingRobotSendService.sendNotice(code, dingTalkTextNotice);
    }

    @Override
    public void sendLink(String code, LinkMsg notice) {
        dingRobotSendService.sendNotice(code, notice);
    }

    @Override
    public void sendMarkdown(String code, MarkdownMsg notice) {
        dingRobotSendService.sendNotice(code, notice);
    }

}
