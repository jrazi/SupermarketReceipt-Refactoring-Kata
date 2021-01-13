package dojo.supermarket.model;

public interface SingleProductSpecialOffer {

    static SingleProductSpecialOffer getInstance(SpecialOfferType specialOfferType) {
        switch (specialOfferType) {
            case ThreeForTwo: return new ThreeForTwoOffer();
            case TwoForAmount: return new TwoForAmountOffer();
            case TenPercentDiscount: return new TenPercentDiscountOffer();
            case FiveForAmount: return new FiveForAmountOffer();
            default: return null;
        }
    }

    Discount calculateDiscount(ProductQuantity productQuantity, Offer offer, double unitPrice);

    boolean enoughQuantityOf(ProductQuantity productQuantity);

}
