package cn.bootx.platform.starter.file.entity;

import cn.bootx.platform.common.mybatisplus.base.MpRealDelEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.starter.file.code.FilePlatformTypeEnum;
import cn.bootx.platform.starter.file.convert.FilePlatformConvert;
import cn.bootx.platform.starter.file.result.FilePlatformResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.Objects;

/**
 * 文件存储平台
 * @author xxm
 * @since 2024/8/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("starter_file_platform")
public class FilePlatform extends MpRealDelEntity implements ToResult<FilePlatformResult> {

    /**
     * 接入类型
     * @see FilePlatformTypeEnum
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String type;

    /** 名称 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String name;

    /** 默认存储平台 */
    private boolean defaultPlatform;

    /** 访问地址 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String url;

    /** 前端直传 */
    private Boolean frontendUpload;

    public String getUrl() {
        return StrUtil.removeSuffix(url, "/");
    }

    public Boolean getFrontendUpload() {
        return Objects.equals(frontendUpload,true);
    }

    /**
     * 转换
     */
    @Override
    public FilePlatformResult toResult() {
        return FilePlatformConvert.CONVERT.toResult(this);
    }
}
