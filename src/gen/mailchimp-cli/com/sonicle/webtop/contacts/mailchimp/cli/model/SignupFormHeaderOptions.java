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
/**
 * Options for customizing your signup form header.
 */
@Schema(description = "Options for customizing your signup form header.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class SignupFormHeaderOptions {
  @JsonProperty("image_url")
  private String imageUrl = null;

  @JsonProperty("text")
  private String text = null;

  @JsonProperty("image_width")
  private String imageWidth = null;

  @JsonProperty("image_height")
  private String imageHeight = null;

  @JsonProperty("image_alt")
  private String imageAlt = null;

  @JsonProperty("image_link")
  private String imageLink = null;

  /**
   * Image alignment.
   */
  public enum ImageAlignEnum {
    NONE("none"),
    LEFT("left"),
    CENTER("center"),
    RIGHT("right");

    private String value;

    ImageAlignEnum(String value) {
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
    public static ImageAlignEnum fromValue(String text) {
      for (ImageAlignEnum b : ImageAlignEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("image_align")
  private ImageAlignEnum imageAlign = null;

  @JsonProperty("image_border_width")
  private String imageBorderWidth = null;

  /**
   * Image border style.
   */
  public enum ImageBorderStyleEnum {
    NONE("none"),
    SOLID("solid"),
    DOTTED("dotted"),
    DASHED("dashed"),
    DOUBLE("double"),
    GROOVE("groove"),
    OUTSET("outset"),
    INSET("inset"),
    RIDGE("ridge");

    private String value;

    ImageBorderStyleEnum(String value) {
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
    public static ImageBorderStyleEnum fromValue(String text) {
      for (ImageBorderStyleEnum b : ImageBorderStyleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("image_border_style")
  private ImageBorderStyleEnum imageBorderStyle = null;

  @JsonProperty("image_border_color")
  private String imageBorderColor = null;

  /**
   * Image link target.
   */
  public enum ImageTargetEnum {
    _BLANK("_blank"),
    NULL("null");

    private String value;

    ImageTargetEnum(String value) {
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
    public static ImageTargetEnum fromValue(String text) {
      for (ImageTargetEnum b : ImageTargetEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("image_target")
  private ImageTargetEnum imageTarget = ImageTargetEnum.NULL;

  public SignupFormHeaderOptions imageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

   /**
   * Header image URL.
   * @return imageUrl
  **/
  @Schema(example = "http://gallery.mailchimp.com/332310cb9a420a9e7fea2858a/images/2491208c-9458-4834-a708-fef4ee736472.png", description = "Header image URL.")
  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public SignupFormHeaderOptions text(String text) {
    this.text = text;
    return this;
  }

   /**
   * Header text.
   * @return text
  **/
  @Schema(example = "Header Text goes here", description = "Header text.")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public SignupFormHeaderOptions imageWidth(String imageWidth) {
    this.imageWidth = imageWidth;
    return this;
  }

   /**
   * Image width, in pixels.
   * @return imageWidth
  **/
  @Schema(example = "800", description = "Image width, in pixels.")
  public String getImageWidth() {
    return imageWidth;
  }

  public void setImageWidth(String imageWidth) {
    this.imageWidth = imageWidth;
  }

  public SignupFormHeaderOptions imageHeight(String imageHeight) {
    this.imageHeight = imageHeight;
    return this;
  }

   /**
   * Image height, in pixels.
   * @return imageHeight
  **/
  @Schema(example = "200", description = "Image height, in pixels.")
  public String getImageHeight() {
    return imageHeight;
  }

  public void setImageHeight(String imageHeight) {
    this.imageHeight = imageHeight;
  }

  public SignupFormHeaderOptions imageAlt(String imageAlt) {
    this.imageAlt = imageAlt;
    return this;
  }

   /**
   * Alt text for the image.
   * @return imageAlt
  **/
  @Schema(example = "This is an image", description = "Alt text for the image.")
  public String getImageAlt() {
    return imageAlt;
  }

  public void setImageAlt(String imageAlt) {
    this.imageAlt = imageAlt;
  }

  public SignupFormHeaderOptions imageLink(String imageLink) {
    this.imageLink = imageLink;
    return this;
  }

   /**
   * The URL that the header image will link to.
   * @return imageLink
  **/
  @Schema(example = "gotothisimage.com", description = "The URL that the header image will link to.")
  public String getImageLink() {
    return imageLink;
  }

  public void setImageLink(String imageLink) {
    this.imageLink = imageLink;
  }

  public SignupFormHeaderOptions imageAlign(ImageAlignEnum imageAlign) {
    this.imageAlign = imageAlign;
    return this;
  }

   /**
   * Image alignment.
   * @return imageAlign
  **/
  @Schema(example = "center", description = "Image alignment.")
  public ImageAlignEnum getImageAlign() {
    return imageAlign;
  }

  public void setImageAlign(ImageAlignEnum imageAlign) {
    this.imageAlign = imageAlign;
  }

  public SignupFormHeaderOptions imageBorderWidth(String imageBorderWidth) {
    this.imageBorderWidth = imageBorderWidth;
    return this;
  }

   /**
   * Image border width.
   * @return imageBorderWidth
  **/
  @Schema(example = "2", description = "Image border width.")
  public String getImageBorderWidth() {
    return imageBorderWidth;
  }

  public void setImageBorderWidth(String imageBorderWidth) {
    this.imageBorderWidth = imageBorderWidth;
  }

  public SignupFormHeaderOptions imageBorderStyle(ImageBorderStyleEnum imageBorderStyle) {
    this.imageBorderStyle = imageBorderStyle;
    return this;
  }

   /**
   * Image border style.
   * @return imageBorderStyle
  **/
  @Schema(example = "solid", description = "Image border style.")
  public ImageBorderStyleEnum getImageBorderStyle() {
    return imageBorderStyle;
  }

  public void setImageBorderStyle(ImageBorderStyleEnum imageBorderStyle) {
    this.imageBorderStyle = imageBorderStyle;
  }

  public SignupFormHeaderOptions imageBorderColor(String imageBorderColor) {
    this.imageBorderColor = imageBorderColor;
    return this;
  }

   /**
   * Image border color.
   * @return imageBorderColor
  **/
  @Schema(example = "#896d6d", description = "Image border color.")
  public String getImageBorderColor() {
    return imageBorderColor;
  }

  public void setImageBorderColor(String imageBorderColor) {
    this.imageBorderColor = imageBorderColor;
  }

  public SignupFormHeaderOptions imageTarget(ImageTargetEnum imageTarget) {
    this.imageTarget = imageTarget;
    return this;
  }

   /**
   * Image link target.
   * @return imageTarget
  **/
  @Schema(example = "_blank", description = "Image link target.")
  public ImageTargetEnum getImageTarget() {
    return imageTarget;
  }

  public void setImageTarget(ImageTargetEnum imageTarget) {
    this.imageTarget = imageTarget;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignupFormHeaderOptions signupFormHeaderOptions = (SignupFormHeaderOptions) o;
    return Objects.equals(this.imageUrl, signupFormHeaderOptions.imageUrl) &&
        Objects.equals(this.text, signupFormHeaderOptions.text) &&
        Objects.equals(this.imageWidth, signupFormHeaderOptions.imageWidth) &&
        Objects.equals(this.imageHeight, signupFormHeaderOptions.imageHeight) &&
        Objects.equals(this.imageAlt, signupFormHeaderOptions.imageAlt) &&
        Objects.equals(this.imageLink, signupFormHeaderOptions.imageLink) &&
        Objects.equals(this.imageAlign, signupFormHeaderOptions.imageAlign) &&
        Objects.equals(this.imageBorderWidth, signupFormHeaderOptions.imageBorderWidth) &&
        Objects.equals(this.imageBorderStyle, signupFormHeaderOptions.imageBorderStyle) &&
        Objects.equals(this.imageBorderColor, signupFormHeaderOptions.imageBorderColor) &&
        Objects.equals(this.imageTarget, signupFormHeaderOptions.imageTarget);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imageUrl, text, imageWidth, imageHeight, imageAlt, imageLink, imageAlign, imageBorderWidth, imageBorderStyle, imageBorderColor, imageTarget);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignupFormHeaderOptions {\n");
    
    sb.append("    imageUrl: ").append(toIndentedString(imageUrl)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    imageWidth: ").append(toIndentedString(imageWidth)).append("\n");
    sb.append("    imageHeight: ").append(toIndentedString(imageHeight)).append("\n");
    sb.append("    imageAlt: ").append(toIndentedString(imageAlt)).append("\n");
    sb.append("    imageLink: ").append(toIndentedString(imageLink)).append("\n");
    sb.append("    imageAlign: ").append(toIndentedString(imageAlign)).append("\n");
    sb.append("    imageBorderWidth: ").append(toIndentedString(imageBorderWidth)).append("\n");
    sb.append("    imageBorderStyle: ").append(toIndentedString(imageBorderStyle)).append("\n");
    sb.append("    imageBorderColor: ").append(toIndentedString(imageBorderColor)).append("\n");
    sb.append("    imageTarget: ").append(toIndentedString(imageTarget)).append("\n");
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
