
```
docker build -t opengl-sandbox:linux-v0.1 -f .devops/Dockerfile.linux .
docker run -it --rm -v ./:/opt/sandbox --name opengl-sandbox opengl-sandbox:linux-v0.1 /bin/bash
``` 



In VSCode, install `Dev Containers` extension