/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package cn.bootx.platform.starter.monitor.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 系统属性结果
 *
 * @author xxm
 * @since 2022/6/10
 */
@Data
@Schema(title = "系统属性结果")
public class SystemMonitorResult implements Serializable {

    private static final long serialVersionUID = 7229392877839912411L;

    @Schema(description = "系统信息")
    private SysOsInfo sysOsInfo;

    @Schema(description = "系统磁盘信息")
    private List<SysDiskInfo> sysDiskInfos;

    @Schema(description = "Java信息")
    private SysJavaInfo sysJavaInfo;

    @Schema(description = "JVM内存信息")
    private SysJvmMemInfo sysJvmMemInfo;

    @Schema(description = "硬件信息")
    private HardwareInfo hardwareInfo;

    @Schema(description = "线程池信息")
    private ThreadPoolInfo threadPoolInfo;

    /**
     * 系统信息内部类
     */
    @Data
    @Schema(title = "系统信息")
    public static class SysOsInfo {

        @Schema(description = "系统名称")
        private String osName;

        @Schema(description = "系统架构")
        private String osArch;

        @Schema(description = "系统版本")
        private String osVersion;

        @Schema(description = "主机名称")
        private String osHostName;

        @Schema(description = "主机ip地址")
        private String osHostAddress;

    }

    /**
     * JVM信息内部类
     */
    @Data
    @Schema(title = "JVM信息内部类")
    public static class SysJavaInfo {

        @Schema(description = "虚拟机名称")
        private String jvmName;

        @Schema(description = "虚拟机版本")
        private String jvmVersion;

        @Schema(description = "虚拟机供应商")
        private String jvmVendor;

        @Schema(description = "java名称")
        private String javaName;

        @Schema(description = "java版本")
        private String javaVersion;

    }

    /**
     * JVM内存信息
     */
    @Data
    @Schema(title = "JVM内存信息")
    public static class SysJvmMemInfo {

        @Schema(description = "最大内存")
        private String jvmMaxMemory;

        @Schema(description = "可用内存")
        private String jvmUsableMemory;

        @Schema(description = "总内存")
        private String jvmTotalMemory;

        @Schema(description = "已使用内存")
        private String jvmUsedMemory;

        @Schema(description = "空余内存")
        private String jvmFreeMemory;

        @Schema(description = "使用率")
        private String jvmMemoryUsedRate;

    }

    /**
     * 硬件信息
     */
    @Data
    @Schema(title = "系统内存信息")
    public static class HardwareInfo {

        @Schema(description = "总内存")
        private String totalMemory;

        @Schema(description = "已使用内存")
        private String usedMemory;

        @Schema(description = "空余内存")
        private String freeMemory;

        @Schema(description = "内存使用率")
        private String usedRateMemory;

        @Schema(description = "CPU型号")
        private String cpuModel;

        @Schema(description = "CPU核心数")
        private Integer cpuNum;

    }

    /**
     * 系统磁盘内部类
     */
    @Data
    @Schema(title = "系统磁盘信息")
    public static class SysDiskInfo {

        @Schema(description = "名称")
        private String name;

        @Schema(description = "总容量")
        private String totalSpace;

        @Schema(description = "已用容量")
        private String usedSpace;

        @Schema(description = "可用容量")
        private String freeSpace;

        @Schema(description = "使用率")
        private String usedRate;

    }

    /**
     * 线程池
     */
    @Data
    @Schema(title = "系统磁盘信息")
    public static class ThreadPoolInfo {

        @Schema(description = "线程池大小")
        private int poolSize;

        @Schema(description = "核心池大小")
        private int corePoolSize;

        @Schema(description = "线程池最大数")
        private int maxPoolSize;

        @Schema(description = "活跃数量")
        private int activeCount;

        @Schema(description = "任务队列数")
        private int queueSize;

        @Schema(description = "使用率")
        private int completedTaskCount;

    }

}
