const express = require('express');
const http = require('http');
const { Server } = require('socket.io');
const path = require('path');

const app = express();
const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"]
  }
});

// 中间件
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ extended: true, limit: '50mb' }));
app.use(express.static(path.join(__dirname)));

// 数据存储（实际生产建议使用数据库）
let users = [];
let messages = [];
let friends = [];
let groups = [];

// 主页
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'chatroom.html'));
});

// Socket.io 连接
io.on('connection', (socket) => {
  console.log('新用户连接:', socket.id);

  // 用户上线
  socket.on('user:online', (userData) => {
    const existingUser = users.find(u => u.id === userData.id);
    if (existingUser) {
      existingUser.socketId = socket.id;
      existingUser.online = true;
      existingUser.lastSeen = Date.now();
    } else {
      users.push({
        ...userData,
        socketId: socket.id,
        online: true,
        lastSeen: Date.now()
      });
    }
    
    // 广播用户上线
    io.emit('users:update', users);
    
    // 发送初始数据
    socket.emit('init:data', {
      users,
      messages,
      friends,
      groups
    });
  });

  // 发送公共消息
  socket.on('message:public', (msg) => {
    const newMsg = {
      ...msg,
      id: Date.now().toString(),
      timestamp: Date.now()
    };
    messages.push(newMsg);
    io.emit('message:public', newMsg);
  });

  // 发送私聊消息
  socket.on('message:private', (msg) => {
    const newMsg = {
      ...msg,
      id: Date.now().toString(),
      timestamp: Date.now()
    };
    messages.push(newMsg);
    
    // 发送给发送方和接收方
    socket.emit('message:private', newMsg);
    const recipient = users.find(u => u.id === msg.toId);
    if (recipient && recipient.socketId) {
      io.to(recipient.socketId).emit('message:private', newMsg);
    }
  });

  // 发送群组消息
  socket.on('message:group', (msg) => {
    const newMsg = {
      ...msg,
      id: Date.now().toString(),
      timestamp: Date.now()
    };
    messages.push(newMsg);
    io.emit('message:group', newMsg);
  });

  // 添加好友
  socket.on('friend:add', (friendData) => {
    friends.push(friendData);
    io.emit('friends:update', friends);
  });

  // 创建群组
  socket.on('group:create', (groupData) => {
    const newGroup = {
      ...groupData,
      id: Date.now().toString(),
      createdAt: Date.now()
    };
    groups.push(newGroup);
    io.emit('groups:update', groups);
  });

  // 用户断开
  socket.on('disconnect', () => {
    const user = users.find(u => u.socketId === socket.id);
    if (user) {
      user.online = false;
      user.lastSeen = Date.now();
      io.emit('users:update', users);
    }
    console.log('用户断开:', socket.id);
  });
});

// 启动服务器
const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
  console.log(`梦幻聊天室服务器已启动！`);
  console.log(`本地访问: http://localhost:${PORT}`);
  console.log(`部署到公网后，所有人都可以通过你的服务器IP访问！`);
});
