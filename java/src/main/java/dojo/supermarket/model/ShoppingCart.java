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
                if (!meetsOffer(p, quantity, offer))
                    continue;;
                double unitPrice = catalog.getUnitPrice(p);
                Discount discount = null;

                if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    discount = getDiscountForTwoForAmount(pq, offer, unitPrice);
                }
                if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    discount = getDiscountForThreeForTwo(pq, unitPrice);
                }
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = getDiscountForTenPercentDiscount(pq, offer, unitPrice);
                }
                if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    discount = getDiscountForFiveForAmount(pq, offer, unitPrice);
                }
                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }

    private Discount getDiscountForFiveForAmount(ProductQuantity productQuantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        int discountGroupCount = quantityAsInt/5;

        double discountTotal = unitPrice * productQuantity.getQuantity() - (offer.argument * discountGroupCount + quantityAsInt % 5 * unitPrice);
        return new Discount(productQuantity.getProduct(),  5 + " for " + offer.argument, -discountTotal);
    }

    private Discount getDiscountForTenPercentDiscount(ProductQuantity productQuantity, Offer offer, double unitPrice) {
        return new Discount(
                productQuantity.getProduct(),
                offer.argument + "% off",
                -productQuantity.getQuantity() * unitPrice * offer.argument / 100.0
        );
    }

    private Discount getDiscountForThreeForTwo(ProductQuantity productQuantity, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        int discountGroupCount = quantityAsInt/3;

        double discountAmount = productQuantity.getQuantity() * unitPrice - ((discountGroupCount * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
        return new Discount(productQuantity.getProduct(), "3 for 2", -discountAmount);
    }

    private Discount getDiscountForTwoForAmount(ProductQuantity productQuantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        int discountGroupCount = quantityAsInt/2;

        double priceOfDiscountedItems = offer.argument * discountGroupCount;
        double priceOfNonDiscountedItems = (quantityAsInt % 2) * unitPrice;
        double totalPrice = priceOfDiscountedItems + priceOfNonDiscountedItems;
        double discountedPrice = unitPrice *  productQuantity.getQuantity() - totalPrice;
        return new Discount(productQuantity.getProduct(), "2 for " + offer.argument, -discountedPrice);
    }


    private boolean meetsOffer(Product p, double quantity, Offer offer) {
        return p.equals(offer.getProduct()) && quantity >= getMinQuantityForDiscount(offer.offerType);
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
