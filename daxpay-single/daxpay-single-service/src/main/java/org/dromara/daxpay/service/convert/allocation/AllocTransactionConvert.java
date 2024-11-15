package org.dromara.daxpay.service.convert.allocation;

import org.dromara.daxpay.core.result.allocation.transaction.AllocDetailResult;
import org.dromara.daxpay.core.result.allocation.transaction.AllocTransactionResult;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocDetail;
import org.dromara.daxpay.service.entity.allocation.transaction.AllocTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/11/15
 */
@Mapper
public interface AllocTransactionConvert {
    AllocTransactionConvert CONVERT = Mappers.getMapper(AllocTransactionConvert.class);

    AllocTransactionResult toResult(AllocTransaction in);

    AllocDetailResult toResult(AllocDetail in);
}
