/*
 * Mailchimp Marketing API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 3.0.55
 * Contact: apihelp@mailchimp.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.sonicle.webtop.contacts.mailchimp.cli.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Trigger settings for the Automation.
 */
@Schema(description = "Trigger settings for the Automation.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class AutomationTrigger {
  @JsonProperty("workflow_type")
  private String workflowType = null;

  public AutomationTrigger workflowType(String workflowType) {
    this.workflowType = workflowType;
    return this;
  }

   /**
   * The type of Automation workflow. Currently only supports &#x27;abandonedCart&#x27;.
   * @return workflowType
  **/
  @Schema(required = true, description = "The type of Automation workflow. Currently only supports 'abandonedCart'.")
  public String getWorkflowType() {
    return workflowType;
  }

  public void setWorkflowType(String workflowType) {
    this.workflowType = workflowType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AutomationTrigger automationTrigger = (AutomationTrigger) o;
    return Objects.equals(this.workflowType, automationTrigger.workflowType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(workflowType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AutomationTrigger {\n");
    
    sb.append("    workflowType: ").append(toIndentedString(workflowType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}