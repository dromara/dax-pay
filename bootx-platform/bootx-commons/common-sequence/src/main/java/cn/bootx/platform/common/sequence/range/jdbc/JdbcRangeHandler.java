package cn.bootx.platform.common.sequence.range.jdbc;

import cn.bootx.platform.common.sequence.exception.SeqException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * jdbc区间管理
 *
 * @author xxm
 * @since 2021/12/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JdbcRangeHandler {

    private final SequenceRangeManager manager;

    /**
     * 第一次不存在，进行初始化,不存在就set，存在就忽略
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void setIfAbsentRange(String key, long rangeStart) {
        // 是否存在
        if (!manager.existedByField(SequenceRange::getRangeKey, key)) {
            manager.save(new SequenceRange().setRangeKey(key).setRangeValue(rangeStart));
        }
    }

    /**
     * 增加区间范围
     */
    @Transactional(rollbackFor = Exception.class)
    public Long incrementRange(String key, int rangeStep) {
        SequenceRange range = manager.findByField(SequenceRange::getRangeKey, key)
            .orElseThrow(() -> new SeqException("区间不存在"));
        // 新区间开始值
        long stepValue = range.getRangeValue() + rangeStep;
        range.setRangeValue(stepValue);
        manager.updateById(range);
        return stepValue;
    }

}
