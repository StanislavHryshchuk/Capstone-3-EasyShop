package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    void addOrIncrease(int userId, int productId, int quantity);

    void update(int userId, int productId, ShoppingCartItem item);

    void delete(int userId);


}
