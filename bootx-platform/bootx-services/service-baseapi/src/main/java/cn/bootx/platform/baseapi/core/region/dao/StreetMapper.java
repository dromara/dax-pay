package cn.bootx.platform.baseapi.core.region.dao;

import cn.bootx.platform.baseapi.core.region.entity.Street;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 街道表
 *
 * @author xxm
 * @since 2022-12-24
 */
@Mapper
public interface StreetMapper extends BaseMapper<Street> {

}
