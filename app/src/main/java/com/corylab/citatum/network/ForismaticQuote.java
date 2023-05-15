package com.corylab.citatum.network;

public class ForismaticQuote {
    private String quoteText;
    private String quoteAuthor;

    public ForismaticQuote(String quoteText, String quoteAuthor) {
        this.quoteText = quoteText;
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }
}