package cn.bootx.platform.common.sequence.range.mongo;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 队列区间
 *
 * @author xxm
 * @since 2021/12/14
 */
@Data
@FieldNameConstants
@Accessors(chain = true)
@Document("common_sequence_range")
public class SequenceRange {

    @Id
    private Long id;

    /** 区间key */
    private String rangeKey;

    /** 区间开始值 */
    private Long rangeValue;

}
