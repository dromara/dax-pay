package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 钉钉卡片消息
 *
 * @author xxm
 * @since 2022/7/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "钉钉卡片消息")
public class ActionCardMsg extends Msg {

    @Schema(description = "消息内容，支持markdown")
    @JsonProperty("action_card")
    private ActionCard actionCard;

    @Data
    @Accessors(chain = true)
    @Schema(title = "消息内容，支持markdown")
    public static class ActionCard {

        @Schema(description = "透出到会话列表和通知的文案")
        private String title;

        @Schema(description = "消息内容")
        private String markdown;

        @JsonProperty("single_title")
        @Schema(description = "标题")
        private String singleTitle;

        @JsonProperty("single_url")
        @Schema(description = "消息点击链接地址")
        private String singleUrl;

        @JsonProperty("btn_orientation")
        @Schema(description = "使用独立跳转ActionCard样式时的按钮排列方式")
        private String btnOrientation;

        @JsonProperty("btn_json_list")
        @Schema(description = "使用独立跳转ActionCard样式时的按钮列表")
        private List<BtnJson> btnJsonList;

        @Data
        @Accessors(chain = true)
        @Schema(title = "使用独立跳转ActionCard样式时的按钮列表")
        public static class BtnJson {

            @Schema(description = "按钮的标题")
            private String title;

            @JsonProperty("action_url")
            @Schema(description = "跳转链接")
            private String actionUrl;

        }

    }

}
