package org.dromara.daxpay.platform.common.translate.service;

import org.dromara.daxpay.platform.common.translate.cache.TransCacheKey;
import org.dromara.daxpay.platform.common.translate.cache.TransCacheManager;
import org.dromara.daxpay.platform.common.translate.model.DictItemData;
import org.dromara.daxpay.platform.common.translate.model.TransFieldInfo;
import org.dromara.daxpay.platform.common.translate.model.TransGroup;
import org.dromara.daxpay.platform.common.translate.model.TransMeta;
import org.dromara.daxpay.platform.common.translate.registry.TransMapperRegistry;
import org.dromara.daxpay.platform.common.translate.spi.DictTranslator;
import org.dromara.daxpay.platform.core.annotation.Trans;
import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/// # 字段翻译服务
///
/// 通过 @Trans 注解声明需要翻译的字段，TransService 在运行时自动进行关联查询和回填
///
/// 翻译流程：
/// 1. 扫描 Result 对象中所有 @Trans 注解字段
/// 2. 区分实体翻译和字典翻译，分别按条件分组
/// 3. 实体翻译：先查缓存，未缓存的源字段值通过 Mapper 执行批量 IN 查询
/// 4. 字典翻译：按字典编码分组，通过 DictTranslator 批量获取字典映射
/// 5. 将查询结果回填到 Result 对象的被 @Trans 注解的字段
@Slf4j
@Service
@RequiredArgsConstructor
public class TransService {

    private final TransMapperRegistry mapperRegistry;
    private final TransCacheManager cacheManager;

    private final DictTranslator dictTranslator;

    /// 类级别的 @Trans 注解元数据缓存
    private final Map<Class<?>, List<TransMeta>> transMetaCache = new ConcurrentHashMap<>();

    /// 类级别的 Field 缓存（含父类字段）
    private final Map<Class<?>, Map<String, Field>> fieldCache = new ConcurrentHashMap<>();

    /// 翻译单个对象
    public void translate(Object result) {
        if (result == null) {
            return;
        }
        translate(Collections.singletonList(result));
    }

    /// 翻译列表
    /// 对列表中所有对象进行翻译，合并相同翻译组的源字段值做批量查询优化
    public void translate(List<?> results) {
        if (CollUtil.isEmpty(results)) {
            return;
        }

        // 遍历所有对象，区分实体翻译和字典翻译
        Map<TransGroup, Set<Object>> entityGroupSourceValues = new LinkedHashMap<>();
        List<TransFieldInfo> entityFieldInfos = new ArrayList<>();
        Map<String, Set<Object>> dictGroupSourceValues = new LinkedHashMap<>();
        List<TransFieldInfo> dictFieldInfos = new ArrayList<>();

        for (Object result : results) {
            List<TransMeta> metaList = scanTransFields(result);
            for (TransMeta meta : metaList) {
                Object sourceValue = getFieldValue(result, meta.source());
                if (sourceValue == null) {
                    continue;
                }
                boolean isDict = !meta.annotation().dictCode().isEmpty();
                if (isDict) {
                    String dictCode = meta.annotation().dictCode();
                    dictGroupSourceValues.computeIfAbsent(dictCode, k -> new LinkedHashSet<>()).add(sourceValue);
                    dictFieldInfos.add(new TransFieldInfo(result, meta.field(), meta.annotation()));
                } else {
                    TransGroup group = new TransGroup(meta.entity(), meta.source(), meta.on(), meta.result(), meta.annotation().cacheTtl());
                    entityGroupSourceValues.computeIfAbsent(group, k -> new LinkedHashSet<>()).add(sourceValue);
                    entityFieldInfos.add(new TransFieldInfo(result, meta.field(), meta.annotation()));
                }
            }
        }

        // 处理实体翻译
        if (!entityGroupSourceValues.isEmpty()) {
            Map<TransGroup, Map<Object, Object>> entityResults = batchQuery(entityGroupSourceValues);
            fillBackEntityTranslations(entityFieldInfos, entityResults);
        }

        // 处理字典翻译
        if (!dictGroupSourceValues.isEmpty()) {
            processDictTranslations(dictGroupSourceValues, dictFieldInfos);
        }
    }

    /// 翻译分页结果
    public void translate(PageResult<?> pageResult) {
        if (pageResult == null) {
            return;
        }
        translate(pageResult.getRecords());
    }

    /// 处理字典翻译
    /// 通过 DictTranslator 获取所有字典项的完整字段映射，按 @Trans.result 提取对应语言值
    private void processDictTranslations(Map<String, Set<Object>> dictGroupSourceValues, List<TransFieldInfo> fieldInfos) {
        if (dictTranslator == null) {
            log.warn("字典翻译不可用，请检查 DictTranslator 实现是否引入");
            return;
        }

        // 按 dictCode 分组获取字典数据
        // 结构：dictCode -> {itemCode -> DictItemData}
        Map<String, Map<String, DictItemData>> dictCodeToDataMap = new LinkedHashMap<>();
        for (String dictCode : dictGroupSourceValues.keySet()) {
            try {
                Map<String, DictItemData> dictData = dictTranslator.findByDictCode(dictCode);
                if (dictData != null) {
                    dictCodeToDataMap.put(dictCode, dictData);
                }
            } catch (Exception e) {
                log.warn("获取字典 [{}] 映射失败", dictCode, e);
            }
        }

        // 回填：根据 @Trans.result 指定的字段名提取对应语言的值（支持 locale 感知）
        for (TransFieldInfo info : fieldInfos) {
            Object sourceValue = getFieldValue(info.result(), info.annotation().source());
            if (sourceValue == null) {
                continue;
            }
            Map<String, DictItemData> dataMap = dictCodeToDataMap.get(info.annotation().dictCode());
            if (dataMap == null) {
                continue;
            }
            DictItemData itemData = dataMap.get(String.valueOf(sourceValue));
            if (itemData == null) {
                continue;
            }
            // 根据当前请求语言选择对应的语言字段
            String resultField = resolveLocaleAwareField(info.annotation().result());
            String translatedValue = itemData.get(resultField);
            if (translatedValue != null) {
                setFieldValue(info.result(), info.field(), translatedValue);
            }
        }
    }

    /// 根据当前请求语言解析多语言字段名
    /// 规则：
    /// - result="nameCn" 或 "nameEn" → 显式指定了语言，按指定字段名取值，不切换
    /// - result="name" 或其他通用名 → 根据 Accept-Language 自动选择 nameEn/nameCn
    private String resolveLocaleAwareField(String resultField) {
        // 显式指定了中/英文，按开发者意图走，不切换
        if ("nameCn".equals(resultField) || "nameEn".equals(resultField)) {
            return resultField;
        }
        // 通用字段名，根据请求语言自动选择
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            return "nameEn";
        }
        return "nameCn";
    }

    /// 回填实体翻译结果
    private void fillBackEntityTranslations(List<TransFieldInfo> fieldInfos, Map<TransGroup, Map<Object, Object>> groupToResultMap) {
        for (TransFieldInfo info : fieldInfos) {
            Object sourceValue = getFieldValue(info.result(), info.annotation().source());
            if (sourceValue == null) {
                continue;
            }
            TransGroup group = new TransGroup(info.annotation().entity(), info.annotation().source(), info.annotation().on(), info.annotation().result(), info.annotation().cacheTtl());
            Map<Object, Object> resultMap = groupToResultMap.get(group);
            if (resultMap == null) {
                continue;
            }
            Object translatedValue = resultMap.get(sourceValue);
            if (translatedValue != null) {
                setFieldValue(info.result(), info.field(), translatedValue);
            }
        }
    }

    /// 扫描对象中所有带 @Trans 注解的字段（含父类字段），结果按 Class 缓存
    private List<TransMeta> scanTransFields(Object result) {
        Class<?> clazz = result.getClass();
        return transMetaCache.computeIfAbsent(clazz, this::doScanTransFields);
    }

    /// 实际扫描类的 @Trans 注解字段
    private List<TransMeta> doScanTransFields(Class<?> clazz) {
        List<TransMeta> metaList = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                Trans trans = field.getAnnotation(Trans.class);
                if (trans != null) {
                    metaList.add(new TransMeta(trans.entity(), trans.source(), trans.on(), trans.result(), field, trans));
                }
            }
            clazz = clazz.getSuperclass();
        }
        return metaList;
    }

    /// 批量查询：对每个翻译组先查缓存，未缓存的源字段值通过 Mapper 执行数据库查询
    private Map<TransGroup, Map<Object, Object>> batchQuery(Map<TransGroup, Set<Object>> groupToSourceValues) {
        Map<TransGroup, Map<Object, Object>> groupToResultMap = new LinkedHashMap<>();

        for (Map.Entry<TransGroup, Set<Object>> entry : groupToSourceValues.entrySet()) {
            TransGroup group = entry.getKey();
            Set<Object> sourceValues = entry.getValue();

            // 先从缓存中查找，区分已缓存和未缓存的值
            Set<Object> uncachedSourceValues = new LinkedHashSet<>();
            Map<Object, Object> cachedResults = new LinkedHashMap<>();

            for (Object sv : sourceValues) {
                TransCacheKey cacheKey = new TransCacheKey(group.entity(), group.source(), group.result(), sv);
                if (cacheManager.contains(cacheKey)) {
                    Object cachedValue = cacheManager.get(cacheKey);
                    if (cachedValue != null) {
                        cachedResults.put(sv, cachedValue);
                    }
                } else {
                    uncachedSourceValues.add(sv);
                }
            }

            // 对未缓存的源字段值执行数据库查询
            if (!uncachedSourceValues.isEmpty()) {
                Map<Object, Object> dbResults = queryFromDatabase(group, uncachedSourceValues);
                cachedResults.putAll(dbResults);

                // 将查询结果写入缓存（含 TTL）
                for (Map.Entry<Object, Object> dbEntry : dbResults.entrySet()) {
                    TransCacheKey cacheKey = new TransCacheKey(group.entity(), group.source(), group.result(), dbEntry.getKey());
                    cacheManager.put(cacheKey, dbEntry.getValue(), group.cacheTtl());
                }

                // 将未匹配到的值标记为 null 缓存，避免重复查库（含 TTL）
                for (Object sv : uncachedSourceValues) {
                    if (!dbResults.containsKey(sv)) {
                        TransCacheKey cacheKey = new TransCacheKey(group.entity(), group.source(), group.result(), sv);
                        cacheManager.put(cacheKey, null, group.cacheTtl());
                    }
                }
            }

            groupToResultMap.put(group, cachedResults);
        }

        return groupToResultMap;
    }

    /// 通过 Mapper 执行数据库查询，获取 source → result 映射
    /// on 字段指定目标实体中的属性名，为空时使用 source 的值
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map<Object, Object> queryFromDatabase(TransGroup group, Set<Object> sourceValues) {
        BaseMapper<?> mapper = mapperRegistry.getMapper(group.entity());
        if (mapper == null) {
            log.warn("翻译模块未找到实体类 {} 对应的 Mapper", group.entity().getSimpleName());
            return Collections.emptyMap();
        }

        TableInfo tableInfo = TableInfoHelper.getTableInfo(group.entity());
        if (tableInfo == null) {
            log.warn("翻译模块未找到实体类 {} 的 TableInfo", group.entity().getSimpleName());
            return Collections.emptyMap();
        }

        // 确定目标实体中的匹配属性名：on 为空时回退到 source
        String targetProperty = group.on().isEmpty() ? group.source() : group.on();
        String sourceColumn = getColumnByPropertyName(tableInfo, targetProperty);
        String resultColumn = getColumnByPropertyName(tableInfo, group.result());

        // 构造 QueryWrapper，使用普通 QueryWrapper + 列名方式
        // 不使用链式调用，因为 raw type 的链式调用会导致类型推断问题
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select(sourceColumn, resultColumn);
        queryWrapper.in(sourceColumn, sourceValues);

        // 执行查询
        List<Map<String, Object>> queryResults = mapper.selectMaps(queryWrapper);

        // 将查询结果转为 source → result 映射
        Map<Object, Object> resultMap = new LinkedHashMap<>();
        for (Map<String, Object> row : queryResults) {
            Object sourceObj = row.get(sourceColumn);
            Object resultObj = row.get(resultColumn);
            if (sourceObj != null) {
                resultMap.put(sourceObj, resultObj);
            }
        }

        return resultMap;
    }

    /// 通过 Java 属性名获取数据库列名
    /// 利用 TableInfo 的字段映射关系，支持继承字段
    private String getColumnByPropertyName(TableInfo tableInfo, String propertyName) {
        // 先在普通字段列表中查找
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            if (fieldInfo.getProperty().equals(propertyName)) {
                return fieldInfo.getColumn();
            }
        }
        // 如果不是普通字段，可能是主键
        if (tableInfo.getKeyProperty().equals(propertyName)) {
            return tableInfo.getKeyColumn();
        }
        // 通用: 翻译字段对应的数据库列不存在
        throw new BizException("error.common.translateColumnNotFound",
                tableInfo.getEntityType().getSimpleName(), propertyName);
    }

    /// 从对象中获取指定字段的值（按 Class 缓存 Field，避免重复反射）
    private Object getFieldValue(Object obj, String fieldName) {
        Field field = resolveField(obj.getClass(), fieldName);
        if (field == null) {
            return null;
        }
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            log.warn("无法访问对象 {} 的字段 {}", obj.getClass().getSimpleName(), fieldName);
            return null;
        }
    }

    /// 按 Class 缓存 Field（含类层次向上查找）
    private Field resolveField(Class<?> clazz, String fieldName) {
        Map<String, Field> fieldMap = fieldCache.computeIfAbsent(clazz, this::buildFieldMap);
        return fieldMap.get(fieldName);
    }

    /// 构建类的字段映射（含父类字段，子类优先覆盖）
    private Map<String, Field> buildFieldMap(Class<?> clazz) {
        Map<String, Field> map = new LinkedHashMap<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                map.putIfAbsent(field.getName(), field);
            }
            clazz = clazz.getSuperclass();
        }
        return map;
    }

    /// 设置对象的指定字段值
    private void setFieldValue(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            log.warn("无法设置对象 {} 的字段 {}", obj.getClass().getSimpleName(), field.getName());
        }
    }
}

