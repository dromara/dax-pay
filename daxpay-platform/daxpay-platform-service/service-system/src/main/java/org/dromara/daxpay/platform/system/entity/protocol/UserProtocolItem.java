package org.dromara.daxpay.platform.system.entity.protocol;

import org.dromara.daxpay.platform.system.convert.protocol.UserProtocolConvert;
import org.dromara.daxpay.platform.system.param.protocol.UserProtocolItemParam;
import org.dromara.daxpay.platform.system.result.protocol.UserProtocolItemResult;
import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户协议项管理
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("base_user_protocol_item")
public class UserProtocolItem extends MpBaseEntity implements ToResult<UserProtocolItemResult> {

    /// 协议id
    private Long protocolId;

    /// 菜单排序
    private Double sortNo;

    /// 协议内容
    private String content;

    @Override
    public UserProtocolItemResult toResult() {
        return UserProtocolConvert.CONVERT.toResult(this);
    }

    public static UserProtocolItem init(UserProtocolItemParam param) {
        return UserProtocolConvert.CONVERT.toEntity(param);
    }
}
