package cn.bootx.platform.common.core.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 文件配置
 *
 * @author xxm
 * @since 2022/7/25
 */
@UtilityClass
public class FileUtil extends cn.hutool.core.io.FileUtil {

    /**
     * 创建临时文件.
     * @param inputStream 输入文件流
     * @param name 文件名
     * @param ext 扩展名
     */
    @SneakyThrows
    public static File createTempFile(InputStream inputStream, String name, String ext) {
        File tempDir = Files.createTempDirectory("temp").toFile();
        tempDir.deleteOnExit();
        return createTmpFile(inputStream, name, ext, tempDir);
    }

    /**
     * 创建临时文件.
     * @param inputStream 输入文件流
     * @param name 文件名
     * @param ext 扩展名
     * @param tmpDirFile 临时文件夹目录
     */
    @SneakyThrows
    public File createTmpFile(InputStream inputStream, String name, String ext, File tmpDirFile) {
        File resultFile = new File(tmpDirFile, name + '.' + ext);
        writeFromStream(inputStream, resultFile);
        resultFile.deleteOnExit();
        return resultFile;
    }

}
