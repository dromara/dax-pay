package cn.daxpay.multi.service.entity.merchant;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.multi.core.enums.SignTypeEnum;
import cn.daxpay.multi.core.enums.TradeNotifyTypeEnum;
import cn.daxpay.multi.service.convert.merchant.MchAppConvert;
import cn.daxpay.multi.service.enums.MchAppStautsEnum;
import cn.daxpay.multi.service.result.merchant.MchAppResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 商户应用
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_mch_app")
public class MchApp extends MpBaseEntity implements ToResult<MchAppResult> {

    /** 商户号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mchNo;

    /** 应用号 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /** 应用名称 */
    private String appName;

    /**
     * 签名方式
     * @see SignTypeEnum
     */
    private String signType;

    /** 签名秘钥 */
    private String signSecret;

    /** 是否对请求进行验签 */
    private boolean reqSign;

    /** 支付限额 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal limitAmount;

    /** 订单默认超时时间(分钟) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer orderTimeout;

    /**
     * 应用状态
     * @see MchAppStautsEnum
     */
    private String status;

    /**
     * 异步消息通知类型, 当前只支持http方式
     * @see TradeNotifyTypeEnum
     */
    private String notifyType;

    /**
     * 通知地址, http/WebSocket 需要配置
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    @Override
    public MchAppResult toResult() {
        return MchAppConvert.CONVERT.toResult(this);
    }
}
