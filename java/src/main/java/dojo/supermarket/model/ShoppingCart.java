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

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product p: productQuantities().keySet()) {
            double quantity = productQuantities.get(p);
            ProductQuantity pq = new ProductQuantity(p, quantity);
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                if (!offer.productQualifiesFor(pq))
                    continue;;
                double unitPrice = catalog.getUnitPrice(p);
                Discount discount = null;
                SingleProductSpecialOffer specialOffer = null;
                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    specialOffer = new TwoForAmountOffer();
                }
                if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    specialOffer = new ThreeForTwoOffer();
                }
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    specialOffer = new TenPercentDiscountOffer();
                }
                if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    specialOffer = new FiveForAmountOffer();
                }
                if (specialOffer != null) {
                    discount = specialOffer.calculateDiscount(pq, offer, unitPrice);
                    receipt.addDiscount(discount);
                }
            }

        }
    }
}
