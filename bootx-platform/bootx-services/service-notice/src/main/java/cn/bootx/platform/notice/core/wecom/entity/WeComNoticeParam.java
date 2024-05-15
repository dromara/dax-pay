package cn.bootx.platform.notice.core.wecom.entity;

import cn.bootx.platform.notice.core.wecom.entity.msg.WeComMsg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;

/**
 * 企业微信发送消息参数
 *
 * @author xxm
 * @since 2022/7/23
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "企业微信发送消息参数")
public class WeComNoticeParam {

    @Schema(description = "接收人配置")
    private WeComNoticeReceive receive;

    @Schema(description = "消息内容")
    private WeComMsg msg;

    /**
     * 转换成钉钉消息发送参数
     */
    public WxCpMessage toWeComNotice() {
        WxCpMessage wxCpMessage = msg.toMsg();
        receive.process(wxCpMessage);
        return wxCpMessage;
    }

}
