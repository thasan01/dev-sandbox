package com.codronin;

import software.amazon.awssdk.services.ec2.Ec2Client;


public class Handler {
    private final Ec2Client ec2Client;

    public Handler() {
        ec2Client = DependencyFactory.ec2Client();
    }

    public void sendRequest() {
        // TODO: invoking the api calls using ec2Client.
    }
}
