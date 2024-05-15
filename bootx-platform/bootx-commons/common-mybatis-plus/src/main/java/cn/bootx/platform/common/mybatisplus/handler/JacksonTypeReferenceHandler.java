package cn.bootx.platform.common.mybatisplus.handler;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * JSON字段类型转换抽象处理器, 需要进行继承实现, 可以在不在JSON字符串中记录数据类型，就可以对一些特殊类型进行反序列化
 * 例如: 集合类型List<T>, 泛型对象ResResult<T> 等
 * 通过 getTypeReference 接口方法, 将要进行反序列的对象传入
 *
 * @author xxm
 * @since 2024/1/3
 */
@Slf4j
@MappedTypes({ Object.class })
@MappedJdbcTypes(value = { JdbcType.VARCHAR, JdbcType.LONGVARCHAR })
public abstract class JacksonTypeReferenceHandler<T> extends AbstractJsonTypeHandler<Object> {

    /**
     * 返回要反序列化的类型对象
     */
    public abstract TypeReference<T> getTypeReference();

    /**
     * 反序列化
     */
    @Override
    protected Object parse(String json) {
        return JacksonUtil.toBean(json, this.getTypeReference());
    }

    /**
     * 序列化
     */
    @Override
    protected String toJson(Object obj) {
        return JacksonUtil.toJson(obj);
    }

}
