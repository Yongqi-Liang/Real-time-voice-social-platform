var express = require('express'); //导入express设置别名为express
var app = express(); //将express实例化为app
var http = require('http').createServer(app); //导入http设置别名为http

var fs = require('fs');

let sslOptions = {
    key: fs.readFileSync('/home/gsxy02/Github-workspaces/Yongqi-Liang/Real-time-voice-social-platform/Server/Cert/privkey.key'), //里面的文件替换成你生成的私钥
    cert: fs.readFileSync('/home/gsxy02/Github-workspaces/Yongqi-Liang/Real-time-voice-social-platform/Server/Cert/cacert.pem') //里面的文件替换成你生成的证书
};

const https = require('https').createServer(sslOptions, app);
var io = require('socket.io')(https); //导入socket.io设置别名为io
    
// 解析ip:port/static/到服务器static目录
app.use("/static", express.static('static/'));

// 在3000端口开启https服务
http.listen(3000,() => { //监听 3000 端口
    console.log('http listen on 3000'); 
});
// 在443端口开启https服务
https.listen(443,() => { //监听 443 端口
    console.log('https listen on 443'); 
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

