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
@Schema(title = "文本消息")
public class WeComTextMsg implements WeComMsg {

    @Schema(description = "文本内容")
    private String content;

    @Override
    public WxCpMessage toMsg() {
        return WxCpMessage.TEXT().content(content).build();
    }

}
