import _ from "lodash";
import "../resources/css/style.css";
import Logo from "../resources/images/logo.png";

function component() {
  const element = document.createElement("div");

  element.innerHTML = _.join(["Hello", "webpack"], " ");
  element.classList.add("hello");

  // Add the image to our existing div.
  const img = new Image();
  img.src = Logo;
  element.appendChild(img);

  return element;
}

document.body.appendChild(component());
