package cn.bootx.platform.notice.exception;

import cn.bootx.platform.common.core.exception.FatalException;

import static cn.bootx.platform.notice.code.NoticeCenterErrorCode.MAIL_TEMPLATE_NOT_EXIST;

/**
 * 模板不存在
 *
 * @author xxm
 * @since 2020/11/18
 */
public class MailTemplateNotExistException extends FatalException {

    public MailTemplateNotExistException() {
        super(MAIL_TEMPLATE_NOT_EXIST, "邮箱模板不存在异常");
    }

}
