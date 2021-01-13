package dojo.supermarket.model;

public class PercentageDiscountOffer implements SingleProductSpecialOffer {

    private double discountPercentage;

    public PercentageDiscountOffer(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Discount calculateDiscount(ProductQuantity productQuantity, double unitPrice) {
        return new Discount(
                productQuantity.getProduct(),
                discountPercentage + "% off",
                -productQuantity.getQuantity() * unitPrice * discountPercentage / 100.0
        );
    }

    @Override
    public boolean enoughQuantityOf(ProductQuantity productQuantity) {
        return productQuantity.getQuantity() >= 0.0;
    }
}
