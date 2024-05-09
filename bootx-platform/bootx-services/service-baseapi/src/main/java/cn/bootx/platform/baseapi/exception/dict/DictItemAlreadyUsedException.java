package cn.bootx.platform.baseapi.exception.dict;

import cn.bootx.platform.baseapi.code.BspErrorCodes;
import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/4/21 11:54
 */
public class DictItemAlreadyUsedException extends BizException implements Serializable {

    public DictItemAlreadyUsedException() {
        super(BspErrorCodes.DICTIONARY_ITEM_ALREADY_USED, "词典项目已被使用.");
    }

}
