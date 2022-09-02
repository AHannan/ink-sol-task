package com.nphase.service;

import com.nphase.entity.Category;
import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ShoppingCartService {

    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /* TASK 2 */
    public BigDecimal calculateTotalPriceWithDiscount(ShoppingCart shoppingCart) {
        final Double discountPercentage = 10.0;
        final int quantityCap = 3;
        return shoppingCart.getProducts()
                .stream()
                .map(product -> {
                    BigDecimal price = product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));
                    if(product.getQuantity() > quantityCap) {
                        price = price.subtract(BigDecimal.valueOf(discountPercentage/100.0).multiply(price));
                    }
                    return price;
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /* TASK 3 */
    public BigDecimal calculateTotalPriceWithDiscountByCategory(ShoppingCart shoppingCart) {
        final Double discountPercentage = 10.0;
        final int quantityCap = 3;
        return calculateTotalPriceWithDiscountByCategory(shoppingCart, discountPercentage, quantityCap);
    }

    /* TASK 4 */
    public BigDecimal calculateTotalPriceWithDiscountByCategory(ShoppingCart shoppingCart, Double discountPercentage, int quantityCap) {
        Map<Category, Integer> map = shoppingCart.getProducts().stream()
                .collect(Collectors.groupingBy(foo -> foo.getCategory(),
                        Collectors.summingInt(foo->foo.getQuantity())));

        return shoppingCart.getProducts()
                .stream()
                .map(product -> {
                    BigDecimal price = product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));
                    if(map.get(product.getCategory()) > quantityCap) {
                        price = price.subtract(BigDecimal.valueOf(discountPercentage/100.0).multiply(price));
                    }
                    return price;
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
