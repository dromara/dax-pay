package org.dromara.daxpay.service.entity.gateway;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.CashierCodeTypeEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.gateway.CashierCodeItemConfigConvert;
import org.dromara.daxpay.service.param.gateway.CashierCodeItemConfigParam;
import org.dromara.daxpay.service.result.gateway.config.CashierCodeItemConfigResult;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 码牌支付配置明细
 * @author xxm
 * @since 2024/11/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_cashier_code_item_config")
public class CashierCodeItemConfig extends MchAppBaseEntity implements ToResult<CashierCodeItemConfigResult> {

    /** 码牌ID */
    private Long codeId;

    /**
     * 码牌类型
     * @see CashierCodeTypeEnum
     */
    @Schema(description = "收银台类型")
    private String type;

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /**
     * 支付方式
     * @see PayMethodEnum
     */
    private String payMethod;

    /**
     * 其他支付方式
     */
    private String otherMethod;

    /** 需要获取OpenId */
    private Boolean needOpenId;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    private String callType;

    public Boolean getNeedOpenId() {
        return Objects.equals(needOpenId, true);
    }

    public static CashierCodeItemConfig init(CashierCodeItemConfigParam param) {
        return CashierCodeItemConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CashierCodeItemConfigResult toResult() {
        return CashierCodeItemConfigConvert.CONVERT.toResult(this);
    }
}
