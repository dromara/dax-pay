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
@Schema(title = "文本卡片消息")
public class WeComTextCardMsg implements WeComMsg {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "点击后跳转的链接")
    private String url;

    @Schema(description = "按钮文字")
    private String btnTxt;

    @Override
    public WxCpMessage toMsg() {
        return WxCpMessage.TEXTCARD().title(title).description(description).url(url).btnTxt(btnTxt).build();
    }

}
