package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;
import com.sonicle.webtop.contacts.mailchimp.cli.model.SubscriberInCustomerJourneysAudience;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class CustomerJourneysApi {
  private ApiClient apiClient;

  public CustomerJourneysApi() {
    this(Configuration.getDefaultApiClient());
  }

  public CustomerJourneysApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Customer Journeys API trigger for a contact
   * A step trigger in a Customer Journey. To use it, create a starting point or step from the Customer Journey builder in the app using the Customer Journeys API condition. We’ll provide a url during the process that includes the {journey_id} and {step_id}. You’ll then be able to use this endpoint to trigger the condition for the posted contact.
   * @param body  (required)
   * @param journeyId The id for the Journey. (required)
   * @param stepId The id for the Step. (required)
   * @return Object
   * @throws ApiException if fails to make API call
   */
  public Object postCustomerJourneysJourneysIdStepsIdActionsTrigger(SubscriberInCustomerJourneysAudience body, Integer journeyId, Integer stepId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postCustomerJourneysJourneysIdStepsIdActionsTrigger");
    }
    // verify the required parameter 'journeyId' is set
    if (journeyId == null) {
      throw new ApiException(400, "Missing the required parameter 'journeyId' when calling postCustomerJourneysJourneysIdStepsIdActionsTrigger");
    }
    // verify the required parameter 'stepId' is set
    if (stepId == null) {
      throw new ApiException(400, "Missing the required parameter 'stepId' when calling postCustomerJourneysJourneysIdStepsIdActionsTrigger");
    }
    // create path and map variables
    String localVarPath = "/customer-journeys/journeys/{journey_id}/steps/{step_id}/actions/trigger"
      .replaceAll("\\{" + "journey_id" + "\\}", apiClient.escapeString(journeyId.toString()))
      .replaceAll("\\{" + "step_id" + "\\}", apiClient.escapeString(stepId.toString()));

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

    GenericType<Object> localVarReturnType = new GenericType<Object>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
