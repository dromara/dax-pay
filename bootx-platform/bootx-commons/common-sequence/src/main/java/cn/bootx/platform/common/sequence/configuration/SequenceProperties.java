package cn.bootx.platform.common.sequence.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 序列生成器参数
 *
 * @author xxm
 * @since 2021/8/6
 */
@Getter
@Setter
@ConfigurationProperties("bootx.common.sequence")
public class SequenceProperties {


    /**
     * 存储类型 默认为数据库
     */
    private Type type = Type.JDBC;

    /**
     * 序列生成器默认前缀(Redis有效)
     */
    private String keyPrefix = "bootx:sequence:";

    /**
     * 默认序列生成器名称
     */
    private String name = "default";

    /**
     * 序列生成器默认步长
     */
    private int step = 1;

    /**
     * 序列生成器默认区间步长
     */
    private int rangeStep = 1000;

    /**
     * 序列生成器默认区间起始位置
     */
    private int rangeStart = 0;

    /**
     * 存储类型
     */
    public enum Type {

        /** redis */
        REDIS,
        /** 数据库 */
        JDBC,
        /** MongoDB */
        MONGO

    }

}
