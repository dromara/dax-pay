package cn.bootx.platform.baseapi.core.parameter.dao;

import cn.bootx.platform.baseapi.core.parameter.entity.SystemParameter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xxm
 * @since 2021/10/25
 */
@Mapper
public interface SystemParamMapper extends BaseMapper<SystemParameter> {

}
