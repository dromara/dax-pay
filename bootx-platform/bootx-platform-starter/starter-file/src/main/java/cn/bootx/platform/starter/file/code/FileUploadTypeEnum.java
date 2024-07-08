package cn.bootx.platform.starter.file.code;

/**
 * 文件上传类型
 *
 * @author xxm
 * @since 2022/1/14
 */
public enum FileUploadTypeEnum {

    /** 本地存储 */
    LOCAL,
    /** 数据库存储 */
    JDBC,
    /** Mongo存储 */
    MONGO,
    /** minio存储 */
    MINIO,
    /** 阿里云 oss存储 */
    ALIYUN_OSS,

    /** 腾讯云 oss存储 */
    TENCENT_OSS

}
