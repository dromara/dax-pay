package cn.bootx.platform.common.log.plumelog;

import cn.bootx.platform.common.log.configuration.LogProperties;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.plumelog.lite.client.IndexUtil;
import com.plumelog.lite.client.InitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * PlumeLog日志删除定时任务的重新实现, 原有的日志清除逻辑是基于Lucene实现的, 文件夹将会残留, 同时还会创建出每个小时的新文件夹
 * @author xxm
 * @since 2023/3/29
 */
@Component
@ConditionalOnClass(name = "com.plumelog.lite.client.InitConfig")
@RequiredArgsConstructor
public class PlumeLogAutoDeleteLogs {
    private final LogProperties logProperties;
    /**
     * 自动删除日志，凌晨0点执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void task(){
        LogProperties.PlumeLog plumeLog = logProperties.getPlumeLog();
        if (plumeLog.getKeepDays()<=0){
            return;
        }
        // 生成不需要删除的日志文件夹名称序列
        Set<String> logDirSeq = IntStream.rangeClosed(0, plumeLog.getKeepDays())
                .mapToObj(i -> IndexUtil.getRunLogIndex(System.currentTimeMillis() - i * InitConfig.MILLS_ONE_DAY))
                .collect(Collectors.toSet());

        // 每天的日志文件夹进行清理
        File dir = new File(InitConfig.LITE_MODE_LOG_PATH + "/data");
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (Objects.isNull(files)){
                return;
            }
            for (File file : files) {
                // 如果不是日志文件夹, 继续向下执行
                if (!StrUtil.startWith(file.getName(),"")){
                    continue;
                }
                // 如果属于不需要删除的日志, 继续向下执行
                if (logDirSeq.contains(file.getName())){
                    continue;
                }
                // 删除过时的日志文件夹
                FileUtil.del(file);
            }
        }
    }

}
