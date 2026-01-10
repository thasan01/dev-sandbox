source .env
mvn ${1} azure-functions:run -Dazure.functions.appName=${azAppName} -Dazure.functions.resourceGroup=${azResourceGroup} -Dazure.functions.appServicePlanName=${azAppServicePlanName} -Dazure.functions.region=${azRegion}
