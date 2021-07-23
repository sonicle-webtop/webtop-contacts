package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;
import com.sonicle.webtop.contacts.mailchimp.cli.model.VerifiedDomains;
import com.sonicle.webtop.contacts.mailchimp.cli.model.VerifiedDomains1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.VerifiedDomains2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.VerifyADomainForSending_;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class VerifiedDomainsApi {
  private ApiClient apiClient;

  public VerifiedDomainsApi() {
    this(Configuration.getDefaultApiClient());
  }

  public VerifiedDomainsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Add domain to account
   * Add a domain to the account.
   * @param body  (required)
   * @return VerifiedDomains
   * @throws ApiException if fails to make API call
   */
  public VerifiedDomains createVerifiedDomain(VerifiedDomains2 body) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createVerifiedDomain");
    }
    // create path and map variables
    String localVarPath = "/verified-domains";

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

    GenericType<VerifiedDomains> localVarReturnType = new GenericType<VerifiedDomains>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Delete domain
   * Delete a verified domain from the account.
   * @param domainName The domain name. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteVerifiedDomain(String domainName) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'domainName' is set
    if (domainName == null) {
      throw new ApiException(400, "Missing the required parameter 'domainName' when calling deleteVerifiedDomain");
    }
    // create path and map variables
    String localVarPath = "/verified-domains/{domain_name}"
      .replaceAll("\\{" + "domain_name" + "\\}", apiClient.escapeString(domainName.toString()));

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
   * Get domain info
   * Get the details for a single domain on the account.
   * @param domainName The domain name. (required)
   * @return VerifiedDomains
   * @throws ApiException if fails to make API call
   */
  public VerifiedDomains getVerifiedDomain(String domainName) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'domainName' is set
    if (domainName == null) {
      throw new ApiException(400, "Missing the required parameter 'domainName' when calling getVerifiedDomain");
    }
    // create path and map variables
    String localVarPath = "/verified-domains/{domain_name}"
      .replaceAll("\\{" + "domain_name" + "\\}", apiClient.escapeString(domainName.toString()));

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

    GenericType<VerifiedDomains> localVarReturnType = new GenericType<VerifiedDomains>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List sending domains
   * Get all of the sending domains on the account.
   * @return VerifiedDomains1
   * @throws ApiException if fails to make API call
   */
  public VerifiedDomains1 getVerifiedDomains() throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/verified-domains";

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

    GenericType<VerifiedDomains1> localVarReturnType = new GenericType<VerifiedDomains1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Verify domain
   * Verify a domain for sending.
   * @param body  (required)
   * @param domainName The domain name. (required)
   * @return VerifiedDomains
   * @throws ApiException if fails to make API call
   */
  public VerifiedDomains verifyDomain(VerifyADomainForSending_ body, String domainName) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling verifyDomain");
    }
    // verify the required parameter 'domainName' is set
    if (domainName == null) {
      throw new ApiException(400, "Missing the required parameter 'domainName' when calling verifyDomain");
    }
    // create path and map variables
    String localVarPath = "/verified-domains/{domain_name}/actions/verify"
      .replaceAll("\\{" + "domain_name" + "\\}", apiClient.escapeString(domainName.toString()));

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

    GenericType<VerifiedDomains> localVarReturnType = new GenericType<VerifiedDomains>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
