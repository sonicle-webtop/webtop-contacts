package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.AbuseComplaint;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AbuseComplaints;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AddListMembers1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AddListMembers2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AddListMembers3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AddWebhook;
import com.sonicle.webtop.contacts.mailchimp.cli.model.AddWebhook1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.BatchAddremoveListMembersTofromStaticSegment;
import com.sonicle.webtop.contacts.mailchimp.cli.model.BatchUpdateListMembers;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CollectionOfEvents;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CollectionOfMemberActivityEvents;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CollectionOfMergeFields;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CollectionOfNotes;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CollectionOfSegments;
import com.sonicle.webtop.contacts.mailchimp.cli.model.CollectionOfTags;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EmailClients;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Events;
import com.sonicle.webtop.contacts.mailchimp.cli.model.GrowthHistory;
import com.sonicle.webtop.contacts.mailchimp.cli.model.GrowthHistory1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Interest;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Interest1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Interest2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InterestCategory;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InterestCategory1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InterestCategory2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.InterestGroupings;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Interests;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List4;
import com.sonicle.webtop.contacts.mailchimp.cli.model.List5;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListActivity;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListLocations;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListMembers;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListMembers1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListMembers2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListSignupForms;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListWebhooks;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ListWebhooks1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MemberActivityEvents;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MemberActivityEvents1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MemberNotes;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MemberNotes1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MemberNotes2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MemberTags;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MembersToAddremoveTofromAStaticSegment;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MembersToSubscribeUnsubscribeTofromAListInBatch;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MergeField;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MergeField1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.MergeField2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SegmentIdMembersBody;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SegmentMembers;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SignupForm;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SignupForm1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberList;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberList1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberList2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberLists;
import com.sonicle.webtop.contacts.mailchimp.cli.model.TagSearchResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class ListsApi {
  private ApiClient apiClient;

  public ListsApi() {
    this(Configuration.getDefaultApiClient());
  }

  public ListsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Delete list
   * Delete a list from your Mailchimp account. If you delete a list, you&#x27;ll lose the list history—including subscriber activity, unsubscribes, complaints, and bounces. You’ll also lose subscribers’ email addresses, unless you exported and backed up your list.
   * @param listId The unique ID for the list. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsId(String listId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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
   * Delete interest category
   * Delete a specific interest category.
   * @param listId The unique ID for the list. (required)
   * @param interestCategoryId The unique ID for the interest category. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsIdInterestCategoriesId(String listId, String interestCategoryId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsIdInterestCategoriesId");
    }
    // verify the required parameter 'interestCategoryId' is set
    if (interestCategoryId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestCategoryId' when calling deleteListsIdInterestCategoriesId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories/{interest_category_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "interest_category_id" + "\\}", apiClient.escapeString(interestCategoryId.toString()));

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
   * Delete interest in category
   * Delete interests or group names in a specific category.
   * @param listId The unique ID for the list. (required)
   * @param interestCategoryId The unique ID for the interest category. (required)
   * @param interestId The specific interest or &#x27;group name&#x27;. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsIdInterestCategoriesIdInterestsId(String listId, String interestCategoryId, String interestId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsIdInterestCategoriesIdInterestsId");
    }
    // verify the required parameter 'interestCategoryId' is set
    if (interestCategoryId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestCategoryId' when calling deleteListsIdInterestCategoriesIdInterestsId");
    }
    // verify the required parameter 'interestId' is set
    if (interestId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestId' when calling deleteListsIdInterestCategoriesIdInterestsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories/{interest_category_id}/interests/{interest_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "interest_category_id" + "\\}", apiClient.escapeString(interestCategoryId.toString()))
      .replaceAll("\\{" + "interest_id" + "\\}", apiClient.escapeString(interestId.toString()));

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
   * Archive list member
   * Archive a list member. To permanently delete, use the delete-permanent action.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsIdMembersId(String listId, String subscriberHash) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsIdMembersId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling deleteListsIdMembersId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
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

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete note
   * Delete a specific note for a specific list member.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param noteId The id for the note. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsIdMembersIdNotesId(String listId, String subscriberHash, String noteId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsIdMembersIdNotesId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling deleteListsIdMembersIdNotesId");
    }
    // verify the required parameter 'noteId' is set
    if (noteId == null) {
      throw new ApiException(400, "Missing the required parameter 'noteId' when calling deleteListsIdMembersIdNotesId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/notes/{note_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()))
      .replaceAll("\\{" + "note_id" + "\\}", apiClient.escapeString(noteId.toString()));

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
   * Delete merge field
   * Delete a specific merge field ([audience field](https://mailchimp.com/help/getting-started-with-merge-tags/)) in a list.
   * @param listId The unique ID for the list. (required)
   * @param mergeId The id for the merge field. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsIdMergeFieldsId(String listId, String mergeId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsIdMergeFieldsId");
    }
    // verify the required parameter 'mergeId' is set
    if (mergeId == null) {
      throw new ApiException(400, "Missing the required parameter 'mergeId' when calling deleteListsIdMergeFieldsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/merge-fields/{merge_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "merge_id" + "\\}", apiClient.escapeString(mergeId.toString()));

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
   * Delete segment
   * Delete a specific segment in a list.
   * @param listId The unique ID for the list. (required)
   * @param segmentId The unique id for the segment. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsIdSegmentsId(String listId, String segmentId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsIdSegmentsId");
    }
    // verify the required parameter 'segmentId' is set
    if (segmentId == null) {
      throw new ApiException(400, "Missing the required parameter 'segmentId' when calling deleteListsIdSegmentsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments/{segment_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "segment_id" + "\\}", apiClient.escapeString(segmentId.toString()));

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
   * Remove list member from segment
   * Remove a member from the specified static segment.
   * @param listId The unique ID for the list. (required)
   * @param segmentId The unique id for the segment. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsIdSegmentsIdMembersId(String listId, String segmentId, String subscriberHash) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsIdSegmentsIdMembersId");
    }
    // verify the required parameter 'segmentId' is set
    if (segmentId == null) {
      throw new ApiException(400, "Missing the required parameter 'segmentId' when calling deleteListsIdSegmentsIdMembersId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling deleteListsIdSegmentsIdMembersId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments/{segment_id}/members/{subscriber_hash}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "segment_id" + "\\}", apiClient.escapeString(segmentId.toString()))
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

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete webhook
   * Delete a specific webhook in a list.
   * @param listId The unique ID for the list. (required)
   * @param webhookId The webhook&#x27;s id. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteListsIdWebhooksId(String listId, String webhookId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling deleteListsIdWebhooksId");
    }
    // verify the required parameter 'webhookId' is set
    if (webhookId == null) {
      throw new ApiException(400, "Missing the required parameter 'webhookId' when calling deleteListsIdWebhooksId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/webhooks/{webhook_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "webhook_id" + "\\}", apiClient.escapeString(webhookId.toString()));

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
   * List member tags
   * Get the tags on a list member.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return CollectionOfTags
   * @throws ApiException if fails to make API call
   */
  public CollectionOfTags getListMemberTags(String listId, String subscriberHash, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListMemberTags");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getListMemberTags");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/tags"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

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

    GenericType<CollectionOfTags> localVarReturnType = new GenericType<CollectionOfTags>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get lists info
   * Get information about all lists in the account.
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param beforeDateCreated Restrict response to lists created before the set date. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param sinceDateCreated Restrict results to lists created after the set date. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param beforeCampaignLastSent Restrict results to lists created before the last campaign send date. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param sinceCampaignLastSent Restrict results to lists created after the last campaign send date. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param email Restrict results to lists that include a specific subscriber&#x27;s email address. (optional)
   * @param sortField Returns files sorted by the specified field. (optional)
   * @param sortDir Determines the order direction for sorted results. (optional)
   * @param hasEcommerceStore Restrict results to lists that contain an active, connected, undeleted ecommerce store. (optional)
   * @param includeTotalContacts Return the total_contacts field in the stats response, which contains an approximate count of all contacts in any state. (optional)
   * @return SubscriberLists
   * @throws ApiException if fails to make API call
   */
  public SubscriberLists getLists(List<String> fields, List<String> excludeFields, Integer count, Integer offset, String beforeDateCreated, String sinceDateCreated, String beforeCampaignLastSent, String sinceCampaignLastSent, String email, String sortField, String sortDir, Boolean hasEcommerceStore, Boolean includeTotalContacts) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/lists";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_date_created", beforeDateCreated));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_date_created", sinceDateCreated));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_campaign_last_sent", beforeCampaignLastSent));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_campaign_last_sent", sinceCampaignLastSent));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "email", email));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_field", sortField));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_dir", sortDir));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "has_ecommerce_store", hasEcommerceStore));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_total_contacts", includeTotalContacts));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<SubscriberLists> localVarReturnType = new GenericType<SubscriberLists>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get list info
   * Get information about a specific list in your Mailchimp account. Results include list members who have signed up but haven&#x27;t confirmed their subscription yet and unsubscribed or cleaned.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param includeTotalContacts Return the total_contacts field in the stats response, which contains an approximate count of all contacts in any state. (optional)
   * @return SubscriberList1
   * @throws ApiException if fails to make API call
   */
  public SubscriberList1 getListsId(String listId, List<String> fields, List<String> excludeFields, Boolean includeTotalContacts) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_total_contacts", includeTotalContacts));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<SubscriberList1> localVarReturnType = new GenericType<SubscriberList1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List abuse reports
   * Get all abuse reports for a specific list.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return AbuseComplaints
   * @throws ApiException if fails to make API call
   */
  public AbuseComplaints getListsIdAbuseReports(String listId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdAbuseReports");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/abuse-reports"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<AbuseComplaints> localVarReturnType = new GenericType<AbuseComplaints>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get abuse report
   * Get details about a specific abuse report.
   * @param listId The unique ID for the list. (required)
   * @param reportId The id for the abuse report. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return AbuseComplaint
   * @throws ApiException if fails to make API call
   */
  public AbuseComplaint getListsIdAbuseReportsId(String listId, String reportId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdAbuseReportsId");
    }
    // verify the required parameter 'reportId' is set
    if (reportId == null) {
      throw new ApiException(400, "Missing the required parameter 'reportId' when calling getListsIdAbuseReportsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/abuse-reports/{report_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "report_id" + "\\}", apiClient.escapeString(reportId.toString()));

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

    GenericType<AbuseComplaint> localVarReturnType = new GenericType<AbuseComplaint>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List recent activity
   * Get up to the previous 180 days of daily detailed aggregated activity stats for a list, not including Automation activity.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return ListActivity
   * @throws ApiException if fails to make API call
   */
  public ListActivity getListsIdActivity(String listId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdActivity");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/activity"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<ListActivity> localVarReturnType = new GenericType<ListActivity>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List top email clients
   * Get a list of the top email clients based on user-agent strings.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EmailClients
   * @throws ApiException if fails to make API call
   */
  public EmailClients getListsIdClients(String listId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdClients");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/clients"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<EmailClients> localVarReturnType = new GenericType<EmailClients>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List growth history data
   * Get a month-by-month summary of a specific list&#x27;s growth activity.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param sortField Returns files sorted by the specified field. (optional)
   * @param sortDir Determines the order direction for sorted results. (optional)
   * @return GrowthHistory
   * @throws ApiException if fails to make API call
   */
  public GrowthHistory getListsIdGrowthHistory(String listId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String sortField, String sortDir) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdGrowthHistory");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/growth-history"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
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

    GenericType<GrowthHistory> localVarReturnType = new GenericType<GrowthHistory>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get growth history by month
   * Get a summary of a specific list&#x27;s growth activity for a specific month and year.
   * @param listId The unique ID for the list. (required)
   * @param month A specific month of list growth history. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return GrowthHistory1
   * @throws ApiException if fails to make API call
   */
  public GrowthHistory1 getListsIdGrowthHistoryId(String listId, String month, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdGrowthHistoryId");
    }
    // verify the required parameter 'month' is set
    if (month == null) {
      throw new ApiException(400, "Missing the required parameter 'month' when calling getListsIdGrowthHistoryId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/growth-history/{month}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "month" + "\\}", apiClient.escapeString(month.toString()));

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

    GenericType<GrowthHistory1> localVarReturnType = new GenericType<GrowthHistory1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List interest categories
   * Get information about a list&#x27;s interest categories.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param type Restrict results a type of interest group (optional)
   * @return InterestGroupings
   * @throws ApiException if fails to make API call
   */
  public InterestGroupings getListsIdInterestCategories(String listId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String type) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdInterestCategories");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "type", type));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<InterestGroupings> localVarReturnType = new GenericType<InterestGroupings>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get interest category info
   * Get information about a specific interest category.
   * @param listId The unique ID for the list. (required)
   * @param interestCategoryId The unique ID for the interest category. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return InterestCategory1
   * @throws ApiException if fails to make API call
   */
  public InterestCategory1 getListsIdInterestCategoriesId(String listId, String interestCategoryId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdInterestCategoriesId");
    }
    // verify the required parameter 'interestCategoryId' is set
    if (interestCategoryId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestCategoryId' when calling getListsIdInterestCategoriesId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories/{interest_category_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "interest_category_id" + "\\}", apiClient.escapeString(interestCategoryId.toString()));

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

    GenericType<InterestCategory1> localVarReturnType = new GenericType<InterestCategory1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List interests in category
   * Get a list of this category&#x27;s interests.
   * @param listId The unique ID for the list. (required)
   * @param interestCategoryId The unique ID for the interest category. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return Interests
   * @throws ApiException if fails to make API call
   */
  public Interests getListsIdInterestCategoriesIdInterests(String listId, String interestCategoryId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdInterestCategoriesIdInterests");
    }
    // verify the required parameter 'interestCategoryId' is set
    if (interestCategoryId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestCategoryId' when calling getListsIdInterestCategoriesIdInterests");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories/{interest_category_id}/interests"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "interest_category_id" + "\\}", apiClient.escapeString(interestCategoryId.toString()));

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

    GenericType<Interests> localVarReturnType = new GenericType<Interests>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get interest in category
   * Get interests or &#x27;group names&#x27; for a specific category.
   * @param listId The unique ID for the list. (required)
   * @param interestCategoryId The unique ID for the interest category. (required)
   * @param interestId The specific interest or &#x27;group name&#x27;. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return Interest1
   * @throws ApiException if fails to make API call
   */
  public Interest1 getListsIdInterestCategoriesIdInterestsId(String listId, String interestCategoryId, String interestId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdInterestCategoriesIdInterestsId");
    }
    // verify the required parameter 'interestCategoryId' is set
    if (interestCategoryId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestCategoryId' when calling getListsIdInterestCategoriesIdInterestsId");
    }
    // verify the required parameter 'interestId' is set
    if (interestId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestId' when calling getListsIdInterestCategoriesIdInterestsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories/{interest_category_id}/interests/{interest_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "interest_category_id" + "\\}", apiClient.escapeString(interestCategoryId.toString()))
      .replaceAll("\\{" + "interest_id" + "\\}", apiClient.escapeString(interestId.toString()));

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

    GenericType<Interest1> localVarReturnType = new GenericType<Interest1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List locations
   * Get the locations (countries) that the list&#x27;s subscribers have been tagged to based on geocoding their IP address.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return ListLocations
   * @throws ApiException if fails to make API call
   */
  public ListLocations getListsIdLocations(String listId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdLocations");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/locations"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<ListLocations> localVarReturnType = new GenericType<ListLocations>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List members info
   * Get information about members in a specific Mailchimp list.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param emailType The email type. (optional)
   * @param status The subscriber&#x27;s status. (optional)
   * @param sinceTimestampOpt Restrict results to subscribers who opted-in after the set timeframe. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param beforeTimestampOpt Restrict results to subscribers who opted-in before the set timeframe. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param sinceLastChanged Restrict results to subscribers whose information changed after the set timeframe. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param beforeLastChanged Restrict results to subscribers whose information changed before the set timeframe. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param uniqueEmailId A unique identifier for the email address across all Mailchimp lists. (optional)
   * @param vipOnly A filter to return only the list&#x27;s VIP members. Passing &#x60;true&#x60; will restrict results to VIP list members, passing &#x60;false&#x60; will return all list members. (optional)
   * @param interestCategoryId The unique id for the interest category. (optional)
   * @param interestIds Used to filter list members by interests. Must be accompanied by interest_category_id and interest_match. The value must be a comma separated list of interest ids present for any supplied interest categories. (optional)
   * @param interestMatch Used to filter list members by interests. Must be accompanied by interest_category_id and interest_ids. \&quot;any\&quot; will match a member with any of the interest supplied, \&quot;all\&quot; will only match members with every interest supplied, and \&quot;none\&quot; will match members without any of the interest supplied. (optional)
   * @param sortField Returns files sorted by the specified field. (optional)
   * @param sortDir Determines the order direction for sorted results. (optional)
   * @param sinceLastCampaign Filter subscribers by those subscribed/unsubscribed/pending/cleaned since last email campaign send. Member status is required to use this filter. (optional)
   * @param unsubscribedSince Filter subscribers by those unsubscribed since a specific date. Using any status other than unsubscribed with this filter will result in an error. (optional)
   * @return ListMembers1
   * @throws ApiException if fails to make API call
   */
  public ListMembers1 getListsIdMembers(String listId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String emailType, String status, String sinceTimestampOpt, String beforeTimestampOpt, String sinceLastChanged, String beforeLastChanged, String uniqueEmailId, Boolean vipOnly, String interestCategoryId, String interestIds, String interestMatch, String sortField, String sortDir, Boolean sinceLastCampaign, String unsubscribedSince) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMembers");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "email_type", emailType));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "status", status));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_timestamp_opt", sinceTimestampOpt));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_timestamp_opt", beforeTimestampOpt));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_last_changed", sinceLastChanged));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_last_changed", beforeLastChanged));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "unique_email_id", uniqueEmailId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "vip_only", vipOnly));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "interest_category_id", interestCategoryId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "interest_ids", interestIds));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "interest_match", interestMatch));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_field", sortField));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_dir", sortDir));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_last_campaign", sinceLastCampaign));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "unsubscribed_since", unsubscribedSince));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<ListMembers1> localVarReturnType = new GenericType<ListMembers1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get member info
   * Get information about a specific list member, including a currently subscribed, unsubscribed, or bounced member.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return ListMembers2
   * @throws ApiException if fails to make API call
   */
  public ListMembers2 getListsIdMembersId(String listId, String subscriberHash, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMembersId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getListsIdMembersId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
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

    GenericType<ListMembers2> localVarReturnType = new GenericType<ListMembers2>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * View recent activity 50
   * Get the last 50 events of a member&#x27;s activity on a specific list, including opens, clicks, and unsubscribes.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param action A comma seperated list of actions to return. (optional)
   * @return MemberActivityEvents
   * @throws ApiException if fails to make API call
   */
  public MemberActivityEvents getListsIdMembersIdActivity(String listId, String subscriberHash, List<String> fields, List<String> excludeFields, List<String> action) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMembersIdActivity");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getListsIdMembersIdActivity");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/activity"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "action", action));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<MemberActivityEvents> localVarReturnType = new GenericType<MemberActivityEvents>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * View recent activity
   * Get a member&#x27;s activity on a specific list, including opens, clicks, and unsubscribes.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param activityFilters A comma-separated list of activity filters that correspond to a set of activity types, e.g \&quot;?activity_filters&#x3D;open,bounce,click\&quot;. (optional)
   * @return MemberActivityEvents1
   * @throws ApiException if fails to make API call
   */
  public MemberActivityEvents1 getListsIdMembersIdActivityFeed(String listId, String subscriberHash, List<String> fields, List<String> excludeFields, Integer count, Integer offset, List<String> activityFilters) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMembersIdActivityFeed");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getListsIdMembersIdActivityFeed");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/activity-feed"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "activity_filters", activityFilters));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<MemberActivityEvents1> localVarReturnType = new GenericType<MemberActivityEvents1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List member events
   * Get events for a contact.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. This endpoint also accepts email addresses. (required)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return CollectionOfEvents
   * @throws ApiException if fails to make API call
   */
  public CollectionOfEvents getListsIdMembersIdEvents(String listId, String subscriberHash, Integer count, Integer offset, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMembersIdEvents");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getListsIdMembersIdEvents");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/events"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
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

    GenericType<CollectionOfEvents> localVarReturnType = new GenericType<CollectionOfEvents>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List member goal events
   * Get the last 50 Goal events for a member on a specific list.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return CollectionOfMemberActivityEvents
   * @throws ApiException if fails to make API call
   */
  public CollectionOfMemberActivityEvents getListsIdMembersIdGoals(String listId, String subscriberHash, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMembersIdGoals");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getListsIdMembersIdGoals");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/goals"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
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

    GenericType<CollectionOfMemberActivityEvents> localVarReturnType = new GenericType<CollectionOfMemberActivityEvents>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List recent member notes
   * Get recent notes for a specific list member.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param sortField Returns notes sorted by the specified field. (optional)
   * @param sortDir Determines the order direction for sorted results. (optional)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return CollectionOfNotes
   * @throws ApiException if fails to make API call
   */
  public CollectionOfNotes getListsIdMembersIdNotes(String listId, String subscriberHash, String sortField, String sortDir, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMembersIdNotes");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getListsIdMembersIdNotes");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/notes"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_field", sortField));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sort_dir", sortDir));
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

    GenericType<CollectionOfNotes> localVarReturnType = new GenericType<CollectionOfNotes>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get member note
   * Get a specific note for a specific list member.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param noteId The id for the note. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return MemberNotes1
   * @throws ApiException if fails to make API call
   */
  public MemberNotes1 getListsIdMembersIdNotesId(String listId, String subscriberHash, String noteId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMembersIdNotesId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling getListsIdMembersIdNotesId");
    }
    // verify the required parameter 'noteId' is set
    if (noteId == null) {
      throw new ApiException(400, "Missing the required parameter 'noteId' when calling getListsIdMembersIdNotesId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/notes/{note_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()))
      .replaceAll("\\{" + "note_id" + "\\}", apiClient.escapeString(noteId.toString()));

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

    GenericType<MemberNotes1> localVarReturnType = new GenericType<MemberNotes1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List merge fields
   * Get a list of all merge fields ([audience fields](https://mailchimp.com/help/getting-started-with-merge-tags/)) for a list.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param type The merge field type. (optional)
   * @param required Whether it&#x27;s a required merge field. (optional)
   * @return CollectionOfMergeFields
   * @throws ApiException if fails to make API call
   */
  public CollectionOfMergeFields getListsIdMergeFields(String listId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String type, Boolean required) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMergeFields");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/merge-fields"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "type", type));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "required", required));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<CollectionOfMergeFields> localVarReturnType = new GenericType<CollectionOfMergeFields>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get merge field
   * Get information about a specific merge field ([audience field](https://mailchimp.com/help/getting-started-with-merge-tags/)) in a list.
   * @param listId The unique ID for the list. (required)
   * @param mergeId The id for the merge field. (required)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @return MergeField1
   * @throws ApiException if fails to make API call
   */
  public MergeField1 getListsIdMergeFieldsId(String listId, String mergeId, List<String> excludeFields, List<String> fields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdMergeFieldsId");
    }
    // verify the required parameter 'mergeId' is set
    if (mergeId == null) {
      throw new ApiException(400, "Missing the required parameter 'mergeId' when calling getListsIdMergeFieldsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/merge-fields/{merge_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "merge_id" + "\\}", apiClient.escapeString(mergeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<MergeField1> localVarReturnType = new GenericType<MergeField1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get segment info
   * Get information about a specific segment.
   * @param listId The unique ID for the list. (required)
   * @param segmentId The unique id for the segment. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param includeCleaned Include cleaned members in response (optional)
   * @param includeTransactional Include transactional members in response (optional)
   * @param includeUnsubscribed Include unsubscribed members in response (optional)
   * @return List4
   * @throws ApiException if fails to make API call
   */
  public List4 getListsIdSegmentsId(String listId, String segmentId, List<String> fields, List<String> excludeFields, Boolean includeCleaned, Boolean includeTransactional, Boolean includeUnsubscribed) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdSegmentsId");
    }
    // verify the required parameter 'segmentId' is set
    if (segmentId == null) {
      throw new ApiException(400, "Missing the required parameter 'segmentId' when calling getListsIdSegmentsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments/{segment_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "segment_id" + "\\}", apiClient.escapeString(segmentId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_cleaned", includeCleaned));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_transactional", includeTransactional));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_unsubscribed", includeUnsubscribed));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<List4> localVarReturnType = new GenericType<List4>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List members in segment
   * Get information about members in a saved segment.
   * @param listId The unique ID for the list. (required)
   * @param segmentId The unique id for the segment. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param includeCleaned Include cleaned members in response (optional)
   * @param includeTransactional Include transactional members in response (optional)
   * @param includeUnsubscribed Include unsubscribed members in response (optional)
   * @return SegmentMembers
   * @throws ApiException if fails to make API call
   */
  public SegmentMembers getListsIdSegmentsIdMembers(String listId, String segmentId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, Boolean includeCleaned, Boolean includeTransactional, Boolean includeUnsubscribed) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdSegmentsIdMembers");
    }
    // verify the required parameter 'segmentId' is set
    if (segmentId == null) {
      throw new ApiException(400, "Missing the required parameter 'segmentId' when calling getListsIdSegmentsIdMembers");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments/{segment_id}/members"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "segment_id" + "\\}", apiClient.escapeString(segmentId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_cleaned", includeCleaned));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_transactional", includeTransactional));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_unsubscribed", includeUnsubscribed));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<SegmentMembers> localVarReturnType = new GenericType<SegmentMembers>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List signup forms
   * Get signup forms for a specific list.
   * @param listId The unique ID for the list. (required)
   * @return ListSignupForms
   * @throws ApiException if fails to make API call
   */
  public ListSignupForms getListsIdSignupForms(String listId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdSignupForms");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/signup-forms"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<ListSignupForms> localVarReturnType = new GenericType<ListSignupForms>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List webhooks
   * Get information about all webhooks for a specific list.
   * @param listId The unique ID for the list. (required)
   * @return ListWebhooks
   * @throws ApiException if fails to make API call
   */
  public ListWebhooks getListsIdWebhooks(String listId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdWebhooks");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/webhooks"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<ListWebhooks> localVarReturnType = new GenericType<ListWebhooks>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get webhook info
   * Get information about a specific webhook.
   * @param listId The unique ID for the list. (required)
   * @param webhookId The webhook&#x27;s id. (required)
   * @return ListWebhooks1
   * @throws ApiException if fails to make API call
   */
  public ListWebhooks1 getListsIdWebhooksId(String listId, String webhookId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling getListsIdWebhooksId");
    }
    // verify the required parameter 'webhookId' is set
    if (webhookId == null) {
      throw new ApiException(400, "Missing the required parameter 'webhookId' when calling getListsIdWebhooksId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/webhooks/{webhook_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "webhook_id" + "\\}", apiClient.escapeString(webhookId.toString()));

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

    GenericType<ListWebhooks1> localVarReturnType = new GenericType<ListWebhooks1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update lists
   * Update the settings for a specific list.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @return SubscriberList1
   * @throws ApiException if fails to make API call
   */
  public SubscriberList1 patchListsId(SubscriberList2 body, String listId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchListsId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling patchListsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<SubscriberList1> localVarReturnType = new GenericType<SubscriberList1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update interest category
   * Update a specific interest category.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param interestCategoryId The unique ID for the interest category. (required)
   * @return InterestCategory1
   * @throws ApiException if fails to make API call
   */
  public InterestCategory1 patchListsIdInterestCategoriesId(InterestCategory2 body, String listId, String interestCategoryId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchListsIdInterestCategoriesId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling patchListsIdInterestCategoriesId");
    }
    // verify the required parameter 'interestCategoryId' is set
    if (interestCategoryId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestCategoryId' when calling patchListsIdInterestCategoriesId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories/{interest_category_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "interest_category_id" + "\\}", apiClient.escapeString(interestCategoryId.toString()));

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

    GenericType<InterestCategory1> localVarReturnType = new GenericType<InterestCategory1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update interest in category
   * Update interests or &#x27;group names&#x27; for a specific category.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param interestCategoryId The unique ID for the interest category. (required)
   * @param interestId The specific interest or &#x27;group name&#x27;. (required)
   * @return Interest1
   * @throws ApiException if fails to make API call
   */
  public Interest1 patchListsIdInterestCategoriesIdInterestsId(Interest2 body, String listId, String interestCategoryId, String interestId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchListsIdInterestCategoriesIdInterestsId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling patchListsIdInterestCategoriesIdInterestsId");
    }
    // verify the required parameter 'interestCategoryId' is set
    if (interestCategoryId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestCategoryId' when calling patchListsIdInterestCategoriesIdInterestsId");
    }
    // verify the required parameter 'interestId' is set
    if (interestId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestId' when calling patchListsIdInterestCategoriesIdInterestsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories/{interest_category_id}/interests/{interest_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "interest_category_id" + "\\}", apiClient.escapeString(interestCategoryId.toString()))
      .replaceAll("\\{" + "interest_id" + "\\}", apiClient.escapeString(interestId.toString()));

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

    GenericType<Interest1> localVarReturnType = new GenericType<Interest1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update list member
   * Update information for a specific list member.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param skipMergeValidation If skip_merge_validation is true, member data will be accepted without merge field values, even if the merge field is usually required. This defaults to false. (optional)
   * @return ListMembers2
   * @throws ApiException if fails to make API call
   */
  public ListMembers2 patchListsIdMembersId(AddListMembers3 body, String listId, String subscriberHash, Boolean skipMergeValidation) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchListsIdMembersId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling patchListsIdMembersId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling patchListsIdMembersId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "skip_merge_validation", skipMergeValidation));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<ListMembers2> localVarReturnType = new GenericType<ListMembers2>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update note
   * Update a specific note for a specific list member.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param noteId The id for the note. (required)
   * @return MemberNotes1
   * @throws ApiException if fails to make API call
   */
  public MemberNotes1 patchListsIdMembersIdNotesId(MemberNotes2 body, String listId, String subscriberHash, String noteId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchListsIdMembersIdNotesId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling patchListsIdMembersIdNotesId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling patchListsIdMembersIdNotesId");
    }
    // verify the required parameter 'noteId' is set
    if (noteId == null) {
      throw new ApiException(400, "Missing the required parameter 'noteId' when calling patchListsIdMembersIdNotesId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/notes/{note_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()))
      .replaceAll("\\{" + "note_id" + "\\}", apiClient.escapeString(noteId.toString()));

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

    GenericType<MemberNotes1> localVarReturnType = new GenericType<MemberNotes1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update merge field
   * Update a specific merge field ([audience field](https://mailchimp.com/help/getting-started-with-merge-tags/)) in a list.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param mergeId The id for the merge field. (required)
   * @return MergeField1
   * @throws ApiException if fails to make API call
   */
  public MergeField1 patchListsIdMergeFieldsId(MergeField2 body, String listId, String mergeId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchListsIdMergeFieldsId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling patchListsIdMergeFieldsId");
    }
    // verify the required parameter 'mergeId' is set
    if (mergeId == null) {
      throw new ApiException(400, "Missing the required parameter 'mergeId' when calling patchListsIdMergeFieldsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/merge-fields/{merge_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "merge_id" + "\\}", apiClient.escapeString(mergeId.toString()));

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

    GenericType<MergeField1> localVarReturnType = new GenericType<MergeField1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update segment
   * Update a specific segment in a list.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param segmentId The unique id for the segment. (required)
   * @return List4
   * @throws ApiException if fails to make API call
   */
  public List4 patchListsIdSegmentsId(List5 body, String listId, String segmentId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchListsIdSegmentsId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling patchListsIdSegmentsId");
    }
    // verify the required parameter 'segmentId' is set
    if (segmentId == null) {
      throw new ApiException(400, "Missing the required parameter 'segmentId' when calling patchListsIdSegmentsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments/{segment_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "segment_id" + "\\}", apiClient.escapeString(segmentId.toString()));

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

    GenericType<List4> localVarReturnType = new GenericType<List4>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update webhook
   * Update the settings for an existing webhook.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param webhookId The webhook&#x27;s id. (required)
   * @return ListWebhooks1
   * @throws ApiException if fails to make API call
   */
  public ListWebhooks1 patchListsIdWebhooksId(AddWebhook1 body, String listId, String webhookId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchListsIdWebhooksId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling patchListsIdWebhooksId");
    }
    // verify the required parameter 'webhookId' is set
    if (webhookId == null) {
      throw new ApiException(400, "Missing the required parameter 'webhookId' when calling patchListsIdWebhooksId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/webhooks/{webhook_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "webhook_id" + "\\}", apiClient.escapeString(webhookId.toString()));

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

    GenericType<ListWebhooks1> localVarReturnType = new GenericType<ListWebhooks1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add event
   * Add an event for a list member.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. This endpoint also accepts email addresses. (required)
   * @throws ApiException if fails to make API call
   */
  public void postListMemberEvents(Events body, String listId, String subscriberHash) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListMemberEvents");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListMemberEvents");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling postListMemberEvents");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/events"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
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
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Add or remove member tags
   * Add or remove tags from a list member. If a tag that does not exist is passed in and set as &#x27;active&#x27;, a new tag will be created.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @throws ApiException if fails to make API call
   */
  public void postListMemberTags(MemberTags body, String listId, String subscriberHash) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListMemberTags");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListMemberTags");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling postListMemberTags");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/tags"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
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
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Add list
   * Create a new list in your Mailchimp account.
   * @param body  (required)
   * @return SubscriberList1
   * @throws ApiException if fails to make API call
   */
  public SubscriberList1 postLists(SubscriberList body) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postLists");
    }
    // create path and map variables
    String localVarPath = "/lists";

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

    GenericType<SubscriberList1> localVarReturnType = new GenericType<SubscriberList1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Batch subscribe or unsubscribe
   * Batch subscribe or unsubscribe list members.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param skipMergeValidation If skip_merge_validation is true, member data will be accepted without merge field values, even if the merge field is usually required. This defaults to false. (optional)
   * @param skipDuplicateCheck If skip_duplicate_check is true, we will ignore duplicates sent in the request when using the batch sub/unsub on the lists endpoint. The status of the first appearance in the request will be saved. This defaults to false. (optional)
   * @return BatchUpdateListMembers
   * @throws ApiException if fails to make API call
   */
  public BatchUpdateListMembers postListsId(MembersToSubscribeUnsubscribeTofromAListInBatch body, String listId, Boolean skipMergeValidation, Boolean skipDuplicateCheck) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "skip_merge_validation", skipMergeValidation));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "skip_duplicate_check", skipDuplicateCheck));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<BatchUpdateListMembers> localVarReturnType = new GenericType<BatchUpdateListMembers>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add interest category
   * Create a new interest category.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @return InterestCategory1
   * @throws ApiException if fails to make API call
   */
  public InterestCategory1 postListsIdInterestCategories(InterestCategory body, String listId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdInterestCategories");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdInterestCategories");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<InterestCategory1> localVarReturnType = new GenericType<InterestCategory1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add interest in category
   * Create a new interest or &#x27;group name&#x27; for a specific category.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param interestCategoryId The unique ID for the interest category. (required)
   * @return Interest1
   * @throws ApiException if fails to make API call
   */
  public Interest1 postListsIdInterestCategoriesIdInterests(Interest body, String listId, String interestCategoryId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdInterestCategoriesIdInterests");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdInterestCategoriesIdInterests");
    }
    // verify the required parameter 'interestCategoryId' is set
    if (interestCategoryId == null) {
      throw new ApiException(400, "Missing the required parameter 'interestCategoryId' when calling postListsIdInterestCategoriesIdInterests");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/interest-categories/{interest_category_id}/interests"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "interest_category_id" + "\\}", apiClient.escapeString(interestCategoryId.toString()));

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

    GenericType<Interest1> localVarReturnType = new GenericType<Interest1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add member to list
   * Add a new member to the list.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param skipMergeValidation If skip_merge_validation is true, member data will be accepted without merge field values, even if the merge field is usually required. This defaults to false. (optional)
   * @return ListMembers2
   * @throws ApiException if fails to make API call
   */
  public ListMembers2 postListsIdMembers(AddListMembers1 body, String listId, Boolean skipMergeValidation) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdMembers");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdMembers");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "skip_merge_validation", skipMergeValidation));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<ListMembers2> localVarReturnType = new GenericType<ListMembers2>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Delete list member
   * Delete all personally identifiable information related to a list member, and remove them from a list. This will make it impossible to re-import the list member.
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @throws ApiException if fails to make API call
   */
  public void postListsIdMembersHashActionsDeletePermanent(String listId, String subscriberHash) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdMembersHashActionsDeletePermanent");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling postListsIdMembersHashActionsDeletePermanent");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/actions/delete-permanent"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
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

    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Add member note
   * Add a new note for a specific subscriber.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @return MemberNotes1
   * @throws ApiException if fails to make API call
   */
  public MemberNotes1 postListsIdMembersIdNotes(MemberNotes body, String listId, String subscriberHash) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdMembersIdNotes");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdMembersIdNotes");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling postListsIdMembersIdNotes");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}/notes"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
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
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<MemberNotes1> localVarReturnType = new GenericType<MemberNotes1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add merge field
   * Add a new merge field ([audience field](https://mailchimp.com/help/getting-started-with-merge-tags/)) for a specific list.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @return MergeField1
   * @throws ApiException if fails to make API call
   */
  public MergeField1 postListsIdMergeFields(MergeField body, String listId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdMergeFields");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdMergeFields");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/merge-fields"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<MergeField1> localVarReturnType = new GenericType<MergeField1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add segment
   * Create a new segment in a specific list.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @return List4
   * @throws ApiException if fails to make API call
   */
  public List4 postListsIdSegments(List3 body, String listId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdSegments");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdSegments");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<List4> localVarReturnType = new GenericType<List4>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Batch add or remove members
   * Batch add/remove list members to static segment
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param segmentId The unique id for the segment. (required)
   * @return BatchAddremoveListMembersTofromStaticSegment
   * @throws ApiException if fails to make API call
   */
  public BatchAddremoveListMembersTofromStaticSegment postListsIdSegmentsId(MembersToAddremoveTofromAStaticSegment body, String listId, String segmentId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdSegmentsId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdSegmentsId");
    }
    // verify the required parameter 'segmentId' is set
    if (segmentId == null) {
      throw new ApiException(400, "Missing the required parameter 'segmentId' when calling postListsIdSegmentsId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments/{segment_id}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "segment_id" + "\\}", apiClient.escapeString(segmentId.toString()));

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

    GenericType<BatchAddremoveListMembersTofromStaticSegment> localVarReturnType = new GenericType<BatchAddremoveListMembersTofromStaticSegment>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add member to segment
   * Add a member to a static segment.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param segmentId The unique id for the segment. (required)
   * @return ListMembers
   * @throws ApiException if fails to make API call
   */
  public ListMembers postListsIdSegmentsIdMembers(SegmentIdMembersBody body, String listId, String segmentId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdSegmentsIdMembers");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdSegmentsIdMembers");
    }
    // verify the required parameter 'segmentId' is set
    if (segmentId == null) {
      throw new ApiException(400, "Missing the required parameter 'segmentId' when calling postListsIdSegmentsIdMembers");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments/{segment_id}/members"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "segment_id" + "\\}", apiClient.escapeString(segmentId.toString()));

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

    GenericType<ListMembers> localVarReturnType = new GenericType<ListMembers>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Customize signup form
   * Customize a list&#x27;s default signup form.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @return SignupForm1
   * @throws ApiException if fails to make API call
   */
  public SignupForm1 postListsIdSignupForms(SignupForm body, String listId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdSignupForms");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdSignupForms");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/signup-forms"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<SignupForm1> localVarReturnType = new GenericType<SignupForm1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add webhook
   * Create a new webhook for a specific list.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @return ListWebhooks1
   * @throws ApiException if fails to make API call
   */
  public ListWebhooks1 postListsIdWebhooks(AddWebhook body, String listId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postListsIdWebhooks");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling postListsIdWebhooks");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/webhooks"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

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

    GenericType<ListWebhooks1> localVarReturnType = new GenericType<ListWebhooks1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List segments
   * Get information about all available segments for a specific list.
   * @param listId The unique ID for the list. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param type Limit results based on segment type. (optional)
   * @param sinceCreatedAt Restrict results to segments created after the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param beforeCreatedAt Restrict results to segments created before the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param includeCleaned Include cleaned members in response (optional)
   * @param includeTransactional Include transactional members in response (optional)
   * @param includeUnsubscribed Include unsubscribed members in response (optional)
   * @param sinceUpdatedAt Restrict results to segments update after the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @param beforeUpdatedAt Restrict results to segments update before the set time. Uses ISO 8601 time format: 2015-10-21T15:41:36+00:00. (optional)
   * @return CollectionOfSegments
   * @throws ApiException if fails to make API call
   */
  public CollectionOfSegments previewASegment(String listId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String type, String sinceCreatedAt, String beforeCreatedAt, Boolean includeCleaned, Boolean includeTransactional, Boolean includeUnsubscribed, String sinceUpdatedAt, String beforeUpdatedAt) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling previewASegment");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/segments"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "type", type));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_created_at", sinceCreatedAt));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_created_at", beforeCreatedAt));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_cleaned", includeCleaned));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_transactional", includeTransactional));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_unsubscribed", includeUnsubscribed));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "since_updated_at", sinceUpdatedAt));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "before_updated_at", beforeUpdatedAt));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<CollectionOfSegments> localVarReturnType = new GenericType<CollectionOfSegments>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add or update list member
   * Add or update a list member.
   * @param body  (required)
   * @param listId The unique ID for the list. (required)
   * @param subscriberHash The MD5 hash of the lowercase version of the list member&#x27;s email address. (required)
   * @param skipMergeValidation If skip_merge_validation is true, member data will be accepted without merge field values, even if the merge field is usually required. This defaults to false. (optional)
   * @return ListMembers2
   * @throws ApiException if fails to make API call
   */
  public ListMembers2 putListsIdMembersId(AddListMembers2 body, String listId, String subscriberHash, Boolean skipMergeValidation) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling putListsIdMembersId");
    }
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling putListsIdMembersId");
    }
    // verify the required parameter 'subscriberHash' is set
    if (subscriberHash == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriberHash' when calling putListsIdMembersId");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/members/{subscriber_hash}"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()))
      .replaceAll("\\{" + "subscriber_hash" + "\\}", apiClient.escapeString(subscriberHash.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "skip_merge_validation", skipMergeValidation));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<ListMembers2> localVarReturnType = new GenericType<ListMembers2>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Search for tags on a list by name.
   * Search for tags on a list by name. If no name is provided, will return all tags on the list.
   * @param listId The unique ID for the list. (required)
   * @param name The search query used to filter tags.  The search query will be compared to each tag as a prefix, so all tags that have a name starting with this field will be returned. (optional)
   * @return TagSearchResults
   * @throws ApiException if fails to make API call
   */
  public TagSearchResults searchTagsByName(String listId, String name) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'listId' is set
    if (listId == null) {
      throw new ApiException(400, "Missing the required parameter 'listId' when calling searchTagsByName");
    }
    // create path and map variables
    String localVarPath = "/lists/{list_id}/tag-search"
      .replaceAll("\\{" + "list_id" + "\\}", apiClient.escapeString(listId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "name", name));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<TagSearchResults> localVarReturnType = new GenericType<TagSearchResults>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
