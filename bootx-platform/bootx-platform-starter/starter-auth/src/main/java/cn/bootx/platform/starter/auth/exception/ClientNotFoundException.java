package cn.bootx.platform.starter.auth.exception;

import cn.bootx.platform.core.exception.BizException;

/**
 * 终端不存在
 *
 * @author xxm
 * @since 2021/8/25
 */
public class ClientNotFoundException extends BizException {

    public ClientNotFoundException() {
        super("未找到对应的终端");
    }

}
