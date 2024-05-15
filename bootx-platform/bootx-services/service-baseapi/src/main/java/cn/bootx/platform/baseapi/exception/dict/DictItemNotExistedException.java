package cn.bootx.platform.baseapi.exception.dict;

import cn.bootx.platform.baseapi.code.BspErrorCodes;
import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/4/21 11:54
 */
public class DictItemNotExistedException extends BizException implements Serializable {

    public DictItemNotExistedException() {
        super(BspErrorCodes.DICTIONARY_ITEM_NOT_EXISTED, "字典项不存在.");
    }

}
