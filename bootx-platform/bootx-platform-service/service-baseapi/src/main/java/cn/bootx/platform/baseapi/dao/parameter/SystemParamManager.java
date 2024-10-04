package cn.bootx.platform.baseapi.dao.parameter;

import cn.bootx.platform.baseapi.entity.parameter.SystemParameter;
import cn.bootx.platform.baseapi.param.parameter.SystemParameterParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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


    /**
     * 根据键名获取键值
     */
    public Optional<SystemParameter> findByKey(String key) {
        return this.findByField(SystemParameter::getKey, key);
    }

    /**
     * key重复检查
     */
    public boolean existsByKey(String key) {
        return existedByField(SystemParameter::getKey, key);
    }

    /**
     * key重复检查
     */
    public boolean existsByKey(String key, Long id) {
        return existedByField(SystemParameter::getKey, key, id);
    }

    /**
     * 分页
     */
    public Page<SystemParameter> page(PageParam pageParam, SystemParameterParam param) {
        Page<SystemParameter> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(StrUtil.isNotBlank(param.getName()), SystemParameter::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getKey()), SystemParameter::getKey, param.getKey())
            .page(mpPage);
    }

}
