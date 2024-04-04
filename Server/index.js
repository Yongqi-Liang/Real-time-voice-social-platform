var express = require('express'); //导入express设置别名为express
var app = express(); //将express实例化为app
var http = require('http').createServer(app); //导入http设置别名为http
var io = require('socket.io')(https); //导入socket.io设置别名为io
const fs = require('fs');

let sslOptions = {
        key: fs.readFileSync('./Cert/privkey.key'),//里面的文件替换成你生成的私钥
        cert: fs.readFileSync('./Cert/cacert.pem')//里面的文件替换成你生成的证书
    };
    
// 解析ip:port/static/到服务器static目录
app.use("/static", express.static('static/'));

// 在3000端口开启http服务
http.listen(443,() => { //监听 3000 端口
    console.log('https listen on'); 
});

//设置页面链接
    // 设置webapp的页面
    app.get('/',(req, res) => {
        res.sendFile(__dirname + '/index.html');
    });
    // 设置camera页面
    app.get('/camera',(req, res) => {
        res.sendFile(__dirname + '/camera.html');
    })

// 设置对socket连接、断开连接的监听
io.on('connection',(socket) => {
    console.log('a user connected : ' + socket.id);
    socket.on('disconnect',() => {
        console.log('user disconnected : ' + socket.id);
    });

    socket.on('chat message',(msg) => {
        console.log(socket.id + 'say:' + msg);
        io.emit('chat message',msg);
    })
});

