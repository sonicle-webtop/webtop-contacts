package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.BatchWebhook;
import com.sonicle.webtop.contacts.mailchimp.cli.model.BatchWebhook1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.BatchWebhook2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.BatchWebhooks;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class BatchWebhooksApi {
  private ApiClient apiClient;

  public BatchWebhooksApi() {
    this(Configuration.getDefaultApiClient());
  }

  public BatchWebhooksApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Delete batch webhook
   * Remove a batch webhook. Webhooks will no longer be sent to the given URL.
   * @param batchWebhookId The unique id for the batch webhook. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteBatchWebhookId(String batchWebhookId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'batchWebhookId' is set
    if (batchWebhookId == null) {
      throw new ApiException(400, "Missing the required parameter 'batchWebhookId' when calling deleteBatchWebhookId");
    }
    // create path and map variables
    String localVarPath = "/batch-webhooks/{batch_webhook_id}"
      .replaceAll("\\{" + "batch_webhook_id" + "\\}", apiClient.escapeString(batchWebhookId.toString()));

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
   * Get batch webhook info
   * Get information about a specific batch webhook.
   * @param batchWebhookId The unique id for the batch webhook. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return BatchWebhook1
   * @throws ApiException if fails to make API call
   */
  public BatchWebhook1 getBatchWebhook(String batchWebhookId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'batchWebhookId' is set
    if (batchWebhookId == null) {
      throw new ApiException(400, "Missing the required parameter 'batchWebhookId' when calling getBatchWebhook");
    }
    // create path and map variables
    String localVarPath = "/batch-webhooks/{batch_webhook_id}"
      .replaceAll("\\{" + "batch_webhook_id" + "\\}", apiClient.escapeString(batchWebhookId.toString()));

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

    GenericType<BatchWebhook1> localVarReturnType = new GenericType<BatchWebhook1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List batch webhooks
   * Get all webhooks that have been configured for batches.
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return BatchWebhooks
   * @throws ApiException if fails to make API call
   */
  public BatchWebhooks getBatchWebhooks(List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/batch-webhooks";

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

    GenericType<BatchWebhooks> localVarReturnType = new GenericType<BatchWebhooks>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update batch webhook
   * Update a webhook that will fire whenever any batch request completes processing.
   * @param body  (required)
   * @param batchWebhookId The unique id for the batch webhook. (required)
   * @return BatchWebhook1
   * @throws ApiException if fails to make API call
   */
  public BatchWebhook1 patchBatchWebhooks(BatchWebhook2 body, String batchWebhookId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchBatchWebhooks");
    }
    // verify the required parameter 'batchWebhookId' is set
    if (batchWebhookId == null) {
      throw new ApiException(400, "Missing the required parameter 'batchWebhookId' when calling patchBatchWebhooks");
    }
    // create path and map variables
    String localVarPath = "/batch-webhooks/{batch_webhook_id}"
      .replaceAll("\\{" + "batch_webhook_id" + "\\}", apiClient.escapeString(batchWebhookId.toString()));

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

    GenericType<BatchWebhook1> localVarReturnType = new GenericType<BatchWebhook1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add batch webhook
   * Configure a webhook that will fire whenever any batch request completes processing.
   * @param body  (required)
   * @return BatchWebhook1
   * @throws ApiException if fails to make API call
   */
  public BatchWebhook1 postBatchWebhooks(BatchWebhook body) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postBatchWebhooks");
    }
    // create path and map variables
    String localVarPath = "/batch-webhooks";

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

    GenericType<BatchWebhook1> localVarReturnType = new GenericType<BatchWebhook1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
