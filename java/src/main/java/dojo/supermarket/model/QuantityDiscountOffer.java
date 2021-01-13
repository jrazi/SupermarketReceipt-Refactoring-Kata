package dojo.supermarket.model;

public class QuantityDiscountOffer implements SingleProductSpecialOffer {

    private int quantityToDiscount;
    private double groupDiscountedPrice;

    public QuantityDiscountOffer(int quantityToDiscount, double groupDiscountedPrice) {
        this.groupDiscountedPrice = groupDiscountedPrice;
        this.quantityToDiscount = quantityToDiscount;
    }

    @Override
    public Discount calculateDiscount(ProductQuantity productQuantity, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        int discountGroupCount = quantityAsInt/quantityToDiscount;

        double priceOfDiscountedItems = groupDiscountedPrice * discountGroupCount;
        double priceOfNonDiscountedItems = (quantityAsInt % quantityToDiscount) * unitPrice;
        double totalPrice = priceOfDiscountedItems + priceOfNonDiscountedItems;
        double discountedPrice = unitPrice *  productQuantity.getQuantity() - totalPrice;
        return new Discount(productQuantity.getProduct(), quantityToDiscount + " for " + groupDiscountedPrice, -discountedPrice);
    }

    @Override
    public boolean enoughQuantityOf(ProductQuantity productQuantity) {
        return productQuantity.getQuantity() >= quantityToDiscount;
    }
}
