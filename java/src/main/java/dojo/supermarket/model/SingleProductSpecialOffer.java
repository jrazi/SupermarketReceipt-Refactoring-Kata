package dojo.supermarket.model;

public interface SingleProductSpecialOffer {

    Discount calculateDiscount(ProductQuantity productQuantity, Offer offer, double unitPrice);

}
