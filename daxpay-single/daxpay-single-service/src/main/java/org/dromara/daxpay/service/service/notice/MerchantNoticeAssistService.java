package org.dromara.daxpay.service.service.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户系统消息通知任务支撑服务
 * @author xxm
 * @since 2024/2/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNoticeAssistService {

    private static final Map<Integer,Integer> DELAY_TIME = new HashMap<>();

    /*
     * key: 当前通知次数, value 下次通知的时间间隔
     * 初始化延迟时间表, 总共会发起16次通知吗, 总计 24h4m
     * 通知频率为0s/15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h
     */
    static {
        DELAY_TIME.put(1, 15 * 1000);
        DELAY_TIME.put(2, 15 * 1000);
        DELAY_TIME.put(3, 30 * 1000);
        DELAY_TIME.put(4, 3 * 60 * 1000);
        DELAY_TIME.put(5, 10 * 60 * 1000);
        DELAY_TIME.put(6, 20 * 60 * 1000);
        DELAY_TIME.put(7, 30 * 60 * 1000);
        DELAY_TIME.put(8, 30 * 60 * 1000);
        DELAY_TIME.put(9, 30 * 60 * 1000);
        DELAY_TIME.put(10, 60 * 60 * 1000);
        DELAY_TIME.put(11, 3 * 60 * 60 * 1000);
        DELAY_TIME.put(12, 3 * 60 * 60 * 1000);
        DELAY_TIME.put(13, 3 * 60 * 60 * 1000);
        DELAY_TIME.put(14, 6 * 60 * 60 * 1000);
        DELAY_TIME.put(15, 6 * 60 * 60 * 1000);
    }

    /**
     * 获取延迟偏移时间
     */
    public int getDelayTime(int delayCount){
        return DELAY_TIME.get(delayCount);
    }
}
