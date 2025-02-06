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
 * Represent the meta-data of a picture associated to a contact.
 **/
@ApiModel(description = "Represent the meta-data of a picture associated to a contact.")
@JsonTypeName("ContactPictureMeta")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-02-06T10:04:09.242+01:00[Europe/Berlin]")
public class ApiContactPictureMeta   {
  private @Valid String mediaType;
  private @Valid Integer width;
  private @Valid Integer height;
  private @Valid Long size;

  /**
   * The image MediaType: like &#39;image-&lt;subtype&gt;&#39;
   **/
  public ApiContactPictureMeta mediaType(String mediaType) {
    this.mediaType = mediaType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "The image MediaType: like 'image-<subtype>'")
  @JsonProperty("mediaType")
  public String getMediaType() {
    return mediaType;
  }

  @JsonProperty("mediaType")
  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  /**
   * The image width in pixels.
   **/
  public ApiContactPictureMeta width(Integer width) {
    this.width = width;
    return this;
  }

  
  @ApiModelProperty(value = "The image width in pixels.")
  @JsonProperty("width")
  public Integer getWidth() {
    return width;
  }

  @JsonProperty("width")
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   * The image height in pixels.
   **/
  public ApiContactPictureMeta height(Integer height) {
    this.height = height;
    return this;
  }

  
  @ApiModelProperty(value = "The image height in pixels.")
  @JsonProperty("height")
  public Integer getHeight() {
    return height;
  }

  @JsonProperty("height")
  public void setHeight(Integer height) {
    this.height = height;
  }

  /**
   * The image size in bytes.
   **/
  public ApiContactPictureMeta size(Long size) {
    this.size = size;
    return this;
  }

  
  @ApiModelProperty(value = "The image size in bytes.")
  @JsonProperty("size")
  public Long getSize() {
    return size;
  }

  @JsonProperty("size")
  public void setSize(Long size) {
    this.size = size;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiContactPictureMeta contactPictureMeta = (ApiContactPictureMeta) o;
    return Objects.equals(this.mediaType, contactPictureMeta.mediaType) &&
        Objects.equals(this.width, contactPictureMeta.width) &&
        Objects.equals(this.height, contactPictureMeta.height) &&
        Objects.equals(this.size, contactPictureMeta.size);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mediaType, width, height, size);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiContactPictureMeta {\n");
    
    sb.append("    mediaType: ").append(toIndentedString(mediaType)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
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
