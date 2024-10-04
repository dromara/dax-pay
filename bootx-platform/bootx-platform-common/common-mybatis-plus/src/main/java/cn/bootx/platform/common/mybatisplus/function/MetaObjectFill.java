package cn.bootx.platform.common.mybatisplus.function;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 元对象填充接口
 * @author xxm
 * @since 2024/6/28
 */
public interface MetaObjectFill {

    /**
     * 插入填充
     */
    default void insertFill(MetaObject metaObject, MetaObjectHandler metaObjectHandler) {
    }

    /**
     * 更新填充
     */
    default void updateFill(MetaObject metaObject, MetaObjectHandler metaObjectHandler) {
    }
}
