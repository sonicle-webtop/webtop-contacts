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
import com.sonicle.webtop.contacts.mailchimp.cli.model.GalleryFolder3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ResourceLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * A list of all folders in the File Manager.
 */
@Schema(description = "A list of all folders in the File Manager.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class FileManagerFolders {
  @JsonProperty("folders")
  private List<GalleryFolder3> folders = null;

  @JsonProperty("total_items")
  private Integer totalItems = null;

  @JsonProperty("_links")
  private List<ResourceLink> _links = null;

  public FileManagerFolders folders(List<GalleryFolder3> folders) {
    this.folders = folders;
    return this;
  }

  public FileManagerFolders addFoldersItem(GalleryFolder3 foldersItem) {
    if (this.folders == null) {
      this.folders = new ArrayList<>();
    }
    this.folders.add(foldersItem);
    return this;
  }

   /**
   * A list of all folders in the File Manager.
   * @return folders
  **/
  @Schema(description = "A list of all folders in the File Manager.")
  public List<GalleryFolder3> getFolders() {
    return folders;
  }

  public void setFolders(List<GalleryFolder3> folders) {
    this.folders = folders;
  }

   /**
   * The total number of items matching the query regardless of pagination.
   * @return totalItems
  **/
  @Schema(description = "The total number of items matching the query regardless of pagination.")
  public Integer getTotalItems() {
    return totalItems;
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
    FileManagerFolders fileManagerFolders = (FileManagerFolders) o;
    return Objects.equals(this.folders, fileManagerFolders.folders) &&
        Objects.equals(this.totalItems, fileManagerFolders.totalItems) &&
        Objects.equals(this._links, fileManagerFolders._links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(folders, totalItems, _links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FileManagerFolders {\n");
    
    sb.append("    folders: ").append(toIndentedString(folders)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
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