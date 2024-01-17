package cn.bootx.platform.daxpay.service.core.system.payinfo.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.core.system.payinfo.convert.PayChannelInfoConvert;
import cn.bootx.platform.daxpay.service.dto.system.payinfo.PayChannelInfoDto;
import cn.bootx.platform.daxpay.service.param.system.payinfo.PayChannelInfoParam;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付通道
 * 都是在代码中定义好的，界面上只做展示和部分信息的修改用，不可以进行增删相关的操作
 * @author xxm
 * @since 2024/1/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付通道信息")
@TableName("pay_channel_info")
public class PayChannelInfo extends MpBaseEntity implements EntityBaseFunction<PayChannelInfoDto> {

    /** 需要与系统中配置的枚举一致 */
    @DbColumn(comment = "代码")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;

    /** 需要与系统中配置的枚举一致 */
    @DbColumn(comment = "名称")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String name;

    /** logo图片 */
    @DbColumn(comment = "ICON")
    private Long iconId;

    /** 卡牌背景色 */
    @DbColumn(comment = "卡牌背景色")
    private String bgColor;

    /** 是否启用 */
    @DbColumn(comment = "是否启用")
    private Boolean enabled;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    public static PayChannelInfo init(PayChannelInfoParam in){
        return PayChannelInfoConvert.CONVERT.convert(in);
    }

    /**
     * 转换
     */
    @Override
    public PayChannelInfoDto toDto() {
        return PayChannelInfoConvert.CONVERT.convert(this);
    }

}
