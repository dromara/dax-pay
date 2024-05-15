package cn.bootx.platform.starter.dingtalk.core.media.entity;

import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 钉钉媒体文件MD5值关联关系
 *
 * @author xxm
 * @since 2022/7/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
@TableName("starter_ding_media_md5")
public class DingMediaMd5 extends MpCreateEntity {

    /** 媒体文件id */
    private String mediaId;

    /** 媒体文件的md5值 */
    private String md5;

}
