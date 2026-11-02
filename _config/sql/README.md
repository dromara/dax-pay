# SQL脚本说明

## 注意事项
- 项目中不直接提供历史SQL文件，如果需要可以查看项目的历史版本，其中的SQL文件就是对应的历史版本
- 全新安装的脚本执行顺序：table.sql -> data.sql
- iam_perm_menu.sql 为可选脚本：菜单专用幂等种子(`CREATE TABLE IF NOT EXISTS` + 插入 `ON CONFLICT ("id") DO NOTHING`，与 data.sql 内的菜单种子同源)，可在存量库重复执行，仅在需要单独补菜单时使用(表结构仍以 table.sql 为准)
- 升级脚本执行顺序：update-tables.sql -> update-datas.sql，两个增量脚本**必须幂等**(重复执行不报错、二次执行零变更)；提供的更新版本是上一个版本升级到当前版本所对应的SQL文件，不能跨版本升级
- data.sql 中 bootx 超级管理员密码为开发默认密码 **121212**（由 redact-data.mjs 预处理时重置，防止开发库改密导致导出密码漂移），生产部署后必须立即修改
- 开发库密码忘记时，可执行 reset-bootx-password.sql 将 bootx 密码重置回 121212

## 脚本介绍
- table.sql：创建表结构(全量)
- data.sql：表数据(全量, 已脱敏)
- iam_perm_menu.sql：菜单专用幂等种子，与 data.sql 的菜单种子同源，可在存量库重复执行、用于单独补菜单；每条插入带 `ON CONFLICT ("id") DO NOTHING`，重复执行零变更
- update-tables.sql：更新表结构(增量, 必须幂等)
- update-datas.sql：更新数据(增量, 必须幂等；每条 INSERT 带 `ON CONFLICT DO NOTHING`)
- 发版口径：每个版本周期内积累的增量, 在定版时随全量 table.sql/data.sql 重导收口后, update-tables.sql 与 update-datas.sql 双双清空重置为空白基线(先例 beta8/beta9/beta10); 发版时这两个文件无需改动, 保持空白即可
- reset-bootx-password.sql：bootx 超管密码重置为开发默认密码(121212)，开发库忘记密码时急救用
