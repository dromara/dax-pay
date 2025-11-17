package cn.bootx.platform.common.mybatisplus.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.BeanHandler;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PropertyPlaceholderHelper;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 数据库工具类
 *
 * @author nn200433
 * @date 2024-03-28 09:32:34
 */
@Slf4j
public class MybatisDbUtil {

    private static final String                    PARAM_KEY_PRE         = "ew.paramNameValuePairs.";
    private static final String                    UPDATE_SET_PREFIX     = "SET";
    private static final String                    SQL_PREFIX_AND_SUFFIX = "'";
    public static final  String                    PLACEHOLDER_PREFIX    = "#{";
    private static final String                    PLACEHOLDER_SUFFIX    = "}";
    private static final PropertyPlaceholderHelper PLACEHOLDER_HELPER    = new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX);

    /**
     * 物理删除
     *
     * @param tableClz 数据表实体类型
     * @param ids      主键
     * @return int     影响行数
     * @author nn200433
     */
    public static int delete(Class<?> tableClz, String... ids) {
        final TableInfo tableInfo = getTableInfo(tableClz);
        final String    tableName = tableInfo.getTableName();
        final String    keyColumn = tableInfo.getKeyColumn();
        Assert.notBlank(keyColumn, "{} 未设置 @TableId 注解！", tableClz);
        int count = 0;
        try {
            count = Db.use(dataSource()).del(Entity.create(tableName).set(keyColumn, ids));
        } catch (Exception e) {
            log.error("", e);
        }
        return count;
    }

    /**
     * 物理删除
     *
     * <p>
     *     请使用{@link  com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper}构造wrapper
     * </p>
     *
     * @param wrapper 包装物
     * @return int    影响行数
     * @author nn200433
     */
    public static <T> int delete(AbstractWrapper<T, ?, ?> wrapper) {
        final Class<T>            entityClass         = (Class<T>) getEntityClass(wrapper);
        final TableInfo           tableInfo           = getTableInfo(entityClass);
        final String              whereSql            = wrapper.getCustomSqlSegment();
        final String              tableName           = tableInfo.getTableName();
        final Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
        int                       count               = 0;
        try {
            final String sql = getSql(SqlMethod.DELETE, tableName, StrUtil.EMPTY, StrUtil.EMPTY, whereSql, null, paramNameValuePairs);
            count = Db.use(dataSource()).execute(sql);
        } catch (Exception e) {
            log.error("", e);
        }
        return count;
    }

    /**
     * 通过ID查询数据（忽略逻辑删除）
     *
     * @param id          主键id值
     * @param entityClass 返回的实体类型
     * @return {@link T }
     * @author nn200433
     */
    public static <T> T selectById(Object id, Class<T> entityClass) {
        final TableInfo           tableInfo           = getTableInfo(entityClass);
        final String              keyColumn           = tableInfo.getKeyColumn();
        final QueryWrapper<T>     wrapper             = new QueryWrapper<T>().eq(keyColumn, id);
        final String              tableName           = tableInfo.getTableName();
        final String              whereSql            = wrapper.getCustomSqlSegment();
        final Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
        T                         result              = null;
        try {
            final String sql = getSql(SqlMethod.SELECT_BY_MAP, tableName, StrUtil.EMPTY, tableInfo.getAllSqlSelect(), whereSql, null, paramNameValuePairs);
            result = Db.use(dataSource()).query(sql, new BeanHandler<T>(entityClass));
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    /**
     * 查询列表（忽略逻辑删除）
     *
     * <p>
     *     请使用{@link  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}构造wrapper。
     *     <br/>
     *     举例：new LambdaQueryWrapper<SmsTemplate>(Entity.class).eq(Entity::getCode, "2");
     * </p>
     *
     * @param wrapper 包装物
     * @return {@link List }<{@link T }>
     * @author nn200433
     */
    public static <T> List<T> selectList(AbstractWrapper<T, ?, ?> wrapper) {
        List<T>                   resultList          = new ArrayList<T>();
        final Class<T>            entityClass         = (Class<T>) getEntityClass(wrapper);
        final TableInfo           tableInfo           = getTableInfo(entityClass);
        final String              whereSql            = wrapper.getCustomSqlSegment();
        final String              columnSql           = wrapper.getSqlSelect();
        final String              tableName           = tableInfo.getTableName();
        final Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
        try {
            final String sql = getSql(SqlMethod.SELECT_LIST, tableName, StrUtil.EMPTY, StrUtil.blankToDefault(columnSql, tableInfo.getAllSqlSelect()), whereSql, null, paramNameValuePairs);
            resultList = Db.use(dataSource()).query(sql, entityClass);
        } catch (Exception e) {
            log.error("", e);
        }
        return resultList;
    }

    /**
     * 更新（忽略逻辑删除）
     *
     * @param entity 实体类
     * @return int   影响行数
     * @author nn200433
     */
    public static <T> int update(T entity) {
        return update(entity, null);
    }

    /**
     * 通过条件更新数据（忽略逻辑删除）
     * <p>
     *     请使用{@link  com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper}构造wrapper
     * </p>
     *
     * @param entity  实体类
     * @param wrapper 包装物
     * @return int    影响行数
     * @author nn200433
     */
    public static <T> int update(T entity, AbstractWrapper<T, ?, ?> wrapper) {
        final TableInfo tableInfo = getTableInfo(entity.getClass());
        final String    tableName = tableInfo.getTableName();
        if (null == wrapper) {
            final String keyProperty = tableInfo.getKeyProperty();
            Assert.notBlank(keyProperty, "{} 未设置 @TableId 注解！", keyProperty);
            wrapper = new UpdateWrapper<T>(entity).eq(keyProperty, ReflectUtil.getFieldValue(entity, keyProperty));
        }
        final String              whereSql            = wrapper.getCustomSqlSegment();
        final Map<String, Object> paramNameValuePairs = wrapper.getParamNameValuePairs();
        int                       count               = 0;
        try {
            final String sql = getSql(SqlMethod.UPDATE, tableName, StrUtil.EMPTY, tableInfo.getAllSqlSet(Boolean.TRUE, StrUtil.EMPTY), whereSql, BeanUtil.beanToMap(entity), paramNameValuePairs);
            count = Db.use(dataSource()).execute(sql);
        } catch (Exception e) {
            log.error("", e);
        }
        return count;
    }

    /**
     * 获取sql
     *
     * @param method              SQL方法
     * @param tableName           数据库表名称
     * @param firstSql            firtSql
     * @param columnSql           字段sql
     * @param whereSql            where条件sql
     * @param setMap              set参数
     * @param paramNameValuePairs where条件值
     * @return {@link String }
     * @author nn200433
     */
    private static String getSql(SqlMethod method, String tableName, String firstSql, String columnSql, String whereSql,
                                 Map<String, Object> setMap, Map<String, Object> paramNameValuePairs) {
        final Map<String, Object> paramMap = new HashMap<String, Object>(paramNameValuePairs.size());
        for (final Map.Entry<String, Object> entry : paramNameValuePairs.entrySet()) {
            paramMap.put(PARAM_KEY_PRE + entry.getKey(), entry.getValue());
        }
        if (CollUtil.isNotEmpty(setMap)) {
            paramMap.putAll(setMap);
        }
        // 害人不浅，包了这么标签
        String originalSql = null;
        switch (method) {
            case DELETE:
                originalSql = String.format(method.getSql(), tableName, whereSql, StrUtil.EMPTY);
                break;
            case UPDATE:
                columnSql = StrUtil.addPrefixIfNot(columnSql, UPDATE_SET_PREFIX + StrUtil.SPACE);
                columnSql = StrUtil.removeSuffix(columnSql, StrUtil.COMMA);
                originalSql = String.format(method.getSql(), tableName, columnSql, whereSql, StrUtil.EMPTY);
                break;
            case SELECT_LIST:
                // 判断个锤子OrderBy注解，麻烦
                originalSql = String.format(method.getSql(), firstSql, columnSql, tableName, whereSql, StrUtil.EMPTY, StrUtil.EMPTY);
                break;
            case SELECT_BY_MAP:
                // 垃圾 SELECT_BY_ID ，限制那么多
                originalSql = String.format(method.getSql(), columnSql, tableName, whereSql, StrUtil.EMPTY);
                break;
            default:
        }
        final String preSql = StrUtil.trim(HtmlUtil.cleanHtmlTag(originalSql));
        return resolveParams(preSql, paramMap);
    }

    /**
     * 从wrapper中尝试获取实体类型
     *
     * @param queryWrapper 条件构造器
     * @param <T>          实体类型
     * @return 实体类型
     */
    private static <T> Class<T> getEntityClass(AbstractWrapper<T, ?, ?> queryWrapper) {
        Class<T> entityClass = queryWrapper.getEntityClass();
        if (entityClass == null) {
            T entity = queryWrapper.getEntity();
            if (entity != null) {
                entityClass = (Class<T>) entity.getClass();
            }
        }
        Assert.notNull(entityClass, "error: can not get entityClass from wrapper");
        return entityClass;
    }

    /**
     * 获取表信息，获取不到报错提示
     *
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return 对应表信息
     */
    private static <T> TableInfo getTableInfo(Class<T> entityClass) {
        return Optional.ofNullable(TableInfoHelper.getTableInfo(entityClass))
                .orElseThrow(() -> ExceptionUtils.mpe("error: can not find TableInfo from Class: \"%s\".", entityClass.getName()));
    }

    /**
     * 解析参数
     *
     * @param str   包含要替换的占位符的值
     * @param param 参数
     * @return @return {@link String }
     * @author nn200433
     */
    private static String resolveParams(String str, Map<String, Object> param) {
        return PLACEHOLDER_HELPER.replacePlaceholders(str, key -> {
            // final String v = MapUtil.(param, key);
            final Object v = param.get(key);
            if (null == v) {
                return "null";
            }
            final Class<?> clz = v.getClass();
            if (clz.equals(Date.class)) {
                final Date vd = (Date) v;
                return StrUtil.wrap(DateUtil.format(vd, DatePattern.NORM_DATETIME_FORMAT), SQL_PREFIX_AND_SUFFIX);
            } else if (clz.equals(LocalDateTime.class)) {
                final LocalDateTime vd = (LocalDateTime) v;
                return StrUtil.wrap(DateUtil.formatLocalDateTime(vd), SQL_PREFIX_AND_SUFFIX);
            }
            return StrUtil.wrap(Convert.toStr(v), SQL_PREFIX_AND_SUFFIX);
        });
    }

    /**
     * 获取数据源
     *
     * @return {@link DataSource }
     * @author nn200433
     */
    private static DataSource dataSource() {
        return SpringUtil.getBean(DataSource.class);
    }

}
