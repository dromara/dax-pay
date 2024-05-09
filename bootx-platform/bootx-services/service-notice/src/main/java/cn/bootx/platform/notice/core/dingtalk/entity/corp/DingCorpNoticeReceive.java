package cn.bootx.platform.notice.core.dingtalk.entity.corp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 企业通知接收人配置
 *
 * @author xxm
 * @since 2022/7/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "企业通知接收人配置")
public class DingCorpNoticeReceive {

    @Schema(description = "接收者的userid列表，最大用户列表长度100")
    private List<String> useridList;

    @Schema(description = "接收者的部门id列表，最大列表长度20")
    private List<String> deptIdList;

    @Schema(description = "是否发送给企业全部用户")
    private boolean toAllUser;

    /**
     * 发送给全部用户
     */
    public static DingCorpNoticeReceive toAll() {
        return new DingCorpNoticeReceive().setToAllUser(true);
    }

}
