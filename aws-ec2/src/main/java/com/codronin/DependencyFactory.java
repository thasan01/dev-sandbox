
package com.codronin;

import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.ec2.Ec2Client;

/**
 * The module containing all dependencies required by the {@link Handler}.
 */
public class DependencyFactory {

    private DependencyFactory() {}

    /**
     * @return an instance of Ec2Client
     */
    public static Ec2Client ec2Client() {
        return Ec2Client.builder()
                       .httpClientBuilder(ApacheHttpClient.builder())
                       .build();
    }
}
