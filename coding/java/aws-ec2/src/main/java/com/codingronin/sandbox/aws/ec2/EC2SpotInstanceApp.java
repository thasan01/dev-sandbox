package com.codingronin.sandbox.aws.ec2;

import static com.codingronin.sandbox.aws.ec2.EC2ClientUtil.createEC2Client;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CancelSpotInstanceRequestsRequest;
import software.amazon.awssdk.services.ec2.model.CancelSpotInstanceRequestsResponse;
import software.amazon.awssdk.services.ec2.model.CancelledSpotInstanceRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstanceStatusRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstanceStatusResponse;
import software.amazon.awssdk.services.ec2.model.DescribeSpotInstanceRequestsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeSpotInstanceRequestsResponse;
import software.amazon.awssdk.services.ec2.model.InstanceInterruptionBehavior;
import software.amazon.awssdk.services.ec2.model.InstanceStatus;
import software.amazon.awssdk.services.ec2.model.InstanceStatusSummary;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.RequestSpotInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RequestSpotInstancesResponse;
import software.amazon.awssdk.services.ec2.model.RequestSpotLaunchSpecification;
import software.amazon.awssdk.services.ec2.model.SpotInstanceRequest;
import software.amazon.awssdk.services.ec2.model.SpotInstanceType;
import software.amazon.awssdk.services.ec2.model.SummaryStatus;
import software.amazon.awssdk.services.ec2.model.TerminateInstancesRequest;

public class EC2SpotInstanceApp {

  static Logger log = LoggerFactory.getLogger(EC2SpotInstanceApp.class);
  static final int WAIT_TIME = 10000;
  Ec2Client ec2;

  public static void main(String[] args) throws InterruptedException {

    try (Ec2Client ec2 = createEC2Client()) {
      long startTime = System.currentTimeMillis();
      EC2SpotInstanceApp app = new EC2SpotInstanceApp(ec2);
      app.run();
      long endTime = System.currentTimeMillis();
      log.info("Total time={}", (endTime - startTime));
    }
  }

  EC2SpotInstanceApp(Ec2Client ec2) {
    this.ec2 = ec2;
  }

  void run() throws InterruptedException {

    String imageId = "ami-0557a15b87f6559cf";
    String keyName = "KP-Prod-CodingRoninWeb";
    String userData = null;
    List<String> securityGroups = Arrays.asList("launch-wizard-1");

    RequestSpotInstancesRequest spotRequest =
        spotRequest(imageId, keyName, userData, securityGroups);

    RequestSpotInstancesResponse spotResponse = ec2.requestSpotInstances(spotRequest);

    Collection<String> instanceIds = waitForSpotInstances(spotResponse, 1);

    waitForActiveInstances(instanceIds);

    List<String> spotRequestIds = spotResponse.spotInstanceRequests().stream()
        .map(SpotInstanceRequest::spotInstanceRequestId).collect(Collectors.toList());
    cancelSpotRequest(spotRequestIds);
    terminateInstance(instanceIds);
  }

  /**
   * Create a Spot Instance Request object based on the input parameters.
   * 
   * @param imageId
   * @param keyName
   * @param userData
   * @param securityGroups
   * @return
   */
  RequestSpotInstancesRequest spotRequest(String imageId, String keyName, String userData,
      List<String> securityGroups) {

    RequestSpotLaunchSpecification.Builder specBuilder = RequestSpotLaunchSpecification.builder()//
        .securityGroups(securityGroups)//
        .instanceType(InstanceType.T2_MICRO)//
        .imageId(imageId) //
        .keyName(keyName);

    if (userData != null)
      specBuilder = specBuilder.userData(userData);

    return RequestSpotInstancesRequest.builder()//
        .instanceCount(1)//
        .type(SpotInstanceType.PERSISTENT)//
        .instanceInterruptionBehavior(InstanceInterruptionBehavior.STOP)//
        .launchSpecification(specBuilder.build())//
        .build();
  }

  /**
   * 
   * @param spotResponse
   * @param instanceCount
   * @return
   * @throws InterruptedException
   */
  Collection<String> waitForSpotInstances(RequestSpotInstancesResponse spotResponse,
      int instanceCount) throws InterruptedException {

    Set<String> createdIds = new HashSet<>();

    List<String> spotInstRequestIds = spotResponse.spotInstanceRequests().stream()
        .map(SpotInstanceRequest::spotInstanceRequestId).collect(Collectors.toList());

    while (createdIds.size() < instanceCount) {
      log.info("Checking for SpotInstanceRequest status.");

      DescribeSpotInstanceRequestsRequest descRequest =
          DescribeSpotInstanceRequestsRequest.builder()//
              .spotInstanceRequestIds(spotInstRequestIds)//
              .build();

      DescribeSpotInstanceRequestsResponse descResponse =
          ec2.describeSpotInstanceRequests(descRequest);

      log.info("Checking status of each instance from response.");
      for (SpotInstanceRequest spotRequest : descResponse.spotInstanceRequests()) {
        String newInstanceId = spotRequest.instanceId();
        if (newInstanceId != null && !createdIds.contains(newInstanceId)) {
          createdIds.add(newInstanceId);
          log.info("Got new instanceId={}", newInstanceId);
        }
      }

      Thread.sleep(WAIT_TIME);
    }
    return createdIds;
  }

  /**
   * 
   * @param instanceIds
   * @throws InterruptedException
   */
  void waitForActiveInstances(Collection<String> instanceIds) throws InterruptedException {

    int instanceCount = instanceIds.size();
    Map<String, SummaryStatus> activeInstances = new HashMap<>();

    DescribeInstanceStatusRequest statusRequest = DescribeInstanceStatusRequest.builder()//
        .instanceIds(instanceIds)//
        .build();

    while (activeInstances.size() < instanceCount) {
      DescribeInstanceStatusResponse statusResponse = ec2.describeInstanceStatus(statusRequest);
      for (InstanceStatus instanceStatus : statusResponse.instanceStatuses()) {

        InstanceStatusSummary status = instanceStatus.instanceStatus();
        String instId = instanceStatus.instanceId();
        SummaryStatus instStatus = status.status();
        log.info("instance={}, status={}", instId, instStatus);

        if (!SummaryStatus.INITIALIZING.equals(status.status())) {
          activeInstances.put(instanceStatus.instanceId(), status.status());
        }
      }
      Thread.sleep(WAIT_TIME);
    }
    log.info("active Instance status={}", activeInstances);
  }

  /**
   * 
   * @param spotInstanceRequestIds
   */
  void cancelSpotRequest(Collection<String> spotInstanceRequestIds) {
    CancelSpotInstanceRequestsRequest cancelRequest = CancelSpotInstanceRequestsRequest.builder()
        .spotInstanceRequestIds(spotInstanceRequestIds).build();

    log.info("Canceling spotInstanceRequestIds={}", spotInstanceRequestIds);
    CancelSpotInstanceRequestsResponse response = ec2.cancelSpotInstanceRequests(cancelRequest);

    if (log.isInfoEnabled()) {
      log.info("Processing CancelSpotInstanceRequestsResponse.");
      for (CancelledSpotInstanceRequest resp : response.cancelledSpotInstanceRequests()) {
        log.info("response instance={}, state={}", resp.spotInstanceRequestId(),
            resp.stateAsString());
      }
    }

    log.info("Cancelled spotInstanceRequestIds={}", spotInstanceRequestIds);
  }

  /**
   * 
   * @param instanceIds
   */
  void terminateInstance(Collection<String> instanceIds) {
    log.info("Terminating EC2 Instances = {}", instanceIds);
    ec2.terminateInstances(TerminateInstancesRequest.builder().instanceIds(instanceIds).build());
    log.info("Terminated EC2 Instances = {}", instanceIds);
  }

}
