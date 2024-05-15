package cn.bootx.platform.notice.core.dingtalk.entity.msg;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.VoiceMsg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 声音消息
 *
 * @author xxm
 * @since 2022/7/19
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "声音工作通知消息")
public class DingVoiceMsg implements DingMsg {

    @Schema(description = "媒体文件id")
    private String mediaId;

    @Schema(description = "音频时长")
    private String duration;

    /**
     * 转换成钉钉消息
     */
    @Override
    public Msg toDingMsg() {
        return new VoiceMsg(mediaId, duration);
    }

}
