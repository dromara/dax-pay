package cn.bootx.platform.notice.core.wecom.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;

/**
 * @author xxm
 * @since 2022/7/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "企微文件消息")
public class WeComFileMsg implements WeComMsg {

    @Schema(description = "资源id")
    private String mediaId;

    @Override
    public WxCpMessage toMsg() {
        return WxCpMessage.FILE().mediaId(mediaId).build();
    }

}
