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
@Schema(title = "企微图片消息")
public class WeComImageMsg implements WeComMsg {

    @Schema(description = "资源id")
    private String mediaId;

    @Override
    public WxCpMessage toMsg() {
        return WxCpMessage.IMAGE().mediaId(mediaId).build();
    }

}
