package cn.daxpay.open.platform.notify.result.wechat;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 微信消息记录结果
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信消息记录结果")
public class WechatMessageRecordResult extends BaseResult {

    @Schema(description = "接收平台用户ID")
    private Long userId;

    @Schema(description = "消息类型(template/uniform)")
    private String messageType;

    @Schema(description = "接收者 OpenId")
    private String openId;

    @Schema(description = "模板ID")
    private String templateId;

    @Schema(description = "模板数据(JSON)")
    private String templateData;

    @Schema(description = "跳转链接")
    private String url;

    @Schema(description = "发送状态(success/failed/sending)")
    private String status;

    @Schema(description = "微信消息ID")
    private String msgId;

    @Schema(description = "错误码")
    private String errorCode;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "发送时间")
    private OffsetDateTime sendTime;

    @Schema(description = "业务场景")
    private String scene;

    @Schema(description = "使用的 AppId")
    private String wxAppId;
}
