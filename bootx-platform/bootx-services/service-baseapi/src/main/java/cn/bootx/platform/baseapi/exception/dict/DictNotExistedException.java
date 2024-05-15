package cn.bootx.platform.baseapi.exception.dict;

import cn.bootx.platform.baseapi.code.BspErrorCodes;
import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/4/21 11:53
 */
public class DictNotExistedException extends BizException implements Serializable {

    public DictNotExistedException() {
        super(BspErrorCodes.DICTIONARY_NOT_EXISTED, "字典不存在.");
    }

}
