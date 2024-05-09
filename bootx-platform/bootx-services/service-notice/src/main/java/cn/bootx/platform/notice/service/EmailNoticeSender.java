package cn.bootx.platform.notice.service;

import cn.bootx.platform.notice.dto.mail.SendMailParam;

/**
 * 邮件发送服务
 *
 * @author xxm
 * @since 2022/7/16
 */
public interface EmailNoticeSender {

    /**
     * 简单邮件发送
     * @param email 邮件地址
     * @param subject 邮件标题
     * @param msg 邮件消息
     */
    void sentSimpleMail(String email, String subject, String msg);

    /**
     * 标准邮件发送方式
     */
    void sendMail(SendMailParam mailParam);

}
