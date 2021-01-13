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
                if (!meetsOffer(p, quantity, offer))
                    continue;;
                double unitPrice = catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                int minQuantityForDiscount = getMinQuantityForDiscount(offer.offerType);
                int discountGroupUnitCount = getDiscountGroupUnitCount(offer.offerType);
                int discountGroupCount = quantityAsInt / discountGroupUnitCount;

                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    double priceOfDiscountedItems = offer.argument * discountGroupCount;
                    double priceOfNonDiscountedItems = (quantityAsInt % 2) * unitPrice;
                    double totalPrice = priceOfDiscountedItems + priceOfNonDiscountedItems;
                    double discountedPrice = unitPrice * quantity - totalPrice;
                    discount = new Discount(p, "2 for " + offer.argument, -discountedPrice);
                }
                if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    double discountAmount = quantity * unitPrice - ((discountGroupCount * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
                    discount = new Discount(p, "3 for 2", -discountAmount);
                }
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
                }
                if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    double discountTotal = unitPrice * quantity - (offer.argument * discountGroupCount + quantityAsInt % 5 * unitPrice);
                    discount = new Discount(p, minQuantityForDiscount + " for " + offer.argument, -discountTotal);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }


    private boolean meetsOffer(Product p, double quantity, Offer offer) {
        return p.equals(offer.getProduct()) && quantity >= getMinQuantityForDiscount(offer.offerType);
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
