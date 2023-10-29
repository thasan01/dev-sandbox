const path = require("path");
const CopyPlugin = require("copy-webpack-plugin");

module.exports = [
  {
    name: "server",
    entry: "./src/server.js",
    target: "node",
    output: {
      path: path.resolve(__dirname, "build"),
      filename: "server.js",
    },

    plugins: [
      new CopyPlugin({
        patterns: [{ from: "views", to: "views" }],
      }),
    ],
  },

  {
    name: "client",
    entry: "./src/index.js",
    output: {
      filename: "client.js",
      path: path.resolve(__dirname, "build"),
    },
    module: {
      rules: [
        {
          test: /\.css$/i,
          use: ["style-loader", "css-loader"],
        },
        {
          test: /\.(png|svg|jpg|jpeg|gif)$/i,
          type: "asset/resource",
        },
      ],
    },
  },
];
