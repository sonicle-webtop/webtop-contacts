package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2006;
import com.sonicle.webtop.contacts.mailchimp.cli.model.LandingPage;
import com.sonicle.webtop.contacts.mailchimp.cli.model.LandingPage1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.LandingPage2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.LandingPageContent;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class LandingPagesApi {
  private ApiClient apiClient;

  public LandingPagesApi() {
    this(Configuration.getDefaultApiClient());
  }

  public LandingPagesApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Delete landing page
   * Delete a landing page.
   * @param pageId The unique id for the page. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteLandingPageId(String pageId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'pageId' is set
    if (pageId == null) {
      throw new ApiException(400, "Missing the required parameter 'pageId' when calling deleteLandingPageId");
    }
    // create path and map variables
    String localVarPath = "/landing-pages/{page_id}"
      .replaceAll("\\{" + "page_id" + "\\}", apiClient.escapeString(pageId.toString()));

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
   * List landing pages
   * Get all landing pages.
   * @param sortDir Determines the order direction for sorted results. (optional)
   * @param sortField Returns files sorted by the specified field. (optional)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @return InlineResponse2006
   * @throws ApiException if fails to make API call
   */
  public InlineResponse2006 getAllLandingPages(String sortDir, String sortField, List<String> fields, List<String> excludeFields, Integer count) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/landing-pages";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_dir", sortDir));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_field", sortField));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<InlineResponse2006> localVarReturnType = new GenericType<InlineResponse2006>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get landing page info
   * Get information about a specific page.
   * @param pageId The unique id for the page. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return LandingPage1
   * @throws ApiException if fails to make API call
   */
  public LandingPage1 getLandingPageId(String pageId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'pageId' is set
    if (pageId == null) {
      throw new ApiException(400, "Missing the required parameter 'pageId' when calling getLandingPageId");
    }
    // create path and map variables
    String localVarPath = "/landing-pages/{page_id}"
      .replaceAll("\\{" + "page_id" + "\\}", apiClient.escapeString(pageId.toString()));

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

    GenericType<LandingPage1> localVarReturnType = new GenericType<LandingPage1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get landing page content
   * Get the the HTML for your landing page.
   * @param pageId The unique id for the page. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return LandingPageContent
   * @throws ApiException if fails to make API call
   */
  public LandingPageContent getLandingPageIdContent(String pageId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'pageId' is set
    if (pageId == null) {
      throw new ApiException(400, "Missing the required parameter 'pageId' when calling getLandingPageIdContent");
    }
    // create path and map variables
    String localVarPath = "/landing-pages/{page_id}/content"
      .replaceAll("\\{" + "page_id" + "\\}", apiClient.escapeString(pageId.toString()));

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

    GenericType<LandingPageContent> localVarReturnType = new GenericType<LandingPageContent>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update landing page
   * Update a landing page.
   * @param body  (required)
   * @param pageId The unique id for the page. (required)
   * @return LandingPage1
   * @throws ApiException if fails to make API call
   */
  public LandingPage1 patchLandingPageId(LandingPage2 body, String pageId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchLandingPageId");
    }
    // verify the required parameter 'pageId' is set
    if (pageId == null) {
      throw new ApiException(400, "Missing the required parameter 'pageId' when calling patchLandingPageId");
    }
    // create path and map variables
    String localVarPath = "/landing-pages/{page_id}"
      .replaceAll("\\{" + "page_id" + "\\}", apiClient.escapeString(pageId.toString()));

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

    GenericType<LandingPage1> localVarReturnType = new GenericType<LandingPage1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add landing page
   * Create a new Mailchimp landing page.
   * @param body  (required)
   * @param useDefaultList Will create the Landing Page using the account&#x27;s Default List instead of requiring a list_id. (optional)
   * @return LandingPage1
   * @throws ApiException if fails to make API call
   */
  public LandingPage1 postAllLandingPages(LandingPage body, Boolean useDefaultList) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postAllLandingPages");
    }
    // create path and map variables
    String localVarPath = "/landing-pages";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "use_default_list", useDefaultList));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<LandingPage1> localVarReturnType = new GenericType<LandingPage1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Publish landing page
   * Publish a landing page that is in draft, unpublished, or has been previously published and edited.
   * @param pageId The unique id for the page. (required)
   * @return LandingPage1
   * @throws ApiException if fails to make API call
   */
  public LandingPage1 postLandingPageIdActionsPublish(String pageId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'pageId' is set
    if (pageId == null) {
      throw new ApiException(400, "Missing the required parameter 'pageId' when calling postLandingPageIdActionsPublish");
    }
    // create path and map variables
    String localVarPath = "/landing-pages/{page_id}/actions/publish"
      .replaceAll("\\{" + "page_id" + "\\}", apiClient.escapeString(pageId.toString()));

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

    GenericType<LandingPage1> localVarReturnType = new GenericType<LandingPage1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Unpublish landing page
   * Unpublish a landing page that is in draft or has been published.
   * @param pageId The unique id for the page. (required)
   * @throws ApiException if fails to make API call
   */
  public void postLandingPageIdActionsUnpublish(String pageId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'pageId' is set
    if (pageId == null) {
      throw new ApiException(400, "Missing the required parameter 'pageId' when calling postLandingPageIdActionsUnpublish");
    }
    // create path and map variables
    String localVarPath = "/landing-pages/{page_id}/actions/unpublish"
      .replaceAll("\\{" + "page_id" + "\\}", apiClient.escapeString(pageId.toString()));

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
