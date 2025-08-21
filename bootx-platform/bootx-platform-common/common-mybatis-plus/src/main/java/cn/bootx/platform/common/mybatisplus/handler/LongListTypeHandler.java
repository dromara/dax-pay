package cn.bootx.platform.common.mybatisplus.handler;

import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.Field;
import java.util.List;

/**
 * List<Long> 的类型转换器类
 * @author xxm
 * @since 2023/8/8
 */
@MappedTypes(List.class)
@MappedJdbcTypes({JdbcType.VARCHAR,JdbcType.LONGVARCHAR})
public class LongListTypeHandler  extends AbstractJsonTypeHandler<List<Long>> {

    public LongListTypeHandler(Class<?> type) {
        super(type);
    }

    public LongListTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }
    @Override
    public List<Long> parse(String json) {
        if (StrUtil.isNotBlank(json)){
            return JSONUtil.toBean(json, new TypeReference<>() {}, false);
        }
        return List.of();
    }

    @Override
    public String toJson(List<Long> obj) {
        return JSONUtil.toJsonStr(obj);
    }
}
