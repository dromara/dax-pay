package cn.bootx.platform.common.sequence.range.mongo;

import cn.bootx.platform.common.sequence.exception.SeqException;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Mongo区间处理器
 *
 * @author xxm
 * @since 2022/1/21
 */
@ConditionalOnClass(name="org.springframework.data.mongodb.core.MongoTemplate")
@Repository
@RequiredArgsConstructor
public class MongoRangeHandler {

    private final MongoTemplate mongoTemplate;

    /**
     * 第一次不存在，进行初始化,不存在就set，存在就忽略
     */
    public void setIfAbsentRange(String key, long rangeStart) {
        // 是否存在
        Criteria criteria = Criteria.where(SequenceRange.Fields.rangeKey).is(key);
        Query query = new Query(criteria);
        if (!mongoTemplate.exists(query, SequenceRange.class)) {
            mongoTemplate.save(
                    new SequenceRange().setRangeKey(key).setRangeValue(rangeStart).setId(IdUtil.getSnowflakeNextId()));
        }
    }

    /**
     * 增加区间范围
     */
    public Long incrementRange(String key, int rangeStep) {
        Criteria criteria = Criteria.where(SequenceRange.Fields.rangeKey).is(key);
        Query query = new Query(criteria);
        SequenceRange range = Optional.ofNullable(mongoTemplate.findOne(query, SequenceRange.class))
            .orElseThrow(() -> new SeqException("区间不存在"));
        // 新区间开始值
        long stepValue = range.getRangeValue() + rangeStep;
        range.setRangeValue(stepValue);
        mongoTemplate.save(range);
        return stepValue;
    }

}
