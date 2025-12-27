/**
 * Session Storage Keys
 * Constants used for accessing sessionStorage data.
 */

export const SESSION_KEYS = {
    // Current active request/transaction
    CURRENT_TRANSACTION_ID: 'session:transaction_id',
    
    // Tab specific states
    ACTIVE_TAB_INDEX: 'session:active_tab',
    
    // Temporary form data (to survive page reload)
    TEMP_PRODUCT_FORM: 'session:temp_product_form',
    
    // User preferences for the current session only
    IS_SIDEBAR_OPEN: 'session:sidebar_open',
    
    // Auth related (if using session storage for short-lived tokens)
    REFRESH_TOKEN: 'session:refresh_token',
    
    // Shopping Cart (if not in local storage)
    GUEST_CART_ID: 'session:guest_cart_id',
    
    // Filter/Search states
    LAST_SEARCH_QUERY: 'session:last_search',
    PRODUCT_FILTER_STATE: 'session:product_filter'
  } as const;
  
  // Helper type for keys
  export type SessionKey = typeof SESSION_KEYS[keyof typeof SESSION_KEYS];
