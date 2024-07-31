package cn.bootx.platform.common.redis.delay.utils;

import cn.bootx.platform.common.redis.delay.constants.JobProcess;
import cn.bootx.platform.common.redis.delay.constants.JobStatus;

/**
 *
 * @author daify
 * @date 2019-07-26 16:41
 **/
public class JobStatusUtils {

    /**
     * 获得下一步节点
     */
    public static JobStatus getNext(JobStatus status, JobProcess process) {
        return switch (process) {
            case PUT -> put(status);
            case FINISH -> finish(status);
            case DELETE -> delete(status);
            case TTR -> ttr(status);
            case RESERVE -> reserve(status);
            case PUT_DELAY -> putDelay(status);
            case TIME_PASS -> timePass(status);
        };
    }


    private static JobStatus timePass(JobStatus status) {
        if (JobStatus.DELAY == status) {
            return JobStatus.READY;
        }
        return null;
    }

    private static JobStatus putDelay(JobStatus status) {
        if (status == null) {
            return JobStatus.DELAY;
        }
        return null;
    }

    private static JobStatus reserve(JobStatus status) {
        if (JobStatus.READY == status) {
            return JobStatus.RESERVED;
        }
        return null;
    }

    private static JobStatus ttr(JobStatus status) {
        if (JobStatus.RESERVED == status) {
            return JobStatus.DELAY;
        }
        return null;
    }

    private static JobStatus finish(JobStatus status) {
        if (JobStatus.RESERVED == status) {
            return JobStatus.DELETED;
        }
        return null;
    }

    private static JobStatus delete(JobStatus status) {
        return JobStatus.DELETED;
    }

    private static JobStatus put(JobStatus status) {
        if (status == null) {
            return JobStatus.READY;
        }
        return null;
    }


}
