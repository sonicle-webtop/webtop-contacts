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
import com.sonicle.webtop.contacts.mailchimp.cli.model.AccountContact;
import com.sonicle.webtop.contacts.mailchimp.cli.model.IndustryStats1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/**
 * The API root resource links to all other resources available in the API.
 */
@Schema(description = "The API root resource links to all other resources available in the API.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class APIRoot {
  @JsonProperty("account_id")
  private String accountId = null;

  @JsonProperty("login_id")
  private String loginId = null;

  @JsonProperty("account_name")
  private String accountName = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("first_name")
  private String firstName = null;

  @JsonProperty("last_name")
  private String lastName = null;

  @JsonProperty("username")
  private String username = null;

  @JsonProperty("avatar_url")
  private String avatarUrl = null;

  @JsonProperty("role")
  private String role = null;

  @JsonProperty("member_since")
  private DateTime memberSince = null;

  /**
   * The type of pricing plan the account is on.
   */
  public enum PricingPlanTypeEnum {
    MONTHLY("monthly"),
    PAY_AS_YOU_GO("pay_as_you_go"),
    FOREVER_FREE("forever_free");

    private String value;

    PricingPlanTypeEnum(String value) {
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
    public static PricingPlanTypeEnum fromValue(String text) {
      for (PricingPlanTypeEnum b : PricingPlanTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("pricing_plan_type")
  private PricingPlanTypeEnum pricingPlanType = null;

  @JsonProperty("first_payment")
  private DateTime firstPayment = null;

  @JsonProperty("account_timezone")
  private String accountTimezone = null;

  @JsonProperty("account_industry")
  private String accountIndustry = null;

  @JsonProperty("contact")
  private AccountContact contact = null;

  @JsonProperty("pro_enabled")
  private Boolean proEnabled = null;

  @JsonProperty("last_login")
  private DateTime lastLogin = null;

  @JsonProperty("total_subscribers")
  private Integer totalSubscribers = null;

  @JsonProperty("industry_stats")
  private IndustryStats1 industryStats = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

   /**
   * The Mailchimp account ID.
   * @return accountId
  **/
  @Schema(description = "The Mailchimp account ID.")
  public String getAccountId() {
    return accountId;
  }

   /**
   * The ID associated with the user who owns this API key. If you can login to multiple accounts, this ID will be the same for each account.
   * @return loginId
  **/
  @Schema(description = "The ID associated with the user who owns this API key. If you can login to multiple accounts, this ID will be the same for each account.")
  public String getLoginId() {
    return loginId;
  }

   /**
   * The name of the account.
   * @return accountName
  **/
  @Schema(description = "The name of the account.")
  public String getAccountName() {
    return accountName;
  }

   /**
   * The account email address.
   * @return email
  **/
  @Schema(description = "The account email address.")
  public String getEmail() {
    return email;
  }

   /**
   * The first name tied to the account.
   * @return firstName
  **/
  @Schema(description = "The first name tied to the account.")
  public String getFirstName() {
    return firstName;
  }

   /**
   * The last name tied to the account.
   * @return lastName
  **/
  @Schema(description = "The last name tied to the account.")
  public String getLastName() {
    return lastName;
  }

   /**
   * The username tied to the account.
   * @return username
  **/
  @Schema(example = "freddie2000", description = "The username tied to the account.")
  public String getUsername() {
    return username;
  }

   /**
   * URL of the avatar for the user.
   * @return avatarUrl
  **/
  @Schema(description = "URL of the avatar for the user.")
  public String getAvatarUrl() {
    return avatarUrl;
  }

   /**
   * The [user role](https://mailchimp.com/help/manage-user-levels-in-your-account/) for the account.
   * @return role
  **/
  @Schema(description = "The [user role](https://mailchimp.com/help/manage-user-levels-in-your-account/) for the account.")
  public String getRole() {
    return role;
  }

   /**
   * The date and time that the account was created in ISO 8601 format.
   * @return memberSince
  **/
  @Schema(example = "2010-01-01T23:59:59Z", description = "The date and time that the account was created in ISO 8601 format.")
  public DateTime getMemberSince() {
    return memberSince;
  }

   /**
   * The type of pricing plan the account is on.
   * @return pricingPlanType
  **/
  @Schema(description = "The type of pricing plan the account is on.")
  public PricingPlanTypeEnum getPricingPlanType() {
    return pricingPlanType;
  }

   /**
   * Date of first payment for monthly plans.
   * @return firstPayment
  **/
  @Schema(example = "2010-01-01T23:59:59Z", description = "Date of first payment for monthly plans.")
  public DateTime getFirstPayment() {
    return firstPayment;
  }

   /**
   * The timezone currently set for the account.
   * @return accountTimezone
  **/
  @Schema(description = "The timezone currently set for the account.")
  public String getAccountTimezone() {
    return accountTimezone;
  }

   /**
   * The user-specified industry associated with the account.
   * @return accountIndustry
  **/
  @Schema(description = "The user-specified industry associated with the account.")
  public String getAccountIndustry() {
    return accountIndustry;
  }

  public APIRoot contact(AccountContact contact) {
    this.contact = contact;
    return this;
  }

   /**
   * Get contact
   * @return contact
  **/
  @Schema(description = "")
  public AccountContact getContact() {
    return contact;
  }

  public void setContact(AccountContact contact) {
    this.contact = contact;
  }

   /**
   * Legacy - whether the account includes [Mailchimp Pro](https://mailchimp.com/help/about-mailchimp-pro/).
   * @return proEnabled
  **/
  @Schema(description = "Legacy - whether the account includes [Mailchimp Pro](https://mailchimp.com/help/about-mailchimp-pro/).")
  public Boolean isProEnabled() {
    return proEnabled;
  }

   /**
   * The date and time of the last login for this account in ISO 8601 format.
   * @return lastLogin
  **/
  @Schema(description = "The date and time of the last login for this account in ISO 8601 format.")
  public DateTime getLastLogin() {
    return lastLogin;
  }

   /**
   * The total number of subscribers across all lists in the account.
   * @return totalSubscribers
  **/
  @Schema(description = "The total number of subscribers across all lists in the account.")
  public Integer getTotalSubscribers() {
    return totalSubscribers;
  }

  public APIRoot industryStats(IndustryStats1 industryStats) {
    this.industryStats = industryStats;
    return this;
  }

   /**
   * Get industryStats
   * @return industryStats
  **/
  @Schema(description = "")
  public IndustryStats1 getIndustryStats() {
    return industryStats;
  }

  public void setIndustryStats(IndustryStats1 industryStats) {
    this.industryStats = industryStats;
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
    APIRoot apIRoot = (APIRoot) o;
    return Objects.equals(this.accountId, apIRoot.accountId) &&
        Objects.equals(this.loginId, apIRoot.loginId) &&
        Objects.equals(this.accountName, apIRoot.accountName) &&
        Objects.equals(this.email, apIRoot.email) &&
        Objects.equals(this.firstName, apIRoot.firstName) &&
        Objects.equals(this.lastName, apIRoot.lastName) &&
        Objects.equals(this.username, apIRoot.username) &&
        Objects.equals(this.avatarUrl, apIRoot.avatarUrl) &&
        Objects.equals(this.role, apIRoot.role) &&
        Objects.equals(this.memberSince, apIRoot.memberSince) &&
        Objects.equals(this.pricingPlanType, apIRoot.pricingPlanType) &&
        Objects.equals(this.firstPayment, apIRoot.firstPayment) &&
        Objects.equals(this.accountTimezone, apIRoot.accountTimezone) &&
        Objects.equals(this.accountIndustry, apIRoot.accountIndustry) &&
        Objects.equals(this.contact, apIRoot.contact) &&
        Objects.equals(this.proEnabled, apIRoot.proEnabled) &&
        Objects.equals(this.lastLogin, apIRoot.lastLogin) &&
        Objects.equals(this.totalSubscribers, apIRoot.totalSubscribers) &&
        Objects.equals(this.industryStats, apIRoot.industryStats) &&
        Objects.equals(this._links, apIRoot._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, loginId, accountName, email, firstName, lastName, username, avatarUrl, role, memberSince, pricingPlanType, firstPayment, accountTimezone, accountIndustry, contact, proEnabled, lastLogin, totalSubscribers, industryStats, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class APIRoot {\n");
    
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    loginId: ").append(toIndentedString(loginId)).append("\n");
    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    avatarUrl: ").append(toIndentedString(avatarUrl)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    memberSince: ").append(toIndentedString(memberSince)).append("\n");
    sb.append("    pricingPlanType: ").append(toIndentedString(pricingPlanType)).append("\n");
    sb.append("    firstPayment: ").append(toIndentedString(firstPayment)).append("\n");
    sb.append("    accountTimezone: ").append(toIndentedString(accountTimezone)).append("\n");
    sb.append("    accountIndustry: ").append(toIndentedString(accountIndustry)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    proEnabled: ").append(toIndentedString(proEnabled)).append("\n");
    sb.append("    lastLogin: ").append(toIndentedString(lastLogin)).append("\n");
    sb.append("    totalSubscribers: ").append(toIndentedString(totalSubscribers)).append("\n");
    sb.append("    industryStats: ").append(toIndentedString(industryStats)).append("\n");
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
