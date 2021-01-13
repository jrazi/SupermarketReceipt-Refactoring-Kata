package dojo.supermarket.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    private SingleProductSpecialOffer singleSpecialOffer;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.singleSpecialOffer = SingleProductSpecialOffer.getInstance(offerType, argument);
        this.product = product;
    }

    public Discount calculateDiscount(ProductQuantity pq, double unitPrice) {
        return singleSpecialOffer.calculateDiscount(pq, unitPrice);
    }

    public boolean productQualifiesFor(ProductQuantity pq) {
        return pq.getProduct().equals(product)
                && singleSpecialOffer.enoughQuantityOf(pq);
    }

}
