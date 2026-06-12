package org.dromara.daxpay.platform.common.translate.registry;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/// # Mapper 注册表
///
/// 通过 SqlSessionTemplate 获取实体类对应的 BaseMapper，并缓存
@Slf4j
@Component
@RequiredArgsConstructor
public class TransMapperRegistry {

    private final SqlSessionTemplate sqlSessionTemplate;

    /// 实体类 → Mapper 缓存（懒加载）
    private final Map<Class<?>, BaseMapper<?>> mapperCache = new ConcurrentHashMap<>();

    /// 获取实体类对应的 Mapper（带缓存）
    public BaseMapper<?> getMapper(Class<?> entityClass) {
        return mapperCache.computeIfAbsent(entityClass, this::doGetMapper);
    }

    /// 实际获取 Mapper
    private BaseMapper<?> doGetMapper(Class<?> entityClass) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        if (tableInfo == null) {
            log.warn("翻译模块未找到实体类 {} 的 TableInfo", entityClass.getSimpleName());
            return null;
        }
        String namespace = tableInfo.getCurrentNamespace();
        try {
            Class<?> mapperClass = Class.forName(namespace);
            Object mapper = sqlSessionTemplate.getMapper(mapperClass);
            if (mapper instanceof BaseMapper) {
                log.debug("翻译模块获取 Mapper: {} → {}", entityClass.getSimpleName(), namespace);
                return (BaseMapper<?>) mapper;
            }
        } catch (ClassNotFoundException e) {
            log.warn("翻译模块未找到 Mapper 类: {}", namespace);
        }
        return null;
    }
}
