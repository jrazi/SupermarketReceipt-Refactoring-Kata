package dojo.supermarket.model;

public class ThreeForTwoOffer implements SingleProductSpecialOffer {

    @Override
    public Discount calculateDiscount(ProductQuantity productQuantity, Offer offer, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        int discountGroupCount = quantityAsInt/3;

        double discountAmount = productQuantity.getQuantity() * unitPrice - ((discountGroupCount * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
        return new Discount(productQuantity.getProduct(), "3 for 2", -discountAmount);
    }
}
