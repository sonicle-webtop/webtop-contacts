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
@JsonTypeName("EasSyncContactStat")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-11-04T12:38:17.584+01:00[Europe/Berlin]")
public class ApiEasSyncContactStat   {
  private @Valid String id;
  private @Valid String etag;

  /**
   * Message ID (internal)
   **/
  public ApiEasSyncContactStat id(String id) {
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
  public ApiEasSyncContactStat etag(String etag) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiEasSyncContactStat easSyncContactStat = (ApiEasSyncContactStat) o;
    return Objects.equals(this.id, easSyncContactStat.id) &&
        Objects.equals(this.etag, easSyncContactStat.etag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, etag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiEasSyncContactStat {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    etag: ").append(toIndentedString(etag)).append("\n");
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

