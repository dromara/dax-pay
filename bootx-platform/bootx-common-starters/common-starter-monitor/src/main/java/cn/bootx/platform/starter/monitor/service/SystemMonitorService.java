package cn.bootx.platform.starter.monitor.service;

import cn.bootx.platform.starter.monitor.entity.SystemMonitorResult;
import cn.bootx.platform.starter.monitor.entity.SystemMonitorResult.*;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.*;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import oshi.hardware.GlobalMemory;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 系统信息
 *
 * @author xxm
 * @since 2022/6/10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemMonitorService {

    private final ThreadPoolTaskExecutor springRawExecutor;

    /**
     * 获取系统监控信息
     */
    public SystemMonitorResult getSystemInfo() {
        // 系统属性结果集
        SystemMonitorResult result = new SystemMonitorResult();
        result.setSysOsInfo(getSysOsInfo());
        result.setSysJavaInfo(getSysJavaInfo());
        result.setSysJvmMemInfo(getSysJvmMemInfo());
        result.setSysDiskInfos(getDiskInfos());
        result.setHardwareInfo(getHardwareInfo());
        result.setThreadPoolInfo(getThreadPoolInfo());
        return result;
    }

    /**
     * 系统信息
     */
    private SysOsInfo getSysOsInfo() {
        OsInfo osInfo = SystemUtil.getOsInfo();
        HostInfo hostInfo = SystemUtil.getHostInfo();

        SysOsInfo sysOsInfo = new SysOsInfo();
        sysOsInfo.setOsName(osInfo.getName());
        sysOsInfo.setOsArch(osInfo.getArch());
        sysOsInfo.setOsVersion(osInfo.getVersion());
        sysOsInfo.setOsHostName(hostInfo.getName());
        sysOsInfo.setOsHostAddress(hostInfo.getAddress());
        return sysOsInfo;
    }

    /**
     * Java信息
     */
    public SysJavaInfo getSysJavaInfo() {
        JvmInfo jvmInfo = SystemUtil.getJvmInfo();
        JavaRuntimeInfo javaRuntimeInfo = SystemUtil.getJavaRuntimeInfo();

        SysJavaInfo sysJavaInfo = new SysJavaInfo();
        sysJavaInfo.setJvmName(jvmInfo.getName());
        sysJavaInfo.setJvmVersion(jvmInfo.getVersion());
        sysJavaInfo.setJvmVendor(jvmInfo.getVendor());
        sysJavaInfo.setJavaName(javaRuntimeInfo.getName());
        sysJavaInfo.setJavaVersion(javaRuntimeInfo.getVersion());
        return sysJavaInfo;
    }

    /**
     * jvm内存信息
     */
    private SysJvmMemInfo getSysJvmMemInfo() {
        RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();

        SysJvmMemInfo sysJvmMemInfo = new SysJvmMemInfo();
        sysJvmMemInfo.setJvmMaxMemory(FileUtil.readableFileSize(runtimeInfo.getMaxMemory()));
        sysJvmMemInfo.setJvmUsableMemory(FileUtil.readableFileSize(runtimeInfo.getUsableMemory()));
        sysJvmMemInfo.setJvmTotalMemory(FileUtil.readableFileSize(runtimeInfo.getTotalMemory()));
        sysJvmMemInfo.setJvmFreeMemory(FileUtil.readableFileSize(runtimeInfo.getFreeMemory()));
        BigDecimal usedMemory = NumberUtil.sub(new BigDecimal(runtimeInfo.getTotalMemory()),
                new BigDecimal(runtimeInfo.getFreeMemory()));
        sysJvmMemInfo.setJvmUsedMemory(FileUtil.readableFileSize(usedMemory.longValue()));
        BigDecimal rate = NumberUtil.div(usedMemory, runtimeInfo.getTotalMemory());
        String usedRate = new DecimalFormat("#.00").format(NumberUtil.mul(rate, 100)) + "%";
        sysJvmMemInfo.setJvmMemoryUsedRate(usedRate);
        return sysJvmMemInfo;
    }

    /**
     * 获取硬件信息
     */
    private HardwareInfo getHardwareInfo() {
        // 内存
        GlobalMemory memory = OshiUtil.getMemory();
        HardwareInfo hardwareInfo = new HardwareInfo();
        long memoryTotal = memory.getTotal();
        long memoryAvailable = memory.getAvailable();
        hardwareInfo.setTotalMemory(FileUtil.readableFileSize(memoryTotal));
        hardwareInfo.setFreeMemory(FileUtil.readableFileSize(memoryAvailable));
        long memoryUsed = memoryTotal - memoryAvailable;
        hardwareInfo.setUsedMemory(FileUtil.readableFileSize(memoryUsed));
        double memoryUsedRate = memoryUsed * 100.0 / memoryTotal;
        String memoryUsedRateStr = new DecimalFormat("#.00").format(memoryUsedRate) + "%";
        hardwareInfo.setUsedRateMemory(memoryUsedRateStr);

        // cpu
        CpuInfo cpuInfo = OshiUtil.getCpuInfo(1);
        hardwareInfo.setCpuModel(cpuInfo.getCpuModel());
        hardwareInfo.setCpuNum(cpuInfo.getCpuNum());
        return hardwareInfo;
    }

    /**
     * 获取磁盘信息
     */
    private List<SysDiskInfo> getDiskInfos() {
        List<SysDiskInfo> list = new ArrayList<>();
        // 当前文件系统类
        FileSystemView fsv = FileSystemView.getFileSystemView();
        // 列出所有windows 磁盘
        File[] fs = File.listRoots();
        for (File f : fs) {
            if (f.getTotalSpace() == 0) {
                continue;
            }
            SysDiskInfo sysDiskInfo = null;
            try {
                sysDiskInfo = new SysDiskInfo();
                sysDiskInfo.setName(fsv.getSystemDisplayName(f));
                sysDiskInfo.setTotalSpace(FileUtil.readableFileSize((f.getTotalSpace())));
                sysDiskInfo.setFreeSpace(FileUtil.readableFileSize(f.getFreeSpace()));
                long used = f.getTotalSpace() - f.getFreeSpace();
                sysDiskInfo.setUsedSpace(FileUtil.readableFileSize(used));
                double restPpt = used * 100.0 / f.getTotalSpace();
                String usedRate = new DecimalFormat("#.00").format(restPpt) + "%";
                sysDiskInfo.setUsedRate(usedRate);
            } catch (Exception e) {
                log.warn("获取磁盘信息失败", e);
            }
            list.add(sysDiskInfo);
        }
        return list;
    }

    /**
     * 获取线程池信息
     */
    public ThreadPoolInfo getThreadPoolInfo() {
        ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo();

        ThreadPoolExecutor threadPoolExecutor = springRawExecutor.getThreadPoolExecutor();

        threadPoolInfo.setPoolSize(springRawExecutor.getPoolSize());
        threadPoolInfo.setPoolSize(springRawExecutor.getCorePoolSize());
        threadPoolInfo.setPoolSize(springRawExecutor.getMaxPoolSize());
        threadPoolInfo.setActiveCount(springRawExecutor.getActiveCount());
        threadPoolInfo.setQueueSize(threadPoolExecutor.getQueue().size());
        threadPoolInfo.setCompletedTaskCount(springRawExecutor.getCorePoolSize());
        return threadPoolInfo;
    }

}
