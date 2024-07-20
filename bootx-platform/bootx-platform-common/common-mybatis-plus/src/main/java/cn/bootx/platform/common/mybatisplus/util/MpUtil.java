package cn.bootx.platform.common.mybatisplus.util;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.annotation.BigField;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * MP工具类
 *
 * @author xxm
 * @since 2020/4/21 10:00
 */
@UtilityClass
public class MpUtil {

    /**
     * 获取分页对象 MyBatis-Plus
     */
    public <T> Page<T> getMpPage(PageParam page) {
        return Page.of(page.getCurrent(), page.getSize());
    }
    /**
     * 获取分页对象 MyBatis-Plus
     */
    public <T> Page<T> getMpPage(PageParam page, Class<T> clazz) {
        return Page.of(page.getCurrent(), page.getSize());
    }

    /**
     * 获取行名称
     * @param function 对象字段对应的读取方法的Lambda表达式
     * @return 字段名
     */
    public <T> String getColumnName(SFunction<T, ?> function) {
        LambdaMeta meta = LambdaUtils.extract(function);
        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(meta.getInstantiatedClass());
        Assert.notEmpty(columnMap, "错误:无法执行.因为无法获取到实体类的表对应缓存!");
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));
        return columnCache.getColumn();
    }

    /**
     * 获取行名称
     * @param readMethod 对象字段对应的读取方法对象
     * @param clazz 实体类类型. 辅助进行判断, 传多个只有第一个生效，可以为空, 为空时使用读取方法对应的Class类，
     * @return 字段名
     */
    @SafeVarargs
    public <T> String getColumnName(Method readMethod, Class<T>... clazz) {
        Class<?> beanClass;
        if (ArrayUtil.isNotEmpty(clazz)) {
            beanClass = clazz[0];
        }
        else {
            beanClass = readMethod.getDeclaringClass();
        }

        Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(beanClass);
        Assert.notEmpty(columnMap, "错误:无法执行.因为无法获取到实体类的表对应缓存!");
        String fieldName = PropertyNamer.methodToProperty(readMethod.getName());
        ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));
        return columnCache.getColumn();
    }

    /**
     * mp page转换为 PageResult 同时进行dto转换
     */
    public <T> PageResult<T> toPageResult(Page<? extends ToResult<T>> page) {
        if (Objects.isNull(page)) {
            return new PageResult<>();
        }
        List<T> collect = page.getRecords().stream().map(ToResult::toResult).toList();
        // 构造 PageResult 对象
        return new PageResult<T>().setSize(page.getSize())
                .setCurrent(page.getCurrent())
                .setTotal(page.getTotal())
                .setRecords(collect);
    }

    /**
     * mp page转换为 PageResult 同时进行dto转换
     */
    public <T> List<T> toListResult(List<? extends ToResult<T>> list) {
        return list.stream().map(ToResult::toResult).toList();
    }

    /**
     * 批量执行语句, 通常用于for循环方式的批量插入
     */
    public <T> void executeBatch(List<T> saveList, Consumer<List<T>> consumer, int batchSize) {
        // 开始游标
        int start = 0;
        // 结束游标
        int end = Math.min(batchSize, saveList.size());
        while (start < end) {
            List<T> list = ListUtil.sub(saveList, start, end);
            start = end;
            end = Math.min(end + batchSize, saveList.size());
            consumer.accept(list);
        }
    }


    /**
     * 字段是否存在长文本注解
     */
    public static boolean excludeBigField(TableFieldInfo tableFieldInfo) {
        BigField annotation = tableFieldInfo.getField().getAnnotation(BigField.class);
        return Objects.isNull(annotation);
    }

    /**
     * 获取的一条数据, 有多条取第一条
     */
    public <T> Optional<T> findOne(LambdaQueryChainWrapper<T> lambdaQuery) {
        Page<T> mpPage = new Page<>(0, 1);
        Page<T> page = lambdaQuery.page(mpPage);
        // 关闭 count 查询
        page.setSearchCount(false);
        if (CollUtil.isNotEmpty(page.getRecords())) {
            return Optional.of(page.getRecords().getFirst());
        }
        return Optional.empty();
    }

    /**
     * 获取关联的 TableInfo
     */
    public TableInfo getTableInfo(String tableName) {
        for (TableInfo tableInfo : TableInfoHelper.getTableInfos()) {
            if (tableName.equalsIgnoreCase(tableInfo.getTableName())) {
                return tableInfo;
            }
        }
        return null;
    }

}
