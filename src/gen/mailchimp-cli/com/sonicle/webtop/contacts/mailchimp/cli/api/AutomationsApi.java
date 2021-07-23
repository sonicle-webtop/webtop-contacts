package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.AutomationEmails;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AutomationWorkflow;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AutomationWorkflow1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AutomationWorkflowEmail;
import org.joda.time.DateTime;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2003;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2004;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;
import com.sonicle.webtop.contacts.mailchimp.cli.model.RemovedSubscribers;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberInAutomationQueue;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberInAutomationQueue1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberInAutomationQueue2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberRemovedFromAutomationWorkflow;
import com.sonicle.webtop.contacts.mailchimp.cli.model.UpdateInformationAboutASpecificWorkflowEmail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class AutomationsApi {
  private ApiClient apiClient;

  public AutomationsApi() {
    this(Configuration.getDefaultApiClient());
  }

  public AutomationsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Archive automation
   * Archiving will permanently end your automation and keep the report data. You’ll be able to replicate your archived automation, but you can’t restart it.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @throws ApiException if fails to make API call
   */
  public void archiveAutomations(String workflowId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling archiveAutomations");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/actions/archive"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()));

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
  /**
   * Delete workflow email
   * Removes an individual classic automation workflow email. Emails from certain workflow types, including the Abandoned Cart Email (abandonedCart) and Product Retargeting Email (abandonedBrowse) Workflows, cannot be deleted.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param workflowEmailId The unique id for the Automation workflow email. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteAutomationsIdEmailsId(String workflowId, String workflowEmailId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling deleteAutomationsIdEmailsId");
    }
    // verify the required parameter 'workflowEmailId' is set
    if (workflowEmailId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowEmailId' when calling deleteAutomationsIdEmailsId");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails/{workflow_email_id}"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "workflow_email_id" + "\\}", apiClient.escapeString(workflowEmailId.toString()));

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
   * List automations
   * Get a summary of an account&#x27;s classic automations.
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param beforeCreateTime Restrict the response to automations created before this time. Uses the ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param sinceCreateTime Restrict the response to automations created after this time. Uses the ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param beforeStartTime Restrict the response to automations started before this time. Uses the ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param sinceStartTime Restrict the response to automations started after this time. Uses the ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param status Restrict the results to automations with the specified status. (optional)
   * @return InlineResponse2003
   * @throws ApiException if fails to make API call
   */
  public InlineResponse2003 getAutomations(Integer count, Integer offset, List<String> fields, List<String> excludeFields, DateTime beforeCreateTime, DateTime sinceCreateTime, DateTime beforeStartTime, DateTime sinceStartTime, String status) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/automations";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_create_time", beforeCreateTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_create_time", sinceCreateTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_start_time", beforeStartTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_start_time", sinceStartTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "status", status));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<InlineResponse2003> localVarReturnType = new GenericType<InlineResponse2003>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get automation info
   * Get a summary of an individual classic automation workflow&#x27;s settings and content. The &#x60;trigger_settings&#x60; object returns information for the first email in the workflow.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return AutomationWorkflow1
   * @throws ApiException if fails to make API call
   */
  public AutomationWorkflow1 getAutomationsId(String workflowId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling getAutomationsId");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()));

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

    GenericType<AutomationWorkflow1> localVarReturnType = new GenericType<AutomationWorkflow1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List automated emails
   * Get a summary of the emails in a classic automation workflow.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @return AutomationEmails
   * @throws ApiException if fails to make API call
   */
  public AutomationEmails getAutomationsIdEmails(String workflowId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling getAutomationsIdEmails");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()));

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

    GenericType<AutomationEmails> localVarReturnType = new GenericType<AutomationEmails>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get workflow email info
   * Get information about an individual classic automation workflow email.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param workflowEmailId The unique id for the Automation workflow email. (required)
   * @return AutomationWorkflowEmail
   * @throws ApiException if fails to make API call
   */
  public AutomationWorkflowEmail getAutomationsIdEmailsId(String workflowId, String workflowEmailId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling getAutomationsIdEmailsId");
    }
    // verify the required parameter 'workflowEmailId' is set
    if (workflowEmailId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowEmailId' when calling getAutomationsIdEmailsId");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails/{workflow_email_id}"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "workflow_email_id" + "\\}", apiClient.escapeString(workflowEmailId.toString()));

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

    GenericType<AutomationWorkflowEmail> localVarReturnType = new GenericType<AutomationWorkflowEmail>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List automated email subscribers
   * Get information about a classic automation email queue.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param workflowEmailId The unique id for the Automation workflow email. (required)
   * @return InlineResponse2004
   * @throws ApiException if fails to make API call
   */
  public InlineResponse2004 getAutomationsIdEmailsIdQueue(String workflowId, String workflowEmailId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling getAutomationsIdEmailsIdQueue");
    }
    // verify the required parameter 'workflowEmailId' is set
    if (workflowEmailId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowEmailId' when calling getAutomationsIdEmailsIdQueue");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails/{workflow_email_id}/queue"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "workflow_email_id" + "\\}", apiClient.escapeString(workflowEmailId.toString()));

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

    GenericType<InlineResponse2004> localVarReturnType = new GenericType<InlineResponse2004>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get automated email subscriber
   * Get information about a specific subscriber in a classic automation email queue.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param workflowEmailId The unique id for the Automation workflow email. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @return SubscriberInAutomationQueue1
   * @throws ApiException if fails to make API call
   */
  public SubscriberInAutomationQueue1 getAutomationsIdEmailsIdQueueId(String workflowId, String workflowEmailId, String subscriberHash) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling getAutomationsIdEmailsIdQueueId");
    }
    // verify the required parameter 'workflowEmailId' is set
    if (workflowEmailId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowEmailId' when calling getAutomationsIdEmailsIdQueueId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getAutomationsIdEmailsIdQueueId");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails/{workflow_email_id}/queue/{subscriber_hash}"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "workflow_email_id" + "\\}", apiClient.escapeString(workflowEmailId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

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

    GenericType<SubscriberInAutomationQueue1> localVarReturnType = new GenericType<SubscriberInAutomationQueue1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List subscribers removed from workflow
   * Get information about subscribers who were removed from a classic automation workflow.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @return RemovedSubscribers
   * @throws ApiException if fails to make API call
   */
  public RemovedSubscribers getAutomationsIdRemovedSubscribers(String workflowId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling getAutomationsIdRemovedSubscribers");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/removed-subscribers"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()));

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

    GenericType<RemovedSubscribers> localVarReturnType = new GenericType<RemovedSubscribers>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get subscriber removed from workflow
   * Get information about a specific subscriber who was removed from a classic automation workflow.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @return SubscriberRemovedFromAutomationWorkflow
   * @throws ApiException if fails to make API call
   */
  public SubscriberRemovedFromAutomationWorkflow getAutomationsIdRemovedSubscribersId(String workflowId, String subscriberHash) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling getAutomationsIdRemovedSubscribersId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getAutomationsIdRemovedSubscribersId");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/removed-subscribers/{subscriber_hash}"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

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

    GenericType<SubscriberRemovedFromAutomationWorkflow> localVarReturnType = new GenericType<SubscriberRemovedFromAutomationWorkflow>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update workflow email
   * Update settings for a classic automation workflow email
   * @param body  (required)
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param workflowEmailId The unique id for the Automation workflow email. (required)
   * @return AutomationWorkflowEmail
   * @throws ApiException if fails to make API call
   */
  public AutomationWorkflowEmail patchAutomationEmailWorkflowId(UpdateInformationAboutASpecificWorkflowEmail body, String workflowId, String workflowEmailId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchAutomationEmailWorkflowId");
    }
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling patchAutomationEmailWorkflowId");
    }
    // verify the required parameter 'workflowEmailId' is set
    if (workflowEmailId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowEmailId' when calling patchAutomationEmailWorkflowId");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails/{workflow_email_id}"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "workflow_email_id" + "\\}", apiClient.escapeString(workflowEmailId.toString()));

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

    GenericType<AutomationWorkflowEmail> localVarReturnType = new GenericType<AutomationWorkflowEmail>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add automation
   * Create a new classic automation in your Mailchimp account.
   * @param body  (required)
   * @return AutomationWorkflow1
   * @throws ApiException if fails to make API call
   */
  public AutomationWorkflow1 postAutomations(AutomationWorkflow body) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postAutomations");
    }
    // create path and map variables
    String localVarPath = "/automations";

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

    GenericType<AutomationWorkflow1> localVarReturnType = new GenericType<AutomationWorkflow1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Pause automation emails
   * Pause all emails in a specific classic automation workflow.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @throws ApiException if fails to make API call
   */
  public void postAutomationsIdActionsPauseAllEmails(String workflowId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling postAutomationsIdActionsPauseAllEmails");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/actions/pause-all-emails"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()));

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
  /**
   * Start automation emails
   * Start all emails in a classic automation workflow.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @throws ApiException if fails to make API call
   */
  public void postAutomationsIdActionsStartAllEmails(String workflowId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling postAutomationsIdActionsStartAllEmails");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/actions/start-all-emails"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()));

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
  /**
   * Pause automated email
   * Pause an automated email.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param workflowEmailId The unique id for the Automation workflow email. (required)
   * @throws ApiException if fails to make API call
   */
  public void postAutomationsIdEmailsIdActionsPause(String workflowId, String workflowEmailId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling postAutomationsIdEmailsIdActionsPause");
    }
    // verify the required parameter 'workflowEmailId' is set
    if (workflowEmailId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowEmailId' when calling postAutomationsIdEmailsIdActionsPause");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails/{workflow_email_id}/actions/pause"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "workflow_email_id" + "\\}", apiClient.escapeString(workflowEmailId.toString()));

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
  /**
   * Start automated email
   * Start an automated email.
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param workflowEmailId The unique id for the Automation workflow email. (required)
   * @throws ApiException if fails to make API call
   */
  public void postAutomationsIdEmailsIdActionsStart(String workflowId, String workflowEmailId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling postAutomationsIdEmailsIdActionsStart");
    }
    // verify the required parameter 'workflowEmailId' is set
    if (workflowEmailId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowEmailId' when calling postAutomationsIdEmailsIdActionsStart");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails/{workflow_email_id}/actions/start"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "workflow_email_id" + "\\}", apiClient.escapeString(workflowEmailId.toString()));

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
  /**
   * Add subscriber to workflow email
   * Manually add a subscriber to a workflow, bypassing the default trigger settings. You can also use this endpoint to trigger a series of automated emails in an API 3.0 workflow type.
   * @param body  (required)
   * @param workflowId The unique id for the Automation workflow. (required)
   * @param workflowEmailId The unique id for the Automation workflow email. (required)
   * @return SubscriberInAutomationQueue1
   * @throws ApiException if fails to make API call
   */
  public SubscriberInAutomationQueue1 postAutomationsIdEmailsIdQueue(SubscriberInAutomationQueue body, String workflowId, String workflowEmailId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postAutomationsIdEmailsIdQueue");
    }
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling postAutomationsIdEmailsIdQueue");
    }
    // verify the required parameter 'workflowEmailId' is set
    if (workflowEmailId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowEmailId' when calling postAutomationsIdEmailsIdQueue");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/emails/{workflow_email_id}/queue"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()))
      .replaceAll("\\{" + "workflow_email_id" + "\\}", apiClient.escapeString(workflowEmailId.toString()));

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

    GenericType<SubscriberInAutomationQueue1> localVarReturnType = new GenericType<SubscriberInAutomationQueue1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Remove subscriber from workflow
   * Remove a subscriber from a specific classic automation workflow. You can remove a subscriber at any point in an automation workflow, regardless of how many emails they&#x27;ve been sent from that workflow. Once they&#x27;re removed, they can never be added back to the same workflow.
   * @param body  (required)
   * @param workflowId The unique id for the Automation workflow. (required)
   * @return SubscriberRemovedFromAutomationWorkflow
   * @throws ApiException if fails to make API call
   */
  public SubscriberRemovedFromAutomationWorkflow postAutomationsIdRemovedSubscribers(SubscriberInAutomationQueue2 body, String workflowId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postAutomationsIdRemovedSubscribers");
    }
    // verify the required parameter 'workflowId' is set
    if (workflowId == null) {
      throw new ApiException(400, "Missing the required parameter 'workflowId' when calling postAutomationsIdRemovedSubscribers");
    }
    // create path and map variables
    String localVarPath = "/automations/{workflow_id}/removed-subscribers"
      .replaceAll("\\{" + "workflow_id" + "\\}", apiClient.escapeString(workflowId.toString()));

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

    GenericType<SubscriberRemovedFromAutomationWorkflow> localVarReturnType = new GenericType<SubscriberRemovedFromAutomationWorkflow>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
