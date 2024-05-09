package cn.bootx.platform.starter.wecom.code;

/**
 * 企微常量
 *
 * @author xxm
 * @since 2022/7/23
 */
public interface WeComCode {

    /* 请求地址 */
    /** 撤回应用消息 */
    String NOTICE_RECALL_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/recall";

    /** 机器人webhook */
    String ROBOT_WEBHOOK_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key={}";

    /** 机器人文件上传 */
    String ROBOT_UPLOAD_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?key={}&type=file";

    /* 消息通知 */
    /** 消息ID */
    String NOTICE_MSG_ID = "msgid";

    /* 请求参数 */
    /** 文件名称 */
    String FILE_NAME = "filename";

    /** 文件长度 */
    String FILE_LENGTH = "filelength";

    /** 文件类型 */
    String CONTENT_TYPE = "content-type";

    /** 文件 */
    String MEDIA = "media";

}
