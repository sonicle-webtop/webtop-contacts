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
import com.sonicle.webtop.contacts.mailchimp.cli.model.Address1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/**
 * Information about a specific customer.
 */
@Schema(description = "Information about a specific customer.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class EcommerceCustomer6 {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("email_address")
  private String emailAddress = null;

  @JsonProperty("opt_in_status")
  private Boolean optInStatus = null;

  @JsonProperty("company")
  private String company = null;

  @JsonProperty("first_name")
  private String firstName = null;

  @JsonProperty("last_name")
  private String lastName = null;

  @JsonProperty("orders_count")
  private Integer ordersCount = null;

  @JsonProperty("total_spent")
  private BigDecimal totalSpent = null;

  @JsonProperty("address")
  private Address1 address = null;

  @JsonProperty("created_at")
  private DateTime createdAt = null;

  @JsonProperty("updated_at")
  private DateTime updatedAt = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

   /**
   * A unique identifier for the customer.
   * @return id
  **/
  @Schema(description = "A unique identifier for the customer.")
  public String getId() {
    return id;
  }

   /**
   * The customer&#x27;s email address.
   * @return emailAddress
  **/
  @Schema(description = "The customer's email address.")
  public String getEmailAddress() {
    return emailAddress;
  }

  public EcommerceCustomer6 optInStatus(Boolean optInStatus) {
    this.optInStatus = optInStatus;
    return this;
  }

   /**
   * The customer&#x27;s opt-in status. This value will never overwrite the opt-in status of a pre-existing Mailchimp list member, but will apply to list members that are added through the e-commerce API endpoints. Customers who don&#x27;t opt in to your Mailchimp list [will be added as &#x60;Transactional&#x60; members](https://mailchimp.com/developer/marketing/docs/e-commerce/#customers).
   * @return optInStatus
  **/
  @Schema(description = "The customer's opt-in status. This value will never overwrite the opt-in status of a pre-existing Mailchimp list member, but will apply to list members that are added through the e-commerce API endpoints. Customers who don't opt in to your Mailchimp list [will be added as `Transactional` members](https://mailchimp.com/developer/marketing/docs/e-commerce/#customers).")
  public Boolean isOptInStatus() {
    return optInStatus;
  }

  public void setOptInStatus(Boolean optInStatus) {
    this.optInStatus = optInStatus;
  }

  public EcommerceCustomer6 company(String company) {
    this.company = company;
    return this;
  }

   /**
   * The customer&#x27;s company.
   * @return company
  **/
  @Schema(description = "The customer's company.")
  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public EcommerceCustomer6 firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

   /**
   * The customer&#x27;s first name.
   * @return firstName
  **/
  @Schema(description = "The customer's first name.")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public EcommerceCustomer6 lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

   /**
   * The customer&#x27;s last name.
   * @return lastName
  **/
  @Schema(description = "The customer's last name.")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

   /**
   * The customer&#x27;s total order count.
   * @return ordersCount
  **/
  @Schema(example = "4", description = "The customer's total order count.")
  public Integer getOrdersCount() {
    return ordersCount;
  }

   /**
   * The total amount the customer has spent.
   * @return totalSpent
  **/
  @Schema(example = "100.0", description = "The total amount the customer has spent.")
  public BigDecimal getTotalSpent() {
    return totalSpent;
  }

  public EcommerceCustomer6 address(Address1 address) {
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @Schema(description = "")
  public Address1 getAddress() {
    return address;
  }

  public void setAddress(Address1 address) {
    this.address = address;
  }

   /**
   * The date and time the customer was created in ISO 8601 format.
   * @return createdAt
  **/
  @Schema(example = "2015-07-15T19:28Z", description = "The date and time the customer was created in ISO 8601 format.")
  public DateTime getCreatedAt() {
    return createdAt;
  }

   /**
   * The date and time the customer was last updated in ISO 8601 format.
   * @return updatedAt
  **/
  @Schema(example = "2015-07-15T19:28Z", description = "The date and time the customer was last updated in ISO 8601 format.")
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
    EcommerceCustomer6 ecommerceCustomer6 = (EcommerceCustomer6) o;
    return Objects.equals(this.id, ecommerceCustomer6.id) &&
        Objects.equals(this.emailAddress, ecommerceCustomer6.emailAddress) &&
        Objects.equals(this.optInStatus, ecommerceCustomer6.optInStatus) &&
        Objects.equals(this.company, ecommerceCustomer6.company) &&
        Objects.equals(this.firstName, ecommerceCustomer6.firstName) &&
        Objects.equals(this.lastName, ecommerceCustomer6.lastName) &&
        Objects.equals(this.ordersCount, ecommerceCustomer6.ordersCount) &&
        Objects.equals(this.totalSpent, ecommerceCustomer6.totalSpent) &&
        Objects.equals(this.address, ecommerceCustomer6.address) &&
        Objects.equals(this.createdAt, ecommerceCustomer6.createdAt) &&
        Objects.equals(this.updatedAt, ecommerceCustomer6.updatedAt) &&
        Objects.equals(this._links, ecommerceCustomer6._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, emailAddress, optInStatus, company, firstName, lastName, ordersCount, totalSpent, address, createdAt, updatedAt, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcommerceCustomer6 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    emailAddress: ").append(toIndentedString(emailAddress)).append("\n");
    sb.append("    optInStatus: ").append(toIndentedString(optInStatus)).append("\n");
    sb.append("    company: ").append(toIndentedString(company)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    ordersCount: ").append(toIndentedString(ordersCount)).append("\n");
    sb.append("    totalSpent: ").append(toIndentedString(totalSpent)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
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