package com.sonicle.webtop.contacts.swagger.v1.model;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import javax.validation.Valid;


/**
 * Bean for carry messageStat fields
 **/
import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
@ApiModel(description = "Bean for carry messageStat fields")

public class SyncContact   {
  
  private @Valid Integer id = null;
  private @Valid String etag = null;
  private @Valid String title = null;
  private @Valid String firstName = null;
  private @Valid String lastName = null;
  private @Valid String nickname = null;
  private @Valid String mobile = null;
  private @Valid String pager1 = null;
  private @Valid String pager2 = null;
  private @Valid String email1 = null;
  private @Valid String email2 = null;
  private @Valid String email3 = null;
  private @Valid String im1 = null;
  private @Valid String im2 = null;
  private @Valid String im3 = null;
  private @Valid String workAddress = null;
  private @Valid String workPostalCode = null;
  private @Valid String workCity = null;
  private @Valid String workState = null;
  private @Valid String workCountry = null;
  private @Valid String workTelephone1 = null;
  private @Valid String workTelephone2 = null;
  private @Valid String workFax = null;
  private @Valid String homeAddress = null;
  private @Valid String homePostalCode = null;
  private @Valid String homeCity = null;
  private @Valid String homeState = null;
  private @Valid String homeCountry = null;
  private @Valid String homeTelephone1 = null;
  private @Valid String homeTelephone2 = null;
  private @Valid String homeFax = null;
  private @Valid String otherAddress = null;
  private @Valid String otherPostalCode = null;
  private @Valid String otherCity = null;
  private @Valid String otherState = null;
  private @Valid String otherCountry = null;
  private @Valid String companyId = null;
  private @Valid String companyName = null;
  private @Valid String function = null;
  private @Valid String department = null;
  private @Valid String manager = null;
  private @Valid String assistant = null;
  private @Valid String assistantTelephone = null;
  private @Valid String partner = null;
  private @Valid String birthday = null;
  private @Valid String anniversary = null;
  private @Valid String url = null;
  private @Valid String notes = null;
  private @Valid String picture = null;

  /**
   * Message ID (internal)
   **/
  public SyncContact id(Integer id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Message ID (internal)")
  @JsonProperty("id")
  @NotNull
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Revision tag
   **/
  public SyncContact etag(String etag) {
    this.etag = etag;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Revision tag")
  @JsonProperty("etag")
  @NotNull
  public String getEtag() {
    return etag;
  }
  public void setEtag(String etag) {
    this.etag = etag;
  }

  /**
   * Title
   **/
  public SyncContact title(String title) {
    this.title = title;
    return this;
  }

  
  @ApiModelProperty(value = "Title")
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * First name
   **/
  public SyncContact firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  
  @ApiModelProperty(value = "First name")
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Last name
   **/
  public SyncContact lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  
  @ApiModelProperty(value = "Last name")
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Nickname
   **/
  public SyncContact nickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

  
  @ApiModelProperty(value = "Nickname")
  @JsonProperty("nickname")
  public String getNickname() {
    return nickname;
  }
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  /**
   * Mobile phone
   **/
  public SyncContact mobile(String mobile) {
    this.mobile = mobile;
    return this;
  }

  
  @ApiModelProperty(value = "Mobile phone")
  @JsonProperty("mobile")
  public String getMobile() {
    return mobile;
  }
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /**
   * Pager 1
   **/
  public SyncContact pager1(String pager1) {
    this.pager1 = pager1;
    return this;
  }

  
  @ApiModelProperty(value = "Pager 1")
  @JsonProperty("pager1")
  public String getPager1() {
    return pager1;
  }
  public void setPager1(String pager1) {
    this.pager1 = pager1;
  }

  /**
   * Pager 2
   **/
  public SyncContact pager2(String pager2) {
    this.pager2 = pager2;
    return this;
  }

  
  @ApiModelProperty(value = "Pager 2")
  @JsonProperty("pager2")
  public String getPager2() {
    return pager2;
  }
  public void setPager2(String pager2) {
    this.pager2 = pager2;
  }

  /**
   * Email address 1
   **/
  public SyncContact email1(String email1) {
    this.email1 = email1;
    return this;
  }

  
  @ApiModelProperty(value = "Email address 1")
  @JsonProperty("email1")
  public String getEmail1() {
    return email1;
  }
  public void setEmail1(String email1) {
    this.email1 = email1;
  }

  /**
   * Email address 2
   **/
  public SyncContact email2(String email2) {
    this.email2 = email2;
    return this;
  }

  
  @ApiModelProperty(value = "Email address 2")
  @JsonProperty("email2")
  public String getEmail2() {
    return email2;
  }
  public void setEmail2(String email2) {
    this.email2 = email2;
  }

  /**
   * Email address 3
   **/
  public SyncContact email3(String email3) {
    this.email3 = email3;
    return this;
  }

  
  @ApiModelProperty(value = "Email address 3")
  @JsonProperty("email3")
  public String getEmail3() {
    return email3;
  }
  public void setEmail3(String email3) {
    this.email3 = email3;
  }

  /**
   * Instant Msg. 1
   **/
  public SyncContact im1(String im1) {
    this.im1 = im1;
    return this;
  }

  
  @ApiModelProperty(value = "Instant Msg. 1")
  @JsonProperty("im1")
  public String getIm1() {
    return im1;
  }
  public void setIm1(String im1) {
    this.im1 = im1;
  }

  /**
   * Instant Msg. 2
   **/
  public SyncContact im2(String im2) {
    this.im2 = im2;
    return this;
  }

  
  @ApiModelProperty(value = "Instant Msg. 2")
  @JsonProperty("im2")
  public String getIm2() {
    return im2;
  }
  public void setIm2(String im2) {
    this.im2 = im2;
  }

  /**
   * Instant Msg. 3
   **/
  public SyncContact im3(String im3) {
    this.im3 = im3;
    return this;
  }

  
  @ApiModelProperty(value = "Instant Msg. 3")
  @JsonProperty("im3")
  public String getIm3() {
    return im3;
  }
  public void setIm3(String im3) {
    this.im3 = im3;
  }

  /**
   * Business address
   **/
  public SyncContact workAddress(String workAddress) {
    this.workAddress = workAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Business address")
  @JsonProperty("workAddress")
  public String getWorkAddress() {
    return workAddress;
  }
  public void setWorkAddress(String workAddress) {
    this.workAddress = workAddress;
  }

  /**
   * Business postal code
   **/
  public SyncContact workPostalCode(String workPostalCode) {
    this.workPostalCode = workPostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Business postal code")
  @JsonProperty("workPostalCode")
  public String getWorkPostalCode() {
    return workPostalCode;
  }
  public void setWorkPostalCode(String workPostalCode) {
    this.workPostalCode = workPostalCode;
  }

  /**
   * Business city
   **/
  public SyncContact workCity(String workCity) {
    this.workCity = workCity;
    return this;
  }

  
  @ApiModelProperty(value = "Business city")
  @JsonProperty("workCity")
  public String getWorkCity() {
    return workCity;
  }
  public void setWorkCity(String workCity) {
    this.workCity = workCity;
  }

  /**
   * Business state/province
   **/
  public SyncContact workState(String workState) {
    this.workState = workState;
    return this;
  }

  
  @ApiModelProperty(value = "Business state/province")
  @JsonProperty("workState")
  public String getWorkState() {
    return workState;
  }
  public void setWorkState(String workState) {
    this.workState = workState;
  }

  /**
   * Business country
   **/
  public SyncContact workCountry(String workCountry) {
    this.workCountry = workCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Business country")
  @JsonProperty("workCountry")
  public String getWorkCountry() {
    return workCountry;
  }
  public void setWorkCountry(String workCountry) {
    this.workCountry = workCountry;
  }

  /**
   * Business telephone 1
   **/
  public SyncContact workTelephone1(String workTelephone1) {
    this.workTelephone1 = workTelephone1;
    return this;
  }

  
  @ApiModelProperty(value = "Business telephone 1")
  @JsonProperty("workTelephone1")
  public String getWorkTelephone1() {
    return workTelephone1;
  }
  public void setWorkTelephone1(String workTelephone1) {
    this.workTelephone1 = workTelephone1;
  }

  /**
   * Business telephone 2
   **/
  public SyncContact workTelephone2(String workTelephone2) {
    this.workTelephone2 = workTelephone2;
    return this;
  }

  
  @ApiModelProperty(value = "Business telephone 2")
  @JsonProperty("workTelephone2")
  public String getWorkTelephone2() {
    return workTelephone2;
  }
  public void setWorkTelephone2(String workTelephone2) {
    this.workTelephone2 = workTelephone2;
  }

  /**
   * Business fax
   **/
  public SyncContact workFax(String workFax) {
    this.workFax = workFax;
    return this;
  }

  
  @ApiModelProperty(value = "Business fax")
  @JsonProperty("workFax")
  public String getWorkFax() {
    return workFax;
  }
  public void setWorkFax(String workFax) {
    this.workFax = workFax;
  }

  /**
   * Home address
   **/
  public SyncContact homeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Home address")
  @JsonProperty("homeAddress")
  public String getHomeAddress() {
    return homeAddress;
  }
  public void setHomeAddress(String homeAddress) {
    this.homeAddress = homeAddress;
  }

  /**
   * Home postal code
   **/
  public SyncContact homePostalCode(String homePostalCode) {
    this.homePostalCode = homePostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Home postal code")
  @JsonProperty("homePostalCode")
  public String getHomePostalCode() {
    return homePostalCode;
  }
  public void setHomePostalCode(String homePostalCode) {
    this.homePostalCode = homePostalCode;
  }

  /**
   * Home city
   **/
  public SyncContact homeCity(String homeCity) {
    this.homeCity = homeCity;
    return this;
  }

  
  @ApiModelProperty(value = "Home city")
  @JsonProperty("homeCity")
  public String getHomeCity() {
    return homeCity;
  }
  public void setHomeCity(String homeCity) {
    this.homeCity = homeCity;
  }

  /**
   * Home state
   **/
  public SyncContact homeState(String homeState) {
    this.homeState = homeState;
    return this;
  }

  
  @ApiModelProperty(value = "Home state")
  @JsonProperty("homeState")
  public String getHomeState() {
    return homeState;
  }
  public void setHomeState(String homeState) {
    this.homeState = homeState;
  }

  /**
   * Home country
   **/
  public SyncContact homeCountry(String homeCountry) {
    this.homeCountry = homeCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Home country")
  @JsonProperty("homeCountry")
  public String getHomeCountry() {
    return homeCountry;
  }
  public void setHomeCountry(String homeCountry) {
    this.homeCountry = homeCountry;
  }

  /**
   * Home telephone 1
   **/
  public SyncContact homeTelephone1(String homeTelephone1) {
    this.homeTelephone1 = homeTelephone1;
    return this;
  }

  
  @ApiModelProperty(value = "Home telephone 1")
  @JsonProperty("homeTelephone1")
  public String getHomeTelephone1() {
    return homeTelephone1;
  }
  public void setHomeTelephone1(String homeTelephone1) {
    this.homeTelephone1 = homeTelephone1;
  }

  /**
   * Home telephone 2
   **/
  public SyncContact homeTelephone2(String homeTelephone2) {
    this.homeTelephone2 = homeTelephone2;
    return this;
  }

  
  @ApiModelProperty(value = "Home telephone 2")
  @JsonProperty("homeTelephone2")
  public String getHomeTelephone2() {
    return homeTelephone2;
  }
  public void setHomeTelephone2(String homeTelephone2) {
    this.homeTelephone2 = homeTelephone2;
  }

  /**
   * Home fax
   **/
  public SyncContact homeFax(String homeFax) {
    this.homeFax = homeFax;
    return this;
  }

  
  @ApiModelProperty(value = "Home fax")
  @JsonProperty("homeFax")
  public String getHomeFax() {
    return homeFax;
  }
  public void setHomeFax(String homeFax) {
    this.homeFax = homeFax;
  }

  /**
   * Other address
   **/
  public SyncContact otherAddress(String otherAddress) {
    this.otherAddress = otherAddress;
    return this;
  }

  
  @ApiModelProperty(value = "Other address")
  @JsonProperty("otherAddress")
  public String getOtherAddress() {
    return otherAddress;
  }
  public void setOtherAddress(String otherAddress) {
    this.otherAddress = otherAddress;
  }

  /**
   * Other postal code
   **/
  public SyncContact otherPostalCode(String otherPostalCode) {
    this.otherPostalCode = otherPostalCode;
    return this;
  }

  
  @ApiModelProperty(value = "Other postal code")
  @JsonProperty("otherPostalCode")
  public String getOtherPostalCode() {
    return otherPostalCode;
  }
  public void setOtherPostalCode(String otherPostalCode) {
    this.otherPostalCode = otherPostalCode;
  }

  /**
   * Other city
   **/
  public SyncContact otherCity(String otherCity) {
    this.otherCity = otherCity;
    return this;
  }

  
  @ApiModelProperty(value = "Other city")
  @JsonProperty("otherCity")
  public String getOtherCity() {
    return otherCity;
  }
  public void setOtherCity(String otherCity) {
    this.otherCity = otherCity;
  }

  /**
   * Other state
   **/
  public SyncContact otherState(String otherState) {
    this.otherState = otherState;
    return this;
  }

  
  @ApiModelProperty(value = "Other state")
  @JsonProperty("otherState")
  public String getOtherState() {
    return otherState;
  }
  public void setOtherState(String otherState) {
    this.otherState = otherState;
  }

  /**
   * Other country
   **/
  public SyncContact otherCountry(String otherCountry) {
    this.otherCountry = otherCountry;
    return this;
  }

  
  @ApiModelProperty(value = "Other country")
  @JsonProperty("otherCountry")
  public String getOtherCountry() {
    return otherCountry;
  }
  public void setOtherCountry(String otherCountry) {
    this.otherCountry = otherCountry;
  }

  /**
   * Company ID
   **/
  public SyncContact companyId(String companyId) {
    this.companyId = companyId;
    return this;
  }

  
  @ApiModelProperty(value = "Company ID")
  @JsonProperty("companyId")
  public String getCompanyId() {
    return companyId;
  }
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  /**
   * Company name
   **/
  public SyncContact companyName(String companyName) {
    this.companyName = companyName;
    return this;
  }

  
  @ApiModelProperty(value = "Company name")
  @JsonProperty("companyName")
  public String getCompanyName() {
    return companyName;
  }
  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  /**
   * Job title
   **/
  public SyncContact function(String function) {
    this.function = function;
    return this;
  }

  
  @ApiModelProperty(value = "Job title")
  @JsonProperty("function")
  public String getFunction() {
    return function;
  }
  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * Department
   **/
  public SyncContact department(String department) {
    this.department = department;
    return this;
  }

  
  @ApiModelProperty(value = "Department")
  @JsonProperty("department")
  public String getDepartment() {
    return department;
  }
  public void setDepartment(String department) {
    this.department = department;
  }

  /**
   * Manager name
   **/
  public SyncContact manager(String manager) {
    this.manager = manager;
    return this;
  }

  
  @ApiModelProperty(value = "Manager name")
  @JsonProperty("manager")
  public String getManager() {
    return manager;
  }
  public void setManager(String manager) {
    this.manager = manager;
  }

  /**
   * Assistant name
   **/
  public SyncContact assistant(String assistant) {
    this.assistant = assistant;
    return this;
  }

  
  @ApiModelProperty(value = "Assistant name")
  @JsonProperty("assistant")
  public String getAssistant() {
    return assistant;
  }
  public void setAssistant(String assistant) {
    this.assistant = assistant;
  }

  /**
   * Assistant telephone
   **/
  public SyncContact assistantTelephone(String assistantTelephone) {
    this.assistantTelephone = assistantTelephone;
    return this;
  }

  
  @ApiModelProperty(value = "Assistant telephone")
  @JsonProperty("assistantTelephone")
  public String getAssistantTelephone() {
    return assistantTelephone;
  }
  public void setAssistantTelephone(String assistantTelephone) {
    this.assistantTelephone = assistantTelephone;
  }

  /**
   * Partner
   **/
  public SyncContact partner(String partner) {
    this.partner = partner;
    return this;
  }

  
  @ApiModelProperty(value = "Partner")
  @JsonProperty("partner")
  public String getPartner() {
    return partner;
  }
  public void setPartner(String partner) {
    this.partner = partner;
  }

  /**
   * Birthday date (ISO date YYYYMMDD)
   **/
  public SyncContact birthday(String birthday) {
    this.birthday = birthday;
    return this;
  }

  
  @ApiModelProperty(value = "Birthday date (ISO date YYYYMMDD)")
  @JsonProperty("birthday")
  public String getBirthday() {
    return birthday;
  }
  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  /**
   * Anniversary date (ISO date YYYYMMDD)
   **/
  public SyncContact anniversary(String anniversary) {
    this.anniversary = anniversary;
    return this;
  }

  
  @ApiModelProperty(value = "Anniversary date (ISO date YYYYMMDD)")
  @JsonProperty("anniversary")
  public String getAnniversary() {
    return anniversary;
  }
  public void setAnniversary(String anniversary) {
    this.anniversary = anniversary;
  }

  /**
   * Web-site URL
   **/
  public SyncContact url(String url) {
    this.url = url;
    return this;
  }

  
  @ApiModelProperty(value = "Web-site URL")
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Notes
   **/
  public SyncContact notes(String notes) {
    this.notes = notes;
    return this;
  }

  
  @ApiModelProperty(value = "Notes")
  @JsonProperty("notes")
  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * Image base64 data URI
   **/
  public SyncContact picture(String picture) {
    this.picture = picture;
    return this;
  }

  
  @ApiModelProperty(value = "Image base64 data URI")
  @JsonProperty("picture")
  public String getPicture() {
    return picture;
  }
  public void setPicture(String picture) {
    this.picture = picture;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SyncContact syncContact = (SyncContact) o;
    return Objects.equals(id, syncContact.id) &&
        Objects.equals(etag, syncContact.etag) &&
        Objects.equals(title, syncContact.title) &&
        Objects.equals(firstName, syncContact.firstName) &&
        Objects.equals(lastName, syncContact.lastName) &&
        Objects.equals(nickname, syncContact.nickname) &&
        Objects.equals(mobile, syncContact.mobile) &&
        Objects.equals(pager1, syncContact.pager1) &&
        Objects.equals(pager2, syncContact.pager2) &&
        Objects.equals(email1, syncContact.email1) &&
        Objects.equals(email2, syncContact.email2) &&
        Objects.equals(email3, syncContact.email3) &&
        Objects.equals(im1, syncContact.im1) &&
        Objects.equals(im2, syncContact.im2) &&
        Objects.equals(im3, syncContact.im3) &&
        Objects.equals(workAddress, syncContact.workAddress) &&
        Objects.equals(workPostalCode, syncContact.workPostalCode) &&
        Objects.equals(workCity, syncContact.workCity) &&
        Objects.equals(workState, syncContact.workState) &&
        Objects.equals(workCountry, syncContact.workCountry) &&
        Objects.equals(workTelephone1, syncContact.workTelephone1) &&
        Objects.equals(workTelephone2, syncContact.workTelephone2) &&
        Objects.equals(workFax, syncContact.workFax) &&
        Objects.equals(homeAddress, syncContact.homeAddress) &&
        Objects.equals(homePostalCode, syncContact.homePostalCode) &&
        Objects.equals(homeCity, syncContact.homeCity) &&
        Objects.equals(homeState, syncContact.homeState) &&
        Objects.equals(homeCountry, syncContact.homeCountry) &&
        Objects.equals(homeTelephone1, syncContact.homeTelephone1) &&
        Objects.equals(homeTelephone2, syncContact.homeTelephone2) &&
        Objects.equals(homeFax, syncContact.homeFax) &&
        Objects.equals(otherAddress, syncContact.otherAddress) &&
        Objects.equals(otherPostalCode, syncContact.otherPostalCode) &&
        Objects.equals(otherCity, syncContact.otherCity) &&
        Objects.equals(otherState, syncContact.otherState) &&
        Objects.equals(otherCountry, syncContact.otherCountry) &&
        Objects.equals(companyId, syncContact.companyId) &&
        Objects.equals(companyName, syncContact.companyName) &&
        Objects.equals(function, syncContact.function) &&
        Objects.equals(department, syncContact.department) &&
        Objects.equals(manager, syncContact.manager) &&
        Objects.equals(assistant, syncContact.assistant) &&
        Objects.equals(assistantTelephone, syncContact.assistantTelephone) &&
        Objects.equals(partner, syncContact.partner) &&
        Objects.equals(birthday, syncContact.birthday) &&
        Objects.equals(anniversary, syncContact.anniversary) &&
        Objects.equals(url, syncContact.url) &&
        Objects.equals(notes, syncContact.notes) &&
        Objects.equals(picture, syncContact.picture);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, etag, title, firstName, lastName, nickname, mobile, pager1, pager2, email1, email2, email3, im1, im2, im3, workAddress, workPostalCode, workCity, workState, workCountry, workTelephone1, workTelephone2, workFax, homeAddress, homePostalCode, homeCity, homeState, homeCountry, homeTelephone1, homeTelephone2, homeFax, otherAddress, otherPostalCode, otherCity, otherState, otherCountry, companyId, companyName, function, department, manager, assistant, assistantTelephone, partner, birthday, anniversary, url, notes, picture);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SyncContact {\n");
    
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

