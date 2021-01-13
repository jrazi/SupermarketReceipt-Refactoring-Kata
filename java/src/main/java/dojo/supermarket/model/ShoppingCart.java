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
                Discount discount = null;

                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    discount = getDiscountForTwoForAmount(p, quantity, offer, unitPrice);
                }
                if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    discount = getDiscountForThreeForTwo(p, quantity, unitPrice);
                }
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = getDiscountForTenPercentDiscount(p, quantity, offer, unitPrice);
                }
                if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    discount = getDiscountForFiveForAmount(p, quantity, offer, unitPrice);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }

    private Discount getDiscountForFiveForAmount(Product p, double quantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) quantity;
        int discountGroupCount = quantityAsInt/5;

        double discountTotal = unitPrice * quantity - (offer.argument * discountGroupCount + quantityAsInt % 5 * unitPrice);
        return new Discount(p,  5 + " for " + offer.argument, -discountTotal);
    }

    private Discount getDiscountForTenPercentDiscount(Product p, double quantity, Offer offer, double unitPrice) {
        return new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
    }

    private Discount getDiscountForThreeForTwo(Product p, double quantity, double unitPrice) {
        int quantityAsInt = (int) quantity;
        int discountGroupCount = quantityAsInt/3;

        double discountAmount = quantity * unitPrice - ((discountGroupCount * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
        return new Discount(p, "3 for 2", -discountAmount);
    }

    private Discount getDiscountForTwoForAmount(Product p, double quantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) quantity;
        int discountGroupCount = quantityAsInt/2;

        double priceOfDiscountedItems = offer.argument * discountGroupCount;
        double priceOfNonDiscountedItems = (quantityAsInt % 2) * unitPrice;
        double totalPrice = priceOfDiscountedItems + priceOfNonDiscountedItems;
        double discountedPrice = unitPrice * quantity - totalPrice;
        return new Discount(p, "2 for " + offer.argument, -discountedPrice);
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
