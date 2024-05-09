package cn.bootx.platform.baseapi.core.dynamicsource.dao;

import cn.bootx.platform.baseapi.core.dynamicsource.entity.DynamicDataSource;
import cn.bootx.platform.baseapi.param.dynamicsource.DynamicDataSourceParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 动态数据源管理
 *
 * @author xxm
 * @since 2022-09-24
 */
@Repository
@RequiredArgsConstructor
public class DynamicDataSourceManager extends BaseManager<DynamicDataSourceMapper, DynamicDataSource> {

    public Optional<DynamicDataSource> findByCode(String code) {
        return findByField(DynamicDataSource::getCode, code);
    }

    public boolean existsByCode(String code) {
        return existedByField(DynamicDataSource::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return existedByField(DynamicDataSource::getCode, code, id);
    }

    /**
     * 分页
     */
    public Page<DynamicDataSource> page(PageParam pageParam, DynamicDataSourceParam param) {
        Page<DynamicDataSource> mpPage = MpUtil.getMpPage(pageParam, DynamicDataSource.class);
        return this.lambdaQuery()
            .select(this.getEntityClass(), MpUtil::excludeBigField)
            .eq(StrUtil.isNotBlank(param.getDatabaseType()), DynamicDataSource::getDatabaseType,
                    param.getDatabaseType())
            .like(StrUtil.isNotBlank(param.getCode()), DynamicDataSource::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getName()), DynamicDataSource::getName, param.getName())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    /**
     * 查询开启自动加载的数据源
     */
    public List<DynamicDataSource>findAllByAutoLoad(){
        return this.findAllByField(DynamicDataSource::isAutoLoad,true);
    }

}
