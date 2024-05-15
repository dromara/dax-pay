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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(title = "企微视频消息")
public class WeComVideoMsg implements WeComMsg {

    @Schema(description = "视频消息的标题")
    private String title;

    @Schema(description = "资源id")
    private String mediaId;

    @Schema(description = "视频消息的描述")
    private String description;

    @Override
    public WxCpMessage toMsg() {
        return WxCpMessage.VIDEO().title(title).mediaId(mediaId).description(description).build();
    }

}
