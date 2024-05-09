package cn.bootx.platform.common.sequence.range.jdbc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 队列区间
 *
 * @author xxm
 * @since 2021/12/14
 */
@Mapper
public interface SequenceRangeMapper extends BaseMapper<SequenceRange> {

}
