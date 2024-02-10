package com.codingronin.spring.webapp.scim.controller.v2;

import static com.bettercloud.scim2.common.utils.ApiConstants.RESOURCE_TYPES_ENDPOINT;
import static com.bettercloud.scim2.common.utils.ApiConstants.SCHEMAS_ENDPOINT;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * This Controller is a workaround, because the Schemas, ResourceTypes, and ServiceProviderConfig
 * endpoints are hardcoded to the servers base URL. This controller provides proxy endpoints for
 * those endpoints.
 * 
 *
 */
@RestController
@RequestMapping("/api/scim/v2")
@SuppressWarnings("unchecked")
public class ScimDefaultCotroller {

  @Value("http://localhost:${server.port}${server.servlet.context-path}")
  String baseUrl;

  @Autowired
  RestTemplate localClient;


  @GetMapping(SCHEMAS_ENDPOINT)
  @ResponseBody
  public Map<String, Object> getSchemas() {
    return localClient.getForEntity(baseUrl + SCHEMAS_ENDPOINT, Map.class).getBody();
  }

  @GetMapping(SCHEMAS_ENDPOINT + "/{id}")
  @ResponseBody
  public Map<String, Object> getSchema(@PathVariable(required = true) String id) {
    return localClient.getForEntity(baseUrl + SCHEMAS_ENDPOINT + "/" + id, Map.class).getBody();
  }

  @GetMapping(RESOURCE_TYPES_ENDPOINT)
  @ResponseBody
  public Map<String, Object> getResourceTypes() {
    return localClient.getForEntity(baseUrl + RESOURCE_TYPES_ENDPOINT, Map.class).getBody();
  }

  @GetMapping(RESOURCE_TYPES_ENDPOINT)
  @ResponseBody
  public Map<String, Object> getResourceType(@PathVariable(required = true) String id) {
    return localClient.getForEntity(baseUrl + RESOURCE_TYPES_ENDPOINT + "/" + id, Map.class)
        .getBody();
  }


}
