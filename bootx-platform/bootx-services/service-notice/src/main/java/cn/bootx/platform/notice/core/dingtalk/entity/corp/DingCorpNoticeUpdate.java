package cn.bootx.platform.notice.core.dingtalk.entity.corp;

import cn.bootx.platform.starter.dingtalk.param.notice.UpdateCorpNotice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 企业通知(OA消息)更新
 *
 * @author xxm
 * @since 2022/7/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "企业通知(OA消息)更新")
public class DingCorpNoticeUpdate {

    @Schema(description = "发送消息时使用的微应用的AgentID")
    private Long taskId;

    @Schema(description = "状态栏值")
    private String statusValue;

    @Schema(description = "状态栏背景色，推荐0xFF加六位颜色值")
    private String statusBg;

    /**
     * 转换为钉钉参数对象
     */
    public UpdateCorpNotice toDingUpdateCorpNotice() {
        return new UpdateCorpNotice().setTaskId(taskId).setStatusValue(statusValue).setStatusBg(statusBg);
    }

}
