package cn.daxpay.open.platform.notify.service.wechat;

import cn.daxpay.open.platform.capability.wechat.message.param.TemplateMessageParam;
import cn.daxpay.open.platform.capability.wechat.message.result.MessageSendResult;
import cn.daxpay.open.platform.capability.wechat.message.service.WechatMpMessageService;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.notify.dao.wechat.WechatMessageRecordManager;
import cn.daxpay.open.platform.notify.entity.wechat.WechatMessageRecord;
import cn.daxpay.open.platform.notify.param.wechat.WechatMessageQuery;
import cn.daxpay.open.platform.notify.result.wechat.WechatMessageRecordResult;
import cn.daxpay.open.platform.system.entity.config.platform.auth.PlatformWechatMpAuthConfig;
import cn.daxpay.open.platform.system.service.config.auth.PlatformWechatMpAuthConfigService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/// # 微信消息记录服务(查询/重发)
///
/// 记录由 [WechatNotifyService] 发送时写入,
/// 本类负责管理端查询与失败重发.
/// 重发从三方平台 [PlatformWechatMpAuthConfigService] 取 AppId/AppSecret,
/// 调能力层 [WechatMpMessageService] 发送.
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMessageRecordService {

    /// 状态编码: 发送中
    public static final String STATUS_SENDING = "sending";

    /// 状态编码: 成功
    public static final String STATUS_SUCCESS = "success";

    /// 状态编码: 失败
    public static final String STATUS_FAILED = "failed";

    /// 消息类型: 公众号模板消息
    public static final String TYPE_TEMPLATE = "template";

    private final WechatMessageRecordManager recordManager;

    private final WechatMpMessageService mpMessageService;

    private final PlatformWechatMpAuthConfigService mpAuthConfigService;

    /// 保存记录
    public void saveRecord(WechatMessageRecord record) {
        recordManager.save(record);
    }

    /// 更新发送状态
    public void updateStatus(Long recordId, String status, String msgId, String errorCode, String errorMsg) {
        recordManager.findById(recordId).ifPresent(record -> {
            record.setStatus(status);
            record.setMsgId(msgId);
            record.setErrorCode(errorCode);
            record.setErrorMsg(errorMsg);
            recordManager.updateById(record);
        });
    }

    /// 分页查询
    public PageResult<WechatMessageRecordResult> page(PageParam pageParam, WechatMessageQuery query) {
        return MpUtil.toPageResult(recordManager.page(pageParam, query));
    }

    /// 详情
    public WechatMessageRecordResult findById(Long id) {
        return recordManager.findById(id)
                .map(WechatMessageRecord::toResult)
                .orElse(null);
    }

    /// 重发(仅公众号模板消息, 只允许重发失败的记录)
    public void resend(Long recordId) {
        WechatMessageRecord record = recordManager.findById(recordId)
                .orElseThrow(() -> new OperationFailException("error.channel.wechat.messageRecordNotExist"));
        if (!STATUS_FAILED.equals(record.getStatus())) {
            throw new OperationFailException("error.channel.wechat.onlyFailedCanResend");
        }
        // 从三方平台取 AppId/AppSecret
        PlatformWechatMpAuthConfig auth = mpAuthConfigService.getWechatMpAuthConfig();
        if (StrUtil.isBlank(auth.getAppId()) || StrUtil.isBlank(auth.getAppSecret())) {
            throw new OperationFailException("error.channel.wechat.mpAuthConfigIncomplete");
        }
        // 构造发送参数
        TemplateMessageParam param = new TemplateMessageParam()
                .setWxAppId(auth.getAppId())
                .setAppSecret(auth.getAppSecret())
                .setOpenId(record.getOpenId())
                .setTemplateId(record.getTemplateId())
                .setUrl(record.getUrl())
                .setScene(record.getScene());
        if (StrUtil.isNotBlank(record.getTemplateData())) {
            param.setData(JSONUtil.toBean(record.getTemplateData(), Map.class));
        }
        // 调能力层发送(失败封装进 result, 不抛)
        MessageSendResult result = mpMessageService.sendTemplateMessage(param);
        // 更新记录状态
        if (Boolean.TRUE.equals(result.getSuccess())) {
            updateStatus(recordId, STATUS_SUCCESS, result.getMsgId(), null, null);
        } else {
            updateStatus(recordId, STATUS_FAILED, null, result.getErrorCode(), result.getErrorMsg());
        }
    }
}
