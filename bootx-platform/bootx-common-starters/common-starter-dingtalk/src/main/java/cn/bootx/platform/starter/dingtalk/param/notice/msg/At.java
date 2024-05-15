package cn.bootx.platform.starter.dingtalk.param.notice.msg;

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 钉钉At
 *
 * @author xxm
 * @since 2020/11/29
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(title = "钉钉@指定用户")
public class At implements Serializable {

    private static final long serialVersionUID = -8677524557109058147L;

    @Schema(description = "@的用户手机号")
    private List<String> atMobiles;

    @Schema(description = "@被@人的用户userid")
    private List<String> atUserIds;

    @Schema(description = "是否@全体")
    private boolean isAtAll = false;

    public At(List<String> atMobiles) {
        this.atMobiles = atMobiles;
    }

    /**
     * 输出@xxx文本
     */
    public String toAtMobiles() {
        if (CollUtil.isNotEmpty(atMobiles)) {
            return " @" + String.join(" @", atMobiles);
        }
        return "";
    }

    /**
     * 输出@xxx文本
     */
    public String toAtUserIds() {
        if (CollUtil.isNotEmpty(atUserIds)) {
            return " @" + String.join(" @", atUserIds);
        }
        return "";
    }

}
