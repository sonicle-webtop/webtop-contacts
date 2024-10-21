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
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Bean for carry messageStat fields
 **/
@ApiModel(description = "Bean for carry messageStat fields")
@JsonTypeName("EasSyncContact")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-10-21T11:44:32.049+02:00[Europe/Berlin]")
public class ApiEasSyncContact   {
  private @Valid String id;
  private @Valid String etag;
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
  private @Valid String manager;
  private @Valid String assistant;
  private @Valid String assistantTelephone;
  private @Valid String partner;
  private @Valid String birthday;
  private @Valid String anniversary;
  private @Valid String url;
  private @Valid String notes;
  private @Valid String picture;

  /**
   * Message ID (internal)
   **/
  public ApiEasSyncContact id(String id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Message ID (internal)")
  @JsonProperty("id")
  @NotNull
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Revision tag
   **/
  public ApiEasSyncContact etag(String etag) {
    this.etag = etag;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Revision tag")
  @JsonProperty("etag")
  @NotNull
  public String getEtag() {
    return etag;
  }

  @JsonProperty("etag")
  public void setEtag(String etag) {
    this.etag = etag;
  }

  /**
   * Title
   **/
  public ApiEasSyncContact title(String title) {
    this.title = title;
    return this;
  }

  
  @ApiModelProperty(value = "Title")
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * First name
   **/
  public ApiEasSyncContact firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  
  @ApiModelProperty(value = "First name")
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  @JsonProperty("firstName")
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Last name
   **/
  public ApiEasSyncContact lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  
  @ApiModelProperty(value = "Last name")
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  @JsonProperty("lastName")
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Nickname
   **/
  public ApiEasSyncContact nickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

  
  @ApiModelProperty(value = "Nickname")
  @JsonProperty("nickname")
  public String getNickname() {
    return nickname;
  }

  @JsonProperty("nickname")
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  /**
   * Mobile phone
   **/
  public ApiEasSyncContact mobile(String mobile) {
    this.mobile = mobile;
    return this;
  }

  
  @ApiModelProperty(value = "Mobile phone")
  @JsonProperty("mobile")
  public String getMobile() {
    return mobile;
  }

  @JsonProperty("mobile")
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /**
   * Pager 1
   **/
  public ApiEasSyncContact pager1(String pager1) {
    this.pager1 = pager1;
    return this;
  }

  
  @ApiModelProperty(value = "Pager 1")
  @JsonProperty("pager1")
  public String getPager1() {
    return pager1;
  }

  @JsonProperty("pager1")
  public void setPager1(String pager1) {
    this.pager1 = pager1;
  }

  /**
   * Pager 2
   **/
  public ApiEasSyncContact pager2(String pager2) {
    this.pager2 = pager2;
    return this;
  }

  
  @ApiModelProperty(value = "Pager 2")
  @JsonProperty("pager2")
  public String getPager2() {
    return pager2;
  }

  @JsonProperty("pager2")
  public void setPager2(String pager2) {
    this.pager2 = pager2;
  }

  /**
   * Email address 1
   **/
  public ApiEasSyncContact email1(String email1) {
    this.email1 = email1;
    return this;
  }

  
  @ApiModelProperty(value = "Email address 1")
  @JsonProperty("email1")
  public String getEmail1() {
    return email1;
  }

  @JsonProperty("email1")
  public void setEmail1(String email1) {
    this.email1 = email1;
  }

  /**
   * Email address 2
   **/
  public ApiEasSyncContact email2(String email2) {
    this.email2 = email2;
    return this;
  }

  
  @ApiModelProperty(value = "Email address 2")
  @JsonProperty("email2")
  public String getEmail2() {
    return email2;
  }

  @JsonProperty("email2")
  public void setEmail2(String email2) {
    this.email2 = email2;
  }

  /**
   * Email address 3
   **/
  public ApiEasSyncContact email3(String email3) {
    this.email3 = email3;
    return this;
  }

  
  @ApiModelProperty(value = "Email address 3")
  @JsonProperty("email3")
  public String getEmail3() {
    return email3;
  }

  @JsonProperty("email3")
  public void setEmail3(String email3) {
    this.email3 = email3;
  }

  /**
   * Instant Msg. 1
   **/
  public ApiEasSyncContact im1(String im1) {
    this.im1 = im1;
    return this;
  }

  
  @ApiModelProperty(value = "Instant Msg. 1")
  @JsonProperty("im1")
  public String getIm1() {
    return im1;
  }

  @JsonProperty("im1")
  public void setIm1(String im1) {
    this.im1 = im1;
  }

  /**
   * Instant Msg. 2
   **/
  public ApiEasSyncContact im2(String im2) {
    this.im2 = im2;
    return this;
  }

  
  @ApiModelProperty(value = "Instant Msg. 2")
  @JsonProperty("im2")
  public String getIm2() {
    return im2;
  }

  @JsonProperty("im2")
  public void setIm2(String im2) {
    this.im2 = im2;
  }

  /**
   * Instant Msg. 3
   **/
  public ApiEasSyncContact im3(String im3) {
    this.im3 = im3;
    return this;
  }

  
  @ApiModelProperty(value = "Instant Msg. 3")
  @JsonProperty("im3")
  public String getIm3() {
    return im3;
  }

  @JsonProperty("im3")
  public void setIm3(String im3) {
    this.im3 = im3;
  }

  /**
   * Business address
   **/
  public ApiEasSyncContact workAddress(String workAddress) {
    this.workAddress = workAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Business address")
  @JsonProperty("workAddress")
  public String getWorkAddress() {
    return workAddress;
  }

  @JsonProperty("workAddress")
  public void setWorkAddress(String workAddress) {
    this.workAddress = workAddress;
  }

  /**
   * Business postal code
   **/
  public ApiEasSyncContact workPostalCode(String workPostalCode) {
    this.workPostalCode = workPostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Business postal code")
  @JsonProperty("workPostalCode")
  public String getWorkPostalCode() {
    return workPostalCode;
  }

  @JsonProperty("workPostalCode")
  public void setWorkPostalCode(String workPostalCode) {
    this.workPostalCode = workPostalCode;
  }

  /**
   * Business city
   **/
  public ApiEasSyncContact workCity(String workCity) {
    this.workCity = workCity;
    return this;
  }

  
  @ApiModelProperty(value = "Business city")
  @JsonProperty("workCity")
  public String getWorkCity() {
    return workCity;
  }

  @JsonProperty("workCity")
  public void setWorkCity(String workCity) {
    this.workCity = workCity;
  }

  /**
   * Business state/province
   **/
  public ApiEasSyncContact workState(String workState) {
    this.workState = workState;
    return this;
  }

  
  @ApiModelProperty(value = "Business state/province")
  @JsonProperty("workState")
  public String getWorkState() {
    return workState;
  }

  @JsonProperty("workState")
  public void setWorkState(String workState) {
    this.workState = workState;
  }

  /**
   * Business country
   **/
  public ApiEasSyncContact workCountry(String workCountry) {
    this.workCountry = workCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Business country")
  @JsonProperty("workCountry")
  public String getWorkCountry() {
    return workCountry;
  }

  @JsonProperty("workCountry")
  public void setWorkCountry(String workCountry) {
    this.workCountry = workCountry;
  }

  /**
   * Business telephone 1
   **/
  public ApiEasSyncContact workTelephone1(String workTelephone1) {
    this.workTelephone1 = workTelephone1;
    return this;
  }

  
  @ApiModelProperty(value = "Business telephone 1")
  @JsonProperty("workTelephone1")
  public String getWorkTelephone1() {
    return workTelephone1;
  }

  @JsonProperty("workTelephone1")
  public void setWorkTelephone1(String workTelephone1) {
    this.workTelephone1 = workTelephone1;
  }

  /**
   * Business telephone 2
   **/
  public ApiEasSyncContact workTelephone2(String workTelephone2) {
    this.workTelephone2 = workTelephone2;
    return this;
  }

  
  @ApiModelProperty(value = "Business telephone 2")
  @JsonProperty("workTelephone2")
  public String getWorkTelephone2() {
    return workTelephone2;
  }

  @JsonProperty("workTelephone2")
  public void setWorkTelephone2(String workTelephone2) {
    this.workTelephone2 = workTelephone2;
  }

  /**
   * Business fax
   **/
  public ApiEasSyncContact workFax(String workFax) {
    this.workFax = workFax;
    return this;
  }

  
  @ApiModelProperty(value = "Business fax")
  @JsonProperty("workFax")
  public String getWorkFax() {
    return workFax;
  }

  @JsonProperty("workFax")
  public void setWorkFax(String workFax) {
    this.workFax = workFax;
  }

  /**
   * Home address
   **/
  public ApiEasSyncContact homeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Home address")
  @JsonProperty("homeAddress")
  public String getHomeAddress() {
    return homeAddress;
  }

  @JsonProperty("homeAddress")
  public void setHomeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
  }

  /**
   * Home postal code
   **/
  public ApiEasSyncContact homePostalCode(String homePostalCode) {
    this.homePostalCode = homePostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Home postal code")
  @JsonProperty("homePostalCode")
  public String getHomePostalCode() {
    return homePostalCode;
  }

  @JsonProperty("homePostalCode")
  public void setHomePostalCode(String homePostalCode) {
    this.homePostalCode = homePostalCode;
  }

  /**
   * Home city
   **/
  public ApiEasSyncContact homeCity(String homeCity) {
    this.homeCity = homeCity;
    return this;
  }

  
  @ApiModelProperty(value = "Home city")
  @JsonProperty("homeCity")
  public String getHomeCity() {
    return homeCity;
  }

  @JsonProperty("homeCity")
  public void setHomeCity(String homeCity) {
    this.homeCity = homeCity;
  }

  /**
   * Home state
   **/
  public ApiEasSyncContact homeState(String homeState) {
    this.homeState = homeState;
    return this;
  }

  
  @ApiModelProperty(value = "Home state")
  @JsonProperty("homeState")
  public String getHomeState() {
    return homeState;
  }

  @JsonProperty("homeState")
  public void setHomeState(String homeState) {
    this.homeState = homeState;
  }

  /**
   * Home country
   **/
  public ApiEasSyncContact homeCountry(String homeCountry) {
    this.homeCountry = homeCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Home country")
  @JsonProperty("homeCountry")
  public String getHomeCountry() {
    return homeCountry;
  }

  @JsonProperty("homeCountry")
  public void setHomeCountry(String homeCountry) {
    this.homeCountry = homeCountry;
  }

  /**
   * Home telephone 1
   **/
  public ApiEasSyncContact homeTelephone1(String homeTelephone1) {
    this.homeTelephone1 = homeTelephone1;
    return this;
  }

  
  @ApiModelProperty(value = "Home telephone 1")
  @JsonProperty("homeTelephone1")
  public String getHomeTelephone1() {
    return homeTelephone1;
  }

  @JsonProperty("homeTelephone1")
  public void setHomeTelephone1(String homeTelephone1) {
    this.homeTelephone1 = homeTelephone1;
  }

  /**
   * Home telephone 2
   **/
  public ApiEasSyncContact homeTelephone2(String homeTelephone2) {
    this.homeTelephone2 = homeTelephone2;
    return this;
  }

  
  @ApiModelProperty(value = "Home telephone 2")
  @JsonProperty("homeTelephone2")
  public String getHomeTelephone2() {
    return homeTelephone2;
  }

  @JsonProperty("homeTelephone2")
  public void setHomeTelephone2(String homeTelephone2) {
    this.homeTelephone2 = homeTelephone2;
  }

  /**
   * Home fax
   **/
  public ApiEasSyncContact homeFax(String homeFax) {
    this.homeFax = homeFax;
    return this;
  }

  
  @ApiModelProperty(value = "Home fax")
  @JsonProperty("homeFax")
  public String getHomeFax() {
    return homeFax;
  }

  @JsonProperty("homeFax")
  public void setHomeFax(String homeFax) {
    this.homeFax = homeFax;
  }

  /**
   * Other address
   **/
  public ApiEasSyncContact otherAddress(String otherAddress) {
    this.otherAddress = otherAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Other address")
  @JsonProperty("otherAddress")
  public String getOtherAddress() {
    return otherAddress;
  }

  @JsonProperty("otherAddress")
  public void setOtherAddress(String otherAddress) {
    this.otherAddress = otherAddress;
  }

  /**
   * Other postal code
   **/
  public ApiEasSyncContact otherPostalCode(String otherPostalCode) {
    this.otherPostalCode = otherPostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Other postal code")
  @JsonProperty("otherPostalCode")
  public String getOtherPostalCode() {
    return otherPostalCode;
  }

  @JsonProperty("otherPostalCode")
  public void setOtherPostalCode(String otherPostalCode) {
    this.otherPostalCode = otherPostalCode;
  }

  /**
   * Other city
   **/
  public ApiEasSyncContact otherCity(String otherCity) {
    this.otherCity = otherCity;
    return this;
  }

  
  @ApiModelProperty(value = "Other city")
  @JsonProperty("otherCity")
  public String getOtherCity() {
    return otherCity;
  }

  @JsonProperty("otherCity")
  public void setOtherCity(String otherCity) {
    this.otherCity = otherCity;
  }

  /**
   * Other state
   **/
  public ApiEasSyncContact otherState(String otherState) {
    this.otherState = otherState;
    return this;
  }

  
  @ApiModelProperty(value = "Other state")
  @JsonProperty("otherState")
  public String getOtherState() {
    return otherState;
  }

  @JsonProperty("otherState")
  public void setOtherState(String otherState) {
    this.otherState = otherState;
  }

  /**
   * Other country
   **/
  public ApiEasSyncContact otherCountry(String otherCountry) {
    this.otherCountry = otherCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Other country")
  @JsonProperty("otherCountry")
  public String getOtherCountry() {
    return otherCountry;
  }

  @JsonProperty("otherCountry")
  public void setOtherCountry(String otherCountry) {
    this.otherCountry = otherCountry;
  }

  /**
   * Company ID
   **/
  public ApiEasSyncContact companyId(String companyId) {
    this.companyId = companyId;
    return this;
  }

  
  @ApiModelProperty(value = "Company ID")
  @JsonProperty("companyId")
  public String getCompanyId() {
    return companyId;
  }

  @JsonProperty("companyId")
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  /**
   * Company name
   **/
  public ApiEasSyncContact companyName(String companyName) {
    this.companyName = companyName;
    return this;
  }

  
  @ApiModelProperty(value = "Company name")
  @JsonProperty("companyName")
  public String getCompanyName() {
    return companyName;
  }

  @JsonProperty("companyName")
  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  /**
   * Job title
   **/
  public ApiEasSyncContact function(String function) {
    this.function = function;
    return this;
  }

  
  @ApiModelProperty(value = "Job title")
  @JsonProperty("function")
  public String getFunction() {
    return function;
  }

  @JsonProperty("function")
  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * Department
   **/
  public ApiEasSyncContact department(String department) {
    this.department = department;
    return this;
  }

  
  @ApiModelProperty(value = "Department")
  @JsonProperty("department")
  public String getDepartment() {
    return department;
  }

  @JsonProperty("department")
  public void setDepartment(String department) {
    this.department = department;
  }

  /**
   * Manager name
   **/
  public ApiEasSyncContact manager(String manager) {
    this.manager = manager;
    return this;
  }

  
  @ApiModelProperty(value = "Manager name")
  @JsonProperty("manager")
  public String getManager() {
    return manager;
  }

  @JsonProperty("manager")
  public void setManager(String manager) {
    this.manager = manager;
  }

  /**
   * Assistant name
   **/
  public ApiEasSyncContact assistant(String assistant) {
    this.assistant = assistant;
    return this;
  }

  
  @ApiModelProperty(value = "Assistant name")
  @JsonProperty("assistant")
  public String getAssistant() {
    return assistant;
  }

  @JsonProperty("assistant")
  public void setAssistant(String assistant) {
    this.assistant = assistant;
  }

  /**
   * Assistant telephone
   **/
  public ApiEasSyncContact assistantTelephone(String assistantTelephone) {
    this.assistantTelephone = assistantTelephone;
    return this;
  }

  
  @ApiModelProperty(value = "Assistant telephone")
  @JsonProperty("assistantTelephone")
  public String getAssistantTelephone() {
    return assistantTelephone;
  }

  @JsonProperty("assistantTelephone")
  public void setAssistantTelephone(String assistantTelephone) {
    this.assistantTelephone = assistantTelephone;
  }

  /**
   * Partner
   **/
  public ApiEasSyncContact partner(String partner) {
    this.partner = partner;
    return this;
  }

  
  @ApiModelProperty(value = "Partner")
  @JsonProperty("partner")
  public String getPartner() {
    return partner;
  }

  @JsonProperty("partner")
  public void setPartner(String partner) {
    this.partner = partner;
  }

  /**
   * Birthday date (ISO date YYYYMMDD)
   **/
  public ApiEasSyncContact birthday(String birthday) {
    this.birthday = birthday;
    return this;
  }

  
  @ApiModelProperty(value = "Birthday date (ISO date YYYYMMDD)")
  @JsonProperty("birthday")
  public String getBirthday() {
    return birthday;
  }

  @JsonProperty("birthday")
  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  /**
   * Anniversary date (ISO date YYYYMMDD)
   **/
  public ApiEasSyncContact anniversary(String anniversary) {
    this.anniversary = anniversary;
    return this;
  }

  
  @ApiModelProperty(value = "Anniversary date (ISO date YYYYMMDD)")
  @JsonProperty("anniversary")
  public String getAnniversary() {
    return anniversary;
  }

  @JsonProperty("anniversary")
  public void setAnniversary(String anniversary) {
    this.anniversary = anniversary;
  }

  /**
   * Web-site URL
   **/
  public ApiEasSyncContact url(String url) {
    this.url = url;
    return this;
  }

  
  @ApiModelProperty(value = "Web-site URL")
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Notes
   **/
  public ApiEasSyncContact notes(String notes) {
    this.notes = notes;
    return this;
  }

  
  @ApiModelProperty(value = "Notes")
  @JsonProperty("notes")
  public String getNotes() {
    return notes;
  }

  @JsonProperty("notes")
  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * Image base64 data URI
   **/
  public ApiEasSyncContact picture(String picture) {
    this.picture = picture;
    return this;
  }

  
  @ApiModelProperty(value = "Image base64 data URI")
  @JsonProperty("picture")
  public String getPicture() {
    return picture;
  }

  @JsonProperty("picture")
  public void setPicture(String picture) {
    this.picture = picture;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiEasSyncContact easSyncContact = (ApiEasSyncContact) o;
    return Objects.equals(this.id, easSyncContact.id) &&
        Objects.equals(this.etag, easSyncContact.etag) &&
        Objects.equals(this.title, easSyncContact.title) &&
        Objects.equals(this.firstName, easSyncContact.firstName) &&
        Objects.equals(this.lastName, easSyncContact.lastName) &&
        Objects.equals(this.nickname, easSyncContact.nickname) &&
        Objects.equals(this.mobile, easSyncContact.mobile) &&
        Objects.equals(this.pager1, easSyncContact.pager1) &&
        Objects.equals(this.pager2, easSyncContact.pager2) &&
        Objects.equals(this.email1, easSyncContact.email1) &&
        Objects.equals(this.email2, easSyncContact.email2) &&
        Objects.equals(this.email3, easSyncContact.email3) &&
        Objects.equals(this.im1, easSyncContact.im1) &&
        Objects.equals(this.im2, easSyncContact.im2) &&
        Objects.equals(this.im3, easSyncContact.im3) &&
        Objects.equals(this.workAddress, easSyncContact.workAddress) &&
        Objects.equals(this.workPostalCode, easSyncContact.workPostalCode) &&
        Objects.equals(this.workCity, easSyncContact.workCity) &&
        Objects.equals(this.workState, easSyncContact.workState) &&
        Objects.equals(this.workCountry, easSyncContact.workCountry) &&
        Objects.equals(this.workTelephone1, easSyncContact.workTelephone1) &&
        Objects.equals(this.workTelephone2, easSyncContact.workTelephone2) &&
        Objects.equals(this.workFax, easSyncContact.workFax) &&
        Objects.equals(this.homeAddress, easSyncContact.homeAddress) &&
        Objects.equals(this.homePostalCode, easSyncContact.homePostalCode) &&
        Objects.equals(this.homeCity, easSyncContact.homeCity) &&
        Objects.equals(this.homeState, easSyncContact.homeState) &&
        Objects.equals(this.homeCountry, easSyncContact.homeCountry) &&
        Objects.equals(this.homeTelephone1, easSyncContact.homeTelephone1) &&
        Objects.equals(this.homeTelephone2, easSyncContact.homeTelephone2) &&
        Objects.equals(this.homeFax, easSyncContact.homeFax) &&
        Objects.equals(this.otherAddress, easSyncContact.otherAddress) &&
        Objects.equals(this.otherPostalCode, easSyncContact.otherPostalCode) &&
        Objects.equals(this.otherCity, easSyncContact.otherCity) &&
        Objects.equals(this.otherState, easSyncContact.otherState) &&
        Objects.equals(this.otherCountry, easSyncContact.otherCountry) &&
        Objects.equals(this.companyId, easSyncContact.companyId) &&
        Objects.equals(this.companyName, easSyncContact.companyName) &&
        Objects.equals(this.function, easSyncContact.function) &&
        Objects.equals(this.department, easSyncContact.department) &&
        Objects.equals(this.manager, easSyncContact.manager) &&
        Objects.equals(this.assistant, easSyncContact.assistant) &&
        Objects.equals(this.assistantTelephone, easSyncContact.assistantTelephone) &&
        Objects.equals(this.partner, easSyncContact.partner) &&
        Objects.equals(this.birthday, easSyncContact.birthday) &&
        Objects.equals(this.anniversary, easSyncContact.anniversary) &&
        Objects.equals(this.url, easSyncContact.url) &&
        Objects.equals(this.notes, easSyncContact.notes) &&
        Objects.equals(this.picture, easSyncContact.picture);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, etag, title, firstName, lastName, nickname, mobile, pager1, pager2, email1, email2, email3, im1, im2, im3, workAddress, workPostalCode, workCity, workState, workCountry, workTelephone1, workTelephone2, workFax, homeAddress, homePostalCode, homeCity, homeState, homeCountry, homeTelephone1, homeTelephone2, homeFax, otherAddress, otherPostalCode, otherCity, otherState, otherCountry, companyId, companyName, function, department, manager, assistant, assistantTelephone, partner, birthday, anniversary, url, notes, picture);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiEasSyncContact {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    etag: ").append(toIndentedString(etag)).append("\n");
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
    sb.append("    manager: ").append(toIndentedString(manager)).append("\n");
    sb.append("    assistant: ").append(toIndentedString(assistant)).append("\n");
    sb.append("    assistantTelephone: ").append(toIndentedString(assistantTelephone)).append("\n");
    sb.append("    partner: ").append(toIndentedString(partner)).append("\n");
    sb.append("    birthday: ").append(toIndentedString(birthday)).append("\n");
    sb.append("    anniversary: ").append(toIndentedString(anniversary)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("    picture: ").append(toIndentedString(picture)).append("\n");
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

