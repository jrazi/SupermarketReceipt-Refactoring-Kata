package dojo.supermarket.model;

public interface SingleProductSpecialOffer {

    static SingleProductSpecialOffer getInstance(SpecialOfferType specialOfferType, double argument) {
        switch (specialOfferType) {
            case ThreeForTwo: return new ExtraForFreeOffer(2);
            case TwoForAmount:
                return new QuantityDiscountOffer(2, argument);
            case FiveForAmount:
                return new QuantityDiscountOffer(5, argument);
            case TenPercentDiscount: return new PercentageDiscountOffer(argument);
            default: return null;
        }
    }

    Discount calculateDiscount(ProductQuantity productQuantity, double unitPrice);

    boolean enoughQuantityOf(ProductQuantity productQuantity);

}
