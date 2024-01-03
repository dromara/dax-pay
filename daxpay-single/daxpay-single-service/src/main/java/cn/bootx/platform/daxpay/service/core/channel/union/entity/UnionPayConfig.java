package cn.bootx.platform.daxpay.service.core.channel.union.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.core.channel.union.convert.UnionPayConvert;
import cn.bootx.platform.daxpay.service.dto.channel.union.UnionPayConfigDto;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 云闪付支付配置
 *
 * @author xxm
 * @since 2022/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "云闪付支付配置")
@Accessors(chain = true)
@TableName("pay_union_pay_config")
public class UnionPayConfig extends MpBaseEntity implements EntityBaseFunction<UnionPayConfigDto> {

    @Override
    public UnionPayConfigDto toDto() {
        return UnionPayConvert.CONVERT.convert(this);
    }

}
