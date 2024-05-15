package cn.bootx.platform.notice.core.dingtalk.entity.corp;

import cn.bootx.platform.starter.dingtalk.param.notice.CorpNotice;
import cn.bootx.platform.notice.core.dingtalk.entity.msg.DingMsg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 钉钉企业通知消息父类
 *
 * @author xxm
 * @since 2022/7/19
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉企业通知消息父类")
@AllArgsConstructor
@NoArgsConstructor
public class DingCorpNoticeParam {

    @Schema(description = "接收人配置")
    private DingCorpNoticeReceive receive;

    @Schema(description = "钉钉消息内容")
    private DingMsg dingMsg;

    /**
     * 转换成钉钉消息发送参数
     */
    public CorpNotice toDingCorpNotice() {
        return new CorpNotice().setToAllUser(receive.isToAllUser())
            .setDeptIdList(receive.getDeptIdList())
            .setUseridList(receive.getUseridList())
            .setMsg(dingMsg.toDingMsg());
    }

}
