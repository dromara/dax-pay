package cn.bootx.platform.iam.core.security.password.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.core.security.password.entity.PasswordLoginFailRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 密码登录失败记录
 * @author xxm
 * @since 2023/9/19
 */
@Slf4j
@Repository
public class PasswordLoginFailRecordManager extends BaseManager<PasswordLoginFailRecordMapper,PasswordLoginFailRecord> {


    /**
     * 根据用户id查询
     */
    public Optional<PasswordLoginFailRecord> findByUserId(Long userId){
        return findByField(PasswordLoginFailRecord::getUserId,userId);
    }

    /**
     * 批量清除登录失败次数
     */
    public void clearBatchFailCount(List<Long> userIds) {
        this.lambdaUpdate()
                .in(PasswordLoginFailRecord::getUserId,userIds)
                .set(PasswordLoginFailRecord::getFailCount,0)
                .update();
    }
}
