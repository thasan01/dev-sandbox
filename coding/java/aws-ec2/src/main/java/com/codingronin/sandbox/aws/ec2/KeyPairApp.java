package com.codingronin.sandbox.aws.ec2;

import static com.codingronin.sandbox.aws.ec2.EC2ClientUtil.createEC2Client;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateKeyPairRequest;
import software.amazon.awssdk.services.ec2.model.CreateKeyPairResponse;
import software.amazon.awssdk.services.ec2.model.DeleteKeyPairRequest;

public class KeyPairApp {

  public static void main(String[] args) throws FileNotFoundException {
    KeyPairApp app = new KeyPairApp();

    try (Ec2Client ec2 = createEC2Client()) {
      app.createKeyPair(ec2, "testkey", "keyfile.pem");
      app.deleteKeyPair(ec2, "testkey", null);
    }
  }

  /**
   * Creates a KeyPair, and saves it to a file.
   * 
   * @param ec2
   * @param keyName
   * @param fileName
   * @throws FileNotFoundException
   */
  void createKeyPair(Ec2Client ec2, String keyName, String fileName) throws FileNotFoundException {
    CreateKeyPairRequest request = CreateKeyPairRequest.builder()//
        .keyName(keyName)//
        .build();

    CreateKeyPairResponse response = ec2.createKeyPair(request);

    if (fileName != null) {
      try (PrintWriter writer = new PrintWriter(fileName)) {
        writer.println(response.keyMaterial());
      }
    }

  }

  /**
   * Deletes a KeyPair based on name or id.
   * 
   * @param ec2
   * @param pairName
   * @param pairId
   */
  void deleteKeyPair(Ec2Client ec2, String pairName, String pairId) {
    DeleteKeyPairRequest request = DeleteKeyPairRequest.builder()//
        .keyName(pairName)//
        .keyPairId(pairId)//
        .build();

    ec2.deleteKeyPair(request);
  }

}
