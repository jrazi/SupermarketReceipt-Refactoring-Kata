package dojo.supermarket.model;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    private SingleProductSpecialOffer specialOffer;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.specialOffer = SingleProductSpecialOffer.getInstance(offerType, argument);
        this.product = product;
    }

    public Discount calculateDiscount(ProductQuantity pq, double unitPrice) {
        return specialOffer.calculateDiscount(pq, unitPrice);
    }

    public boolean productQualifiesFor(ProductQuantity pq) {
        return pq.getProduct().equals(product)
                && specialOffer.enoughQuantityOf(pq);
    }

}
