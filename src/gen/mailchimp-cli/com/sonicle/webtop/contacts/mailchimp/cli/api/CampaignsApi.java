package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.ActionsScheduleBody;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ActionsTestBody;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Campaign;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Campaign1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Campaign2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Campaign3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignContent;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignContent1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignFeedback;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignFeedback1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignFeedback2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignReports;
import org.joda.time.DateTime;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2005;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SendChecklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class CampaignsApi {
  private ApiClient apiClient;

  public CampaignsApi() {
    this(Configuration.getDefaultApiClient());
  }

  public CampaignsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Delete campaign
   * Remove a campaign from your Mailchimp account.
   * @param campaignId The unique id for the campaign. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteCampaignsId(String campaignId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling deleteCampaignsId");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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
   * Delete campaign feedback message
   * Remove a specific feedback message for a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param feedbackId The unique id for the feedback message. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteCampaignsIdFeedbackId(String campaignId, String feedbackId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling deleteCampaignsIdFeedbackId");
    }
    // verify the required parameter 'feedbackId' is set
    if (feedbackId == null) {
      throw new ApiException(400, "Missing the required parameter 'feedbackId' when calling deleteCampaignsIdFeedbackId");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/feedback/{feedback_id}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "feedback_id" + "\\}", apiClient.escapeString(feedbackId.toString()));

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
   * List campaigns
   * Get all campaigns in an account.
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param type The campaign type. (optional)
   * @param status The status of the campaign. (optional)
   * @param beforeSendTime Restrict the response to campaigns sent before the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param sinceSendTime Restrict the response to campaigns sent after the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param beforeCreateTime Restrict the response to campaigns created before the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param sinceCreateTime Restrict the response to campaigns created after the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param listId The unique id for the list. (optional)
   * @param folderId The unique folder id. (optional)
   * @param memberId Retrieve campaigns sent to a particular list member. Member ID is The MD5 hash of the lowercase version of the list member’s email address. (optional)
   * @param sortField Returns files sorted by the specified field. (optional)
   * @param sortDir Determines the order direction for sorted results. (optional)
   * @return InlineResponse2005
   * @throws ApiException if fails to make API call
   */
  public InlineResponse2005 getCampaigns(List<String> fields, List<String> excludeFields, Integer count, Integer offset, String type, String status, DateTime beforeSendTime, DateTime sinceSendTime, DateTime beforeCreateTime, DateTime sinceCreateTime, String listId, String folderId, String memberId, String sortField, String sortDir) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/campaigns";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "type", type));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "status", status));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_send_time", beforeSendTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_send_time", sinceSendTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_create_time", beforeCreateTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_create_time", sinceCreateTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "list_id", listId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "folder_id", folderId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "member_id", memberId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_field", sortField));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_dir", sortDir));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<InlineResponse2005> localVarReturnType = new GenericType<InlineResponse2005>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get campaign info
   * Get information about a specific campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return Campaign1
   * @throws ApiException if fails to make API call
   */
  public Campaign1 getCampaignsId(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getCampaignsId");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<Campaign1> localVarReturnType = new GenericType<Campaign1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get campaign content
   * Get the the HTML and plain-text content for a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return CampaignContent
   * @throws ApiException if fails to make API call
   */
  public CampaignContent getCampaignsIdContent(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getCampaignsIdContent");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/content"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<CampaignContent> localVarReturnType = new GenericType<CampaignContent>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List campaign feedback
   * Get team feedback while you&#x27;re working together on a Mailchimp campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return CampaignReports
   * @throws ApiException if fails to make API call
   */
  public CampaignReports getCampaignsIdFeedback(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getCampaignsIdFeedback");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/feedback"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<CampaignReports> localVarReturnType = new GenericType<CampaignReports>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get campaign feedback message
   * Get a specific feedback message from a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param feedbackId The unique id for the feedback message. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return CampaignFeedback1
   * @throws ApiException if fails to make API call
   */
  public CampaignFeedback1 getCampaignsIdFeedbackId(String campaignId, String feedbackId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getCampaignsIdFeedbackId");
    }
    // verify the required parameter 'feedbackId' is set
    if (feedbackId == null) {
      throw new ApiException(400, "Missing the required parameter 'feedbackId' when calling getCampaignsIdFeedbackId");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/feedback/{feedback_id}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "feedback_id" + "\\}", apiClient.escapeString(feedbackId.toString()));

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

    GenericType<CampaignFeedback1> localVarReturnType = new GenericType<CampaignFeedback1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get campaign send checklist
   * Review the send checklist for a campaign, and resolve any issues before sending.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return SendChecklist
   * @throws ApiException if fails to make API call
   */
  public SendChecklist getCampaignsIdSendChecklist(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getCampaignsIdSendChecklist");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/send-checklist"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<SendChecklist> localVarReturnType = new GenericType<SendChecklist>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update campaign settings
   * Update some or all of the settings for a specific campaign.
   * @param body  (required)
   * @param campaignId The unique id for the campaign. (required)
   * @return Campaign1
   * @throws ApiException if fails to make API call
   */
  public Campaign1 patchCampaignsId(Campaign2 body, String campaignId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchCampaignsId");
    }
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling patchCampaignsId");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<Campaign1> localVarReturnType = new GenericType<Campaign1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update campaign feedback message
   * Update a specific feedback message for a campaign.
   * @param body  (required)
   * @param campaignId The unique id for the campaign. (required)
   * @param feedbackId The unique id for the feedback message. (required)
   * @return CampaignFeedback1
   * @throws ApiException if fails to make API call
   */
  public CampaignFeedback1 patchCampaignsIdFeedbackId(CampaignFeedback2 body, String campaignId, String feedbackId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchCampaignsIdFeedbackId");
    }
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling patchCampaignsIdFeedbackId");
    }
    // verify the required parameter 'feedbackId' is set
    if (feedbackId == null) {
      throw new ApiException(400, "Missing the required parameter 'feedbackId' when calling patchCampaignsIdFeedbackId");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/feedback/{feedback_id}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "feedback_id" + "\\}", apiClient.escapeString(feedbackId.toString()));

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

    GenericType<CampaignFeedback1> localVarReturnType = new GenericType<CampaignFeedback1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add campaign
   * Create a new Mailchimp campaign.
   * @param body  (required)
   * @return Campaign1
   * @throws ApiException if fails to make API call
   */
  public Campaign1 postCampaigns(Campaign body) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postCampaigns");
    }
    // create path and map variables
    String localVarPath = "/campaigns";

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

    GenericType<Campaign1> localVarReturnType = new GenericType<Campaign1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Cancel campaign
   * Cancel a Regular or Plain-Text Campaign after you send, before all of your recipients receive it. This feature is included with Mailchimp Pro.
   * @param campaignId The unique id for the campaign. (required)
   * @throws ApiException if fails to make API call
   */
  public void postCampaignsIdActionsCancelSend(String campaignId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsCancelSend");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/cancel-send"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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
   * Resend campaign
   * Creates a Resend to Non-Openers version of this campaign. We will also check if this campaign meets the criteria for Resend to Non-Openers campaigns.
   * @param campaignId The unique id for the campaign. (required)
   * @return Campaign3
   * @throws ApiException if fails to make API call
   */
  public Campaign3 postCampaignsIdActionsCreateResend(String campaignId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsCreateResend");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/create-resend"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<Campaign3> localVarReturnType = new GenericType<Campaign3>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Pause rss campaign
   * Pause an RSS-Driven campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @throws ApiException if fails to make API call
   */
  public void postCampaignsIdActionsPause(String campaignId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsPause");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/pause"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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
   * Replicate campaign
   * Replicate a campaign in saved or send status.
   * @param campaignId The unique id for the campaign. (required)
   * @return Campaign3
   * @throws ApiException if fails to make API call
   */
  public Campaign3 postCampaignsIdActionsReplicate(String campaignId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsReplicate");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/replicate"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<Campaign3> localVarReturnType = new GenericType<Campaign3>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Resume rss campaign
   * Resume an RSS-Driven campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @throws ApiException if fails to make API call
   */
  public void postCampaignsIdActionsResume(String campaignId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsResume");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/resume"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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
   * Schedule campaign
   * Schedule a campaign for delivery. If you&#x27;re using Multivariate Campaigns to test send times or sending RSS Campaigns, use the send action instead.
   * @param body  (required)
   * @param campaignId The unique id for the campaign. (required)
   * @throws ApiException if fails to make API call
   */
  public void postCampaignsIdActionsSchedule(ActionsScheduleBody body, String campaignId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postCampaignsIdActionsSchedule");
    }
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsSchedule");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/schedule"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Send campaign
   * Send a Mailchimp campaign. For RSS Campaigns, the campaign will send according to its schedule. All other campaigns will send immediately.
   * @param campaignId The unique id for the campaign. (required)
   * @throws ApiException if fails to make API call
   */
  public void postCampaignsIdActionsSend(String campaignId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsSend");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/send"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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
   * Send test email
   * Send a test email.
   * @param body  (required)
   * @param campaignId The unique id for the campaign. (required)
   * @throws ApiException if fails to make API call
   */
  public void postCampaignsIdActionsTest(ActionsTestBody body, String campaignId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postCampaignsIdActionsTest");
    }
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsTest");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/test"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Unschedule campaign
   * Unschedule a scheduled campaign that hasn&#x27;t started sending.
   * @param campaignId The unique id for the campaign. (required)
   * @throws ApiException if fails to make API call
   */
  public void postCampaignsIdActionsUnschedule(String campaignId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdActionsUnschedule");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/actions/unschedule"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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
   * Add campaign feedback
   * Add feedback on a specific campaign.
   * @param body  (required)
   * @param campaignId The unique id for the campaign. (required)
   * @return CampaignFeedback1
   * @throws ApiException if fails to make API call
   */
  public CampaignFeedback1 postCampaignsIdFeedback(CampaignFeedback body, String campaignId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postCampaignsIdFeedback");
    }
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling postCampaignsIdFeedback");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/feedback"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<CampaignFeedback1> localVarReturnType = new GenericType<CampaignFeedback1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Set campaign content
   * Set the content for a campaign.
   * @param body  (required)
   * @param campaignId The unique id for the campaign. (required)
   * @return CampaignContent
   * @throws ApiException if fails to make API call
   */
  public CampaignContent putCampaignsIdContent(CampaignContent1 body, String campaignId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling putCampaignsIdContent");
    }
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling putCampaignsIdContent");
    }
    // create path and map variables
    String localVarPath = "/campaigns/{campaign_id}/content"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<CampaignContent> localVarReturnType = new GenericType<CampaignContent>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
