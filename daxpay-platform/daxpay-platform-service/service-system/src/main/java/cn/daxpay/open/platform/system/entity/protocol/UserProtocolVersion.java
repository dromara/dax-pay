package cn.daxpay.open.platform.system.entity.protocol;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.system.convert.protocol.UserProtocolVersionConvert;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolVersionParam;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolVersionResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 用户协议版本
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("base_user_protocol_version")
public class UserProtocolVersion extends MpBaseEntity implements ToResult<UserProtocolVersionResult> {

    /// 协议ID
    private Long protocolId;

    /// 语言
    private String language;

    /// 版本号
    private Integer versionNo;

    /// 版本标签
    private String versionLabel;

    /// 标题
    private String title;

    /// 内容(Markdown)
    private String content;

    /// 渲染后的HTML
    private String contentHtml;

    /// 内容格式
    private String contentFormat;

    /// 状态
    private String status;

    /// 生效时间
    private OffsetDateTime effectiveTime;

    /// 变更说明
    private String summary;

    @Override
    public UserProtocolVersionResult toResult() {
        return UserProtocolVersionConvert.CONVERT.toResult(this);
    }

    public static UserProtocolVersion init(UserProtocolVersionParam param) {
        return UserProtocolVersionConvert.CONVERT.toEntity(param);
    }
}
