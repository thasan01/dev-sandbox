package com.codingronin.sandbox.aws.ec2;

import static com.codingronin.sandbox.aws.ec2.EC2ClientUtil.createEC2Client;
import static com.codingronin.sandbox.aws.ec2.EC2ClientUtil.tagResources;
import java.util.Arrays;
import java.util.List;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Address;
import software.amazon.awssdk.services.ec2.model.AllocateAddressRequest;
import software.amazon.awssdk.services.ec2.model.AllocateAddressResponse;
import software.amazon.awssdk.services.ec2.model.AssociateAddressRequest;
import software.amazon.awssdk.services.ec2.model.DescribeAddressesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeAddressesResponse;
import software.amazon.awssdk.services.ec2.model.Filter;


public class ElasticIPApp {

  public static void main(String[] args) {

    Ec2Client ec2 = createEC2Client();

    ElasticIPApp app = new ElasticIPApp();
    String alloc = app.createElasticIpAddress(ec2);
    System.out.println("created new elastic ip=" + alloc);

    String[][] tags = {{"Name", "foobar"}};
    tagResources(ec2, Arrays.asList(alloc), tags);
    System.out.println("tagged ip address");


    // Address.builder().sdkFields().stream().map(SdkField::locationName).forEach(System.out::println);

  }

  String createElasticIpAddress(Ec2Client ec2) {
    AllocateAddressResponse response =
        ec2.allocateAddress(AllocateAddressRequest.builder().build());
    return response.allocationId();
  }

  /**
   * 
   * @param ec2
   * @param tagName
   * @param tagValue
   * @return
   */
  Address getElasticIpAddressByTag(Ec2Client ec2, String tagName, String tagValue) {

    Filter tagfilter = Filter.builder()//
        .name("tag:" + tagName)//
        .values(tagValue)//
        .build();

    DescribeAddressesRequest request = DescribeAddressesRequest.builder()//
        .filters(tagfilter)//
        .build();

    DescribeAddressesResponse response = ec2.describeAddresses(request);
    List<Address> attrs = response.addresses();
    return attrs != null && !attrs.isEmpty() ? attrs.get(0) : null;
  }

  /**
   * 
   * @param ec2
   * @param instanceId
   * @param addressId
   */
  void attachElasticIpToEc2Instance(Ec2Client ec2, String instanceId, String addressId) {
    ec2.associateAddress(AssociateAddressRequest.builder()//
        .instanceId(instanceId)//
        .allocationId(addressId)//
        .build());
  }


}
