package cn.bootx.platform.common.sequence.range.jdbc;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 队列区间
 *
 * @author xxm
 * @since 2021/12/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("common_sequence_range")
public class SequenceRange extends MpBaseEntity {

    /** 区间key */
    private String rangeKey;

    /** 区间开始值 */
    private Long rangeValue;

}
