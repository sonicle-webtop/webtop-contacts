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
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * One day&#x27;s worth of list activity. Doesn&#x27;t include Automation activity.
 */
@Schema(description = "One day's worth of list activity. Doesn't include Automation activity.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class DailyListActivity {
  @JsonProperty("day")
  private String day = null;

  @JsonProperty("emails_sent")
  private Integer emailsSent = null;

  @JsonProperty("unique_opens")
  private Integer uniqueOpens = null;

  @JsonProperty("recipient_clicks")
  private Integer recipientClicks = null;

  @JsonProperty("hard_bounce")
  private Integer hardBounce = null;

  @JsonProperty("soft_bounce")
  private Integer softBounce = null;

  @JsonProperty("subs")
  private Integer subs = null;

  @JsonProperty("unsubs")
  private Integer unsubs = null;

  @JsonProperty("other_adds")
  private Integer otherAdds = null;

  @JsonProperty("other_removes")
  private Integer otherRemoves = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

   /**
   * The date for the activity summary.
   * @return day
  **/
  @Schema(description = "The date for the activity summary.")
  public String getDay() {
    return day;
  }

   /**
   * The total number of emails sent on the date for the activity summary.
   * @return emailsSent
  **/
  @Schema(description = "The total number of emails sent on the date for the activity summary.")
  public Integer getEmailsSent() {
    return emailsSent;
  }

   /**
   * The number of unique opens.
   * @return uniqueOpens
  **/
  @Schema(description = "The number of unique opens.")
  public Integer getUniqueOpens() {
    return uniqueOpens;
  }

   /**
   * The number of clicks.
   * @return recipientClicks
  **/
  @Schema(description = "The number of clicks.")
  public Integer getRecipientClicks() {
    return recipientClicks;
  }

   /**
   * The number of hard bounces.
   * @return hardBounce
  **/
  @Schema(description = "The number of hard bounces.")
  public Integer getHardBounce() {
    return hardBounce;
  }

   /**
   * The number of soft bounces
   * @return softBounce
  **/
  @Schema(description = "The number of soft bounces")
  public Integer getSoftBounce() {
    return softBounce;
  }

   /**
   * The number of subscribes.
   * @return subs
  **/
  @Schema(description = "The number of subscribes.")
  public Integer getSubs() {
    return subs;
  }

   /**
   * The number of unsubscribes.
   * @return unsubs
  **/
  @Schema(description = "The number of unsubscribes.")
  public Integer getUnsubs() {
    return unsubs;
  }

   /**
   * The number of subscribers who may have been added outside of the [double opt-in process](https://mailchimp.com/help/about-double-opt-in/), such as imports or API activity.
   * @return otherAdds
  **/
  @Schema(description = "The number of subscribers who may have been added outside of the [double opt-in process](https://mailchimp.com/help/about-double-opt-in/), such as imports or API activity.")
  public Integer getOtherAdds() {
    return otherAdds;
  }

   /**
   * The number of subscribers who may have been removed outside of unsubscribing or reporting an email as spam (for example, deleted subscribers).
   * @return otherRemoves
  **/
  @Schema(description = "The number of subscribers who may have been removed outside of unsubscribing or reporting an email as spam (for example, deleted subscribers).")
  public Integer getOtherRemoves() {
    return otherRemoves;
  }

   /**
   * A list of link types and descriptions for the API schema documents.
   * @return _links
  **/
  @Schema(description = "A list of link types and descriptions for the API schema documents.")
  public List<ResourceLink> getLinks() {
    return _links;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DailyListActivity dailyListActivity = (DailyListActivity) o;
    return Objects.equals(this.day, dailyListActivity.day) &&
        Objects.equals(this.emailsSent, dailyListActivity.emailsSent) &&
        Objects.equals(this.uniqueOpens, dailyListActivity.uniqueOpens) &&
        Objects.equals(this.recipientClicks, dailyListActivity.recipientClicks) &&
        Objects.equals(this.hardBounce, dailyListActivity.hardBounce) &&
        Objects.equals(this.softBounce, dailyListActivity.softBounce) &&
        Objects.equals(this.subs, dailyListActivity.subs) &&
        Objects.equals(this.unsubs, dailyListActivity.unsubs) &&
        Objects.equals(this.otherAdds, dailyListActivity.otherAdds) &&
        Objects.equals(this.otherRemoves, dailyListActivity.otherRemoves) &&
        Objects.equals(this._links, dailyListActivity._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(day, emailsSent, uniqueOpens, recipientClicks, hardBounce, softBounce, subs, unsubs, otherAdds, otherRemoves, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DailyListActivity {\n");
    
    sb.append("    day: ").append(toIndentedString(day)).append("\n");
    sb.append("    emailsSent: ").append(toIndentedString(emailsSent)).append("\n");
    sb.append("    uniqueOpens: ").append(toIndentedString(uniqueOpens)).append("\n");
    sb.append("    recipientClicks: ").append(toIndentedString(recipientClicks)).append("\n");
    sb.append("    hardBounce: ").append(toIndentedString(hardBounce)).append("\n");
    sb.append("    softBounce: ").append(toIndentedString(softBounce)).append("\n");
    sb.append("    subs: ").append(toIndentedString(subs)).append("\n");
    sb.append("    unsubs: ").append(toIndentedString(unsubs)).append("\n");
    sb.append("    otherAdds: ").append(toIndentedString(otherAdds)).append("\n");
    sb.append("    otherRemoves: ").append(toIndentedString(otherRemoves)).append("\n");
    sb.append("    _links: ").append(toIndentedString(_links)).append("\n");
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
