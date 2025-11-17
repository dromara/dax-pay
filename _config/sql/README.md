# SQL脚本说明

## 注意事项
- 默认只提供postgresql的SQL，MySQL数据脚本可以根据教程自行转换[PG转MySQL脚本流程](https://yibeiguangnian.feishu.cn/wiki/ZyiYwwfCfiv9TBkt8kIcjubynvs)
- 项目中不直接提供历史SQL文件，如果需要可以去飞书[系统安装包](https://yibeiguangnian.feishu.cn/wiki/ITXMw7riaiifVdkn1Zwc2sWYnJh)中下载历史版本
- 项目中不直接提供历史SQL文件，如果需要可以查看项目的历史版本，其中的SQL文件就是对应的历史版本
- 全新安装的脚本执行顺序：tables.sql -> datas.sql
- 升级脚本执行顺序：update-tables.sql -> update-datas.sql提供的更新版本是上一个版本升级到当前版本所对应的SQL文件，不能跨版本升级

## 脚本介绍
- tables.sql：创建表结构(全量)
- datas.sql：表数据(全量)
- update-tables.sql：更新表结构
- update-datas.sql：更新数据
