package cn.bootx.platform.iam.core.security.password.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.core.security.password.entity.PasswordChangeHistory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 密码历史
 * @author xxm
 * @since 2023-09-19
 */
@Repository
@RequiredArgsConstructor
public class PasswordChangeHistoryManager extends BaseManager<PasswordChangeHistoryMapper, PasswordChangeHistory> {

    /**
     * 查询指定用户最近N条历史
     */
    public List<PasswordChangeHistory> findAllByUserAndCount(Long userId, int count){
        Page<PasswordChangeHistory> page = new Page<>(1,count);
        page.setSearchCount(false);
        return lambdaQuery()
                .eq(PasswordChangeHistory::getUserId,userId)
                .orderByDesc(PasswordChangeHistory::getId)
                .page(page)
                .getRecords();
    }
}
