package cn.bootx.platform.common.mybatisplus.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List<Long> 的类型转换器类
 * @author xxm
 * @since 2023/8/8
 */
@MappedTypes(List.class)
@MappedJdbcTypes({JdbcType.VARCHAR,JdbcType.LONGVARCHAR})
public class LongListTypeHandler  extends AbstractJsonTypeHandler<List<Long>> {

    private static final String COMMA = ",";

    @Override
    protected List<Long> parse(String value) {
        if (StrUtil.isNotBlank(value)){
            long[] longs = StrUtil.splitToLong(value, COMMA);
            return  Arrays.stream(longs).boxed().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    protected String toJson(List<Long> obj) {
        return CollUtil.join(obj, COMMA);

    }
}
