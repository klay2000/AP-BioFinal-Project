public class Slider {
    int x;
    int y;
    int maxValue;
    int value;
    int maxPosition;
    int sizeX = 25;
    int sizeY = 10;
    String sliderText;

    boolean pressed = false;

    public Slider(int x, int y, int maxValue, int initialValue, double scale, String sliderText){
        value = initialValue;
        this.x = x;
        this.y = y;
        this.maxValue = maxValue;
        maxPosition = (int)scale*maxValue;
        this.sliderText = sliderText;
    }
}
