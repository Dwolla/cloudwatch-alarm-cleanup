require('webpack');

module.exports = require('./scalajs.webpack.config');

module.exports.target = 'node';
module.exports.output.library = "[name]";
module.exports.output.libraryTarget = 'umd';
