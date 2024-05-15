package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 钉钉OA消息
 *
 * @author xxm
 * @since 2022/7/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "钉钉OA消息")
public class OaMsg extends Msg {

    @Schema(description = "OA消息体")
    private Oa oa;

    @Data
    @Accessors(chain = true)
    @Schema(title = "OA消息体")
    public static class Oa {

        @JsonProperty("message_url")
        @Schema(description = "消息点击链接地址")
        private String messageUrl;

        @JsonProperty("pc_message_url")
        @Schema(description = "PC端点击消息时跳转到的地址")
        private String pcMessageUrl;

        @Schema(description = "消息头部内容")
        private Head head;

        @Schema(description = "消息体")
        private Body body;

        @JsonProperty("status_bar")
        @Schema(description = "消息状态栏")
        private StatusBar statusBar;

        @Data
        @Accessors(chain = true)
        @Schema(title = "消息头部内容")
        public static class Head {

            @JsonProperty("bgcolor")
            @Schema(description = "消息头部的背景颜色")
            private String bgColor;

            @Schema(description = "消息的头部标题")
            private String text;

        }

        @Data
        @Accessors(chain = true)
        @Schema(title = "消息状态栏")
        public static class StatusBar {

            @Schema(description = "状态栏文案")
            @JsonProperty("status_value")
            private String statusValue;

            @Schema(description = "状态栏背景色")
            @JsonProperty("status_bg")
            private String statusBg;

        }

        @Data
        @Accessors(chain = true)
        @Schema(title = "消息体")
        public static class Body {

            @Schema(description = "消息体的标题")
            private String title;

            @Schema(description = "消息体的表单")
            private List<Form> form;

            @Schema(description = "单行富文本信息")
            private Rich rich;

            @Schema(description = "消息体的内容")
            private String content;

            @Schema(description = "消息体中的图片(媒体id)")
            private String image;

            @Schema(description = "自定义的附件数目")
            @JsonProperty("file_count")
            private String fileCount;

            @Schema(description = "自定义的作者名字")
            private String author;

            @Data
            @Accessors(chain = true)
            @Schema(title = "消息体的表单，最多显示6个，超过会被隐藏")
            public static class Form {

                @Schema(description = "消息体的关键字")
                private String key;

                @Schema(description = "消息体的关键字对应的值")
                private String value;

            }

            @Data
            @Accessors(chain = true)
            @Schema(title = "单行富文本信息")
            public static class Rich {

                @Schema(description = "单行富文本信息的数目")
                private String num;

                @Schema(description = "单行富文本信息的单位")
                private String unit;

            }

        }

    }

}
