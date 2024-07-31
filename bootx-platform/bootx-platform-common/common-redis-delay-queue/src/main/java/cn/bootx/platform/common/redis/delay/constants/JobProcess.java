package cn.bootx.platform.common.redis.delay.constants;

import lombok.Getter;

/**
 * 任务执行动作
 * @author daify
 * @date 2019-07-26 16:07
 **/
@Getter
public enum JobProcess {

    /**
     * 创建任务
     */
    PUT,
    /**
     * 创建延时任务
     */
    PUT_DELAY,
    /**
     * 延时结束
     */
    TIME_PASS,
    /**
     * 请求超时
     */
    TTR,
    /**
     * 消费者确认
     */
    RESERVE,
    /**
     * 完成
     */
    FINISH,
    /**
     * 删除
     */
    DELETE;
}
