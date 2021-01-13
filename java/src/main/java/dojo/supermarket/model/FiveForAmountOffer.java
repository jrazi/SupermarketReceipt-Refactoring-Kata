package dojo.supermarket.model;

public class FiveForAmountOffer  {
    public Discount calculateDiscount(ProductQuantity productQuantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        int discountGroupCount = quantityAsInt/5;

        double discountTotal = unitPrice * productQuantity.getQuantity() - (offer.argument * discountGroupCount + quantityAsInt % 5 * unitPrice);
        return new Discount(productQuantity.getProduct(),  5 + " for " + offer.argument, -discountTotal);
    }

    public boolean enoughQuantityOf(ProductQuantity productQuantity) {
        return productQuantity.getQuantity() >= 5.0;
    }
}
