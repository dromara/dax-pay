package cn.bootx.platform.notice.core.dingtalk.entity.msg;

import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.ActionCardMsg;
import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 钉钉卡片工作通知消息
 *
 * @author xxm
 * @since 2022/7/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉卡片工作通知消息")
public class DingActionCardMsg implements DingMsg {

    @Schema(description = "透出到会话列表和通知的文案")
    private String title;

    @Schema(description = "消息内容")
    private String markdown;

    @Schema(description = "标题")
    private String singleTitle;

    @Schema(description = "消息点击链接地址")
    private String singleUrl;

    @Schema(description = "使用独立跳转ActionCard样式时的按钮排列方式(1竖直排列/2横向排列)")
    private String btnOrientation;

    @Schema(description = "独立跳转ActionCard样式时的按钮列表")
    private List<OrientationBtn> orientationBtnList;

    @Data
    @Accessors(chain = true)
    @Schema(title = "独立跳转ActionCard样式时的按钮列表")
    public static class OrientationBtn {

        @Schema(description = "按钮的标题")
        private String title;

        @JsonProperty("action_url")
        @Schema(description = "跳转链接")
        private String actionUrl;

    }

    /**
     * 转换成钉钉消息
     */
    public Msg toDingMsg() {
        ActionCardMsg.ActionCard actionCard = new ActionCardMsg.ActionCard().setTitle(title)
            .setMarkdown(markdown)
            .setSingleTitle(singleTitle)
            .setSingleUrl(singleUrl)
            .setBtnOrientation(btnOrientation);
        if (CollUtil.isNotEmpty(orientationBtnList)) {
            List<ActionCardMsg.ActionCard.BtnJson> btnJsons = orientationBtnList.stream()
                .map(o -> new ActionCardMsg.ActionCard.BtnJson().setTitle(o.title).setActionUrl(o.actionUrl))
                .collect(Collectors.toList());
            actionCard.setBtnJsonList(btnJsons);
        }

        return new ActionCardMsg().setActionCard(actionCard);
    }

}
