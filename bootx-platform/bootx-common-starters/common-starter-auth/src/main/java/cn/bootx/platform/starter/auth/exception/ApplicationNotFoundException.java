package cn.bootx.platform.starter.auth.exception;

import cn.bootx.platform.common.core.exception.BizException;

/**
 * 应用不存在
 *
 * @author xxm
 * @since 2022/6/27
 */
public class ApplicationNotFoundException extends BizException {

    public ApplicationNotFoundException() {
        super("未找到对应的应用");
    }

}
