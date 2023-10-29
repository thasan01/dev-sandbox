const express = require("express");

var app = express();

// set the view engine to ejs
app.set("view engine", "ejs");
app.engine("ejs", require("ejs").__express);

app.get("/", function (req, res) {
  res.render("page");
});

app.listen(5000);
