package cn.bootx.platform.notice.controller;

import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.notice.core.dingtalk.entity.corp.DingCorpNoticeReceive;
import cn.bootx.platform.notice.core.dingtalk.entity.msg.DingTextMsg;
import cn.bootx.platform.notice.core.template.service.MessageTemplateService;
import cn.bootx.platform.notice.core.wecom.entity.WeComNoticeReceive;
import cn.bootx.platform.notice.core.wecom.entity.msg.WeComTextMsg;
import cn.bootx.platform.notice.service.DingTalkNoticeSender;
import cn.bootx.platform.notice.service.EmailNoticeSender;
import cn.bootx.platform.notice.service.WeComNoticeSender;
import cn.bootx.platform.notice.service.WeComRobotNoticeSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "nc测试")
@RestController
@RequestMapping("/nc/test")
@RequiredArgsConstructor
public class NcDemoController {

    private final MessageTemplateService messageTemplateService;

    private final EmailNoticeSender mailSendService;

    private final DingTalkNoticeSender dingTalkNoticeSender;

    private final WeComNoticeSender weComNoticeSender;

    private final WeComRobotNoticeSender weComRobotNoticeSender;

    @Operation(summary = "邮件消息测试")
    @PostMapping("/sendMsg")
    public ResResult<Void> sendMsg() {
        // 传入模板code和参数
        Map<String, Object> map = new HashMap<>();
        String data = messageTemplateService.rendering("code", map);
        // 调用发送
        mailSendService.sentSimpleMail("xxm@bootx.cn", "测试邮件", data);
        return Res.ok();
    }

    @Operation(summary = "钉钉消息测试")
    @PostMapping("/sendDingMsg")
    public ResResult<Void> sendDingMsg() {
        DingTextMsg msg = new DingTextMsg("中文通知");
        DingCorpNoticeReceive receive = new DingCorpNoticeReceive().setUseridList(Collections.singletonList(""));
        dingTalkNoticeSender.sendTextCorpNotice(msg, receive);
        return Res.ok();
    }

    @SneakyThrows
    @Operation(summary = "钉钉图片消息测试")
    @PostMapping("/sendDingImageMsg")
    public ResResult<Void> sendDingImageMsg(MultipartFile file) {
        DingCorpNoticeReceive receive = new DingCorpNoticeReceive()
            .setUseridList(Collections.singletonList("manager7303"));
        dingTalkNoticeSender.sendImageCorpNotice(file.getInputStream(), receive);
        return Res.ok();
    }

    @SneakyThrows
    @Operation(summary = "钉钉文件消息测试")
    @PostMapping("/sendDingFileMsg")
    public ResResult<Void> sendDingFileMsg(MultipartFile file) {
        DingCorpNoticeReceive receive = new DingCorpNoticeReceive()
            .setUseridList(Collections.singletonList("manager7303"));
        dingTalkNoticeSender.sendFileCorpNotice(file.getInputStream(), file.getOriginalFilename(), receive);
        return Res.ok();
    }

    @Operation(summary = "企微消息测试")
    @PostMapping("/sendWeComMsg")
    public ResResult<String> sendWeComMsg() {
        WeComTextMsg msg = new WeComTextMsg("企微消息测试");
        WeComNoticeReceive receive = new WeComNoticeReceive();
        receive.setUseridList(Collections.singletonList("XiaXiangMing"));
        return Res.ok(weComNoticeSender.sendTextNotice(msg, receive));
    }

    @SneakyThrows
    @Operation(summary = "企微图片消息测试")
    @PostMapping("/sendImageNotice")
    public ResResult<String> sendImageNotice(MultipartFile file) {
        WeComNoticeReceive receive = new WeComNoticeReceive();
        receive.setUseridList(Collections.singletonList("XiaXiangMing"));
        return Res.ok(weComNoticeSender.sendImageNotice(file.getInputStream(), receive));
    }

    @Operation(summary = "企微消息撤回")
    @PostMapping("/recallNotice")
    public ResResult<Void> recallNotice(String msgId) {
        weComNoticeSender.recallNotice(msgId);
        return Res.ok();
    }

    @SneakyThrows
    @Operation(summary = "企微机器人图片发送")
    @PostMapping("/p1")
    public ResResult<Void> p1(MultipartFile file) {
        weComRobotNoticeSender.sendImageNotice("bootx", file.getInputStream());
        return Res.ok();
    }

    @SneakyThrows
    @Operation(summary = "企微机器人文件发送")
    @PostMapping("/p2")
    public ResResult<Void> p2(MultipartFile file) {
        weComRobotNoticeSender.sendFIleNotice("bootx", file.getInputStream(), file.getOriginalFilename());
        return Res.ok();
    }

}
