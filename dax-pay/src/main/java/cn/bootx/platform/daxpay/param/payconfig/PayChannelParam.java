package cn.bootx.platform.daxpay.param.payconfig;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付通道
 *
 * @author xxm
 * @date 2021/6/30
 */
@Data
@Accessors(chain = true)
public class PayChannelParam {

    /** 主键 */
    private Long id;

    /**
     * 通道代码(唯一)
     * @see PayChannelEnum
     */
    private String code;

    /** 名称 */
    private String name;

    /** 页面展示：卡片-图标 */
    private String icon;

    /** 页面展示：卡片-背景色 */
    private String bgColor;

    /** 状态 */
    private Integer state;

    /** 备注 */
    private String remark;

}
