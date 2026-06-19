package cn.daxpay.open.payment.old.pay.service.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/// # 客户系统消息通知任务支撑服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNoticeAssistService {

    /// key: 当前通知次数, value 下次通知的时间间隔
    /// 初始化延迟时间表, 通常总共会发起16次通知吗, 总计 24h4m, 处理不完善的情况下可能会多发一次(间隔6小时), 不影响业务
    /// 延迟时间表如下:
    /// 通知频率为0s/15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h
    private static final Map<Integer,Integer> DELAY_TIME = Map.ofEntries(
            Map.entry(1,15 * 1000),
            Map.entry(2,15 * 1000),
            Map.entry(3,30 * 1000),
            Map.entry(4,3 * 60 * 1000),
            Map.entry(5,10 * 60 * 1000),
            Map.entry(6,20 * 60 * 1000),
            Map.entry(7,30 * 60 * 1000),
            Map.entry(8,30 * 60 * 1000),
            Map.entry(9,30 * 60 * 1000),
            Map.entry(10,60 * 60 * 1000),
            Map.entry(11,3 * 60 * 60 * 1000),
            Map.entry(12,3 * 60 * 60 * 1000),
            Map.entry(13,3 * 60 * 60 * 1000),
            Map.entry(14,6 * 60 * 60 * 1000),
            Map.entry(15  ,6 * 60 * 60 * 1000),
            Map.entry(16  ,6 * 60 * 60 * 1000),
            Map.entry(17  ,6 * 60 * 60 * 1000),
            Map.entry(18  ,6 * 60 * 60 * 1000)
    );

    /// 获取延迟偏移时间
    public int getDelayTime(int delayCount){
        return DELAY_TIME.get(delayCount);
    }
}

