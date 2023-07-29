
## Prerequisites
Following software must be installed:
 - JDK 17 or higher [Link](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
 - Apache Maven  3.8.5 or higher [Link]()
 - Azure CLI 2.49.0 [Link](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli#install)
 - Azure Function Core Tools [Link](https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=windows%2Cportal%2Cv2%2Cbash&pivots=programming-language-java#install-the-azure-functions-core-tools)

Check [Appendix: Check Requirements](#check-requirements) section on how to verify versions.

## Run Project Manually


To run the project locally, run the following command:
`mvn clean package azure-functions:run`


## Deploy Project
First login to Azure CLI:

`az login`

Then run:

`mvn azure-functions:deploy`

## Appendix

### Check Requirements

To check the versions of the required softwares:
 - JDK: `java -version`
 - Maven: `mvn -version`
 - Azure CLI: `az version`
 - Azure Func Core Tool: `fucn --version`
