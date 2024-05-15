package cn.bootx.platform.baseapi.exception.dict;

import cn.bootx.platform.baseapi.code.BspErrorCodes;
import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/4/21 11:53
 */
public class DictItemAlreadyExistedException extends BizException implements Serializable {

    public DictItemAlreadyExistedException() {
        super(BspErrorCodes.DICTIONARY_ITEM_ALREADY_EXISTED, "字典项目已存在.");
    }

}
