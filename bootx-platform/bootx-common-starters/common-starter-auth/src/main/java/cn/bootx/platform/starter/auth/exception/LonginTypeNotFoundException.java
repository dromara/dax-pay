package cn.bootx.platform.starter.auth.exception;

import cn.bootx.platform.common.core.exception.BizException;

/**
 * 登录方式不存在
 *
 * @author xxm
 * @since 2022/11/6
 */
public class LonginTypeNotFoundException extends BizException {

    public LonginTypeNotFoundException() {
        super("未找到对应的登录方式");
    }

}
