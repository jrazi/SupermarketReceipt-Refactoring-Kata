package dojo.supermarket.model;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    double argument;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
    }

    public boolean productQualifiesFor(ProductQuantity pq) {
        return pq.getProduct().equals(getProduct())
                && SingleProductSpecialOffer.getInstance(offerType).enoughQuantityOf(pq);
    }

    Product getProduct() {
        return this.product;
    }


}
