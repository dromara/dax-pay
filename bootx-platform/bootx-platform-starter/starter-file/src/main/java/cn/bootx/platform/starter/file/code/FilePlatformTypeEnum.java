package cn.bootx.platform.starter.file.code;

import cn.bootx.platform.core.exception.BizException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 存储平台类型
 *
 * @author xxm
 * @since 2022/1/14
 */
@Getter
@RequiredArgsConstructor
public enum FilePlatformTypeEnum {
    LOCAL("local",false),
    FTP("ftp",false),
    SFTP("sftp",false),
    WEB_DAV("web_dav",false),
    // S3 存储， 现在系统只支持这一种方式
    AMAZON_S3("amazon-s3",true),
    MINIO("minio",true),
    ALI("ali",true),
    HUAWEI("huawei",true),
    TENCENT("tencent",true),
    BAIDU("baidu",true),
    UPYUN("upyun",true),
    QINIU("qiniu",true),
    GOOGLE_CLOUD("google_cloud",true),
    FAST_DFS("fast_dfs",true),
    AZURE("azure",true);

    private final String code;
    /** 前端直传 */
    private final boolean frontendUpload;

    public static FilePlatformTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.code, code))
                .findFirst()
                .orElseThrow(() -> new BizException("不支持的类型"));
    }

}
