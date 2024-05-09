package cn.bootx.platform.iam.core.client.dao;

import cn.bootx.platform.iam.core.client.entity.LonginType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 终端
 *
 * @author xxm
 * @since 2021/8/25
 */
@Mapper
public interface LoginTypeMapper extends BaseMapper<LonginType> {

}
