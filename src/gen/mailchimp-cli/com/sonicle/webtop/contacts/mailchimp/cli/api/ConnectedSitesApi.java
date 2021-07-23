package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.ConnectedSite;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ConnectedSite1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ConnectedSites;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class ConnectedSitesApi {
  private ApiClient apiClient;

  public ConnectedSitesApi() {
    this(Configuration.getDefaultApiClient());
  }

  public ConnectedSitesApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Delete connected site
   * Remove a connected site from your Mailchimp account.
   * @param connectedSiteId The unique identifier for the site. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteConnectedSitesId(String connectedSiteId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'connectedSiteId' is set
    if (connectedSiteId == null) {
      throw new ApiException(400, "Missing the required parameter 'connectedSiteId' when calling deleteConnectedSitesId");
    }
    // create path and map variables
    String localVarPath = "/connected-sites/{connected_site_id}"
      .replaceAll("\\{" + "connected_site_id" + "\\}", apiClient.escapeString(connectedSiteId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * List connected sites
   * Get all connected sites in an account.
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return ConnectedSites
   * @throws ApiException if fails to make API call
   */
  public ConnectedSites getConnectedSites(List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/connected-sites";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<ConnectedSites> localVarReturnType = new GenericType<ConnectedSites>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get connected site
   * Get information about a specific connected site.
   * @param connectedSiteId The unique identifier for the site. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return ConnectedSite1
   * @throws ApiException if fails to make API call
   */
  public ConnectedSite1 getConnectedSitesId(String connectedSiteId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'connectedSiteId' is set
    if (connectedSiteId == null) {
      throw new ApiException(400, "Missing the required parameter 'connectedSiteId' when calling getConnectedSitesId");
    }
    // create path and map variables
    String localVarPath = "/connected-sites/{connected_site_id}"
      .replaceAll("\\{" + "connected_site_id" + "\\}", apiClient.escapeString(connectedSiteId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<ConnectedSite1> localVarReturnType = new GenericType<ConnectedSite1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add connected site
   * Create a new Mailchimp connected site.
   * @param body  (required)
   * @return ConnectedSite1
   * @throws ApiException if fails to make API call
   */
  public ConnectedSite1 postConnectedSites(ConnectedSite body) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postConnectedSites");
    }
    // create path and map variables
    String localVarPath = "/connected-sites";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<ConnectedSite1> localVarReturnType = new GenericType<ConnectedSite1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Verify connected site script
   * Verify that the connected sites script has been installed, either via the script URL or fragment.
   * @param connectedSiteId The unique identifier for the site. (required)
   * @throws ApiException if fails to make API call
   */
  public void postConnectedSitesIdActionsVerifyScriptInstallation(String connectedSiteId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'connectedSiteId' is set
    if (connectedSiteId == null) {
      throw new ApiException(400, "Missing the required parameter 'connectedSiteId' when calling postConnectedSitesIdActionsVerifyScriptInstallation");
    }
    // create path and map variables
    String localVarPath = "/connected-sites/{connected_site_id}/actions/verify-script-installation"
      .replaceAll("\\{" + "connected_site_id" + "\\}", apiClient.escapeString(connectedSiteId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
}
