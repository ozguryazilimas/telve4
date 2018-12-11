var packageJSON = require('./package.json');
var path = require('path');
var webpack = require('webpack');

const PATHS = {
  build: path.join(__dirname, 'target', 'classes', 'META-INF', 'resources', 'webjars', packageJSON.name, packageJSON.version)
};


module.exports = {
    entry: './src/main/jsapp/index.js',
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            }
        ]
  },
  output: {
    path: PATHS.build,
    filename: 'uppy-bundle.js',
    libraryTarget: 'var',
    library: 'Uppy'
  }
}; 
