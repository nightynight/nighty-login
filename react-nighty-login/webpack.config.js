var webpack = require('webpack');
var path = require('path');
module.exports = {
    entry: "./src/index.js",
    output: {
        path: __dirname + "/build",//打包后的js文件存放的地方
        filename: "nighty.login.js"
    },
    module: {
        loaders: [
            {test:/\.css$/,loader:'style-loader!css-loader'},
            {
                test: /\.(js|jsx)$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
            }
        ]
    },
    plugins:[
    ],
};
