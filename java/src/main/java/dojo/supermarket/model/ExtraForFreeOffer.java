package dojo.supermarket.model;

public class ExtraForFreeOffer implements SingleProductSpecialOffer {

    private int quantityToReceiveExtra;

    public ExtraForFreeOffer(int quantityToReceiveExtra) {
        this.quantityToReceiveExtra = quantityToReceiveExtra;
    }

    @Override
    public Discount calculateDiscount(ProductQuantity productQuantity, double unitPrice) {
        int quantityAsInt = (int) productQuantity.getQuantity();
        int discountGroupCount = quantityAsInt/(quantityToReceiveExtra+1);

        double discountAmount = productQuantity.getQuantity() * unitPrice - ((discountGroupCount * (quantityToReceiveExtra) * unitPrice) + quantityAsInt % (quantityToReceiveExtra+1) * unitPrice);
        return new Discount(productQuantity.getProduct(), (quantityToReceiveExtra+1) + " for " + quantityToReceiveExtra, -discountAmount);
    }

    @Override
    public boolean enoughQuantityOf(ProductQuantity productQuantity) {
        return productQuantity.getQuantity() >= quantityToReceiveExtra;
    }
}
