package cn.daxpay.open.platform.iam.convert.dashboardpreference;

import cn.daxpay.open.platform.iam.entity.dashboardpreference.UserDashboardPreference;
import cn.daxpay.open.platform.iam.result.dashboardpreference.QuickEntryResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDashboardPreferenceConvert {

    UserDashboardPreferenceConvert CONVERT = Mappers.getMapper(UserDashboardPreferenceConvert.class);

    QuickEntryResult toResult(UserDashboardPreference in);

}
