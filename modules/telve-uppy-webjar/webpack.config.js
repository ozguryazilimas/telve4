const packageJSON = require('./package.json');
const path = require('path');

const buildPath = path.join(__dirname, 'target/classes/META-INF/resources/webjars', packageJSON.name, packageJSON.version);

var webpack = require("webpack");

module.exports = {
    mode: 'production',
    entry: './src/main/jsapp/index.js',
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            }
        ]
  },
  plugins: [
    new webpack.ProvidePlugin({
        Promise: ['es6-promise', 'Promise']
    })
  ],
  output: {
    path: buildPath,
    filename: 'uppy-bundle.js',
    libraryTarget: 'var',
    library: 'Uppy'
  }
}; 
