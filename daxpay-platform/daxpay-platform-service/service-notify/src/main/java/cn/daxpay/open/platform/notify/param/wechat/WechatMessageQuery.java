package cn.daxpay.open.platform.notify.param.wechat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 微信消息记录查询参数(管理端分页)
@Data
@Accessors(chain = true)
@Schema(title = "微信消息记录查询")
public class WechatMessageQuery {

    @Schema(description = "接收者 OpenId")
    private String openId;

    @Schema(description = "消息类型(template/uniform)")
    private String messageType;

    @Schema(description = "发送状态(success/failed/sending)")
    private String status;

    @Schema(description = "发送时间起")
    private OffsetDateTime startTime;

    @Schema(description = "发送时间止")
    private OffsetDateTime endTime;
}
