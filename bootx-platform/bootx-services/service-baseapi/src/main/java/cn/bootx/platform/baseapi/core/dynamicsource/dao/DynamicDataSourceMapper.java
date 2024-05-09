package cn.bootx.platform.baseapi.core.dynamicsource.dao;

import cn.bootx.platform.baseapi.core.dynamicsource.entity.DynamicDataSource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态数据源管理
 *
 * @author xxm
 * @since 2022-09-24
 */
@Mapper
public interface DynamicDataSourceMapper extends BaseMapper<DynamicDataSource> {

}
