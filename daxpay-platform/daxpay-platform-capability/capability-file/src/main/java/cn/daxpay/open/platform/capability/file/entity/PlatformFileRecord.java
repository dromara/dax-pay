package cn.daxpay.open.platform.capability.file.entity;

import cn.daxpay.open.platform.capability.file.code.FileUploadStatusEnum;
import cn.daxpay.open.platform.capability.file.convert.PlatformFileRecordConvert;
import cn.daxpay.open.platform.capability.file.result.PlatformFileRecordResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 平台文件记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "starter_platform_file_record", autoResultMap = true)
public class PlatformFileRecord extends MpBaseEntity implements ToResult<PlatformFileRecordResult> {

    /// 文件大小，单位字节
    private Long size;

    /// 文件名称（不含路径）
    private String filename;

    /// 原始文件名
    private String originalFilename;

    /// 存储路径（以/开头，不含文件名）
    private String path;

    /// 文件扩展名
    private String ext;

    /// MIME类型
    private String contentType;

    /// 访问类型
    /// @see cn.daxpay.open.platform.capability.file.code.UploadAccessTypeEnum
    private String accessType;

    /// 业务分类
    private String bizType;

    /// 状态
    /// @see FileUploadStatusEnum
    private String status;

    /// 备注
    private String remark;

    @Override
    public PlatformFileRecordResult toResult() {
        return PlatformFileRecordConvert.CONVERT.convert(this);
    }
}

