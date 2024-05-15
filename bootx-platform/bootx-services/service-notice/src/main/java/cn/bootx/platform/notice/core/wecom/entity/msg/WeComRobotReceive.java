package cn.bootx.platform.notice.core.wecom.entity.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 企微机器人接收人配置
 *
 * @author xxm
 * @since 2022/7/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "企微机器人接收人配置")
public class WeComRobotReceive {

    List<String> mentionedList;

    List<String> mobileList;

}
