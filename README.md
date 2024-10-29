### 数据权限设计思想
- 用户管理、角色管理、部门管理，可操作本部门及子部门数据
- 菜单管理、定时任务、参数管理、字典管理、系统日志，没有数据权限
- 业务功能，按照用户数据权限，查询、操作数据【没有本部门数据权限，也能查询本人数据】

<br> 


**项目结构**
```
├─renren-common     公共模块
│ 
├─renren-admin      管理后台
│    ├─db  数据库SQL脚本
│    │ 
│    ├─modules  模块
│    │    ├─job 定时任务
│    │    ├─log 日志管理
│    │    ├─oss 文件存储
│    │    ├─security 安全模块
│    │    └─sys 系统管理(核心)
│    │ 
│    └─resources 
│        ├─mapper   MyBatis文件
│        ├─public  静态资源
│        └─application.yml   全局配置文件
```

<br>

**技术选型：**
- 核心框架：Spring Boot 3.x
- 安全框架：Apache Shiro 1.12
- 持久层框架：MyBatis 3.5
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.2
- 日志管理：Logback
- 页面交互：Vue3.x

<br>

**软件需求**
- JDK17+
- Maven3.6+
- MySQL8.0+
- Oracle 11g+
- SQLServer 2012+
- PostgreSQL 9.4+
- 达梦8
<br>



![输入图片说明](renren-admin/src/main/resources/public/1.png)

<br>

![输入图片说明](renren-admin/src/main/resources/public/2.png)

<br>

