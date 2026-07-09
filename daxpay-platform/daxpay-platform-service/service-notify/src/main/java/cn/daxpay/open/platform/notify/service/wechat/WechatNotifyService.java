package cn.daxpay.open.platform.notify.service.wechat;

import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.capability.wechat.message.param.TemplateMessageParam;
import cn.daxpay.open.platform.capability.wechat.message.result.MessageSendResult;
import cn.daxpay.open.platform.capability.wechat.message.service.WechatMpMessageService;
import cn.daxpay.open.platform.iam.service.social.IamUserSocialBindStore;
import cn.daxpay.open.platform.notify.entity.wechat.WechatMessageRecord;
import cn.daxpay.open.platform.system.entity.config.platform.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.service.config.PlatformWechatMpAuthConfigService;
import cn.daxpay.open.platform.system.service.config.PlatformWechatNotifyConfigService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

/// # 微信公众号模板通知发送门面(编排层)
///
/// 业务方通过 [sendToUser] 发送模板通知, 内部完成:
/// "读三方平台凭据 -> 读消息通知配置 -> 反查 openId -> 选模板 -> 调能力层发送 -> 写记录"全链路.
///
/// 降级策略(不抛异常, 不阻塞主业务, 每个降级点都写一条 failed 记录留痕):
/// - 三方平台公众号凭据不全(AppId/AppSecret 为空): 标"凭据未配置"
/// - 用户未绑定公众号(source=weChat 无 openId): 标"未绑定"
/// - 场景未配置模板: 标"场景无模板"
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatNotifyService {

    /// 社交来源: 微信公众号(对应 [SocialSourceEnum.WECHAT_MP], 绑定存于 iam_user_social)
    private static final String SOURCE_WECHAT_MP = SocialSourceEnum.WECHAT_MP.getCode();

    private final PlatformWechatMpAuthConfigService mpAuthConfigService;

    private final PlatformWechatNotifyConfigService notifyConfigService;

    private final IamUserSocialBindStore socialBindStore;

    private final WechatMpMessageService mpMessageService;

    private final WechatMessageRecordService recordService;

    /// 发送模板通知给指定平台用户
    ///
    /// @param userId 平台用户ID
    /// @param scene  业务场景(trade/operate), 决定使用哪个模板
    /// @param data   模板变量(key=字段名, value=字段值)
    /// @param url    点击跳转链接(可空)
    /// @return 发送结果(success=false 时含错误信息, 不抛异常)
    public MessageSendResult sendToUser(Long userId, String scene, Map<String, String> data, String url) {
        // 读三方平台公众号凭据
        PlatformWechatMpAuthConfig auth = mpAuthConfigService.getWechatMpAuthConfig();
        // 预写一条 sending 记录(便于追溯, 即使降级也留痕)
        WechatMessageRecord record = buildRecord(userId, scene, auth.getAppId());
        recordService.saveRecord(record);

        // 降级1: 三方平台凭据不全
        if (StrUtil.isBlank(auth.getAppId()) || StrUtil.isBlank(auth.getAppSecret())) {
            return degrade(record, "wechat mp auth config incomplete (appId/appSecret missing in third platform)", userId);
        }
        // 反查 openId(userId -> weChat 绑定)
        String openId = socialBindStore.findOpenIdByUserId(userId, SOURCE_WECHAT_MP).orElse(null);
        record.setOpenId(openId);
        // 降级2: 未绑定公众号
        if (StrUtil.isBlank(openId)) {
            return degrade(record, "user not bound to wechat official account", userId);
        }
        // 选模板(系统平台非加密配置 wechat_notify)
        String templateId = notifyConfigService.getTemplateIdByScene(scene);
        // 降级3: 场景无模板
        if (StrUtil.isBlank(templateId)) {
            return degrade(record, "no template configured for scene: " + scene, userId);
        }
        record.setTemplateId(templateId);
        // 构造参数调能力层(失败封装进 result, 不抛)
        TemplateMessageParam param = new TemplateMessageParam()
                .setWxAppId(auth.getAppId())
                .setAppSecret(auth.getAppSecret())
                .setOpenId(openId)
                .setTemplateId(templateId)
                .setUrl(url)
                .setScene(scene)
                .setData(data);
        MessageSendResult result = mpMessageService.sendTemplateMessage(param);
        // 回填 templateData/url 到记录(JSON 落库, 便于重发)
        record.setTemplateData(JSONUtil.toJsonStr(data));
        record.setUrl(url);
        // 更新记录状态
        if (Boolean.TRUE.equals(result.getSuccess())) {
            recordService.updateStatus(record.getId(), WechatMessageRecordService.STATUS_SUCCESS,
                    result.getMsgId(), null, null);
        } else {
            recordService.updateStatus(record.getId(), WechatMessageRecordService.STATUS_FAILED,
                    null, result.getErrorCode(), result.getErrorMsg());
        }
        return result;
    }

    /// 降级处理: 写 failed 记录并返回失败结果
    private MessageSendResult degrade(WechatMessageRecord record, String msg, Long userId) {
        log.warn("微信通知发送降级: {}, userId={}", msg, userId);
        recordService.updateStatus(record.getId(), WechatMessageRecordService.STATUS_FAILED, null, null, msg);
        return new MessageSendResult().setSuccess(false).setErrorMsg(msg);
    }

    /// 构造预写记录(sending 状态, openId/templateId/templateData/url 后续回填)
    private WechatMessageRecord buildRecord(Long userId, String scene, String wxAppId) {
        WechatMessageRecord record = new WechatMessageRecord();
        record.setUserId(userId);
        record.setMessageType(WechatMessageRecordService.TYPE_TEMPLATE);
        record.setScene(scene);
        record.setWxAppId(wxAppId);
        record.setStatus(WechatMessageRecordService.STATUS_SENDING);
        record.setSendTime(OffsetDateTime.now(ZoneOffset.UTC));
        return record;
    }
}
