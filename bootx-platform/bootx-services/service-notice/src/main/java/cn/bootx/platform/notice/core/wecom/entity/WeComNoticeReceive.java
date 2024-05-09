package cn.bootx.platform.notice.core.wecom.entity;

import cn.bootx.platform.common.core.util.CollUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;

import java.util.List;

/**
 * @author xxm
 * @since 2022/7/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "企业微信接收人参数")
public class WeComNoticeReceive {

    @Schema(description = "是否发送给全部用户")
    private boolean toAllUser;

    @Schema(description = "接收者的userid列表，最大用户列表长度1000")
    private List<String> useridList;

    @Schema(description = "接收者的部门id列表，最大列表长度100")
    private List<String> deptIdList;

    @Schema(description = "接收者的部门id列表，最大列表长度100")
    private List<String> tagList;

    @Schema(description = "是否是保密消息")
    private boolean safe;

    public void process(WxCpMessage wxCpMessage) {
        wxCpMessage.setSafe(safe ? "1" : "0");
        if (toAllUser) {
            wxCpMessage.setToUser("@all");
            return;
        }
        if (CollUtil.isNotEmpty(useridList)) {
            wxCpMessage.setToUser(String.join("|", useridList));
        }
        if (CollUtil.isNotEmpty(deptIdList)) {
            wxCpMessage.setToParty(String.join("|", deptIdList));
        }
        if (CollUtil.isNotEmpty(tagList)) {
            wxCpMessage.setToTag(String.join("|", tagList));
        }
    }

}
