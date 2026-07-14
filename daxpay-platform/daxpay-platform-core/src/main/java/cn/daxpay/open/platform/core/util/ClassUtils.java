package cn.daxpay.open.platform.core.util;

import cn.hutool.core.util.ClassUtil;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Objects;

/// # 类工具类
///
@UtilityClass
public class ClassUtils {

    /// 获取字段属性
    public Field getField(Class<?> clas, String fieldName){
        if (Objects.isNull(clas)){
            throw new IllegalArgumentException("类为空");
        }
        // 查询是否有该属性
        Field field = ClassUtil.getDeclaredField(clas, fieldName);
        if (Objects.nonNull(field)){
            return field;
        }
        // 递归查询父类中的字段值
        if (Objects.nonNull(clas.getSuperclass())){
            return getField(clas.getSuperclass(),fieldName);
        }
        return null;
    }
}
