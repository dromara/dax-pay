package cn.bootx.daxpay.dto.payconfig;

import cn.bootx.common.core.rest.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 支付通道支持的支付方式
 *
 * @author xxm
 * @date 2021/6/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PayChannelWayDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 4579797594767439540L;

    /** 支付方式代码 */
    private String code;

    /** 支付方式名称 */
    private String name;

    /** 通道id */
    private Long channelId;

    /** 通道code */
    private String channelCode;

    /** 备注 */
    private String remark;

}
