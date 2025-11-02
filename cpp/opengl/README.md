
For Windows
```
docker build -t opengl-sandbox:windows-v0.1 -f .devops/Dockerfile.windows .
docker run -it --rm -v ./:C:/app --name opengl-sandbox opengl-sandbox:windows-v0.1
```

For Linux
```
docker build -t opengl-sandbox:linux-v0.1 -f .devops/Dockerfile.linux .
docker run -it --rm -v ./:/opt/sandbox --name opengl-sandbox opengl-sandbox:linux-v0.1 /bin/bash
``` 



In VSCode, install `Dev Containers` extension