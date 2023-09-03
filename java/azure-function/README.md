## Prerequisites

test
Following software must be installed:

- JDK 17 or higher [Link](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Apache Maven 3.8.5 or higher [Link](https://maven.apache.org/download.cgi)
- Azure CLI 2.49.0 [Link](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli#install)
- Azure Function Core Tools [Link](https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=windows%2Cportal%2Cv2%2Cbash&pivots=programming-language-java#install-the-azure-functions-core-tools)

Check [Appendix: Check Requirements](#check-requirements) section on how to verify versions.

## DevOps Pipeline Setup

Follow the steps outlined [here](tutorial/devops-setup.md) to setup DevOps Pipeline.

## Run Project Manually

This project is intended to be managed through the Azure DevOps Pipelines. However, this section describes how to build and deploy it manually.

In order to support multiple environments, Azure details are not hardcoded into the `pom.xml`. Instead, they are passed as command line arguments when calling the Maven commands. Helper scripts are created to stored in the `bin` folder to simplify the deployment process. These scripts should be executed from the project root folder.

First update the `.env` file and with Azure specific details.

### Build the Project

To build the project:

```
./bin/build.sh
```

To do a clean build:

```
./bin/build.sh clean
```

### Run the Project

To run the project locally (via the Function Core Tool)

```
./bin/run.sh
```

To build and run:

```
./bin/run.sh "clean package"
```

### Deploy the Project

First login using Azure CLI

```
az login
```

Then deploy the project:

```
./bin/deploy.sh
```

To build and deploy:

```
./bin/deploy.sh "clean package"
```

## Appendix

### Check Requirements

To check the versions of the required softwares:

- JDK: `java -version`
- Maven: `mvn -version`
- Azure CLI: `az version`
- Azure Func Core Tool: `fucn --version`
