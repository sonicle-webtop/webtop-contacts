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
import com.sonicle.webtop.contacts.mailchimp.cli.model.Address;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Automations;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ConnectedSite2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/**
 * An individual store in an account.
 */
@Schema(description = "An individual store in an account.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class EcommerceStore3 {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("list_id")
  private String listId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("platform")
  private String platform = null;

  @JsonProperty("domain")
  private String domain = null;

  @JsonProperty("is_syncing")
  private Boolean isSyncing = null;

  @JsonProperty("email_address")
  private String emailAddress = null;

  @JsonProperty("currency_code")
  private String currencyCode = null;

  @JsonProperty("money_format")
  private String moneyFormat = null;

  @JsonProperty("primary_locale")
  private String primaryLocale = null;

  @JsonProperty("timezone")
  private String timezone = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("address")
  private Address address = null;

  @JsonProperty("connected_site")
  private ConnectedSite2 connectedSite = null;

  @JsonProperty("automations")
  private Automations automations = null;

  @JsonProperty("list_is_active")
  private Boolean listIsActive = null;

  @JsonProperty("created_at")
  private DateTime createdAt = null;

  @JsonProperty("updated_at")
  private DateTime updatedAt = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

   /**
   * The unique identifier for the store.
   * @return id
  **/
  @Schema(example = "example_store", description = "The unique identifier for the store.")
  public String getId() {
    return id;
  }

   /**
   * The unique identifier for the list that&#x27;s associated with the store. The &#x60;list_id&#x60; for a specific store can&#x27;t change.
   * @return listId
  **/
  @Schema(example = "1a2df69511", description = "The unique identifier for the list that's associated with the store. The `list_id` for a specific store can't change.")
  public String getListId() {
    return listId;
  }

  public EcommerceStore3 name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name of the store.
   * @return name
  **/
  @Schema(example = "Freddie's Cat Hat Emporium", description = "The name of the store.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public EcommerceStore3 platform(String platform) {
    this.platform = platform;
    return this;
  }

   /**
   * The e-commerce platform of the store.
   * @return platform
  **/
  @Schema(description = "The e-commerce platform of the store.")
  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public EcommerceStore3 domain(String domain) {
    this.domain = domain;
    return this;
  }

   /**
   * The store domain.  The store domain must be unique within a user account.
   * @return domain
  **/
  @Schema(example = "example.com", description = "The store domain.  The store domain must be unique within a user account.")
  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public EcommerceStore3 isSyncing(Boolean isSyncing) {
    this.isSyncing = isSyncing;
    return this;
  }

   /**
   * Whether to disable automations because the store is currently [syncing](https://mailchimp.com/developer/marketing/docs/e-commerce/#pausing-store-automations).
   * @return isSyncing
  **/
  @Schema(description = "Whether to disable automations because the store is currently [syncing](https://mailchimp.com/developer/marketing/docs/e-commerce/#pausing-store-automations).")
  public Boolean isIsSyncing() {
    return isSyncing;
  }

  public void setIsSyncing(Boolean isSyncing) {
    this.isSyncing = isSyncing;
  }

  public EcommerceStore3 emailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

   /**
   * The email address for the store.
   * @return emailAddress
  **/
  @Schema(example = "freddie@mailchimp.com", description = "The email address for the store.")
  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public EcommerceStore3 currencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

   /**
   * The three-letter ISO 4217 code for the currency that the store accepts.
   * @return currencyCode
  **/
  @Schema(example = "USD", description = "The three-letter ISO 4217 code for the currency that the store accepts.")
  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public EcommerceStore3 moneyFormat(String moneyFormat) {
    this.moneyFormat = moneyFormat;
    return this;
  }

   /**
   * The currency format for the store. For example: &#x60;$&#x60;, &#x60;£&#x60;, etc.
   * @return moneyFormat
  **/
  @Schema(example = "$", description = "The currency format for the store. For example: `$`, `£`, etc.")
  public String getMoneyFormat() {
    return moneyFormat;
  }

  public void setMoneyFormat(String moneyFormat) {
    this.moneyFormat = moneyFormat;
  }

  public EcommerceStore3 primaryLocale(String primaryLocale) {
    this.primaryLocale = primaryLocale;
    return this;
  }

   /**
   * The primary locale for the store. For example: &#x60;en&#x60;, &#x60;de&#x60;, etc.
   * @return primaryLocale
  **/
  @Schema(example = "fr", description = "The primary locale for the store. For example: `en`, `de`, etc.")
  public String getPrimaryLocale() {
    return primaryLocale;
  }

  public void setPrimaryLocale(String primaryLocale) {
    this.primaryLocale = primaryLocale;
  }

  public EcommerceStore3 timezone(String timezone) {
    this.timezone = timezone;
    return this;
  }

   /**
   * The timezone for the store.
   * @return timezone
  **/
  @Schema(example = "Eastern", description = "The timezone for the store.")
  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public EcommerceStore3 phone(String phone) {
    this.phone = phone;
    return this;
  }

   /**
   * The store phone number.
   * @return phone
  **/
  @Schema(example = "404-444-4444", description = "The store phone number.")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public EcommerceStore3 address(Address address) {
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @Schema(description = "")
  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public EcommerceStore3 connectedSite(ConnectedSite2 connectedSite) {
    this.connectedSite = connectedSite;
    return this;
  }

   /**
   * Get connectedSite
   * @return connectedSite
  **/
  @Schema(description = "")
  public ConnectedSite2 getConnectedSite() {
    return connectedSite;
  }

  public void setConnectedSite(ConnectedSite2 connectedSite) {
    this.connectedSite = connectedSite;
  }

  public EcommerceStore3 automations(Automations automations) {
    this.automations = automations;
    return this;
  }

   /**
   * Get automations
   * @return automations
  **/
  @Schema(description = "")
  public Automations getAutomations() {
    return automations;
  }

  public void setAutomations(Automations automations) {
    this.automations = automations;
  }

   /**
   * The status of the list connected to the store, namely if it&#x27;s deleted or disabled.
   * @return listIsActive
  **/
  @Schema(description = "The status of the list connected to the store, namely if it's deleted or disabled.")
  public Boolean isListIsActive() {
    return listIsActive;
  }

   /**
   * The date and time the store was created in ISO 8601 format.
   * @return createdAt
  **/
  @Schema(example = "2015-07-15T19:28Z", description = "The date and time the store was created in ISO 8601 format.")
  public DateTime getCreatedAt() {
    return createdAt;
  }

   /**
   * The date and time the store was last updated in ISO 8601 format.
   * @return updatedAt
  **/
  @Schema(example = "2015-07-15T19:28Z", description = "The date and time the store was last updated in ISO 8601 format.")
  public DateTime getUpdatedAt() {
    return updatedAt;
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
    EcommerceStore3 ecommerceStore3 = (EcommerceStore3) o;
    return Objects.equals(this.id, ecommerceStore3.id) &&
        Objects.equals(this.listId, ecommerceStore3.listId) &&
        Objects.equals(this.name, ecommerceStore3.name) &&
        Objects.equals(this.platform, ecommerceStore3.platform) &&
        Objects.equals(this.domain, ecommerceStore3.domain) &&
        Objects.equals(this.isSyncing, ecommerceStore3.isSyncing) &&
        Objects.equals(this.emailAddress, ecommerceStore3.emailAddress) &&
        Objects.equals(this.currencyCode, ecommerceStore3.currencyCode) &&
        Objects.equals(this.moneyFormat, ecommerceStore3.moneyFormat) &&
        Objects.equals(this.primaryLocale, ecommerceStore3.primaryLocale) &&
        Objects.equals(this.timezone, ecommerceStore3.timezone) &&
        Objects.equals(this.phone, ecommerceStore3.phone) &&
        Objects.equals(this.address, ecommerceStore3.address) &&
        Objects.equals(this.connectedSite, ecommerceStore3.connectedSite) &&
        Objects.equals(this.automations, ecommerceStore3.automations) &&
        Objects.equals(this.listIsActive, ecommerceStore3.listIsActive) &&
        Objects.equals(this.createdAt, ecommerceStore3.createdAt) &&
        Objects.equals(this.updatedAt, ecommerceStore3.updatedAt) &&
        Objects.equals(this._links, ecommerceStore3._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, listId, name, platform, domain, isSyncing, emailAddress, currencyCode, moneyFormat, primaryLocale, timezone, phone, address, connectedSite, automations, listIsActive, createdAt, updatedAt, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcommerceStore3 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    listId: ").append(toIndentedString(listId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    isSyncing: ").append(toIndentedString(isSyncing)).append("\n");
    sb.append("    emailAddress: ").append(toIndentedString(emailAddress)).append("\n");
    sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
    sb.append("    moneyFormat: ").append(toIndentedString(moneyFormat)).append("\n");
    sb.append("    primaryLocale: ").append(toIndentedString(primaryLocale)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    connectedSite: ").append(toIndentedString(connectedSite)).append("\n");
    sb.append("    automations: ").append(toIndentedString(automations)).append("\n");
    sb.append("    listIsActive: ").append(toIndentedString(listIsActive)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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