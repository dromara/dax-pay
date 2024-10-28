package cn.bootx.platform.baseapi.service.parameter;

import cn.bootx.platform.baseapi.dao.parameter.SystemParamManager;
import cn.bootx.platform.baseapi.entity.parameter.SystemParameter;
import cn.bootx.platform.baseapi.param.parameter.SystemParameterParam;
import cn.bootx.platform.baseapi.result.parameter.SystemParameterResult;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 系统参数
 *
 * @author xxm
 * @since 2021/10/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemParamService {

    private final SystemParamManager systemParamManager;

    /**
     * 添加
     */
    public void add(SystemParameterParam param) {
        SystemParameter systemParameter = SystemParameter.init(param);
        if (systemParamManager.existsByKey(systemParameter.getParamKey())) {
            throw new BizException("key重复");
        }
        // 默认非内置
        systemParameter.setInternal(false);
        systemParamManager.save(systemParameter);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(SystemParameterParam param) {
        SystemParameter systemParameter = systemParamManager.findById(param.getId())
            .orElseThrow(() -> new BizException("参数项不存在"));

        if (systemParamManager.existsByKey(param.getParamKey(), param.getId())) {
            throw new BizException("key重复");
        }
        BeanUtil.copyProperties(param, systemParameter, CopyOptions.create().ignoreNullValue());
        systemParamManager.updateById(systemParameter);
    }

    /**
     * 分页
     */
    public PageResult<SystemParameterResult> page(PageParam pageParam, SystemParameterParam param) {
        return MpUtil.toPageResult(systemParamManager.page(pageParam, param));
    }

    /**
     * 获取单条
     */
    public SystemParameterResult findById(Long id) {
        return systemParamManager.findById(id).map(SystemParameter::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据键名获取键值
     */
    public String findByKey(String key) {
        var param = systemParamManager.findByKey(key).orElseThrow(DataNotExistException::new);
        if (Objects.equals(param.getEnable(), false)) {
            throw new BizException("该参数已停用");
        }
        return param.getValue();
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        SystemParameter systemParameter = systemParamManager.findById(id)
            .orElseThrow(() -> new BizException("系统参数不存在"));
        if (systemParameter.isInternal()) {
            throw new BizException("内置参数不可以被删除");
        }
        systemParamManager.deleteById(id);
    }

    /**
     * 判断编码是否存在
     */
    public boolean existsByKey(String key) {
        return systemParamManager.existsByKey(key);
    }

    /**
     * 判断编码是否存在
     */
    public boolean existsByKey(String key, Long id) {
        return systemParamManager.existsByKey(key, id);
    }
}
