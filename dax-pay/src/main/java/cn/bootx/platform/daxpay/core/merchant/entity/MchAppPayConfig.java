package cn.bootx.platform.daxpay.core.merchant.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.merchant.convert.MchAppPayConfigConvert;
import cn.bootx.platform.daxpay.dto.merchant.MchAppPayConfigDto;
import cn.bootx.platform.daxpay.param.merchant.MchAppPayConfigParam;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* 商户应用支付配置
* @author xxm
* @date 2023-05-19
*/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_mch_app_config")
public class MchAppPayConfig extends MpBaseEntity implements EntityBaseFunction<MchAppPayConfigDto> {

    /** 关联配置ID */
    @DbColumn(comment = "关联应用ID")
    private Long appId;

    /** 关联配置ID */
    @DbColumn(comment = "关联配置ID")
    private Long configId;

    /** 类型 */
    @DbColumn(comment = "支付通道类型")
    private String channel;

    /** 类型 */
    @DbColumn(comment = "支付通道名称")
    private String channelName;

    /** 状态 */
    @DbColumn(comment = "状态")
    private String state;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /** 创建对象 */
    public static MchAppPayConfig init(MchAppPayConfigParam in) {
            return MchAppPayConfigConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public MchAppPayConfigDto toDto() {
        return MchAppPayConfigConvert.CONVERT.convert(this);
    }
}
