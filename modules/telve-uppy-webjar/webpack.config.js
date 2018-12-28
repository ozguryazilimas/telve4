const packageJSON = require('./package.json');
const path = require('path');

const buildPath = path.join(__dirname, 'target/classes/META-INF/resources/webjars', packageJSON.name, packageJSON.version);

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
  output: {
    path: buildPath,
    filename: 'uppy-bundle.js',
    libraryTarget: 'var',
    library: 'Uppy'
  }
}; 
