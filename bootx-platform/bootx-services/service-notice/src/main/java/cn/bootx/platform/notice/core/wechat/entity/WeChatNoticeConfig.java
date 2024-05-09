package cn.bootx.platform.notice.core.wechat.entity;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信消息配置
 *
 * @author xxm
 * @since 2021/8/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("notice_wechat_config")
public class WeChatNoticeConfig extends MpBaseEntity {

}
