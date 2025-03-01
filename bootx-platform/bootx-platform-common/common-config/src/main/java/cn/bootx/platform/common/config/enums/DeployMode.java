package cn.bootx.platform.common.config.enums;

/**
 * 部署模式
 * @author xxm
 * @since 2025/1/31
 */
public enum DeployMode {

    /**
     * 分模块部署, 如果有多个业务系统, 每套业务系统都是构建为独立的jar进行部署, 不同业务系统天生进行隔离
     */
    MODULE,
    /**
     * 融合部署, 如果有多个业务系统, 但打包成一个jar进行部署的模式, 自行处理业务系统的隔离
     */
    FUSION;
}
