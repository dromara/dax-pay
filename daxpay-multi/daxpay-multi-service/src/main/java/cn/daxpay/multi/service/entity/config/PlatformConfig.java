package cn.daxpay.multi.service.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 平台配置
 * @author xxm
 * @since 2024/6/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PlatformConfig extends MpBaseEntity {
}
