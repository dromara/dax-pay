package cn.bootx.platform.common.redis.delay.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务类别
 * @author daify
 * @date 2019-07-29 11:25
 **/
@AllArgsConstructor
@Getter
public enum  JobTopic {

    TOPIC_ONE("one"),
    TOPIC_TWO("two");

    private final String topic;
}
