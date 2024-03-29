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
import com.sonicle.webtop.contacts.mailchimp.cli.model.TwitterStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
/**
 * A summary of Twitter activity for a campaign.
 */
@Schema(description = "A summary of Twitter activity for a campaign.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")
public class TwitterStats {
  @JsonProperty("tweets")
  private Integer tweets = null;

  @JsonProperty("first_tweet")
  private String firstTweet = null;

  @JsonProperty("last_tweet")
  private String lastTweet = null;

  @JsonProperty("retweets")
  private Integer retweets = null;

  @JsonProperty("statuses")
  private List<TwitterStatus> statuses = null;

   /**
   * The number of tweets including a link to the campaign.
   * @return tweets
  **/
  @Schema(description = "The number of tweets including a link to the campaign.")
  public Integer getTweets() {
    return tweets;
  }

   /**
   * The day and time of the first recorded tweet with a link to the campaign.
   * @return firstTweet
  **/
  @Schema(description = "The day and time of the first recorded tweet with a link to the campaign.")
  public String getFirstTweet() {
    return firstTweet;
  }

   /**
   * The day and time of the last recorded tweet with a link to the campaign.
   * @return lastTweet
  **/
  @Schema(description = "The day and time of the last recorded tweet with a link to the campaign.")
  public String getLastTweet() {
    return lastTweet;
  }

   /**
   * The number of retweets that include a link to the campaign.
   * @return retweets
  **/
  @Schema(description = "The number of retweets that include a link to the campaign.")
  public Integer getRetweets() {
    return retweets;
  }

   /**
   * A summary of tweets that include a link to the campaign.
   * @return statuses
  **/
  @Schema(description = "A summary of tweets that include a link to the campaign.")
  public List<TwitterStatus> getStatuses() {
    return statuses;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TwitterStats twitterStats = (TwitterStats) o;
    return Objects.equals(this.tweets, twitterStats.tweets) &&
        Objects.equals(this.firstTweet, twitterStats.firstTweet) &&
        Objects.equals(this.lastTweet, twitterStats.lastTweet) &&
        Objects.equals(this.retweets, twitterStats.retweets) &&
        Objects.equals(this.statuses, twitterStats.statuses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tweets, firstTweet, lastTweet, retweets, statuses);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TwitterStats {\n");
    
    sb.append("    tweets: ").append(toIndentedString(tweets)).append("\n");
    sb.append("    firstTweet: ").append(toIndentedString(firstTweet)).append("\n");
    sb.append("    lastTweet: ").append(toIndentedString(lastTweet)).append("\n");
    sb.append("    retweets: ").append(toIndentedString(retweets)).append("\n");
    sb.append("    statuses: ").append(toIndentedString(statuses)).append("\n");
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
