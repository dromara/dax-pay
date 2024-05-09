package cn.bootx.platform.notice.core.dingtalk.entity.msg;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.LinkMsg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钉钉链接信息
 *
 * @author xxm
 * @since 2022/7/19
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉链接信息")
public class DingLinkMsg implements DingMsg {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String text;

    @Schema(description = "点击消息跳转的URL")
    private String messageUrl;

    @Schema(description = "图片URL")
    private String picUrl;

    /**
     * 转换成钉钉消息
     */
    @Override
    public Msg toDingMsg() {
        return new LinkMsg(title, text, messageUrl, picUrl);
    }

}
