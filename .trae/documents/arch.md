
## 1. Architecture Design
纯前端架构，使用浏览器LocalStorage和BroadcastChannel API实现数据同步。

```mermaid
graph TB
    subgraph 浏览器客户端A
        UI[用户界面]
        StateA[状态管理]
        BC_A[BroadcastChannel]
        LS_A[LocalStorage]
    end
    
    subgraph 浏览器客户端B
        UI2[用户界面]
        StateB[状态管理]
        BC_B[BroadcastChannel]
        LS_B[LocalStorage]
    end
    
    UI --&gt; StateA
    StateA --&gt; BC_A
    StateA --&gt; LS_A
    BC_A --&gt; BC_B
    LS_A &lt;--&gt; LS_B
    BC_B --&gt; StateB
    StateB --&gt; UI2
```

## 2. Technology Description
- Frontend: 纯HTML5 + CSS3 + JavaScript (无需框架)
- 通信机制: BroadcastChannel API (浏览器标签页间通信)
- 数据持久化: LocalStorage
- 文件处理: File API + Base64编码
- 无后端服务依赖

## 3. Route Definitions
| Route | Purpose |
|-------|---------|
| / | 聊天室主页面 |

## 4. Data Model

### 4.1 Message 数据结构
```typescript
interface Message {
  id: string;
  type: 'text' | 'file';
  content: string;
  timestamp: number;
  userId: string;
  userName: string;
  file?: {
    name: string;
    type: string;
    size: number;
    data: string; // Base64
  };
}
```

### 4.2 User 数据结构
```typescript
interface User {
  id: string;
  name: string;
  avatarColor: string;
  online: boolean;
}
```

## 5. Core Modules

### 5.1 消息管理模块
- 消息创建、存储、读取
- 消息广播机制
- 消息历史记录

### 5.2 文件处理模块
- 文件选择和拖拽上传
- 文件Base64编码
- 文件预览和下载

### 5.3 用户管理模块
- 匿名用户ID生成
- 用户名生成
- 在线用户状态

### 5.4 UI组件
- 消息气泡渲染
- 文件上传区
- 用户列表
