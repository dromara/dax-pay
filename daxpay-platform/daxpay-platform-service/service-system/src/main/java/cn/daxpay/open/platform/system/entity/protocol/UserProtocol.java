package cn.daxpay.open.platform.system.entity.protocol;

import cn.daxpay.open.platform.system.convert.protocol.UserProtocolConvert;
import cn.daxpay.open.platform.system.param.protocol.UserProtocolParam;
import cn.daxpay.open.platform.system.result.protocol.UserProtocolResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户协议管理
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("base_user_protocol")
public class UserProtocol extends MpBaseEntity implements ToResult<UserProtocolResult> {

    /// 名称
    private String name;

    /// 显示名称
    private String showName;

    /// 类型
    private String type;

    /// 端类型
    private String clientType;

    /// 内容格式
    private String contentFormat;

    /// 协议内容
    private String content;

    /// 默认协议
    private Boolean defaultProtocol;

    @Override
    public UserProtocolResult toResult() {
        return UserProtocolConvert.CONVERT.toResult(this);
    }

    public static UserProtocol init(UserProtocolParam param) {
        return UserProtocolConvert.CONVERT.toEntity(param);
    }
}

