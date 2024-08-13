package cn.bootx.platform.starter.file.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 存储平台类型
 *
 * @author xxm
 * @since 2022/1/14
 */
@Getter
@RequiredArgsConstructor
public enum FilePlatformTypeEnum {
    LOCAL("local"),
    FTP("ftp"),
    SFTP("sftp"),
    WEB_DAV("web_dav"),
    AMAZON("amazon"),
    MINIO("minio"),
    ALI("ali"),
    HUAWEI("huawei"),
    TENCENT("tencent"),
    BAIDU("baidu"),
    UPYUN("upyun"),
    QINIU("qiniu"),
    GOOGLE_CLOUD("google_cloud"),
    FAST_DFS("fast_dfs"),
    AZURE("azure");

    private final String code;

}
