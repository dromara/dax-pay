package cn.bootx.platform.baseapi.core.chinaword.dao;

import cn.bootx.platform.baseapi.core.chinaword.entity.ChinaWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 敏感词
 * @author xxm
 * @since 2023-08-09
 */
@Mapper
public interface ChinaWordMapper extends BaseMapper<ChinaWord> {
}
