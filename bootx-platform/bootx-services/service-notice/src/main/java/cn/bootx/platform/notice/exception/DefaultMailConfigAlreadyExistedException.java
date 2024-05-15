package cn.bootx.platform.notice.exception;

import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

import static cn.bootx.platform.notice.code.NoticeCenterErrorCode.DEFAULT_MAIL_CONFIG_ALREADY_EXISTED;

/**
 * 默认邮箱配置 已存在异常
 *
 * @author xxm
 * @since 2020/5/2 14:43
 */
public class DefaultMailConfigAlreadyExistedException extends BizException implements Serializable {

    private static final long serialVersionUID = 2775127389987392832L;

    public DefaultMailConfigAlreadyExistedException() {
        super(DEFAULT_MAIL_CONFIG_ALREADY_EXISTED, "默认邮箱配置已存在");
    }

}
