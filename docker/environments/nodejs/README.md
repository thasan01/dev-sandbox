## Run the container
Pull the node image from dockerhub
```
docker pull node:20-bookworm-slim
```

Go to the directory with the source code and run:
```
 docker run -it --rm -v "$(pwd):/app" -w /app node:20-bookworm-slim bash
```
