package cn.bootx.platform.notice.core.wecom.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;

/**
 * @author xxm
 * @since 2022/7/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(title = "企微markdown消息")
public class WeComMarkdownMsg implements WeComMsg {

    @Schema(description = "markdown消息")
    private String content;

    @Override
    public WxCpMessage toMsg() {
        return WxCpMessage.MARKDOWN().content(content).build();
    }

}
