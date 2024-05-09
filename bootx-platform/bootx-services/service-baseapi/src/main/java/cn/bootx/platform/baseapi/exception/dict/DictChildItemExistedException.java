package cn.bootx.platform.baseapi.exception.dict;

import cn.bootx.platform.baseapi.code.BspErrorCodes;
import cn.bootx.platform.common.core.exception.BizException;

import java.io.Serializable;

/**
 * @author xxm
 * @since 2020/4/16 22:08
 */
public class DictChildItemExistedException extends BizException implements Serializable {

    private static final long serialVersionUID = -3964173905076738575L;

    public DictChildItemExistedException() {
        super(BspErrorCodes.CHILD_ITEM_EXISTED, "存在字典子项，您无法将其删除。");
    }

}
