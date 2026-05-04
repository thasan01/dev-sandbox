This is a minimal nodejs container running the gemini-cli tool.

## Build the container
Run the following command from the same directory
```
docker build -t gemini-cli .
```


## Run the container

In order to use it first get an API Key from [Google AI Studios](https://aistudio.google.com/api-keys)

Go to root location or the code repository from where you want to run the agent. Create an `.env` file and store the API key.

```
GEMINI_API_KEY=<GEMINI_API_KEY>
```

Run the following command to start the container:
```
docker run -it --rm --name vibecode  --env-file .env -w /app -v "$PWD":/app gemini-cli --skip-trust
```

