package cn.bootx.platform.notice.exception;

import cn.bootx.platform.common.core.exception.FatalException;

import static cn.bootx.platform.notice.code.NoticeCenterErrorCode.MAIL_CONFIG_NOT_EXIST;

/**
 * 邮箱配置不存在异常
 */
public class MailConfigNotExistException extends FatalException {

    public MailConfigNotExistException() {
        super(MAIL_CONFIG_NOT_EXIST, "邮箱配置不存在异常");
    }

}
