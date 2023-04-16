package com.codronin.sandbox.aws.ssm;

import static software.amazon.awssdk.services.ssm.model.CommandInvocationStatus.IN_PROGRESS;
import static software.amazon.awssdk.services.ssm.model.CommandInvocationStatus.PENDING;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.CommandInvocation;
import software.amazon.awssdk.services.ssm.model.CommandInvocationStatus;
import software.amazon.awssdk.services.ssm.model.ListCommandInvocationsRequest;
import software.amazon.awssdk.services.ssm.model.SendCommandRequest;
import software.amazon.awssdk.services.ssm.model.SendCommandResponse;
import software.amazon.awssdk.services.ssm.model.Target;

/**
 * 
 * https://stackoverflow.com/a/65986421
 *
 */
public class SSMRunCommand {

  static Logger log = LoggerFactory.getLogger(SSMRunCommand.class);

  /**
   * 
   * @param args args[0] - comma separated vales of EC2 instance IDs
   */
  public static void main(String[] args) {

    SsmClient ssmClient = SsmClient.builder().build();

    List<String> instanceIds = Arrays.asList(args[0].split(","));
    List<String> shellCommands = Arrays.asList("whoami");

    log.info("Sending Command using AWS SSM for instances: {}", instanceIds);
    SendCommandResponse response = sendCommand(ssmClient, instanceIds, shellCommands);
    waitForResponse(ssmClient, response);
  }

  static SendCommandResponse sendCommand(SsmClient ssm, List<String> instanceIds,
      List<String> commands) {
    Target target = Target.builder().key("InstanceIds").values(instanceIds).build();

    Map<String, List<String>> params = new HashMap<>();
    params.put("commands", commands);

    SendCommandRequest request = SendCommandRequest.builder()//
        .targets(target)//
        .parameters(params)//
        .build();
    return ssm.sendCommand(request);
  }

  static void waitForResponse(SsmClient ssm, SendCommandResponse cmdResponse) {
    String commandId = cmdResponse.command().commandId();

    CommandInvocationStatus status;
    do {
      log.info("Checking status of command response: {}", commandId);
      ListCommandInvocationsRequest statusRequest = ListCommandInvocationsRequest.builder()//
          .commandId(commandId)//
          .build();

      CommandInvocation invocation = ssm.listCommandInvocations(statusRequest)//
          .commandInvocations().get(0);

      status = invocation.status();
      log.info("Recieved command response status: {}", status);
    } while (PENDING.equals(status) || IN_PROGRESS.equals(status));

    log.info("Final Command Status {}", status);
  }

}
