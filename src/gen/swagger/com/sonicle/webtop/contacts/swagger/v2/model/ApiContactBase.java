package com.sonicle.webtop.contacts.swagger.v2.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;

import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represent a contact object with base updateable fields.
 **/
@ApiModel(description = "Represent a contact object with base updateable fields.")
@JsonTypeName("ContactBase")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-01-31T17:20:03.694+01:00[Europe/Berlin]")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiContactBase   {
  private @Valid String displayName;
  private @Valid String title;
  private @Valid String firstName;
  private @Valid String lastName;
  private @Valid String nickname;
  private @Valid String mobile;
  private @Valid String pager1;
  private @Valid String pager2;
  private @Valid String email1;
  private @Valid String email2;
  private @Valid String email3;
  private @Valid String im1;
  private @Valid String im2;
  private @Valid String im3;
  private @Valid String workAddress;
  private @Valid String workPostalCode;
  private @Valid String workCity;
  private @Valid String workState;
  private @Valid String workCountry;
  private @Valid String workTelephone1;
  private @Valid String workTelephone2;
  private @Valid String workFax;
  private @Valid String homeAddress;
  private @Valid String homePostalCode;
  private @Valid String homeCity;
  private @Valid String homeState;
  private @Valid String homeCountry;
  private @Valid String homeTelephone1;
  private @Valid String homeTelephone2;
  private @Valid String homeFax;
  private @Valid String otherAddress;
  private @Valid String otherPostalCode;
  private @Valid String otherCity;
  private @Valid String otherState;
  private @Valid String otherCountry;
  private @Valid String companyId;
  private @Valid String companyName;
  private @Valid String function;
  private @Valid String department;
  private @Valid String assistantName;
  private @Valid String assistantTelephone;
  private @Valid String managerName;
  private @Valid String partnerName;
  private @Valid String birthday;
  private @Valid String anniversary;
  private @Valid String url;
  private @Valid String notes;

  /**
   * The contact&#39;s display name.
   **/
  public ApiContactBase displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's display name.")
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * The contact&#39;s title.
   **/
  public ApiContactBase title(String title) {
    this.title = title;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's title.")
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * The contact&#39;s first-name.
   **/
  public ApiContactBase firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's first-name.")
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  @JsonProperty("firstName")
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * The contact&#39;s last-name.
   **/
  public ApiContactBase lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's last-name.")
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  @JsonProperty("lastName")
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * The contact&#39;s nickname.
   **/
  public ApiContactBase nickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's nickname.")
  @JsonProperty("nickname")
  public String getNickname() {
    return nickname;
  }

  @JsonProperty("nickname")
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  /**
   * The contact&#39;s mobile number.
   **/
  public ApiContactBase mobile(String mobile) {
    this.mobile = mobile;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's mobile number.")
  @JsonProperty("mobile")
  public String getMobile() {
    return mobile;
  }

  @JsonProperty("mobile")
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /**
   * The contact&#39;s pager number #1.
   **/
  public ApiContactBase pager1(String pager1) {
    this.pager1 = pager1;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's pager number #1.")
  @JsonProperty("pager1")
  public String getPager1() {
    return pager1;
  }

  @JsonProperty("pager1")
  public void setPager1(String pager1) {
    this.pager1 = pager1;
  }

  /**
   * The contact&#39;s pager number #2.
   **/
  public ApiContactBase pager2(String pager2) {
    this.pager2 = pager2;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's pager number #2.")
  @JsonProperty("pager2")
  public String getPager2() {
    return pager2;
  }

  @JsonProperty("pager2")
  public void setPager2(String pager2) {
    this.pager2 = pager2;
  }

  /**
   * The contact&#39;s email address #1.
   **/
  public ApiContactBase email1(String email1) {
    this.email1 = email1;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's email address #1.")
  @JsonProperty("email1")
  public String getEmail1() {
    return email1;
  }

  @JsonProperty("email1")
  public void setEmail1(String email1) {
    this.email1 = email1;
  }

  /**
   * The contact&#39;s email address #2.
   **/
  public ApiContactBase email2(String email2) {
    this.email2 = email2;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's email address #2.")
  @JsonProperty("email2")
  public String getEmail2() {
    return email2;
  }

  @JsonProperty("email2")
  public void setEmail2(String email2) {
    this.email2 = email2;
  }

  /**
   * The contact&#39;s email address #3.
   **/
  public ApiContactBase email3(String email3) {
    this.email3 = email3;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's email address #3.")
  @JsonProperty("email3")
  public String getEmail3() {
    return email3;
  }

  @JsonProperty("email3")
  public void setEmail3(String email3) {
    this.email3 = email3;
  }

  /**
   * The contact&#39;s IM #1.
   **/
  public ApiContactBase im1(String im1) {
    this.im1 = im1;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's IM #1.")
  @JsonProperty("im1")
  public String getIm1() {
    return im1;
  }

  @JsonProperty("im1")
  public void setIm1(String im1) {
    this.im1 = im1;
  }

  /**
   * The contact&#39;s IM #2.
   **/
  public ApiContactBase im2(String im2) {
    this.im2 = im2;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's IM #2.")
  @JsonProperty("im2")
  public String getIm2() {
    return im2;
  }

  @JsonProperty("im2")
  public void setIm2(String im2) {
    this.im2 = im2;
  }

  /**
   * The contact&#39;s IM #3.
   **/
  public ApiContactBase im3(String im3) {
    this.im3 = im3;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's IM #3.")
  @JsonProperty("im3")
  public String getIm3() {
    return im3;
  }

  @JsonProperty("im3")
  public void setIm3(String im3) {
    this.im3 = im3;
  }

  /**
   * Contact&#39;s work address.
   **/
  public ApiContactBase workAddress(String workAddress) {
    this.workAddress = workAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's work address.")
  @JsonProperty("workAddress")
  public String getWorkAddress() {
    return workAddress;
  }

  @JsonProperty("workAddress")
  public void setWorkAddress(String workAddress) {
    this.workAddress = workAddress;
  }

  /**
   * Contact&#39;s work address postal-code.
   **/
  public ApiContactBase workPostalCode(String workPostalCode) {
    this.workPostalCode = workPostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's work address postal-code.")
  @JsonProperty("workPostalCode")
  public String getWorkPostalCode() {
    return workPostalCode;
  }

  @JsonProperty("workPostalCode")
  public void setWorkPostalCode(String workPostalCode) {
    this.workPostalCode = workPostalCode;
  }

  /**
   * Contact&#39;s work address city.
   **/
  public ApiContactBase workCity(String workCity) {
    this.workCity = workCity;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's work address city.")
  @JsonProperty("workCity")
  public String getWorkCity() {
    return workCity;
  }

  @JsonProperty("workCity")
  public void setWorkCity(String workCity) {
    this.workCity = workCity;
  }

  /**
   * Contact&#39;s work address state.
   **/
  public ApiContactBase workState(String workState) {
    this.workState = workState;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's work address state.")
  @JsonProperty("workState")
  public String getWorkState() {
    return workState;
  }

  @JsonProperty("workState")
  public void setWorkState(String workState) {
    this.workState = workState;
  }

  /**
   * Contact&#39;s work address country.
   **/
  public ApiContactBase workCountry(String workCountry) {
    this.workCountry = workCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's work address country.")
  @JsonProperty("workCountry")
  public String getWorkCountry() {
    return workCountry;
  }

  @JsonProperty("workCountry")
  public void setWorkCountry(String workCountry) {
    this.workCountry = workCountry;
  }

  /**
   * Contact&#39;s work telephone number #1.
   **/
  public ApiContactBase workTelephone1(String workTelephone1) {
    this.workTelephone1 = workTelephone1;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's work telephone number #1.")
  @JsonProperty("workTelephone1")
  public String getWorkTelephone1() {
    return workTelephone1;
  }

  @JsonProperty("workTelephone1")
  public void setWorkTelephone1(String workTelephone1) {
    this.workTelephone1 = workTelephone1;
  }

  /**
   * Contact&#39;s work telephone number #2.
   **/
  public ApiContactBase workTelephone2(String workTelephone2) {
    this.workTelephone2 = workTelephone2;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's work telephone number #2.")
  @JsonProperty("workTelephone2")
  public String getWorkTelephone2() {
    return workTelephone2;
  }

  @JsonProperty("workTelephone2")
  public void setWorkTelephone2(String workTelephone2) {
    this.workTelephone2 = workTelephone2;
  }

  /**
   * Contact&#39;s work fax number.
   **/
  public ApiContactBase workFax(String workFax) {
    this.workFax = workFax;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's work fax number.")
  @JsonProperty("workFax")
  public String getWorkFax() {
    return workFax;
  }

  @JsonProperty("workFax")
  public void setWorkFax(String workFax) {
    this.workFax = workFax;
  }

  /**
   * Contact&#39;s home address.
   **/
  public ApiContactBase homeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's home address.")
  @JsonProperty("homeAddress")
  public String getHomeAddress() {
    return homeAddress;
  }

  @JsonProperty("homeAddress")
  public void setHomeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
  }

  /**
   * Contact&#39;s home address postal-code.
   **/
  public ApiContactBase homePostalCode(String homePostalCode) {
    this.homePostalCode = homePostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's home address postal-code.")
  @JsonProperty("homePostalCode")
  public String getHomePostalCode() {
    return homePostalCode;
  }

  @JsonProperty("homePostalCode")
  public void setHomePostalCode(String homePostalCode) {
    this.homePostalCode = homePostalCode;
  }

  /**
   * Contact&#39;s home address city.
   **/
  public ApiContactBase homeCity(String homeCity) {
    this.homeCity = homeCity;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's home address city.")
  @JsonProperty("homeCity")
  public String getHomeCity() {
    return homeCity;
  }

  @JsonProperty("homeCity")
  public void setHomeCity(String homeCity) {
    this.homeCity = homeCity;
  }

  /**
   * Contact&#39;s home address state.
   **/
  public ApiContactBase homeState(String homeState) {
    this.homeState = homeState;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's home address state.")
  @JsonProperty("homeState")
  public String getHomeState() {
    return homeState;
  }

  @JsonProperty("homeState")
  public void setHomeState(String homeState) {
    this.homeState = homeState;
  }

  /**
   * Contact&#39;s home address country.
   **/
  public ApiContactBase homeCountry(String homeCountry) {
    this.homeCountry = homeCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's home address country.")
  @JsonProperty("homeCountry")
  public String getHomeCountry() {
    return homeCountry;
  }

  @JsonProperty("homeCountry")
  public void setHomeCountry(String homeCountry) {
    this.homeCountry = homeCountry;
  }

  /**
   * Contact&#39;s home telephone number #1.
   **/
  public ApiContactBase homeTelephone1(String homeTelephone1) {
    this.homeTelephone1 = homeTelephone1;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's home telephone number #1.")
  @JsonProperty("homeTelephone1")
  public String getHomeTelephone1() {
    return homeTelephone1;
  }

  @JsonProperty("homeTelephone1")
  public void setHomeTelephone1(String homeTelephone1) {
    this.homeTelephone1 = homeTelephone1;
  }

  /**
   * Contact&#39;s home telephone number #2.
   **/
  public ApiContactBase homeTelephone2(String homeTelephone2) {
    this.homeTelephone2 = homeTelephone2;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's home telephone number #2.")
  @JsonProperty("homeTelephone2")
  public String getHomeTelephone2() {
    return homeTelephone2;
  }

  @JsonProperty("homeTelephone2")
  public void setHomeTelephone2(String homeTelephone2) {
    this.homeTelephone2 = homeTelephone2;
  }

  /**
   * Contact&#39;s home fax number.
   **/
  public ApiContactBase homeFax(String homeFax) {
    this.homeFax = homeFax;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's home fax number.")
  @JsonProperty("homeFax")
  public String getHomeFax() {
    return homeFax;
  }

  @JsonProperty("homeFax")
  public void setHomeFax(String homeFax) {
    this.homeFax = homeFax;
  }

  /**
   * Contact&#39;s other address.
   **/
  public ApiContactBase otherAddress(String otherAddress) {
    this.otherAddress = otherAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's other address.")
  @JsonProperty("otherAddress")
  public String getOtherAddress() {
    return otherAddress;
  }

  @JsonProperty("otherAddress")
  public void setOtherAddress(String otherAddress) {
    this.otherAddress = otherAddress;
  }

  /**
   * Contact&#39;s other address postal-code.
   **/
  public ApiContactBase otherPostalCode(String otherPostalCode) {
    this.otherPostalCode = otherPostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's other address postal-code.")
  @JsonProperty("otherPostalCode")
  public String getOtherPostalCode() {
    return otherPostalCode;
  }

  @JsonProperty("otherPostalCode")
  public void setOtherPostalCode(String otherPostalCode) {
    this.otherPostalCode = otherPostalCode;
  }

  /**
   * Contact&#39;s other address city.
   **/
  public ApiContactBase otherCity(String otherCity) {
    this.otherCity = otherCity;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's other address city.")
  @JsonProperty("otherCity")
  public String getOtherCity() {
    return otherCity;
  }

  @JsonProperty("otherCity")
  public void setOtherCity(String otherCity) {
    this.otherCity = otherCity;
  }

  /**
   * Contact&#39;s other address state.
   **/
  public ApiContactBase otherState(String otherState) {
    this.otherState = otherState;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's other address state.")
  @JsonProperty("otherState")
  public String getOtherState() {
    return otherState;
  }

  @JsonProperty("otherState")
  public void setOtherState(String otherState) {
    this.otherState = otherState;
  }

  /**
   * Contact&#39;s other address country.
   **/
  public ApiContactBase otherCountry(String otherCountry) {
    this.otherCountry = otherCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's other address country.")
  @JsonProperty("otherCountry")
  public String getOtherCountry() {
    return otherCountry;
  }

  @JsonProperty("otherCountry")
  public void setOtherCountry(String otherCountry) {
    this.otherCountry = otherCountry;
  }

  /**
   * The ID of the contact&#39;s company.
   **/
  public ApiContactBase companyId(String companyId) {
    this.companyId = companyId;
    return this;
  }

  
  @ApiModelProperty(value = "The ID of the contact's company.")
  @JsonProperty("companyId")
  public String getCompanyId() {
    return companyId;
  }

  @JsonProperty("companyId")
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  /**
   * The name of the contact&#39;s company.
   **/
  public ApiContactBase companyName(String companyName) {
    this.companyName = companyName;
    return this;
  }

  
  @ApiModelProperty(value = "The name of the contact's company.")
  @JsonProperty("companyName")
  public String getCompanyName() {
    return companyName;
  }

  @JsonProperty("companyName")
  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  /**
   * The contact&#39;s job title.
   **/
  public ApiContactBase function(String function) {
    this.function = function;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's job title.")
  @JsonProperty("function")
  public String getFunction() {
    return function;
  }

  @JsonProperty("function")
  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * The contact&#39;s department.
   **/
  public ApiContactBase department(String department) {
    this.department = department;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's department.")
  @JsonProperty("department")
  public String getDepartment() {
    return department;
  }

  @JsonProperty("department")
  public void setDepartment(String department) {
    this.department = department;
  }

  /**
   * The name of the contact&#39;s assistant.
   **/
  public ApiContactBase assistantName(String assistantName) {
    this.assistantName = assistantName;
    return this;
  }

  
  @ApiModelProperty(value = "The name of the contact's assistant.")
  @JsonProperty("assistantName")
  public String getAssistantName() {
    return assistantName;
  }

  @JsonProperty("assistantName")
  public void setAssistantName(String assistantName) {
    this.assistantName = assistantName;
  }

  /**
   * The telephone number of the contact&#39;s assistant.
   **/
  public ApiContactBase assistantTelephone(String assistantTelephone) {
    this.assistantTelephone = assistantTelephone;
    return this;
  }

  
  @ApiModelProperty(value = "The telephone number of the contact's assistant.")
  @JsonProperty("assistantTelephone")
  public String getAssistantTelephone() {
    return assistantTelephone;
  }

  @JsonProperty("assistantTelephone")
  public void setAssistantTelephone(String assistantTelephone) {
    this.assistantTelephone = assistantTelephone;
  }

  /**
   * The name of the contact&#39;s manager.
   **/
  public ApiContactBase managerName(String managerName) {
    this.managerName = managerName;
    return this;
  }

  
  @ApiModelProperty(value = "The name of the contact's manager.")
  @JsonProperty("managerName")
  public String getManagerName() {
    return managerName;
  }

  @JsonProperty("managerName")
  public void setManagerName(String managerName) {
    this.managerName = managerName;
  }

  /**
   * The name of the contact&#39;s spouse/partner.
   **/
  public ApiContactBase partnerName(String partnerName) {
    this.partnerName = partnerName;
    return this;
  }

  
  @ApiModelProperty(value = "The name of the contact's spouse/partner.")
  @JsonProperty("partnerName")
  public String getPartnerName() {
    return partnerName;
  }

  @JsonProperty("partnerName")
  public void setPartnerName(String partnerName) {
    this.partnerName = partnerName;
  }

  /**
   * The contact&#39;s birthday date in ISO 8601 format.
   **/
  public ApiContactBase birthday(String birthday) {
    this.birthday = birthday;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's birthday date in ISO 8601 format.")
  @JsonProperty("birthday")
  public String getBirthday() {
    return birthday;
  }

  @JsonProperty("birthday")
  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  /**
   * The contact&#39;s anniversary date in ISO 8601 format.
   **/
  public ApiContactBase anniversary(String anniversary) {
    this.anniversary = anniversary;
    return this;
  }

  
  @ApiModelProperty(value = "The contact's anniversary date in ISO 8601 format.")
  @JsonProperty("anniversary")
  public String getAnniversary() {
    return anniversary;
  }

  @JsonProperty("anniversary")
  public void setAnniversary(String anniversary) {
    this.anniversary = anniversary;
  }

  /**
   * Contact&#39;s reference URL.
   **/
  public ApiContactBase url(String url) {
    this.url = url;
    return this;
  }

  
  @ApiModelProperty(value = "Contact's reference URL.")
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * The user&#39;s notes about the contact.
   **/
  public ApiContactBase notes(String notes) {
    this.notes = notes;
    return this;
  }

  
  @ApiModelProperty(value = "The user's notes about the contact.")
  @JsonProperty("notes")
  public String getNotes() {
    return notes;
  }

  @JsonProperty("notes")
  public void setNotes(String notes) {
    this.notes = notes;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiContactBase contactBase = (ApiContactBase) o;
    return Objects.equals(this.displayName, contactBase.displayName) &&
        Objects.equals(this.title, contactBase.title) &&
        Objects.equals(this.firstName, contactBase.firstName) &&
        Objects.equals(this.lastName, contactBase.lastName) &&
        Objects.equals(this.nickname, contactBase.nickname) &&
        Objects.equals(this.mobile, contactBase.mobile) &&
        Objects.equals(this.pager1, contactBase.pager1) &&
        Objects.equals(this.pager2, contactBase.pager2) &&
        Objects.equals(this.email1, contactBase.email1) &&
        Objects.equals(this.email2, contactBase.email2) &&
        Objects.equals(this.email3, contactBase.email3) &&
        Objects.equals(this.im1, contactBase.im1) &&
        Objects.equals(this.im2, contactBase.im2) &&
        Objects.equals(this.im3, contactBase.im3) &&
        Objects.equals(this.workAddress, contactBase.workAddress) &&
        Objects.equals(this.workPostalCode, contactBase.workPostalCode) &&
        Objects.equals(this.workCity, contactBase.workCity) &&
        Objects.equals(this.workState, contactBase.workState) &&
        Objects.equals(this.workCountry, contactBase.workCountry) &&
        Objects.equals(this.workTelephone1, contactBase.workTelephone1) &&
        Objects.equals(this.workTelephone2, contactBase.workTelephone2) &&
        Objects.equals(this.workFax, contactBase.workFax) &&
        Objects.equals(this.homeAddress, contactBase.homeAddress) &&
        Objects.equals(this.homePostalCode, contactBase.homePostalCode) &&
        Objects.equals(this.homeCity, contactBase.homeCity) &&
        Objects.equals(this.homeState, contactBase.homeState) &&
        Objects.equals(this.homeCountry, contactBase.homeCountry) &&
        Objects.equals(this.homeTelephone1, contactBase.homeTelephone1) &&
        Objects.equals(this.homeTelephone2, contactBase.homeTelephone2) &&
        Objects.equals(this.homeFax, contactBase.homeFax) &&
        Objects.equals(this.otherAddress, contactBase.otherAddress) &&
        Objects.equals(this.otherPostalCode, contactBase.otherPostalCode) &&
        Objects.equals(this.otherCity, contactBase.otherCity) &&
        Objects.equals(this.otherState, contactBase.otherState) &&
        Objects.equals(this.otherCountry, contactBase.otherCountry) &&
        Objects.equals(this.companyId, contactBase.companyId) &&
        Objects.equals(this.companyName, contactBase.companyName) &&
        Objects.equals(this.function, contactBase.function) &&
        Objects.equals(this.department, contactBase.department) &&
        Objects.equals(this.assistantName, contactBase.assistantName) &&
        Objects.equals(this.assistantTelephone, contactBase.assistantTelephone) &&
        Objects.equals(this.managerName, contactBase.managerName) &&
        Objects.equals(this.partnerName, contactBase.partnerName) &&
        Objects.equals(this.birthday, contactBase.birthday) &&
        Objects.equals(this.anniversary, contactBase.anniversary) &&
        Objects.equals(this.url, contactBase.url) &&
        Objects.equals(this.notes, contactBase.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(displayName, title, firstName, lastName, nickname, mobile, pager1, pager2, email1, email2, email3, im1, im2, im3, workAddress, workPostalCode, workCity, workState, workCountry, workTelephone1, workTelephone2, workFax, homeAddress, homePostalCode, homeCity, homeState, homeCountry, homeTelephone1, homeTelephone2, homeFax, otherAddress, otherPostalCode, otherCity, otherState, otherCountry, companyId, companyName, function, department, assistantName, assistantTelephone, managerName, partnerName, birthday, anniversary, url, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiContactBase {\n");
    
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    mobile: ").append(toIndentedString(mobile)).append("\n");
    sb.append("    pager1: ").append(toIndentedString(pager1)).append("\n");
    sb.append("    pager2: ").append(toIndentedString(pager2)).append("\n");
    sb.append("    email1: ").append(toIndentedString(email1)).append("\n");
    sb.append("    email2: ").append(toIndentedString(email2)).append("\n");
    sb.append("    email3: ").append(toIndentedString(email3)).append("\n");
    sb.append("    im1: ").append(toIndentedString(im1)).append("\n");
    sb.append("    im2: ").append(toIndentedString(im2)).append("\n");
    sb.append("    im3: ").append(toIndentedString(im3)).append("\n");
    sb.append("    workAddress: ").append(toIndentedString(workAddress)).append("\n");
    sb.append("    workPostalCode: ").append(toIndentedString(workPostalCode)).append("\n");
    sb.append("    workCity: ").append(toIndentedString(workCity)).append("\n");
    sb.append("    workState: ").append(toIndentedString(workState)).append("\n");
    sb.append("    workCountry: ").append(toIndentedString(workCountry)).append("\n");
    sb.append("    workTelephone1: ").append(toIndentedString(workTelephone1)).append("\n");
    sb.append("    workTelephone2: ").append(toIndentedString(workTelephone2)).append("\n");
    sb.append("    workFax: ").append(toIndentedString(workFax)).append("\n");
    sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
    sb.append("    homePostalCode: ").append(toIndentedString(homePostalCode)).append("\n");
    sb.append("    homeCity: ").append(toIndentedString(homeCity)).append("\n");
    sb.append("    homeState: ").append(toIndentedString(homeState)).append("\n");
    sb.append("    homeCountry: ").append(toIndentedString(homeCountry)).append("\n");
    sb.append("    homeTelephone1: ").append(toIndentedString(homeTelephone1)).append("\n");
    sb.append("    homeTelephone2: ").append(toIndentedString(homeTelephone2)).append("\n");
    sb.append("    homeFax: ").append(toIndentedString(homeFax)).append("\n");
    sb.append("    otherAddress: ").append(toIndentedString(otherAddress)).append("\n");
    sb.append("    otherPostalCode: ").append(toIndentedString(otherPostalCode)).append("\n");
    sb.append("    otherCity: ").append(toIndentedString(otherCity)).append("\n");
    sb.append("    otherState: ").append(toIndentedString(otherState)).append("\n");
    sb.append("    otherCountry: ").append(toIndentedString(otherCountry)).append("\n");
    sb.append("    companyId: ").append(toIndentedString(companyId)).append("\n");
    sb.append("    companyName: ").append(toIndentedString(companyName)).append("\n");
    sb.append("    function: ").append(toIndentedString(function)).append("\n");
    sb.append("    department: ").append(toIndentedString(department)).append("\n");
    sb.append("    assistantName: ").append(toIndentedString(assistantName)).append("\n");
    sb.append("    assistantTelephone: ").append(toIndentedString(assistantTelephone)).append("\n");
    sb.append("    managerName: ").append(toIndentedString(managerName)).append("\n");
    sb.append("    partnerName: ").append(toIndentedString(partnerName)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    anniversary: ").append(toIndentedString(anniversary)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}
