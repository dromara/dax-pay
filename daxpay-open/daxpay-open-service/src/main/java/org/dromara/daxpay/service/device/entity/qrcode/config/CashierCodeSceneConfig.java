package org.dromara.daxpay.service.device.entity.qrcode.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.core.enums.CashierSceneEnum;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.service.device.convert.qrcode.CashierCodeSceneConfigConvert;
import org.dromara.daxpay.service.device.param.qrcode.config.CashierCodeSceneConfigParam;
import org.dromara.daxpay.service.device.result.qrcode.config.CashierCodeSceneConfigResult;
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
@TableName("pay_cashier_code_scene_config")
public class CashierCodeSceneConfig extends MpBaseEntity implements ToResult<CashierCodeSceneConfigResult> {

    /** 配置ID */
    private Long configId;

    /**
     * 收银场景
     * @see CashierSceneEnum
     */
    @Schema(description = "收银场景")
    private String scene;

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

    /** OpenId获取方式 */
    private String openIdGetType;

    /**
     * 发起调用的类型
     * @see GatewayCallTypeEnum
     */
    private String callType;

    public Boolean getNeedOpenId() {
        return Objects.equals(needOpenId, true);
    }

    public static CashierCodeSceneConfig init(CashierCodeSceneConfigParam param) {
        return CashierCodeSceneConfigConvert.CONVERT.toEntity(param);
    }

    /**
     * 转换
     */
    @Override
    public CashierCodeSceneConfigResult toResult() {
        return CashierCodeSceneConfigConvert.CONVERT.toResult(this);
    }
}
