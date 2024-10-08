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
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/**
 * The settings specific to A/B test campaigns.
 */
@Schema(description = "The settings specific to A/B test campaigns.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class ABTestOptions {
  /**
   * The combination that performs the best. This may be determined automatically by click rate, open rate, or total revenue -- or you may choose manually based on the reporting data you find the most valuable. For Multivariate Campaigns testing send_time, winner_criteria is ignored. For Multivariate Campaigns with &#x27;manual&#x27; as the winner_criteria, the winner must be chosen in the Mailchimp web application.
   */
  public enum WinnerCriteriaEnum {
    OPENS("opens"),
    CLICKS("clicks"),
    MANUAL("manual"),
    TOTAL_REVENUE("total_revenue");

    private String value;

    WinnerCriteriaEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static WinnerCriteriaEnum fromValue(String text) {
      for (WinnerCriteriaEnum b : WinnerCriteriaEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("winner_criteria")
  private WinnerCriteriaEnum winnerCriteria = null;

  @JsonProperty("wait_time")
  private Integer waitTime = null;

  @JsonProperty("test_size")
  private Integer testSize = null;

  @JsonProperty("subject_lines")
  private List<String> subjectLines = null;

  @JsonProperty("send_times")
  private List<DateTime> sendTimes = null;

  @JsonProperty("from_names")
  private List<String> fromNames = null;

  @JsonProperty("reply_to_addresses")
  private List<String> replyToAddresses = null;

  public ABTestOptions winnerCriteria(WinnerCriteriaEnum winnerCriteria) {
    this.winnerCriteria = winnerCriteria;
    return this;
  }

   /**
   * The combination that performs the best. This may be determined automatically by click rate, open rate, or total revenue -- or you may choose manually based on the reporting data you find the most valuable. For Multivariate Campaigns testing send_time, winner_criteria is ignored. For Multivariate Campaigns with &#x27;manual&#x27; as the winner_criteria, the winner must be chosen in the Mailchimp web application.
   * @return winnerCriteria
  **/
  @Schema(required = true, description = "The combination that performs the best. This may be determined automatically by click rate, open rate, or total revenue -- or you may choose manually based on the reporting data you find the most valuable. For Multivariate Campaigns testing send_time, winner_criteria is ignored. For Multivariate Campaigns with 'manual' as the winner_criteria, the winner must be chosen in the Mailchimp web application.")
  public WinnerCriteriaEnum getWinnerCriteria() {
    return winnerCriteria;
  }

  public void setWinnerCriteria(WinnerCriteriaEnum winnerCriteria) {
    this.winnerCriteria = winnerCriteria;
  }

  public ABTestOptions waitTime(Integer waitTime) {
    this.waitTime = waitTime;
    return this;
  }

   /**
   * The number of minutes to wait before choosing the winning campaign. The value of wait_time must be greater than 0 and in whole hours, specified in minutes.
   * @return waitTime
  **/
  @Schema(description = "The number of minutes to wait before choosing the winning campaign. The value of wait_time must be greater than 0 and in whole hours, specified in minutes.")
  public Integer getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(Integer waitTime) {
    this.waitTime = waitTime;
  }

  public ABTestOptions testSize(Integer testSize) {
    this.testSize = testSize;
    return this;
  }

   /**
   * The percentage of recipients to send the test combinations to, must be a value between 10 and 100.
   * @return testSize
  **/
  @Schema(description = "The percentage of recipients to send the test combinations to, must be a value between 10 and 100.")
  public Integer getTestSize() {
    return testSize;
  }

  public void setTestSize(Integer testSize) {
    this.testSize = testSize;
  }

  public ABTestOptions subjectLines(List<String> subjectLines) {
    this.subjectLines = subjectLines;
    return this;
  }

  public ABTestOptions addSubjectLinesItem(String subjectLinesItem) {
    if (this.subjectLines == null) {
      this.subjectLines = new ArrayList<>();
    }
    this.subjectLines.add(subjectLinesItem);
    return this;
  }

   /**
   * The possible subject lines to test. If no subject lines are provided, settings.subject_line will be used.
   * @return subjectLines
  **/
  @Schema(description = "The possible subject lines to test. If no subject lines are provided, settings.subject_line will be used.")
  public List<String> getSubjectLines() {
    return subjectLines;
  }

  public void setSubjectLines(List<String> subjectLines) {
    this.subjectLines = subjectLines;
  }

  public ABTestOptions sendTimes(List<DateTime> sendTimes) {
    this.sendTimes = sendTimes;
    return this;
  }

  public ABTestOptions addSendTimesItem(DateTime sendTimesItem) {
    if (this.sendTimes == null) {
      this.sendTimes = new ArrayList<>();
    }
    this.sendTimes.add(sendTimesItem);
    return this;
  }

   /**
   * The possible send times to test. The times provided should be in the format YYYY-MM-DD HH:MM:SS. If send_times are provided to test, the test_size will be set to 100% and winner_criteria will be ignored.
   * @return sendTimes
  **/
  @Schema(description = "The possible send times to test. The times provided should be in the format YYYY-MM-DD HH:MM:SS. If send_times are provided to test, the test_size will be set to 100% and winner_criteria will be ignored.")
  public List<DateTime> getSendTimes() {
    return sendTimes;
  }

  public void setSendTimes(List<DateTime> sendTimes) {
    this.sendTimes = sendTimes;
  }

  public ABTestOptions fromNames(List<String> fromNames) {
    this.fromNames = fromNames;
    return this;
  }

  public ABTestOptions addFromNamesItem(String fromNamesItem) {
    if (this.fromNames == null) {
      this.fromNames = new ArrayList<>();
    }
    this.fromNames.add(fromNamesItem);
    return this;
  }

   /**
   * The possible from names. The number of from_names provided must match the number of reply_to_addresses. If no from_names are provided, settings.from_name will be used.
   * @return fromNames
  **/
  @Schema(description = "The possible from names. The number of from_names provided must match the number of reply_to_addresses. If no from_names are provided, settings.from_name will be used.")
  public List<String> getFromNames() {
    return fromNames;
  }

  public void setFromNames(List<String> fromNames) {
    this.fromNames = fromNames;
  }

  public ABTestOptions replyToAddresses(List<String> replyToAddresses) {
    this.replyToAddresses = replyToAddresses;
    return this;
  }

  public ABTestOptions addReplyToAddressesItem(String replyToAddressesItem) {
    if (this.replyToAddresses == null) {
      this.replyToAddresses = new ArrayList<>();
    }
    this.replyToAddresses.add(replyToAddressesItem);
    return this;
  }

   /**
   * The possible reply-to addresses. The number of reply_to_addresses provided must match the number of from_names. If no reply_to_addresses are provided, settings.reply_to will be used.
   * @return replyToAddresses
  **/
  @Schema(description = "The possible reply-to addresses. The number of reply_to_addresses provided must match the number of from_names. If no reply_to_addresses are provided, settings.reply_to will be used.")
  public List<String> getReplyToAddresses() {
    return replyToAddresses;
  }

  public void setReplyToAddresses(List<String> replyToAddresses) {
    this.replyToAddresses = replyToAddresses;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ABTestOptions abTestOptions = (ABTestOptions) o;
    return Objects.equals(this.winnerCriteria, abTestOptions.winnerCriteria) &&
        Objects.equals(this.waitTime, abTestOptions.waitTime) &&
        Objects.equals(this.testSize, abTestOptions.testSize) &&
        Objects.equals(this.subjectLines, abTestOptions.subjectLines) &&
        Objects.equals(this.sendTimes, abTestOptions.sendTimes) &&
        Objects.equals(this.fromNames, abTestOptions.fromNames) &&
        Objects.equals(this.replyToAddresses, abTestOptions.replyToAddresses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(winnerCriteria, waitTime, testSize, subjectLines, sendTimes, fromNames, replyToAddresses);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ABTestOptions {\n");
    
    sb.append("    winnerCriteria: ").append(toIndentedString(winnerCriteria)).append("\n");
    sb.append("    waitTime: ").append(toIndentedString(waitTime)).append("\n");
    sb.append("    testSize: ").append(toIndentedString(testSize)).append("\n");
    sb.append("    subjectLines: ").append(toIndentedString(subjectLines)).append("\n");
    sb.append("    sendTimes: ").append(toIndentedString(sendTimes)).append("\n");
    sb.append("    fromNames: ").append(toIndentedString(fromNames)).append("\n");
    sb.append("    replyToAddresses: ").append(toIndentedString(replyToAddresses)).append("\n");
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
