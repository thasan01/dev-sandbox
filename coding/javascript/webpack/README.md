# WebPack Project

This is a sample project to build UI and backend server projects using WebPack build tool.

To build the project:
`npm run build`

To run the server:
`npm run server`
Then go to http:// localhost:5000

docker run -it --rm -v "$(pwd -W)":/app --name dev-sandbox -e NODE_ENV=development -p 5000:5000 node:22-alpine sh
