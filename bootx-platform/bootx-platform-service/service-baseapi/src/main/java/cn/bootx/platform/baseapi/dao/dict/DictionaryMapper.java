package cn.bootx.platform.baseapi.dao.dict;

import cn.bootx.platform.baseapi.entity.dict.Dictionary;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典
 *
 * @author xxm
 * @since 2021/8/4
 */
@Mapper
public interface DictionaryMapper extends MPJBaseMapper<Dictionary> {

}
