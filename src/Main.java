import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;

public class Main extends PApplet{

    // set window width and height
    int width = 750;
    int height = 750;

    // overlay thingy font
    PFont f;
    PFont sliderFont;

    // set plant size
    int plantSize = 10;

    // create map of plant ids
    int plantMap[][] = new int [width/plantSize+1] [height/plantSize+1];

    // set initial number of plants
    int initialPlantNum = 1;

    // create lists for plant storage, removal, and addition respectively
    public static ArrayList<Plant> plants = new ArrayList<Plant>();
    public static ArrayList<Plant> removePlants = new ArrayList<Plant>();
    public static ArrayList<Plant> addPlants = new ArrayList<Plant>();

    // set herbivore size
    int herbivoreSize = 10;

    // create map of herbivore ids
    int herbivoreMap[][] = new int [width/herbivoreSize+1] [height/herbivoreSize+1];

    // set initial number of herbivores
    int initialHerbivoreNum = 40;

    // create lists for herbivore storage, removal, and addition respectively
    public static ArrayList<Herbivore> herbivores = new ArrayList<Herbivore>();
    public static ArrayList<Herbivore> removeHerbivores = new ArrayList<Herbivore>();
    public static ArrayList<Herbivore> addHerbivores = new ArrayList<Herbivore>();

    // list of sliders
    public static  ArrayList<Slider> sliders = new ArrayList<Slider>();

    // removes entities
    public void remove(){
        // removes plants and resets plant remove list
        for(Plant plant : removePlants)plants.remove(plant);
        removePlants = new ArrayList<Plant>();

        // removes herbivores and resets herbivore remove list
        for(Herbivore herbivore : removeHerbivores)herbivores.remove(herbivore);
        removeHerbivores = new ArrayList<Herbivore>();

    }

    // adds entities
    public void add(){
        // adds plants and resets plant add list
        for(Plant plant : addPlants){
            plants.add(plant);
        }
        addPlants = new ArrayList<Plant>();

        // adds herbivores and resets herbivore remove list
        for(Herbivore herbivore : addHerbivores)herbivores.add(herbivore);
        addHerbivores = new ArrayList<Herbivore>();

    }

    // initial setup
    public  void setup(){
        // set up font f
        f = createFont("Arial",35,true);
        sliderFont = createFont("Arial",10,true);
    }

    // initial setup
    public void settings(){
        // sets window size
        size(width, height);

        // add and clears sliders
        sliders = new ArrayList<Slider>();
        sliders.add(new Slider(width - 150, 25, 50, 10, 1, "Plant Mutation Rate"));
        sliders.add(new Slider(width - 150, 50, 50, 15, 1, "Herbivore Mutation Rate"));
        sliders.add(new Slider(width - 150, 75, 10, 1, 1, "Sunlight Multiplier"));

        // reset lists for plant storage, removal, and addition respectively
        plants = new ArrayList<Plant>();
        removePlants = new ArrayList<Plant>();
        addPlants = new ArrayList<Plant>();

        // reset lists for herbivore storage, removal, and addition respectively
        herbivores = new ArrayList<Herbivore>();
        removeHerbivores = new ArrayList<Herbivore>();
        addHerbivores = new ArrayList<Herbivore>();

        // initializes plant map
        for(int x = 0; x < plantMap.length; x++){
            for(int y = 0; y < plantMap[0].length; y++){
                plantMap[x][y] = -1;
            }
        }

        // adds initial plants
        for(int i = 0; i < initialPlantNum; i++){
            plants.add(new Plant(width/plantSize, height/plantSize));
        }

        // initializes herbivore map
        for(int x = 0; x < herbivoreMap.length; x++){
            for(int y = 0; y < herbivoreMap[0].length; y++){
                herbivoreMap[x][y] = -1;
            }
        }

        // adds initial herbivore
        for(int i = 0; i < initialHerbivoreNum; i++){
            herbivores.add(new Herbivore(width/herbivoreSize, height/herbivoreSize));
        }

    }

    // draws and runs stuff
    public void draw() throws java.util.ConcurrentModificationException{
        // draws background
        background(0);

        //tries to draw/run plants/herbivores
        try {
            // cycles through and draws/runs all plants
            for (Plant plant : plants) {
                fill(plant.r, plant.g, plant.b);

                rect(plant.x * plantSize, plant.y * plantSize, plantSize, plantSize);

                plant.run(plants, removePlants, addPlants, plantMap);
            }

            // cycles through and draws/runs all herbivores
            for (Herbivore herbivore : herbivores) {
                fill(herbivore.r, herbivore.g, herbivore.b);

                ellipse((herbivore.x * herbivoreSize) + herbivoreSize/2, (herbivore.y * herbivoreSize) + herbivoreSize/2, herbivoreSize, herbivoreSize);

                herbivore.run(herbivores, removeHerbivores, addHerbivores, plants, herbivoreMap, plantMap);
            }
        }
        catch(java.util.ConcurrentModificationException e){}

        // removes then adds plants/herbivores
        remove();
        add();

        // draws overlay thingy
        if(mousePressed && mouseButton == RIGHT){
            // number of lines of text
            int textSpacing = 1;

            // makes herbivore and plant stored and sets their defaults
            Plant plant = new Plant( 0, 0);
            Herbivore herbivore = new Herbivore( 0, 0);

            textFont(f);
            fill(255);
            if(mouseX / plantSize > width/plantSize || mouseY/plantSize > height/plantSize || mouseX / plantSize < 0 || mouseY/plantSize < 0){
                text("Cursor off screen.", 5, textSpacing++*30);
            }
            else{
                text("Tile Position: ("+mouseX/plantSize + ", "+ mouseY/plantSize+")", 5, textSpacing++*30);
                // stores actual herbivore and plant then draws info about them
                if(herbivoreMap[mouseX/plantSize][mouseY/plantSize] > -1)herbivore = herbivores.get(herbivoreMap[(int)Math.floor(mouseX/plantSize)][(int)Math.floor(mouseY/plantSize)]);
                if(plantMap[mouseX/plantSize][mouseY/plantSize] > -1)plant = plants.get(plantMap[(int)Math.floor(mouseX/plantSize)][(int)Math.floor(mouseY/plantSize)]);
                if(plantMap[mouseX/plantSize][mouseY/plantSize] > -1)text("Plant Energy: " + plant.energy, 5, textSpacing++*30);
                if(herbivoreMap[mouseX/plantSize][mouseY/plantSize] > -1)text("Herbivore Energy: " + herbivore.energy, 5, textSpacing++*30);
            }

        }


        // moves sliders
        if(mousePressed && mouseButton == LEFT){
            for(Slider slider : sliders){
                if(mouseX > slider.x - 1 && mouseX < slider.x + slider.maxValue + 1 && mouseY > slider.y && mouseY < slider.y + slider.sizeY){
                    slider.pressed = true;
                    slider.value = mouseX - slider.x;
                }
                else slider.pressed = false;
            }
        }
        else{
            for(Slider slider : sliders){
                slider.pressed = false;
            }
        }

        // draws sliders
        for(Slider slider : sliders){
            fill(127);
            rect(slider.x - slider.sizeX/2, slider.y, slider.maxValue + (slider.sizeX), slider.sizeY);
            fill(255);
            textFont(sliderFont);
            rect(slider.x + slider.value - (slider.sizeX/2), slider.y, slider.sizeX, slider.sizeY);
            text(slider.sliderText + ": " + slider.value, slider.x-slider.sizeX/2, slider.y + slider.sizeY + 10);
        }

        if(herbivores.size() < 1){
            settings();
        }

        // set variables to their sliders
        Plant.mutationRate = sliders.get(0).value;
        Plant.energyGain = sliders.get(2).value;
        Herbivore.mutationRate = sliders.get(1).value;

    }
    

    public static void main(String... args){
        PApplet.main("Main");
    }
}