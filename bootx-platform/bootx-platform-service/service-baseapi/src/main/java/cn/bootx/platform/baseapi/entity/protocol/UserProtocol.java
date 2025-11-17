package cn.bootx.platform.baseapi.entity.protocol;

import cn.bootx.platform.baseapi.convert.protocol.UserProtocolConvert;
import cn.bootx.platform.baseapi.param.protocol.UserProtocolParam;
import cn.bootx.platform.baseapi.result.protocol.UserProtocolResult;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户协议管理
 * @author xxm
 * @since 2025/5/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("base_user_protocol")
public class UserProtocol extends MpBaseEntity implements ToResult<UserProtocolResult> {

    /** 名称 */
    private String name;

    /** 显示名称 */
    private String showName;

    /** 类型 */
    private String type;

    /** 默认协议 */
    private Boolean defaultProtocol;

    /** 内容 */
    private String content;

    @Override
    public UserProtocolResult toResult() {
        return UserProtocolConvert.CONVERT.toResult(this);
    }

    public static UserProtocol init(UserProtocolParam param) {
        return UserProtocolConvert.CONVERT.toEntity(param);
    }
}
