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
import java.math.BigDecimal;
/**
 * The billing address for the order.
 */
@Schema(description = "The billing address for the order.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class BillingAddress {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("address1")
  private String address1 = null;

  @JsonProperty("address2")
  private String address2 = null;

  @JsonProperty("city")
  private String city = null;

  @JsonProperty("province")
  private String province = null;

  @JsonProperty("province_code")
  private String provinceCode = null;

  @JsonProperty("postal_code")
  private String postalCode = null;

  @JsonProperty("country")
  private String country = null;

  @JsonProperty("country_code")
  private String countryCode = null;

  @JsonProperty("longitude")
  private BigDecimal longitude = null;

  @JsonProperty("latitude")
  private BigDecimal latitude = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("company")
  private String company = null;

  public BillingAddress name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name associated with the billing address.
   * @return name
  **/
  @Schema(example = "Freddie Chimpenheimer", description = "The name associated with the billing address.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BillingAddress address1(String address1) {
    this.address1 = address1;
    return this;
  }

   /**
   * The billing address for the order.
   * @return address1
  **/
  @Schema(example = "675 Ponce de Leon Ave NE", description = "The billing address for the order.")
  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public BillingAddress address2(String address2) {
    this.address2 = address2;
    return this;
  }

   /**
   * An additional field for the billing address.
   * @return address2
  **/
  @Schema(example = "Suite 5000", description = "An additional field for the billing address.")
  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public BillingAddress city(String city) {
    this.city = city;
    return this;
  }

   /**
   * The city in the billing address.
   * @return city
  **/
  @Schema(example = "Atlanta", description = "The city in the billing address.")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public BillingAddress province(String province) {
    this.province = province;
    return this;
  }

   /**
   * The state or normalized province in the billing address.
   * @return province
  **/
  @Schema(example = "Georgia", description = "The state or normalized province in the billing address.")
  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public BillingAddress provinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
    return this;
  }

   /**
   * The two-letter code for the province in the billing address.
   * @return provinceCode
  **/
  @Schema(example = "GA", description = "The two-letter code for the province in the billing address.")
  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  public BillingAddress postalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

   /**
   * The postal or zip code in the billing address.
   * @return postalCode
  **/
  @Schema(example = "30308", description = "The postal or zip code in the billing address.")
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public BillingAddress country(String country) {
    this.country = country;
    return this;
  }

   /**
   * The country in the billing address.
   * @return country
  **/
  @Schema(example = "United States", description = "The country in the billing address.")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public BillingAddress countryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

   /**
   * The two-letter code for the country in the billing address.
   * @return countryCode
  **/
  @Schema(example = "US", description = "The two-letter code for the country in the billing address.")
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public BillingAddress longitude(BigDecimal longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * The longitude for the billing address location.
   * @return longitude
  **/
  @Schema(example = "-75.68903", description = "The longitude for the billing address location.")
  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public BillingAddress latitude(BigDecimal latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * The latitude for the billing address location.
   * @return latitude
  **/
  @Schema(example = "45.427408", description = "The latitude for the billing address location.")
  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public BillingAddress phone(String phone) {
    this.phone = phone;
    return this;
  }

   /**
   * The phone number for the billing address
   * @return phone
  **/
  @Schema(example = "8675309", description = "The phone number for the billing address")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public BillingAddress company(String company) {
    this.company = company;
    return this;
  }

   /**
   * The company associated with the billing address.
   * @return company
  **/
  @Schema(description = "The company associated with the billing address.")
  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BillingAddress billingAddress = (BillingAddress) o;
    return Objects.equals(this.name, billingAddress.name) &&
        Objects.equals(this.address1, billingAddress.address1) &&
        Objects.equals(this.address2, billingAddress.address2) &&
        Objects.equals(this.city, billingAddress.city) &&
        Objects.equals(this.province, billingAddress.province) &&
        Objects.equals(this.provinceCode, billingAddress.provinceCode) &&
        Objects.equals(this.postalCode, billingAddress.postalCode) &&
        Objects.equals(this.country, billingAddress.country) &&
        Objects.equals(this.countryCode, billingAddress.countryCode) &&
        Objects.equals(this.longitude, billingAddress.longitude) &&
        Objects.equals(this.latitude, billingAddress.latitude) &&
        Objects.equals(this.phone, billingAddress.phone) &&
        Objects.equals(this.company, billingAddress.company);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, address1, address2, city, province, provinceCode, postalCode, country, countryCode, longitude, latitude, phone, company);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BillingAddress {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    address1: ").append(toIndentedString(address1)).append("\n");
    sb.append("    address2: ").append(toIndentedString(address2)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    province: ").append(toIndentedString(province)).append("\n");
    sb.append("    provinceCode: ").append(toIndentedString(provinceCode)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    company: ").append(toIndentedString(company)).append("\n");
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