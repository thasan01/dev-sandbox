package com.codingronin.sandbox.aws.ec2;

import java.util.ArrayList;
import java.util.List;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.Tag;

public class EC2ClientUtil {

  private EC2ClientUtil() {}

  public static Ec2Client createEC2Client() {
    return Ec2Client.builder().build();
  }


  public static void tagResources(Ec2Client ec2, List<String> resourceIds, String[][] tags) {
    List<Tag> tagList = new ArrayList<>();
    for (int i = 0; i < tags.length; i++)
      tagList.add(Tag.builder().key(tags[i][0]).value(tags[i][1]).build());

    ec2.createTags(CreateTagsRequest.builder().tags(tagList).resources(resourceIds).build());
  }

}
