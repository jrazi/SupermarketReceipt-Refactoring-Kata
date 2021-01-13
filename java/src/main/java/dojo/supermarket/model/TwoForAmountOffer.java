package dojo.supermarket.model;

public class TwoForAmountOffer implements SingleProductSpecialOffer {

    @Override
    public Discount calculateDiscount(ProductQuantity productQuantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        int discountGroupCount = quantityAsInt/2;

        double priceOfDiscountedItems = offer.argument * discountGroupCount;
        double priceOfNonDiscountedItems = (quantityAsInt % 2) * unitPrice;
        double totalPrice = priceOfDiscountedItems + priceOfNonDiscountedItems;
        double discountedPrice = unitPrice *  productQuantity.getQuantity() - totalPrice;
        return new Discount(productQuantity.getProduct(), "2 for " + offer.argument, -discountedPrice);
    }
}
