package cn.daxpay.open.platform.system.dao.dict;

import cn.daxpay.open.platform.system.entity.dict.Dict;
import cn.daxpay.open.platform.system.param.dict.DictParam;
import cn.daxpay.open.platform.common.mybatisplus.base.MpIdEntity;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 字典
///
@Repository
@AllArgsConstructor
public class DictManager extends BaseManager<DictMapper, Dict> {

    /// 根据code查询重复
    public boolean existsByCode(String code) {
        return existedByField(Dict::getCode, code);
    }

    /// 根据code查询重复 排除id
    public boolean existsByCode(String code, Long id) {
        return existedByField(Dict::getCode, code, id);
    }

    public Page<Dict> page(PageParam pageParam, DictParam param) {
        Page<Dict> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(StrUtil.isNotBlank(param.getCode()), Dict::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getName()), Dict::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getDictType()), Dict::getDictType, param.getDictType())
            .page(mpPage);
    }

    public List<Dict> findAllByEnable(boolean enable) {
        return lambdaQuery().eq(Dict::getEnable, enable).list();
    }

}
