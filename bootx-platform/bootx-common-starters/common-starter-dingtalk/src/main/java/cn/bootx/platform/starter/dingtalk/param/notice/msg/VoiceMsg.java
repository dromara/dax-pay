package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import cn.bootx.platform.starter.dingtalk.code.DingTalkNoticeCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 钉钉语音消息
 *
 * @author xxm
 * @since 2022/7/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钉钉语音消息")
public class VoiceMsg extends Msg implements Serializable {

    @Schema(description = "钉钉语音")
    private DingVoice voice;

    public VoiceMsg() {
        super(DingTalkNoticeCode.MSG_VOICE);
    }

    public VoiceMsg(String mediaId, String duration) {
        super(DingTalkNoticeCode.MSG_VOICE);
        this.voice = new DingVoice(mediaId, duration);
    }

    @Data
    @Accessors(chain = true)
    @Schema(title = "钉钉语音")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DingVoice {

        @JsonProperty("media_id")
        @Schema(description = "媒体文件id")
        private String mediaId;

        @JsonProperty("duration")
        @Schema(description = "音频时长")
        private String duration;

    }

}
