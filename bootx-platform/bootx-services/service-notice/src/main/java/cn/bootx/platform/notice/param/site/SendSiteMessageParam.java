package cn.bootx.platform.notice.param.site;

import cn.bootx.platform.notice.code.SiteMessageCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * 站内信发送参数
 *
 * @author xxm
 * @since 2021/8/8
 */
@Data
@Accessors(chain = true)
@Schema(title = "站内信发送参数")
public class SendSiteMessageParam {

    /** 主键 */
    private Long id;

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String content;

    /** 发送者id */
    private Long senderId;

    /** 发送者姓名 */
    private String senderName;

    /**
     * 接收者类型
     * @see SiteMessageCode#RECEIVE_ALL
     */
    private String receiveType;

    /** 接收者id */
    private List<Long> receiveIds;

    /** 截至有效期 接收值为全体的时候必填写, 超过有效期后 */
    private LocalDate efficientTime;

    /** 用户处理方式 跳转路由/跳转链接/打开组件 */
    private String handleType;

    @Data
    @Accessors(chain = true)
    @Schema(title = "接收用户信息")
    public static class User {

        /** 接收者id */
        private Long receiveId;

        /** 接收者姓名 */
        private String receiveName;

    }

}
