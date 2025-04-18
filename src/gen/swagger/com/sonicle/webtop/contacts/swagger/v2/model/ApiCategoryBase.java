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
 * Represent a category object with base updateable fields.
 **/
@ApiModel(description = "Represent a category object with base updateable fields.")
@JsonTypeName("CategoryBase")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-02-06T10:04:09.242+01:00[Europe/Berlin]")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiCategoryBase   {
  public enum ProviderEnum {

    LOCAL(String.valueOf("local")), CARDDAV(String.valueOf("carddav"));


    private String value;

    ProviderEnum (String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
	public static ProviderEnum fromString(String s) {
        for (ProviderEnum b : ProviderEnum.values()) {
            // using Objects.toString() to be safe if value type non-object type
            // because types like 'int' etc. will be auto-boxed
            if (java.util.Objects.toString(b.value).equals(s)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected string value '" + s + "'");
	}
	
    @JsonCreator
    public static ProviderEnum fromValue(String value) {
        for (ProviderEnum b : ProviderEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private @Valid ProviderEnum provider;
  private @Valid Boolean builtIn;
  private @Valid String name;
  private @Valid String description;
  private @Valid String color;
  public enum EasSyncEnum {

    OFF(String.valueOf("off")), READ(String.valueOf("read")), READ_WRITE(String.valueOf("read-write"));


    private String value;

    EasSyncEnum (String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
	public static EasSyncEnum fromString(String s) {
        for (EasSyncEnum b : EasSyncEnum.values()) {
            // using Objects.toString() to be safe if value type non-object type
            // because types like 'int' etc. will be auto-boxed
            if (java.util.Objects.toString(b.value).equals(s)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected string value '" + s + "'");
	}
	
    @JsonCreator
    public static EasSyncEnum fromValue(String value) {
        for (EasSyncEnum b : EasSyncEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private @Valid EasSyncEnum easSync;
  private @Valid Boolean isPrivate;
  private @Valid Integer remoteSyncFrequency;
  private @Valid String remoteSyncTimestamp;
  private @Valid String remoteSyncToken;
  private @Valid String providerParams;

  /**
   **/
  public ApiCategoryBase provider(ProviderEnum provider) {
    this.provider = provider;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("provider")
  @NotNull
  public ProviderEnum getProvider() {
    return provider;
  }

  @JsonProperty("provider")
  public void setProvider(ProviderEnum provider) {
    this.provider = provider;
  }

  /**
   **/
  public ApiCategoryBase builtIn(Boolean builtIn) {
    this.builtIn = builtIn;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("builtIn")
  @NotNull
  public Boolean getBuiltIn() {
    return builtIn;
  }

  @JsonProperty("builtIn")
  public void setBuiltIn(Boolean builtIn) {
    this.builtIn = builtIn;
  }

  /**
   **/
  public ApiCategoryBase name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public ApiCategoryBase description(String description) {
    this.description = description;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  public ApiCategoryBase color(String color) {
    this.color = color;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("color")
  @NotNull
  public String getColor() {
    return color;
  }

  @JsonProperty("color")
  public void setColor(String color) {
    this.color = color;
  }

  /**
   **/
  public ApiCategoryBase easSync(EasSyncEnum easSync) {
    this.easSync = easSync;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("easSync")
  @NotNull
  public EasSyncEnum getEasSync() {
    return easSync;
  }

  @JsonProperty("easSync")
  public void setEasSync(EasSyncEnum easSync) {
    this.easSync = easSync;
  }

  /**
   **/
  public ApiCategoryBase isPrivate(Boolean isPrivate) {
    this.isPrivate = isPrivate;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("isPrivate")
  @NotNull
  public Boolean getIsPrivate() {
    return isPrivate;
  }

  @JsonProperty("isPrivate")
  public void setIsPrivate(Boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

  /**
   **/
  public ApiCategoryBase remoteSyncFrequency(Integer remoteSyncFrequency) {
    this.remoteSyncFrequency = remoteSyncFrequency;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("remoteSyncFrequency")
  public Integer getRemoteSyncFrequency() {
    return remoteSyncFrequency;
  }

  @JsonProperty("remoteSyncFrequency")
  public void setRemoteSyncFrequency(Integer remoteSyncFrequency) {
    this.remoteSyncFrequency = remoteSyncFrequency;
  }

  /**
   **/
  public ApiCategoryBase remoteSyncTimestamp(String remoteSyncTimestamp) {
    this.remoteSyncTimestamp = remoteSyncTimestamp;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("remoteSyncTimestamp")
  public String getRemoteSyncTimestamp() {
    return remoteSyncTimestamp;
  }

  @JsonProperty("remoteSyncTimestamp")
  public void setRemoteSyncTimestamp(String remoteSyncTimestamp) {
    this.remoteSyncTimestamp = remoteSyncTimestamp;
  }

  /**
   **/
  public ApiCategoryBase remoteSyncToken(String remoteSyncToken) {
    this.remoteSyncToken = remoteSyncToken;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("remoteSyncToken")
  public String getRemoteSyncToken() {
    return remoteSyncToken;
  }

  @JsonProperty("remoteSyncToken")
  public void setRemoteSyncToken(String remoteSyncToken) {
    this.remoteSyncToken = remoteSyncToken;
  }

  /**
   **/
  public ApiCategoryBase providerParams(String providerParams) {
    this.providerParams = providerParams;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("providerParams")
  public String getProviderParams() {
    return providerParams;
  }

  @JsonProperty("providerParams")
  public void setProviderParams(String providerParams) {
    this.providerParams = providerParams;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiCategoryBase categoryBase = (ApiCategoryBase) o;
    return Objects.equals(this.provider, categoryBase.provider) &&
        Objects.equals(this.builtIn, categoryBase.builtIn) &&
        Objects.equals(this.name, categoryBase.name) &&
        Objects.equals(this.description, categoryBase.description) &&
        Objects.equals(this.color, categoryBase.color) &&
        Objects.equals(this.easSync, categoryBase.easSync) &&
        Objects.equals(this.isPrivate, categoryBase.isPrivate) &&
        Objects.equals(this.remoteSyncFrequency, categoryBase.remoteSyncFrequency) &&
        Objects.equals(this.remoteSyncTimestamp, categoryBase.remoteSyncTimestamp) &&
        Objects.equals(this.remoteSyncToken, categoryBase.remoteSyncToken) &&
        Objects.equals(this.providerParams, categoryBase.providerParams);
  }

  @Override
  public int hashCode() {
    return Objects.hash(provider, builtIn, name, description, color, easSync, isPrivate, remoteSyncFrequency, remoteSyncTimestamp, remoteSyncToken, providerParams);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiCategoryBase {\n");
    
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
    sb.append("    builtIn: ").append(toIndentedString(builtIn)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    easSync: ").append(toIndentedString(easSync)).append("\n");
    sb.append("    isPrivate: ").append(toIndentedString(isPrivate)).append("\n");
    sb.append("    remoteSyncFrequency: ").append(toIndentedString(remoteSyncFrequency)).append("\n");
    sb.append("    remoteSyncTimestamp: ").append(toIndentedString(remoteSyncTimestamp)).append("\n");
    sb.append("    remoteSyncToken: ").append(toIndentedString(remoteSyncToken)).append("\n");
    sb.append("    providerParams: ").append(toIndentedString(providerParams)).append("\n");
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
