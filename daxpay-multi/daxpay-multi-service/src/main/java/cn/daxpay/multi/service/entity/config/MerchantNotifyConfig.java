package cn.daxpay.multi.service.entity.config;

import cn.daxpay.multi.service.common.entity.MchBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户应用消息通知配置
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant_notify_config")
public class MerchantNotifyConfig extends MchBaseEntity {

    /** 消息通知类型编码 */
    private String code;

    /** 是否订阅 */
    private boolean subscribe;
}
