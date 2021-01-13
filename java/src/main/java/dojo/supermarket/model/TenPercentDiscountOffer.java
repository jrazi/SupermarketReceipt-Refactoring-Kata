package dojo.supermarket.model;

public class TenPercentDiscountOffer implements SingleProductSpecialOffer {

    @Override
    public Discount calculateDiscount(ProductQuantity productQuantity, Offer offer, double unitPrice) {
        return new Discount(
                productQuantity.getProduct(),
                offer.argument + "% off",
                -productQuantity.getQuantity() * unitPrice * offer.argument / 100.0
        );
    }
}
