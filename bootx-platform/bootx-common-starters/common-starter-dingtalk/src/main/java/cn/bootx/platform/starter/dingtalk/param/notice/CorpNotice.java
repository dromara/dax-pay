package cn.bootx.platform.starter.dingtalk.param.notice;

import cn.bootx.platform.starter.dingtalk.param.notice.msg.Msg;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.hutool.core.lang.Opt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 钉钉发送工作通知参数
 *
 * @author xxm
 * @since 2022/7/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "钉钉发送工作通知参数")
public class CorpNotice {

    @Schema(description = "发送消息时使用的微应用的AgentID")
    private Long agentId;

    @Schema(description = "接收者的userid列表，最大用户列表长度100")
    private List<String> useridList;

    @Schema(description = "接收者的部门id列表，最大列表长度20")
    private List<String> deptIdList;

    @Schema(description = "是否发送给企业全部用户")
    private boolean toAllUser;

    @Schema(description = "消息内容")
    private Msg msg;

    /**
     * 输出参数
     */
    public String toParam() {
        Map<String, Object> map = new HashMap<>(5);
        String useridList = Opt.ofEmptyAble(this.useridList).map(list -> String.join(",", list)).orElse(null);
        String deptIdList = Opt.ofEmptyAble(this.deptIdList).map(list -> String.join(",", list)).orElse(null);
        map.put("agent_id", agentId);
        map.put("userid_list", useridList);
        map.put("dept_id_list", deptIdList);
        map.put("to_all_user", toAllUser);
        map.put("msg", msg);
        return JacksonUtil.toJson(map);
    }

}
