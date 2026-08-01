# AIFlow API 摘要

版权所有 © 2026 上海如静知华信息科技有限公司。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录并获取 JWT |
| GET | `/api/admin/dashboard` | 智能体运营数据 |
| GET | `/api/admin/work-orders` | 智能体运行实例清单 |
| GET | `/api/shopfloor/dashboard` | 人机协同工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 提交人工节点结果 |
| POST | `/api/shopfloor/ai-preview` | 调用可替换 AI Provider 生成执行计划 |
