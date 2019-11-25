import java.util.ArrayList;

public class Herbivore {

    // vars for herbivore color
    int r;
    int g;
    int b;

    // herbivore position
    int x;
    int y;

    // maximum herbivore position
    int maxX;
    int maxY;

    // amount of energy herbivore has
    int energy = 1000;

    // maximum amount of energy herbivore can hold
    int maxEnergy = 60;

    // threshold to have a child
    int childThreshold = maxEnergy-1;

    // amount of energy required to have a child (child starting energy is equal to this minus childloss)
    int childCost = 35;

    // amount of energy lost during transfer
    int childLoss = 5;

    // maximum change in any given color value during reproduction
    public static int mutationRate = 15;

    // amount of energy consumed from plants per cycle
    int energyConsumed = 4;

    // amount of energy lost during consumption of plant
    int consumptionLoss = 1;

    // maximum energy multiplier
    double maxEnergyMult = 1.0;

    // minimum energy multiplier
    double minEnergyMult = -1;

    // multiplied by age to get living cost
    double livingCostMult = 0.005;

    // age of herbivore
    int age = 0;

    // constructor for starting plants
    public Herbivore(int xBounds, int yBounds){
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

    // constructor for reproducing herbivores
    private Herbivore(Herbivore parent, int x, int y){
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

    // does herbivorey stuff
    void run(ArrayList<Herbivore> list, ArrayList<Herbivore> removeList, ArrayList<Herbivore> addList, ArrayList<Plant> plantList, int [][] herbivoreMap, int[][] plantMap){
        //if the energy is lower than the max energy eat
        if(energy < maxEnergy && plantMap[x][y] > -1){
            Plant plant = plantList.get(plantMap[x][y]);

            plant.energy -= energyConsumed;

            double colorSimilarity = Math.abs(r - plant.r) + Math.abs(g - plant.g) + Math.abs(b - plant.b);

            double colorSimilarityMultiplier = ((-colorSimilarity + 755) * ((maxEnergyMult-minEnergyMult) / 755)) + minEnergyMult;

            energy += (int)((energyConsumed - consumptionLoss) * colorSimilarityMultiplier);
        }

        // remove living cost
        energy -= age*livingCostMult;

        // set id in herbivoreMap
        herbivoreMap[x][y] = list.indexOf(this);

        // if possible and there is space reproduce
        if(energy > childThreshold){
            //reproduce
            reproduce(addList, herbivoreMap);
        }

        // if energy less than one die
        if(energy < 1){
            // remove from herbivore list
            removeList.add(this);
            // remove from herbivore map
            herbivoreMap[x][y] = -1;
        }

        // increase age
        age++;
    }

    // reproduces plant
    void reproduce(ArrayList<Herbivore> addList, int[][] herbivoreMap){
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
        while(herbivoreMap[childX][childY] != -1){

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

        // temporarily add child to herbivore map
        herbivoreMap[childX][childY] = -2;

        // add child to herbivore list
        addList.add(new Herbivore(this, childX, childY));
    }

}
