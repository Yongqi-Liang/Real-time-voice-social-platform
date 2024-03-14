# 使用官方 Node.js 镜像作为基础
FROM node:latest

# 设置工作目录
WORKDIR /app

# 复制 package.json 和 package-lock.json（如果存在）
COPY package*.json ./

# 安装依赖
RUN npm install

# 将当前目录下的所有文件复制到容器的 /app 目录中
COPY . .

# 暴露端口（根据你的应用需要修改端口号）
EXPOSE 3000

# 运行 Node.js 服务器
CMD ["node", "server.js"]
