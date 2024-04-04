package com.djamware.springsecuritymongo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
public class Payment {
    @Id
    private String id;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String bookingId;
    private String CardDetails;
    private double PaymentAmount;

    public Payment(String id, String bookingId, String cardDetails, double paymentAmount) {
        this.id = id;
        this.bookingId = bookingId;
        CardDetails = cardDetails;
        PaymentAmount = paymentAmount;
    }

    public Payment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCardDetails() {
        return CardDetails;
    }

    public void setCardDetails(String cardDetails) {
        CardDetails = cardDetails;
    }

    public double getPaymentAmount() {
        return PaymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        PaymentAmount = paymentAmount;
    }


}
