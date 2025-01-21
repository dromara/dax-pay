package cn.bootx.platform.common.mybatisplus.handler;

import cn.bootx.platform.core.util.JsonUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * List<Long> 的类型转换器类
 * @author xxm
 * @since 2023/8/8
 */
@MappedTypes(List.class)
@MappedJdbcTypes({JdbcType.VARCHAR,JdbcType.LONGVARCHAR})
public class LongListTypeHandler  extends AbstractJsonTypeHandler<List<Long>> {

    /**
     * 默认初始化
     */
    public LongListTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public List<Long> parse(String json) {
        if (StrUtil.isNotBlank(json)){
            return JsonUtil.toBean(json, new TypeReference<>() {}, false);
        }
        return List.of();
    }

    @Override
    public String toJson(List<Long> obj) {
        return JsonUtil.toJsonStr(obj);
    }
}
