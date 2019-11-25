import java.util.ArrayList;

public class Plant {

    // vars for plant color
    int r;
    int g;
    int b;

    //plant position
    int x;
    int y;

    // maximum plant position
    int maxX;
    int maxY;

    // amount of energy plant has
    int energy = 160;

    // maximum amount of energy plant can hold
    int maxEnergy = 160;

    // threshold to have a child
    int childThreshold = maxEnergy-1;

    // amount of energy required to have a child (child starting energy is equal to this minus childloss)
    int childCost = 35;

    // amount of energy lost during transfer
    int childLoss = 5;

    // energy gained per cycle
    public static int energyGain = 1;

    // maximum change in any given color value during reproduction
    public static int mutationRate = 10;

    // constructor for starting plants
    public Plant(int xBounds, int yBounds){
        // sets color
        r = (int)(Math.random() * 255);
        g = (int)(Math.random() * 255);
        b = (int)(Math.random() * 255);

        // sets max plant position
        maxX = xBounds;
        maxY = yBounds;

        // sets plant position
        x = (int)Math.floor(Math.random() * xBounds);
        y = (int)Math.floor(Math.random() * yBounds);
    }

    // constructor for reproducing plants
    private Plant(Plant parent, int x, int y){
        // sets energy to parents childThreshold minus parents childloss
        this.energy = parent.childThreshold - parent.childLoss;

        // sets position to provided values
        this.x = x;
        this.y = y;

        // sets max position to parents max position
        this.maxX = parent.maxX;
        this.maxY = parent.maxY;

        // sets colors to randomly changed parent colors
        this.r = parent.r + (int)(Math.round(Math.random()*(parent.mutationRate*2)-mutationRate));
        this.g = parent.g + (int)(Math.round(Math.random()*(parent.mutationRate*2)-mutationRate));
        this.b = parent.b + (int)(Math.round(Math.random()*(parent.mutationRate*2)-mutationRate));
    }

    // does planty stuff
    void run(ArrayList<Plant> list, ArrayList<Plant> removeList, ArrayList<Plant> addList, int [][] plantMap){
        //if the energy is lower than the max energy add energy
        if(energy < maxEnergy){
            energy+=energyGain;
        }

        //set id in plantMap
        plantMap[x][y] = list.indexOf(this);

        // if possible and there is space reproduce
        if(energy > childThreshold){
            //reproduce
            reproduce(addList, plantMap);
        }

        // if energy less than one die
        if(energy < 1){
            // remove from plant list
            removeList.add(this);
            // remove from plant map
            plantMap[x][y] = -1;
        }

    }

    // reproduces plant
    void reproduce(ArrayList<Plant> addList, int[][] plantMap){
        //sets position attempt counter
        int i = 0;

        // sets position
        int childX = x + (int) (Math.floor((Math.random() * 2) - 0.5));
        int childY = y + (int) (Math.floor((Math.random() * 2) - 0.5));

        // if position is out of bounds loop
        if(childX > maxX) childX = 0;
        if(childX < 0) childX = maxX;
        if(childY > maxY) childY = 0;
        if(childY < 0) childY = maxY;

        // look for appropriate position
        while(plantMap[childX][childY] != -1){

            // sets position
            childX = x + (int) (Math.floor((Math.random() * 2) - 0.5));
            childY = y + (int) (Math.floor((Math.random() * 2) - 0.5));

            // if position is out of bounds loop
            if(childX > maxX) childX = 0;
            if(childX < 0) childX = maxX;
            if(childY > maxY) childY = 0;
            if(childY < 0) childY = maxY;

            // iterate position attempt counter
            i++;
            // if position attempt more than max stop
            if (i > 1){
                return;
            }
        }

        // subtract child cost
        energy -= childCost;

        // temporarily add child to plant map
        plantMap[childX][childY] = -2;

        // add child to plant list
        addList.add(new Plant(this, childX, childY));
    }

}
