package cn.bootx.platform.notice.core.dingtalk.entity.msg;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.FileMsg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 钉钉文件工作通知消息
 *
 * @author xxm
 * @since 2022/7/19
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "钉钉图片工作通知消息")
public class DingFileMsg implements DingMsg {

    @Schema(description = "媒体文件id")
    private String mediaId;

    /**
     * 转换成钉钉消息
     */
    @Override
    public Msg toDingMsg() {
        return new FileMsg(mediaId);
    }

}
