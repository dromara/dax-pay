# SQL脚本说明

## 注意事项
- MySQL名录下对应的是MySQL的数据库脚本，postgresql下是PGSQL数据库的数据库脚本
- 项目中不直接提供历史SQL文件，如果需要可以查看项目的git历史版本，其中的SQL文件就是对应的历史版本
- 全新安装的脚本执行顺序：tables.sql -> datas.sql
- 升级脚本执行顺序：update-tables.sql -> update-datas.sql -> update-update-perm.sql(可选)，提供的更新版本是上一个版本升级到当前版本所对应的SQL文件，不能跨版本升级
- update-perm.sql是数据权限脚本，如果没有对数据权限进行过修改，执行脚本覆盖原有数据即可，如果修改了数据权限，就不要执行这个脚本，自行处理数据权限即可

## 脚本介绍
- tables.sql：创建表结构(全量)
- datas.sql：表数据(全量)
- update-tables.sql：更新表结构
- update-datas.sql：更新数据
- update-perm.sql：数据权限脚本(更新会清除旧数据)
