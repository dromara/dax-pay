package cn.daxpay.open.platform.iam.entity.social;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户第三方账号绑定
///
/// 记录本地用户与第三方平台账号的绑定关系, 一个三方账号仅能绑定一个本地用户(唯一索引 source+open_id)
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("iam_user_social")
public class IamUserSocial extends MpBaseEntity {

    /// 本地用户ID(关联 iam_user_info.id)
    private Long userId;

    /// 身份域编码(admin/merchant)
    private String clientCode;

    /// 平台编码(weChat/weCom/qq/github/gitee/feishu/dingTalk)
    private String source;

    /// 平台用户唯一标识(openid/uuid)
    private String openId;

    /// 平台昵称
    private String username;

    /// 平台头像
    private String avatar;
}
