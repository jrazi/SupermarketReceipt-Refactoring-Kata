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
            if (offers.containsKey(p)) {
                Offer offer = offers.get(p);
                double unitPrice = catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                int minQuantityForDiscount = getMinQuantityForDiscount(offer.offerType);
                int discountGroupUnitCount = getDiscountGroupUnitCount(offer.offerType);

                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    if (quantityAsInt >= minQuantityForDiscount) {
                        int discountGroupCount = quantityAsInt / discountGroupUnitCount;
                        double priceOfDiscountedItems = offer.argument * discountGroupCount;
                        double priceOfNonDiscountedItems = (quantityAsInt % 2) * unitPrice;
                        double totalPrice = priceOfDiscountedItems + priceOfNonDiscountedItems;
                        double discountedPrice = unitPrice * quantity - totalPrice;
                        discount = new Discount(p, "2 for " + offer.argument, -discountedPrice);
                    }

                }
                int discountGroupCount = quantityAsInt / discountGroupUnitCount;
                if (offer.offerType == SpecialOfferType.ThreeForTwo && quantityAsInt > minQuantityForDiscount) {
                    double discountAmount = quantity * unitPrice - ((discountGroupCount * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
                    discount = new Discount(p, "3 for 2", -discountAmount);
                }
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
                }
                if (offer.offerType == SpecialOfferType.FiveForAmount && quantityAsInt >= minQuantityForDiscount) {
                    double discountTotal = unitPrice * quantity - (offer.argument * discountGroupCount + quantityAsInt % 5 * unitPrice);
                    discount = new Discount(p, minQuantityForDiscount + " for " + offer.argument, -discountTotal);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }



    private int getDiscountGroupUnitCount(SpecialOfferType offerType) {
        switch (offerType) {
            case ThreeForTwo: return 3;
            default: return getMinQuantityForDiscount(offerType);
        }
    }

    private int getMinQuantityForDiscount(SpecialOfferType offerType) {
        switch (offerType) {
            case TwoForAmount:
            case ThreeForTwo:
                return 2;
            case FiveForAmount:
                return 5;
            default:
                return 1;
        }
    }

}
