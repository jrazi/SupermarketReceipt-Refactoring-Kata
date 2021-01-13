package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    Receipt createReceipt(SupermarketCatalog catalog, Map<Product, Offer> offers) {
        Receipt receipt = new Receipt();
        for (ProductQuantity pq: items) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            receipt.addProduct(p, quantity, unitPrice, price);
        }
        List<Discount> discountList = getDiscountListForOffers(offers, catalog);
        discountList
                .stream()
                .forEach(discount -> receipt.addDiscount(discount));
        return receipt;
    }

    private List<Discount> getDiscountListForOffers(Map<Product, Offer> offers, SupermarketCatalog catalog) {
        List<Discount> discountList = new ArrayList<>();
        for (Product p: productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            ProductQuantity pq = new ProductQuantity(p, quantity);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                if (!offer.productQualifiesFor(pq))
                    continue;;
                double unitPrice = catalog.getUnitPrice(p);
                Discount discount = offer.calculateDiscount(pq, unitPrice);
                discountList.add(discount);
            }
        }
        return discountList;
    }
}
