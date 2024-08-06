package cn.daxpay.multi.service.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.service.convert.constant.MerchantNotifyConstConvert;
import cn.daxpay.multi.service.result.constant.MerchantNotifyConstResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户订阅通知类型
 * @see cn.daxpay.multi.service.enums.NotifyTypeEnum
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant_notify_const")
public class MerchantNotifyConst extends MpIdEntity implements ToResult<MerchantNotifyConstResult> {

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 启用 */
    private boolean enable;

    /**
     * 转换
     */
    @Override
    public MerchantNotifyConstResult toResult() {
        return MerchantNotifyConstConvert.CONVERT.toResult(this);
    }
}
