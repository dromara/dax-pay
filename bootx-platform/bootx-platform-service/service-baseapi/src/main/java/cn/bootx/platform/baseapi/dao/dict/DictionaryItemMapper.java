package cn.bootx.platform.baseapi.dao.dict;

import cn.bootx.platform.baseapi.entity.dict.DictionaryItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典项
 *
 * @author xxm
 * @since 2020/11/13
 */
@Mapper
public interface DictionaryItemMapper extends BaseMapper<DictionaryItem> {

}
