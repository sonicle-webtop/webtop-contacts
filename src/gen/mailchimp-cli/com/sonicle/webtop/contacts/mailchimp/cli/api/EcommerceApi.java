package com.sonicle.webtop.contacts.mailchimp.cli.api;

import com.sonicle.webtop.contacts.mailchimp.cli.ApiException;
import com.sonicle.webtop.contacts.mailchimp.cli.ApiClient;
import com.sonicle.webtop.contacts.mailchimp.cli.Configuration;
import com.sonicle.webtop.contacts.mailchimp.cli.Pair;

import javax.ws.rs.core.GenericType;

import com.sonicle.webtop.contacts.mailchimp.cli.model.CartLines;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Carts;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Customers;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCart;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCart1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCart2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCartLineItem2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCartLineItem3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCartLineItem4;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCustomer2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCustomer3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCustomer4;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceCustomer5;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceOrder;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceOrder1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceOrder2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceOrderLineItem2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceOrderLineItem3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceOrderLineItem4;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProduct;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProduct1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProduct2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductImage2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductImage3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductImage4;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductImages;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductVariant2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductVariant3;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductVariant4;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductVariant5;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceProductVariants;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommercePromoCode;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommercePromoCode1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommercePromoCode2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommercePromoRule;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommercePromoRule1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommercePromoRule2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceStore;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceStore1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceStore2;
import com.sonicle.webtop.contacts.mailchimp.cli.model.EcommerceStores;
import com.sonicle.webtop.contacts.mailchimp.cli.model.OrderLines;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Orders;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Orders1;
import com.sonicle.webtop.contacts.mailchimp.cli.model.ProblemDetailDocument;
import com.sonicle.webtop.contacts.mailchimp.cli.model.Products;
import com.sonicle.webtop.contacts.mailchimp.cli.model.PromoCodes;
import com.sonicle.webtop.contacts.mailchimp.cli.model.PromoRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-22T12:55:27.492+02:00[Europe/Berlin]")public class EcommerceApi {
  private ApiClient apiClient;

  public EcommerceApi() {
    this(Configuration.getDefaultApiClient());
  }

  public EcommerceApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Delete store
   * Delete a store. Deleting a store will also delete any associated subresources, including Customers, Orders, Products, and Carts.
   * @param storeId The store id. (required)
   * @return Object
   * @throws ApiException if fails to make API call
   */
  public Object deleteEcommerceStoresId(String storeId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<Object> localVarReturnType = new GenericType<Object>() {};
    return apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Delete cart
   * Delete a cart.
   * @param storeId The store id. (required)
   * @param cartId The id for the cart. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdCartsId(String storeId, String cartId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdCartsId");
    }
    // verify the required parameter 'cartId' is set
    if (cartId == null) {
      throw new ApiException(400, "Missing the required parameter 'cartId' when calling deleteEcommerceStoresIdCartsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts/{cart_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "cart_id" + "\\}", apiClient.escapeString(cartId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete cart line item
   * Delete a specific cart line item.
   * @param storeId The store id. (required)
   * @param cartId The id for the cart. (required)
   * @param lineId The id for the line item of a cart. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdCartsLinesId(String storeId, String cartId, String lineId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdCartsLinesId");
    }
    // verify the required parameter 'cartId' is set
    if (cartId == null) {
      throw new ApiException(400, "Missing the required parameter 'cartId' when calling deleteEcommerceStoresIdCartsLinesId");
    }
    // verify the required parameter 'lineId' is set
    if (lineId == null) {
      throw new ApiException(400, "Missing the required parameter 'lineId' when calling deleteEcommerceStoresIdCartsLinesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts/{cart_id}/lines/{line_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "cart_id" + "\\}", apiClient.escapeString(cartId.toString()))
      .replaceAll("\\{" + "line_id" + "\\}", apiClient.escapeString(lineId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete customer
   * Delete a customer from a store.
   * @param storeId The store id. (required)
   * @param customerId The id for the customer of a store. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdCustomersId(String storeId, String customerId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdCustomersId");
    }
    // verify the required parameter 'customerId' is set
    if (customerId == null) {
      throw new ApiException(400, "Missing the required parameter 'customerId' when calling deleteEcommerceStoresIdCustomersId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/customers/{customer_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "customer_id" + "\\}", apiClient.escapeString(customerId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete order
   * Delete an order.
   * @param storeId The store id. (required)
   * @param orderId The id for the order in a store. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdOrdersId(String storeId, String orderId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdOrdersId");
    }
    // verify the required parameter 'orderId' is set
    if (orderId == null) {
      throw new ApiException(400, "Missing the required parameter 'orderId' when calling deleteEcommerceStoresIdOrdersId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders/{order_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete order line item
   * Delete a specific order line item.
   * @param storeId The store id. (required)
   * @param orderId The id for the order in a store. (required)
   * @param lineId The id for the line item of an order. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdOrdersIdLinesId(String storeId, String orderId, String lineId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdOrdersIdLinesId");
    }
    // verify the required parameter 'orderId' is set
    if (orderId == null) {
      throw new ApiException(400, "Missing the required parameter 'orderId' when calling deleteEcommerceStoresIdOrdersIdLinesId");
    }
    // verify the required parameter 'lineId' is set
    if (lineId == null) {
      throw new ApiException(400, "Missing the required parameter 'lineId' when calling deleteEcommerceStoresIdOrdersIdLinesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders/{order_id}/lines/{line_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()))
      .replaceAll("\\{" + "line_id" + "\\}", apiClient.escapeString(lineId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete product
   * Delete a product.
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdProductsId(String storeId, String productId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdProductsId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling deleteEcommerceStoresIdProductsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete product image
   * Delete a product image.
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param imageId The id for the product image. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdProductsIdImagesId(String storeId, String productId, String imageId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdProductsIdImagesId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling deleteEcommerceStoresIdProductsIdImagesId");
    }
    // verify the required parameter 'imageId' is set
    if (imageId == null) {
      throw new ApiException(400, "Missing the required parameter 'imageId' when calling deleteEcommerceStoresIdProductsIdImagesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/images/{image_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()))
      .replaceAll("\\{" + "image_id" + "\\}", apiClient.escapeString(imageId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete product variant
   * Delete a product variant.
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param variantId The id for the product variant. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdProductsIdVariantsId(String storeId, String productId, String variantId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling deleteEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'variantId' is set
    if (variantId == null) {
      throw new ApiException(400, "Missing the required parameter 'variantId' when calling deleteEcommerceStoresIdProductsIdVariantsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/variants/{variant_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()))
      .replaceAll("\\{" + "variant_id" + "\\}", apiClient.escapeString(variantId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete promo code
   * Delete a promo code from a store.
   * @param storeId The store id. (required)
   * @param promoRuleId The id for the promo rule of a store. (required)
   * @param promoCodeId The id for the promo code of a store. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdPromocodesId(String storeId, String promoRuleId, String promoCodeId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdPromocodesId");
    }
    // verify the required parameter 'promoRuleId' is set
    if (promoRuleId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoRuleId' when calling deleteEcommerceStoresIdPromocodesId");
    }
    // verify the required parameter 'promoCodeId' is set
    if (promoCodeId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoCodeId' when calling deleteEcommerceStoresIdPromocodesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules/{promo_rule_id}/promo-codes/{promo_code_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "promo_rule_id" + "\\}", apiClient.escapeString(promoRuleId.toString()))
      .replaceAll("\\{" + "promo_code_id" + "\\}", apiClient.escapeString(promoCodeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Delete promo rule
   * Delete a promo rule from a store.
   * @param storeId The store id. (required)
   * @param promoRuleId The id for the promo rule of a store. (required)
   * @throws ApiException if fails to make API call
   */
  public void deleteEcommerceStoresIdPromorulesId(String storeId, String promoRuleId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling deleteEcommerceStoresIdPromorulesId");
    }
    // verify the required parameter 'promoRuleId' is set
    if (promoRuleId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoRuleId' when calling deleteEcommerceStoresIdPromorulesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules/{promo_rule_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "promo_rule_id" + "\\}", apiClient.escapeString(promoRuleId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * List account orders
   * Get information about an account&#x27;s orders.
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param campaignId Restrict results to orders with a specific &#x60;campaign_id&#x60; value. (optional)
   * @param outreachId Restrict results to orders with a specific &#x60;outreach_id&#x60; value. (optional)
   * @param customerId Restrict results to orders made by a specific customer. (optional)
   * @param hasOutreach Restrict results to orders that have an outreach attached. For example, an email campaign or Facebook ad. (optional)
   * @return Orders
   * @throws ApiException if fails to make API call
   */
  public Orders getEcommerceOrders(List<String> fields, List<String> excludeFields, Integer count, Integer offset, String campaignId, String outreachId, String customerId, Boolean hasOutreach) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/ecommerce/orders";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "campaign_id", campaignId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "outreach_id", outreachId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "customer_id", customerId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "has_outreach", hasOutreach));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<Orders> localVarReturnType = new GenericType<Orders>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List stores
   * Get information about all stores in the account.
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return EcommerceStores
   * @throws ApiException if fails to make API call
   */
  public EcommerceStores getEcommerceStores(List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // create path and map variables
    String localVarPath = "/ecommerce/stores";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceStores> localVarReturnType = new GenericType<EcommerceStores>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get store info
   * Get information about a specific store.
   * @param storeId The store id. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceStore1
   * @throws ApiException if fails to make API call
   */
  public EcommerceStore1 getEcommerceStoresId(String storeId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceStore1> localVarReturnType = new GenericType<EcommerceStore1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List carts
   * Get information about a store&#x27;s carts.
   * @param storeId The store id. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return Carts
   * @throws ApiException if fails to make API call
   */
  public Carts getEcommerceStoresIdCarts(String storeId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdCarts");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<Carts> localVarReturnType = new GenericType<Carts>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get cart info
   * Get information about a specific cart.
   * @param storeId The store id. (required)
   * @param cartId The id for the cart. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceCart1
   * @throws ApiException if fails to make API call
   */
  public EcommerceCart1 getEcommerceStoresIdCartsId(String storeId, String cartId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdCartsId");
    }
    // verify the required parameter 'cartId' is set
    if (cartId == null) {
      throw new ApiException(400, "Missing the required parameter 'cartId' when calling getEcommerceStoresIdCartsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts/{cart_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "cart_id" + "\\}", apiClient.escapeString(cartId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCart1> localVarReturnType = new GenericType<EcommerceCart1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List cart line items
   * Get information about a cart&#x27;s line items.
   * @param storeId The store id. (required)
   * @param cartId The id for the cart. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return CartLines
   * @throws ApiException if fails to make API call
   */
  public CartLines getEcommerceStoresIdCartsIdLines(String storeId, String cartId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdCartsIdLines");
    }
    // verify the required parameter 'cartId' is set
    if (cartId == null) {
      throw new ApiException(400, "Missing the required parameter 'cartId' when calling getEcommerceStoresIdCartsIdLines");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts/{cart_id}/lines"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "cart_id" + "\\}", apiClient.escapeString(cartId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<CartLines> localVarReturnType = new GenericType<CartLines>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get cart line item
   * Get information about a specific cart line item.
   * @param storeId The store id. (required)
   * @param cartId The id for the cart. (required)
   * @param lineId The id for the line item of a cart. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceCartLineItem3
   * @throws ApiException if fails to make API call
   */
  public EcommerceCartLineItem3 getEcommerceStoresIdCartsIdLinesId(String storeId, String cartId, String lineId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdCartsIdLinesId");
    }
    // verify the required parameter 'cartId' is set
    if (cartId == null) {
      throw new ApiException(400, "Missing the required parameter 'cartId' when calling getEcommerceStoresIdCartsIdLinesId");
    }
    // verify the required parameter 'lineId' is set
    if (lineId == null) {
      throw new ApiException(400, "Missing the required parameter 'lineId' when calling getEcommerceStoresIdCartsIdLinesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts/{cart_id}/lines/{line_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "cart_id" + "\\}", apiClient.escapeString(cartId.toString()))
      .replaceAll("\\{" + "line_id" + "\\}", apiClient.escapeString(lineId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCartLineItem3> localVarReturnType = new GenericType<EcommerceCartLineItem3>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List customers
   * Get information about a store&#x27;s customers.
   * @param storeId The store id. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param emailAddress Restrict the response to customers with the email address. (optional)
   * @return Customers
   * @throws ApiException if fails to make API call
   */
  public Customers getEcommerceStoresIdCustomers(String storeId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String emailAddress) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdCustomers");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/customers"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "email_address", emailAddress));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<Customers> localVarReturnType = new GenericType<Customers>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get customer info
   * Get information about a specific customer.
   * @param storeId The store id. (required)
   * @param customerId The id for the customer of a store. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceCustomer3
   * @throws ApiException if fails to make API call
   */
  public EcommerceCustomer3 getEcommerceStoresIdCustomersId(String storeId, String customerId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdCustomersId");
    }
    // verify the required parameter 'customerId' is set
    if (customerId == null) {
      throw new ApiException(400, "Missing the required parameter 'customerId' when calling getEcommerceStoresIdCustomersId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/customers/{customer_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "customer_id" + "\\}", apiClient.escapeString(customerId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCustomer3> localVarReturnType = new GenericType<EcommerceCustomer3>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List orders
   * Get information about a store&#x27;s orders.
   * @param storeId The store id. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @param customerId Restrict results to orders made by a specific customer. (optional)
   * @param hasOutreach Restrict results to orders that have an outreach attached. For example, an email campaign or Facebook ad. (optional)
   * @param campaignId Restrict results to orders with a specific &#x60;campaign_id&#x60; value. (optional)
   * @param outreachId Restrict results to orders with a specific &#x60;outreach_id&#x60; value. (optional)
   * @return Orders1
   * @throws ApiException if fails to make API call
   */
  public Orders1 getEcommerceStoresIdOrders(String storeId, List<String> fields, List<String> excludeFields, Integer count, Integer offset, String customerId, Boolean hasOutreach, String campaignId, String outreachId) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdOrders");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "customer_id", customerId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "has_outreach", hasOutreach));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "campaign_id", campaignId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "outreach_id", outreachId));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<Orders1> localVarReturnType = new GenericType<Orders1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get order info
   * Get information about a specific order.
   * @param storeId The store id. (required)
   * @param orderId The id for the order in a store. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceOrder1
   * @throws ApiException if fails to make API call
   */
  public EcommerceOrder1 getEcommerceStoresIdOrdersId(String storeId, String orderId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdOrdersId");
    }
    // verify the required parameter 'orderId' is set
    if (orderId == null) {
      throw new ApiException(400, "Missing the required parameter 'orderId' when calling getEcommerceStoresIdOrdersId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders/{order_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceOrder1> localVarReturnType = new GenericType<EcommerceOrder1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List order line items
   * Get information about an order&#x27;s line items.
   * @param storeId The store id. (required)
   * @param orderId The id for the order in a store. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return OrderLines
   * @throws ApiException if fails to make API call
   */
  public OrderLines getEcommerceStoresIdOrdersIdLines(String storeId, String orderId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdOrdersIdLines");
    }
    // verify the required parameter 'orderId' is set
    if (orderId == null) {
      throw new ApiException(400, "Missing the required parameter 'orderId' when calling getEcommerceStoresIdOrdersIdLines");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders/{order_id}/lines"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<OrderLines> localVarReturnType = new GenericType<OrderLines>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get order line item
   * Get information about a specific order line item.
   * @param storeId The store id. (required)
   * @param orderId The id for the order in a store. (required)
   * @param lineId The id for the line item of an order. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceOrderLineItem3
   * @throws ApiException if fails to make API call
   */
  public EcommerceOrderLineItem3 getEcommerceStoresIdOrdersIdLinesId(String storeId, String orderId, String lineId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdOrdersIdLinesId");
    }
    // verify the required parameter 'orderId' is set
    if (orderId == null) {
      throw new ApiException(400, "Missing the required parameter 'orderId' when calling getEcommerceStoresIdOrdersIdLinesId");
    }
    // verify the required parameter 'lineId' is set
    if (lineId == null) {
      throw new ApiException(400, "Missing the required parameter 'lineId' when calling getEcommerceStoresIdOrdersIdLinesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders/{order_id}/lines/{line_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()))
      .replaceAll("\\{" + "line_id" + "\\}", apiClient.escapeString(lineId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceOrderLineItem3> localVarReturnType = new GenericType<EcommerceOrderLineItem3>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List product
   * Get information about a store&#x27;s products.
   * @param storeId The store id. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return Products
   * @throws ApiException if fails to make API call
   */
  public Products getEcommerceStoresIdProducts(String storeId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdProducts");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<Products> localVarReturnType = new GenericType<Products>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get product info
   * Get information about a specific product.
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceProduct1
   * @throws ApiException if fails to make API call
   */
  public EcommerceProduct1 getEcommerceStoresIdProductsId(String storeId, String productId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdProductsId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling getEcommerceStoresIdProductsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProduct1> localVarReturnType = new GenericType<EcommerceProduct1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List product images
   * Get information about a product&#x27;s images.
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return EcommerceProductImages
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductImages getEcommerceStoresIdProductsIdImages(String storeId, String productId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdProductsIdImages");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling getEcommerceStoresIdProductsIdImages");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/images"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductImages> localVarReturnType = new GenericType<EcommerceProductImages>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get product image info
   * Get information about a specific product image.
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param imageId The id for the product image. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceProductImage3
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductImage3 getEcommerceStoresIdProductsIdImagesId(String storeId, String productId, String imageId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdProductsIdImagesId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling getEcommerceStoresIdProductsIdImagesId");
    }
    // verify the required parameter 'imageId' is set
    if (imageId == null) {
      throw new ApiException(400, "Missing the required parameter 'imageId' when calling getEcommerceStoresIdProductsIdImagesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/images/{image_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()))
      .replaceAll("\\{" + "image_id" + "\\}", apiClient.escapeString(imageId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductImage3> localVarReturnType = new GenericType<EcommerceProductImage3>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List product variants
   * Get information about a product&#x27;s variants.
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return EcommerceProductVariants
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductVariants getEcommerceStoresIdProductsIdVariants(String storeId, String productId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdProductsIdVariants");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling getEcommerceStoresIdProductsIdVariants");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/variants"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductVariants> localVarReturnType = new GenericType<EcommerceProductVariants>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get product variant info
   * Get information about a specific product variant.
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param variantId The id for the product variant. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommerceProductVariant3
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductVariant3 getEcommerceStoresIdProductsIdVariantsId(String storeId, String productId, String variantId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling getEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'variantId' is set
    if (variantId == null) {
      throw new ApiException(400, "Missing the required parameter 'variantId' when calling getEcommerceStoresIdProductsIdVariantsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/variants/{variant_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()))
      .replaceAll("\\{" + "variant_id" + "\\}", apiClient.escapeString(variantId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductVariant3> localVarReturnType = new GenericType<EcommerceProductVariant3>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List promo codes
   * Get information about a store&#x27;s promo codes.
   * @param promoRuleId The id for the promo rule of a store. (required)
   * @param storeId The store id. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return PromoCodes
   * @throws ApiException if fails to make API call
   */
  public PromoCodes getEcommerceStoresIdPromocodes(String promoRuleId, String storeId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'promoRuleId' is set
    if (promoRuleId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoRuleId' when calling getEcommerceStoresIdPromocodes");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdPromocodes");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules/{promo_rule_id}/promo-codes"
      .replaceAll("\\{" + "promo_rule_id" + "\\}", apiClient.escapeString(promoRuleId.toString()))
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<PromoCodes> localVarReturnType = new GenericType<PromoCodes>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get promo code
   * Get information about a specific promo code.
   * @param storeId The store id. (required)
   * @param promoRuleId The id for the promo rule of a store. (required)
   * @param promoCodeId The id for the promo code of a store. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommercePromoCode1
   * @throws ApiException if fails to make API call
   */
  public EcommercePromoCode1 getEcommerceStoresIdPromocodesId(String storeId, String promoRuleId, String promoCodeId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdPromocodesId");
    }
    // verify the required parameter 'promoRuleId' is set
    if (promoRuleId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoRuleId' when calling getEcommerceStoresIdPromocodesId");
    }
    // verify the required parameter 'promoCodeId' is set
    if (promoCodeId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoCodeId' when calling getEcommerceStoresIdPromocodesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules/{promo_rule_id}/promo-codes/{promo_code_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "promo_rule_id" + "\\}", apiClient.escapeString(promoRuleId.toString()))
      .replaceAll("\\{" + "promo_code_id" + "\\}", apiClient.escapeString(promoCodeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommercePromoCode1> localVarReturnType = new GenericType<EcommercePromoCode1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * List promo rules
   * Get information about a store&#x27;s promo rules.
   * @param storeId The store id. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @param count The number of records to return. Default value is 10. Maximum value is 1000 (optional, default to 10)
   * @param offset Used for [pagination](https://mailchimp.com/developer/marketing/docs/methods-parameters/#pagination), this it the number of records from a collection to skip. Default value is 0. (optional, default to 0)
   * @return PromoRules
   * @throws ApiException if fails to make API call
   */
  public PromoRules getEcommerceStoresIdPromorules(String storeId, List<String> fields, List<String> excludeFields, Integer count, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdPromorules");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<PromoRules> localVarReturnType = new GenericType<PromoRules>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get promo rule
   * Get information about a specific promo rule.
   * @param storeId The store id. (required)
   * @param promoRuleId The id for the promo rule of a store. (required)
   * @param fields A comma-separated list of fields to return. Reference parameters of sub-objects with dot notation. (optional)
   * @param excludeFields A comma-separated list of fields to exclude. Reference parameters of sub-objects with dot notation. (optional)
   * @return EcommercePromoRule1
   * @throws ApiException if fails to make API call
   */
  public EcommercePromoRule1 getEcommerceStoresIdPromorulesId(String storeId, String promoRuleId, List<String> fields, List<String> excludeFields) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling getEcommerceStoresIdPromorulesId");
    }
    // verify the required parameter 'promoRuleId' is set
    if (promoRuleId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoRuleId' when calling getEcommerceStoresIdPromorulesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules/{promo_rule_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "promo_rule_id" + "\\}", apiClient.escapeString(promoRuleId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "fields", fields));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "exclude_fields", excludeFields));


    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommercePromoRule1> localVarReturnType = new GenericType<EcommercePromoRule1>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update store
   * Update a store.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @return EcommerceStore1
   * @throws ApiException if fails to make API call
   */
  public EcommerceStore1 patchEcommerceStoresId(EcommerceStore2 body, String storeId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceStore1> localVarReturnType = new GenericType<EcommerceStore1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update cart
   * Update a specific cart.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param cartId The id for the cart. (required)
   * @return EcommerceCart1
   * @throws ApiException if fails to make API call
   */
  public EcommerceCart1 patchEcommerceStoresIdCartsId(EcommerceCart2 body, String storeId, String cartId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdCartsId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdCartsId");
    }
    // verify the required parameter 'cartId' is set
    if (cartId == null) {
      throw new ApiException(400, "Missing the required parameter 'cartId' when calling patchEcommerceStoresIdCartsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts/{cart_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "cart_id" + "\\}", apiClient.escapeString(cartId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCart1> localVarReturnType = new GenericType<EcommerceCart1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update cart line item
   * Update a specific cart line item.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param cartId The id for the cart. (required)
   * @param lineId The id for the line item of a cart. (required)
   * @return EcommerceCartLineItem3
   * @throws ApiException if fails to make API call
   */
  public EcommerceCartLineItem3 patchEcommerceStoresIdCartsIdLinesId(EcommerceCartLineItem4 body, String storeId, String cartId, String lineId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdCartsIdLinesId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdCartsIdLinesId");
    }
    // verify the required parameter 'cartId' is set
    if (cartId == null) {
      throw new ApiException(400, "Missing the required parameter 'cartId' when calling patchEcommerceStoresIdCartsIdLinesId");
    }
    // verify the required parameter 'lineId' is set
    if (lineId == null) {
      throw new ApiException(400, "Missing the required parameter 'lineId' when calling patchEcommerceStoresIdCartsIdLinesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts/{cart_id}/lines/{line_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "cart_id" + "\\}", apiClient.escapeString(cartId.toString()))
      .replaceAll("\\{" + "line_id" + "\\}", apiClient.escapeString(lineId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCartLineItem3> localVarReturnType = new GenericType<EcommerceCartLineItem3>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update customer
   * Update a customer.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param customerId The id for the customer of a store. (required)
   * @return EcommerceCustomer3
   * @throws ApiException if fails to make API call
   */
  public EcommerceCustomer3 patchEcommerceStoresIdCustomersId(EcommerceCustomer5 body, String storeId, String customerId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdCustomersId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdCustomersId");
    }
    // verify the required parameter 'customerId' is set
    if (customerId == null) {
      throw new ApiException(400, "Missing the required parameter 'customerId' when calling patchEcommerceStoresIdCustomersId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/customers/{customer_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "customer_id" + "\\}", apiClient.escapeString(customerId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCustomer3> localVarReturnType = new GenericType<EcommerceCustomer3>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update order
   * Update a specific order.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param orderId The id for the order in a store. (required)
   * @return EcommerceOrder1
   * @throws ApiException if fails to make API call
   */
  public EcommerceOrder1 patchEcommerceStoresIdOrdersId(EcommerceOrder2 body, String storeId, String orderId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdOrdersId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdOrdersId");
    }
    // verify the required parameter 'orderId' is set
    if (orderId == null) {
      throw new ApiException(400, "Missing the required parameter 'orderId' when calling patchEcommerceStoresIdOrdersId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders/{order_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceOrder1> localVarReturnType = new GenericType<EcommerceOrder1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update order line item
   * Update a specific order line item.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param orderId The id for the order in a store. (required)
   * @param lineId The id for the line item of an order. (required)
   * @return EcommerceOrderLineItem3
   * @throws ApiException if fails to make API call
   */
  public EcommerceOrderLineItem3 patchEcommerceStoresIdOrdersIdLinesId(EcommerceOrderLineItem4 body, String storeId, String orderId, String lineId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdOrdersIdLinesId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdOrdersIdLinesId");
    }
    // verify the required parameter 'orderId' is set
    if (orderId == null) {
      throw new ApiException(400, "Missing the required parameter 'orderId' when calling patchEcommerceStoresIdOrdersIdLinesId");
    }
    // verify the required parameter 'lineId' is set
    if (lineId == null) {
      throw new ApiException(400, "Missing the required parameter 'lineId' when calling patchEcommerceStoresIdOrdersIdLinesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders/{order_id}/lines/{line_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()))
      .replaceAll("\\{" + "line_id" + "\\}", apiClient.escapeString(lineId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceOrderLineItem3> localVarReturnType = new GenericType<EcommerceOrderLineItem3>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update product
   * Update a specific product.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @return EcommerceProduct1
   * @throws ApiException if fails to make API call
   */
  public EcommerceProduct1 patchEcommerceStoresIdProductsId(EcommerceProduct2 body, String storeId, String productId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdProductsId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdProductsId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling patchEcommerceStoresIdProductsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProduct1> localVarReturnType = new GenericType<EcommerceProduct1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update product image
   * Update a product image.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param imageId The id for the product image. (required)
   * @return EcommerceProductImage3
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductImage3 patchEcommerceStoresIdProductsIdImagesId(EcommerceProductImage4 body, String storeId, String productId, String imageId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdProductsIdImagesId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdProductsIdImagesId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling patchEcommerceStoresIdProductsIdImagesId");
    }
    // verify the required parameter 'imageId' is set
    if (imageId == null) {
      throw new ApiException(400, "Missing the required parameter 'imageId' when calling patchEcommerceStoresIdProductsIdImagesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/images/{image_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()))
      .replaceAll("\\{" + "image_id" + "\\}", apiClient.escapeString(imageId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductImage3> localVarReturnType = new GenericType<EcommerceProductImage3>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update product variant
   * Update a product variant.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param variantId The id for the product variant. (required)
   * @return EcommerceProductVariant3
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductVariant3 patchEcommerceStoresIdProductsIdVariantsId(EcommerceProductVariant5 body, String storeId, String productId, String variantId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling patchEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'variantId' is set
    if (variantId == null) {
      throw new ApiException(400, "Missing the required parameter 'variantId' when calling patchEcommerceStoresIdProductsIdVariantsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/variants/{variant_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()))
      .replaceAll("\\{" + "variant_id" + "\\}", apiClient.escapeString(variantId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductVariant3> localVarReturnType = new GenericType<EcommerceProductVariant3>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update promo code
   * Update a promo code.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param promoRuleId The id for the promo rule of a store. (required)
   * @param promoCodeId The id for the promo code of a store. (required)
   * @return EcommercePromoCode1
   * @throws ApiException if fails to make API call
   */
  public EcommercePromoCode1 patchEcommerceStoresIdPromocodesId(EcommercePromoCode2 body, String storeId, String promoRuleId, String promoCodeId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdPromocodesId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdPromocodesId");
    }
    // verify the required parameter 'promoRuleId' is set
    if (promoRuleId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoRuleId' when calling patchEcommerceStoresIdPromocodesId");
    }
    // verify the required parameter 'promoCodeId' is set
    if (promoCodeId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoCodeId' when calling patchEcommerceStoresIdPromocodesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules/{promo_rule_id}/promo-codes/{promo_code_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "promo_rule_id" + "\\}", apiClient.escapeString(promoRuleId.toString()))
      .replaceAll("\\{" + "promo_code_id" + "\\}", apiClient.escapeString(promoCodeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommercePromoCode1> localVarReturnType = new GenericType<EcommercePromoCode1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Update promo rule
   * Update a promo rule.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param promoRuleId The id for the promo rule of a store. (required)
   * @return EcommercePromoRule1
   * @throws ApiException if fails to make API call
   */
  public EcommercePromoRule1 patchEcommerceStoresIdPromorulesId(EcommercePromoRule2 body, String storeId, String promoRuleId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling patchEcommerceStoresIdPromorulesId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling patchEcommerceStoresIdPromorulesId");
    }
    // verify the required parameter 'promoRuleId' is set
    if (promoRuleId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoRuleId' when calling patchEcommerceStoresIdPromorulesId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules/{promo_rule_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "promo_rule_id" + "\\}", apiClient.escapeString(promoRuleId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommercePromoRule1> localVarReturnType = new GenericType<EcommercePromoRule1>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add store
   * Add a new store to your Mailchimp account.
   * @param body  (required)
   * @return EcommerceStore1
   * @throws ApiException if fails to make API call
   */
  public EcommerceStore1 postEcommerceStores(EcommerceStore body) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStores");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceStore1> localVarReturnType = new GenericType<EcommerceStore1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add cart
   * Add a new cart to a store.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @return EcommerceCart1
   * @throws ApiException if fails to make API call
   */
  public EcommerceCart1 postEcommerceStoresIdCarts(EcommerceCart body, String storeId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdCarts");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdCarts");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCart1> localVarReturnType = new GenericType<EcommerceCart1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add cart line item
   * Add a new line item to an existing cart.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param cartId The id for the cart. (required)
   * @return EcommerceCartLineItem3
   * @throws ApiException if fails to make API call
   */
  public EcommerceCartLineItem3 postEcommerceStoresIdCartsIdLines(EcommerceCartLineItem2 body, String storeId, String cartId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdCartsIdLines");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdCartsIdLines");
    }
    // verify the required parameter 'cartId' is set
    if (cartId == null) {
      throw new ApiException(400, "Missing the required parameter 'cartId' when calling postEcommerceStoresIdCartsIdLines");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/carts/{cart_id}/lines"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "cart_id" + "\\}", apiClient.escapeString(cartId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCartLineItem3> localVarReturnType = new GenericType<EcommerceCartLineItem3>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add customer
   * Add a new customer to a store.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @return EcommerceCustomer3
   * @throws ApiException if fails to make API call
   */
  public EcommerceCustomer3 postEcommerceStoresIdCustomers(EcommerceCustomer2 body, String storeId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdCustomers");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdCustomers");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/customers"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCustomer3> localVarReturnType = new GenericType<EcommerceCustomer3>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add order
   * Add a new order to a store.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @return EcommerceOrder1
   * @throws ApiException if fails to make API call
   */
  public EcommerceOrder1 postEcommerceStoresIdOrders(EcommerceOrder body, String storeId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdOrders");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdOrders");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceOrder1> localVarReturnType = new GenericType<EcommerceOrder1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add order line item
   * Add a new line item to an existing order.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param orderId The id for the order in a store. (required)
   * @return EcommerceOrderLineItem3
   * @throws ApiException if fails to make API call
   */
  public EcommerceOrderLineItem3 postEcommerceStoresIdOrdersIdLines(EcommerceOrderLineItem2 body, String storeId, String orderId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdOrdersIdLines");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdOrdersIdLines");
    }
    // verify the required parameter 'orderId' is set
    if (orderId == null) {
      throw new ApiException(400, "Missing the required parameter 'orderId' when calling postEcommerceStoresIdOrdersIdLines");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/orders/{order_id}/lines"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "order_id" + "\\}", apiClient.escapeString(orderId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceOrderLineItem3> localVarReturnType = new GenericType<EcommerceOrderLineItem3>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add product
   * Add a new product to a store.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @return EcommerceProduct1
   * @throws ApiException if fails to make API call
   */
  public EcommerceProduct1 postEcommerceStoresIdProducts(EcommerceProduct body, String storeId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdProducts");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdProducts");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProduct1> localVarReturnType = new GenericType<EcommerceProduct1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add product image
   * Add a new image to the product.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @return EcommerceProductImage3
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductImage3 postEcommerceStoresIdProductsIdImages(EcommerceProductImage2 body, String storeId, String productId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdProductsIdImages");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdProductsIdImages");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling postEcommerceStoresIdProductsIdImages");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/images"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductImage3> localVarReturnType = new GenericType<EcommerceProductImage3>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add product variant
   * Add a new variant to the product.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @return EcommerceProductVariant3
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductVariant3 postEcommerceStoresIdProductsIdVariants(EcommerceProductVariant2 body, String storeId, String productId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdProductsIdVariants");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdProductsIdVariants");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling postEcommerceStoresIdProductsIdVariants");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/variants"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductVariant3> localVarReturnType = new GenericType<EcommerceProductVariant3>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add promo code
   * Add a new promo code to a store.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param promoRuleId The id for the promo rule of a store. (required)
   * @return EcommercePromoCode1
   * @throws ApiException if fails to make API call
   */
  public EcommercePromoCode1 postEcommerceStoresIdPromocodes(EcommercePromoCode body, String storeId, String promoRuleId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdPromocodes");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdPromocodes");
    }
    // verify the required parameter 'promoRuleId' is set
    if (promoRuleId == null) {
      throw new ApiException(400, "Missing the required parameter 'promoRuleId' when calling postEcommerceStoresIdPromocodes");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules/{promo_rule_id}/promo-codes"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "promo_rule_id" + "\\}", apiClient.escapeString(promoRuleId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommercePromoCode1> localVarReturnType = new GenericType<EcommercePromoCode1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add promo rule
   * Add a new promo rule to a store.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @return EcommercePromoRule1
   * @throws ApiException if fails to make API call
   */
  public EcommercePromoRule1 postEcommerceStoresIdPromorules(EcommercePromoRule body, String storeId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling postEcommerceStoresIdPromorules");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling postEcommerceStoresIdPromorules");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/promo-rules"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommercePromoRule1> localVarReturnType = new GenericType<EcommercePromoRule1>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add or update customer
   * Add or update a customer.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param customerId The id for the customer of a store. (required)
   * @return EcommerceCustomer3
   * @throws ApiException if fails to make API call
   */
  public EcommerceCustomer3 putEcommerceStoresIdCustomersId(EcommerceCustomer4 body, String storeId, String customerId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling putEcommerceStoresIdCustomersId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling putEcommerceStoresIdCustomersId");
    }
    // verify the required parameter 'customerId' is set
    if (customerId == null) {
      throw new ApiException(400, "Missing the required parameter 'customerId' when calling putEcommerceStoresIdCustomersId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/customers/{customer_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "customer_id" + "\\}", apiClient.escapeString(customerId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceCustomer3> localVarReturnType = new GenericType<EcommerceCustomer3>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Add or update product variant
   * Add or update a product variant.
   * @param body  (required)
   * @param storeId The store id. (required)
   * @param productId The id for the product of a store. (required)
   * @param variantId The id for the product variant. (required)
   * @return EcommerceProductVariant3
   * @throws ApiException if fails to make API call
   */
  public EcommerceProductVariant3 putEcommerceStoresIdProductsIdVariantsId(EcommerceProductVariant4 body, String storeId, String productId, String variantId) throws ApiException {
    Object localVarPostBody = body;
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling putEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'storeId' is set
    if (storeId == null) {
      throw new ApiException(400, "Missing the required parameter 'storeId' when calling putEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'productId' is set
    if (productId == null) {
      throw new ApiException(400, "Missing the required parameter 'productId' when calling putEcommerceStoresIdProductsIdVariantsId");
    }
    // verify the required parameter 'variantId' is set
    if (variantId == null) {
      throw new ApiException(400, "Missing the required parameter 'variantId' when calling putEcommerceStoresIdProductsIdVariantsId");
    }
    // create path and map variables
    String localVarPath = "/ecommerce/stores/{store_id}/products/{product_id}/variants/{variant_id}"
      .replaceAll("\\{" + "store_id" + "\\}", apiClient.escapeString(storeId.toString()))
      .replaceAll("\\{" + "product_id" + "\\}", apiClient.escapeString(productId.toString()))
      .replaceAll("\\{" + "variant_id" + "\\}", apiClient.escapeString(variantId.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json", "application/problem+json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<EcommerceProductVariant3> localVarReturnType = new GenericType<EcommerceProductVariant3>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
