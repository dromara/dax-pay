package cn.bootx.platform.common.mybatisplus.handler;

import cn.bootx.platform.common.mybatisplus.function.MetaObjectFill;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * mp自动填填充
 *
 * @author xxm
 * @since 2021/7/27
 */
@Component
@RequiredArgsConstructor
public class MpMetaObjectHandler implements MetaObjectHandler {
    private final List<MetaObjectFill> metaObjectFills;
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObjectFills.forEach(metaObjectFill -> metaObjectFill.insertFill(metaObject, this));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObjectFills.forEach(metaObjectFill -> metaObjectFill.updateFill(metaObject, this));
    }

}
