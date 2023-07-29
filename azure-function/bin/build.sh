source ${azFnEnv}.env
mvn clean package -Dazure.functions.appName=${azAppName} -Dazure.functions.resourceGroup=${azResourceGroup} -Dazure.functions.appServicePlanName=${azAppServicePlanName} -Dazure.functions.region=${azRegion}
