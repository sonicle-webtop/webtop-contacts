package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.AbuseComplaint1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AbuseComplaints1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignAdviceReport;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignReport;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignReports1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CampaignSubReports;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ClickDetailMember;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ClickDetailMembers;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ClickDetailReport;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ClickDetailReport1;
import org.joda.time.DateTime;
import com.sonicle.webtop.contacts.mailchimp.cli.model.DomainPerformance;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EepurlActivity;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EmailActivity;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EmailActivity1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InlineResponse2007;
import com.sonicle.webtop.contacts.mailchimp.cli.model.OpenActivity;
import com.sonicle.webtop.contacts.mailchimp.cli.model.OpenDetailReport;
import com.sonicle.webtop.contacts.mailchimp.cli.model.OpenLocations;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SentTo;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Unsubscribes;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Unsubscribes1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class ReportsApi {
  private ApiClient apiClient;

  public ReportsApi() {
    this(Configuration.getDefaultApiClient());
  }

  public ReportsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * List campaign reports
   * Get campaign reports.
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param type The campaign type. (optional)
   * @param beforeSendTime Restrict the response to campaigns sent before the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param sinceSendTime Restrict the response to campaigns sent after the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @return CampaignReports1
   * @throws ApiException if fails to make API call
   */
  public CampaignReports1 getReports(List<String> fields, List<String> excludeFields, Integer count, Integer offset, String type, DateTime beforeSendTime, DateTime sinceSendTime) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/reports";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "type", type));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_send_time", beforeSendTime));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_send_time", sinceSendTime));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<CampaignReports1> localVarReturnType = new GenericType<CampaignReports1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get campaign report
   * Get report details for a specific sent campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return CampaignReport
   * @throws ApiException if fails to make API call
   */
  public CampaignReport getReportsId(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}"
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

    GenericType<CampaignReport> localVarReturnType = new GenericType<CampaignReport>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List abuse reports
   * Get a list of abuse complaints for a specific campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return AbuseComplaints1
   * @throws ApiException if fails to make API call
   */
  public AbuseComplaints1 getReportsIdAbuseReportsId(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdAbuseReportsId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/abuse-reports"
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

    GenericType<AbuseComplaints1> localVarReturnType = new GenericType<AbuseComplaints1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get abuse report
   * Get information about a specific abuse report for a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param reportId The id for the abuse report. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return AbuseComplaint1
   * @throws ApiException if fails to make API call
   */
  public AbuseComplaint1 getReportsIdAbuseReportsIdId(String campaignId, String reportId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdAbuseReportsIdId");
    }
    // verify the required parameter 'reportId' is set
    if (reportId == null) {
      throw new ApiException(400, "Missing the required parameter 'reportId' when calling getReportsIdAbuseReportsIdId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/abuse-reports/{report_id}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "report_id" + "\\}", apiClient.escapeString(reportId.toString()));

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

    GenericType<AbuseComplaint1> localVarReturnType = new GenericType<AbuseComplaint1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List campaign feedback
   * Get feedback based on a campaign&#x27;s statistics. Advice feedback is based on campaign stats like opens, clicks, unsubscribes, bounces, and more.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return CampaignAdviceReport
   * @throws ApiException if fails to make API call
   */
  public CampaignAdviceReport getReportsIdAdvice(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdAdvice");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/advice"
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

    GenericType<CampaignAdviceReport> localVarReturnType = new GenericType<CampaignAdviceReport>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List campaign details
   * Get information about clicks on specific links in your Mailchimp campaigns.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return ClickDetailReport
   * @throws ApiException if fails to make API call
   */
  public ClickDetailReport getReportsIdClickDetails(String campaignId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdClickDetails");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/click-details"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<ClickDetailReport> localVarReturnType = new GenericType<ClickDetailReport>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get campaign link details
   * Get click details for a specific link in a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param linkId The id for the link. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return ClickDetailReport1
   * @throws ApiException if fails to make API call
   */
  public ClickDetailReport1 getReportsIdClickDetailsId(String campaignId, String linkId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdClickDetailsId");
    }
    // verify the required parameter 'linkId' is set
    if (linkId == null) {
      throw new ApiException(400, "Missing the required parameter 'linkId' when calling getReportsIdClickDetailsId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/click-details/{link_id}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "link_id" + "\\}", apiClient.escapeString(linkId.toString()));

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

    GenericType<ClickDetailReport1> localVarReturnType = new GenericType<ClickDetailReport1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List clicked link subscribers
   * Get information about list members who clicked on a specific link in a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param linkId The id for the link. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return ClickDetailMembers
   * @throws ApiException if fails to make API call
   */
  public ClickDetailMembers getReportsIdClickDetailsIdMembers(String campaignId, String linkId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdClickDetailsIdMembers");
    }
    // verify the required parameter 'linkId' is set
    if (linkId == null) {
      throw new ApiException(400, "Missing the required parameter 'linkId' when calling getReportsIdClickDetailsIdMembers");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/click-details/{link_id}/members"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "link_id" + "\\}", apiClient.escapeString(linkId.toString()));

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

    GenericType<ClickDetailMembers> localVarReturnType = new GenericType<ClickDetailMembers>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get clicked link subscriber
   * Get information about a specific subscriber who clicked a link in a specific campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param linkId The id for the link. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return ClickDetailMember
   * @throws ApiException if fails to make API call
   */
  public ClickDetailMember getReportsIdClickDetailsIdMembersId(String campaignId, String linkId, String subscriberHash, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdClickDetailsIdMembersId");
    }
    // verify the required parameter 'linkId' is set
    if (linkId == null) {
      throw new ApiException(400, "Missing the required parameter 'linkId' when calling getReportsIdClickDetailsIdMembersId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getReportsIdClickDetailsIdMembersId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/click-details/{link_id}/members/{subscriber_hash}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "link_id" + "\\}", apiClient.escapeString(linkId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

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

    GenericType<ClickDetailMember> localVarReturnType = new GenericType<ClickDetailMember>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List domain performance stats
   * Get statistics for the top-performing email domains in a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return DomainPerformance
   * @throws ApiException if fails to make API call
   */
  public DomainPerformance getReportsIdDomainPerformance(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdDomainPerformance");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/domain-performance"
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

    GenericType<DomainPerformance> localVarReturnType = new GenericType<DomainPerformance>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List campaign product activity
   * Get breakdown of product activity for a campaign
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param sortField Returns files sorted by the specified field. (optional)
   * @return InlineResponse2007
   * @throws ApiException if fails to make API call
   */
  public InlineResponse2007 getReportsIdEcommerceProductActivity(String campaignId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String sortField) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdEcommerceProductActivity");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/ecommerce-product-activity"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_field", sortField));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<InlineResponse2007> localVarReturnType = new GenericType<InlineResponse2007>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List EepURL activity
   * Get a summary of social activity for the campaign, tracked by EepURL.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EepurlActivity
   * @throws ApiException if fails to make API call
   */
  public EepurlActivity getReportsIdEepurl(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdEepurl");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/eepurl"
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

    GenericType<EepurlActivity> localVarReturnType = new GenericType<EepurlActivity>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List email activity
   * Get a list of member&#x27;s subscriber activity in a specific campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param since Restrict results to email activity events that occur after a specific time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @return EmailActivity
   * @throws ApiException if fails to make API call
   */
  public EmailActivity getReportsIdEmailActivity(String campaignId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String since) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdEmailActivity");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/email-activity"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since", since));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EmailActivity> localVarReturnType = new GenericType<EmailActivity>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get subscriber email activity
   * Get a specific list member&#x27;s activity in a campaign including opens, clicks, and bounces.
   * @param campaignId The unique id for the campaign. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param since Restrict results to email activity events that occur after a specific time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @return EmailActivity1
   * @throws ApiException if fails to make API call
   */
  public EmailActivity1 getReportsIdEmailActivityId(String campaignId, String subscriberHash, List<String> fields, List<String> excludeFields, String since) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdEmailActivityId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getReportsIdEmailActivityId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/email-activity/{subscriber_hash}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since", since));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EmailActivity1> localVarReturnType = new GenericType<EmailActivity1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List top open activities
   * Get top open locations for a specific campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return OpenLocations
   * @throws ApiException if fails to make API call
   */
  public OpenLocations getReportsIdLocations(String campaignId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdLocations");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/locations"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<OpenLocations> localVarReturnType = new GenericType<OpenLocations>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List campaign open details
   * Get detailed information about any campaign emails that were opened by a list member.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param since Restrict results to campaign open events that occur after a specific time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @return OpenDetailReport
   * @throws ApiException if fails to make API call
   */
  public OpenDetailReport getReportsIdOpenDetails(String campaignId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String since) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdOpenDetails");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/open-details"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since", since));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<OpenDetailReport> localVarReturnType = new GenericType<OpenDetailReport>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get opened campaign subscriber
   * Get information about a specific subscriber who opened a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return OpenActivity
   * @throws ApiException if fails to make API call
   */
  public OpenActivity getReportsIdOpenDetailsIdMembersId(String campaignId, String subscriberHash, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdOpenDetailsIdMembersId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getReportsIdOpenDetailsIdMembersId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/open-details/{subscriber_hash}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

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

    GenericType<OpenActivity> localVarReturnType = new GenericType<OpenActivity>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List campaign recipients
   * Get information about campaign recipients.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return SentTo
   * @throws ApiException if fails to make API call
   */
  public SentTo getReportsIdSentTo(String campaignId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdSentTo");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/sent-to"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<SentTo> localVarReturnType = new GenericType<SentTo>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get campaign recipient info
   * Get information about a specific campaign recipient.
   * @param campaignId The unique id for the campaign. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return SentTo
   * @throws ApiException if fails to make API call
   */
  public SentTo getReportsIdSentToId(String campaignId, String subscriberHash, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdSentToId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getReportsIdSentToId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/sent-to/{subscriber_hash}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

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

    GenericType<SentTo> localVarReturnType = new GenericType<SentTo>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List child campaign reports
   * Get a list of reports with child campaigns for a specific parent campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return CampaignSubReports
   * @throws ApiException if fails to make API call
   */
  public CampaignSubReports getReportsIdSubReportsId(String campaignId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdSubReportsId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/sub-reports"
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

    GenericType<CampaignSubReports> localVarReturnType = new GenericType<CampaignSubReports>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List unsubscribed members
   * Get information about members who have unsubscribed from a specific campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return Unsubscribes
   * @throws ApiException if fails to make API call
   */
  public Unsubscribes getReportsIdUnsubscribed(String campaignId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdUnsubscribed");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/unsubscribed"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()));

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

    GenericType<Unsubscribes> localVarReturnType = new GenericType<Unsubscribes>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get unsubscribed member
   * Get information about a specific list member who unsubscribed from a campaign.
   * @param campaignId The unique id for the campaign. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return Unsubscribes1
   * @throws ApiException if fails to make API call
   */
  public Unsubscribes1 getReportsIdUnsubscribedId(String campaignId, String subscriberHash, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'campaignId' is set
    if (campaignId == null) {
      throw new ApiException(400, "Missing the required parameter 'campaignId' when calling getReportsIdUnsubscribedId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getReportsIdUnsubscribedId");
    }
    // create path and map variables
    String localVarPath = "/reports/{campaign_id}/unsubscribed/{subscriber_hash}"
      .replaceAll("\\{" + "campaign_id" + "\\}", apiClient.escapeString(campaignId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

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

    GenericType<Unsubscribes1> localVarReturnType = new GenericType<Unsubscribes1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
