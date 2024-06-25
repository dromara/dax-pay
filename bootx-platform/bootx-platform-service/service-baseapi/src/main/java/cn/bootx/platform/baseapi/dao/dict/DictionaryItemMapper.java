package cn.bootx.platform.baseapi.dao.dict;

import cn.bootx.platform.baseapi.entity.dict.DictionaryItem;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典项
 *
 * @author xxm
 * @since 2020/11/13
 */
@Mapper
public interface DictionaryItemMapper extends MPJBaseMapper<DictionaryItem> {

}
