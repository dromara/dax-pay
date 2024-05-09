package cn.bootx.platform.baseapi.core.parameter.dao;

import cn.bootx.platform.baseapi.code.CachingCode;
import cn.bootx.platform.baseapi.core.parameter.entity.SystemParameter;
import cn.bootx.platform.baseapi.param.system.SystemParameterParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

/**
 * 系统参数
 *
 * @author xxm
 * @since 2021/10/25
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SystemParamManager extends BaseManager<SystemParamMapper, SystemParameter> {

    @Override
    @CacheEvict(value = CachingCode.SYSTEM_PARAM, allEntries = true)
    public SystemParameter updateById(SystemParameter systemParameter) {
        return super.updateById(systemParameter);
    }

    @Override
    @CacheEvict(value = CachingCode.SYSTEM_PARAM, allEntries = true)
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }

    /**
     * 根据键名获取键值
     */
    @Cacheable(value = CachingCode.SYSTEM_PARAM, key = "#key")
    public Optional<SystemParameter> findByParamKey(String key) {
        return this.findByField(SystemParameter::getParamKey, key);
    }

    /**
     * key重复检查
     */
    public boolean existsByKey(String key) {
        return existedByField(SystemParameter::getParamKey, key);
    }

    /**
     * key重复检查
     */
    public boolean existsByKey(String key, Long id) {
        return existedByField(SystemParameter::getParamKey, key, id);
    }

    /**
     * 分页
     */
    public Page<SystemParameter> page(PageParam pageParam, SystemParameterParam param) {
        Page<SystemParameter> mpPage = MpUtil.getMpPage(pageParam, SystemParameter.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(StrUtil.isNotBlank(param.getName()), SystemParameter::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getParamKey()), SystemParameter::getParamKey, param.getParamKey())
            .page(mpPage);
    }

}
