package cn.bootx.platform.starter.file.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.starter.file.convert.FileConvert;
import cn.bootx.platform.starter.file.result.UploadFileResult;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.x.file.storage.core.FileInfo;

import java.time.LocalDateTime;

/**
 * 上传文件信息
 *
 * @author xxm
 * @since 2022/1/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "starter_file_upload_info", autoResultMap = true)
public class UploadFileInfo extends MpIdEntity implements ToResult<UploadFileResult> {

    /**
     * 文件访问地址
     */
    private String url;

    /**
     * 文件大小，单位字节
     */
    private Long size;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 原始文件名
     */
    private String originalFilename;

    /**
     * 基础存储路径
     */
    private String basePath;

    /**
     * 存储路径
     */
    private String path;

    /**
     * 文件扩展名
     */
    private String ext;

    /**
     * MIME 类型
     */
    private String contentType;

    /**
     * 存储平台
     */
    private String platform;

    /**
     * 缩略图访问路径
     */
    private String thUrl;

    /**
     * 缩略图名称
     */
    private String thFilename;

    /**
     * 缩略图大小，单位字节
     */
    private Long thSize;

    /**
     * 缩略图 MIME 类型
     */
    private String thContentType;

    /**
     * 文件所属对象id
     */
    private String objectId;

    /**
     * 文件所属对象类型，例如用户头像，评价图片
     */
    private String objectType;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Override
    public UploadFileResult toResult() {
        return FileConvert.CONVERT.convert(this);
    }

    /**
     * 初始化创建
     */
    public static UploadFileInfo init(FileInfo fileInfo){
        return FileConvert.CONVERT.convert(fileInfo);
    }

    /**
     * 转换为 x.file.storage 的文件信息对象
     */
    public FileInfo toFileInfo(){
        return FileConvert.CONVERT.toFileInfo(this);
    }

}
