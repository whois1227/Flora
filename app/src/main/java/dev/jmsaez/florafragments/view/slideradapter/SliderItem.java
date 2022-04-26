package dev.jmsaez.florafragments.view.slideradapter;

public class SliderItem {
    private String imgUrl;

    // Constructor method.
    public SliderItem(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
