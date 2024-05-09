package cn.bootx.platform.baseapi.exception.dict;

import cn.bootx.platform.baseapi.code.BspErrorCodes;
import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/4/10 15:14
 */
public class DictAlreadyExistedException extends BizException implements Serializable {

    public DictAlreadyExistedException() {
        super(BspErrorCodes.DICTIONARY_ALREADY_EXISTED, "字典已经存在.");
    }

}
