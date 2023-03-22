package com.codingronin.sandbox.aws.lambda;

import java.util.Map;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeAccountAttributesResponse;
import software.amazon.awssdk.services.ec2.model.InstanceInterruptionBehavior;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.RequestSpotInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RequestSpotLaunchSpecification;
import software.amazon.awssdk.services.ec2.model.SpotInstanceType;

public class HelloWorldLambdaHandler implements RequestHandler<Map<String, String>, String> {

  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public String handleRequest(Map<String, String> event, Context context) {

    LambdaLogger logger = context.getLogger();
    String response = "200 OK";
    // log execution details
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    // process event
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass().toString());

    launchEc2(logger);

    return response;
  }


  void launchEc2(LambdaLogger log) {

    log.log("Getting Ec2Cleint.");
    Region region = Region.US_EAST_1;

    Ec2Client ec2 = Ec2Client.builder().region(region).build();

    log.log("Using Ec2Cleint to make AWS API call.");
    DescribeAccountAttributesResponse resp = ec2.describeAccountAttributes();
    log.log("AccountAttributes: " + resp.accountAttributes());
  }


  RequestSpotInstancesRequest spotRequest(String instanceId, String keyPair, String userData) {

    RequestSpotLaunchSpecification.Builder specBuilder = RequestSpotLaunchSpecification.builder()//
        .instanceType(InstanceType.T2_MICRO)//
        .imageId(instanceId) //
        .keyName(keyPair);

    if (userData != null)
      specBuilder = specBuilder.userData(userData);


    return RequestSpotInstancesRequest.builder()//
        .type(SpotInstanceType.PERSISTENT)//
        .instanceInterruptionBehavior(InstanceInterruptionBehavior.STOP)//
        .launchSpecification(specBuilder.build())//
        .build();
  }


}
