/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.mobi.dataview;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;


public class VehicleGenerator implements Serializable
{
    private List<String> namesPool;
    private List<String> chassisPool;
    private Random randomizer = new Random(System.nanoTime());
    private NumberFormat numberFormatter;
    

    public VehicleGenerator() 
    {
        this.namesPool = getVehicleDescriptions();
        this.chassisPool = getChassisDescriptions();
        this.randomizer = new Random(System.nanoTime());
        this.numberFormatter = makeFormatter();
    }
    
    public ArrayList<Car> getRandomCars(final int quantity)
    {
        return new ArrayList<Car>() {{
            int size = quantity;
            while (size > 0) {
                size--;
                add(getRandomCar());
            }
        }};
    }
    
    public ArrayList<Car> addCarsToList(int quantityToAdd, ArrayList<Car> list)
    {
        int currentListSize = list.size();
        int position = 0;
        /*by the end of this loop we will have partial copy, full copy or more then one copy of our list,
        appended to the end of it */
        for(int i = 0; i< quantityToAdd; i++)
        {
            //add Car from the begining of the list to the end of it
            Car copyReference = list.get(position); //this pointer is mostly for code readabllity
            //new car.id =  car list size+position value+id of the 1st element in the predefined car list
            Car car = new Car(currentListSize+position+1, copyReference.getName(),
                                      copyReference.getChassisCodename(), copyReference.getWeight(),
                                      copyReference.getAcceleration(), copyReference.getMpg(),
                                      copyReference.getCost());
            list.add(car);
            //move pointer to one position up in the list
            position ++;
            //check if we copied all elements of the original list
            if(position>=currentListSize)
            {
                //reset position pointer to the begining
                position = 0;
                //since our list doubled in size and contain full pattern of the original list we can increase currentListSize
                currentListSize = list.size();
            }
        }
        return list;
    }
    
    public ArrayList<Car> removeCarsFromList(int quantityToRemove, ArrayList<Car> list)
    {
        return new ArrayList<Car>(list.subList(0, list.size()-quantityToRemove-1));
    }
    
    public ArrayList<Car> getCarsForLazyLoading(int quantity)
    {
        return new ArrayList<Car>(generateRandomCars(quantity));
    }
    
    public List<String> getChassisPool() { return chassisPool; }
    public List<String> getNamesPool() { return namesPool; }
    
    ///////////////////// THIS SECTION CONTAIN METHODS FOR TRUE RANDOM CAR GENERATION////////////////////
   //////////////// IT WAS REMOVED FROM PUBLIC CLASS INTERFACE DUE TO QC TEST REQUIREMENTS////////////
    
    private Car getRandomCar()
    {
        Car randomCar = new Car(randomizer.nextInt(10000000), generateName(),
                                  generateChassis(), generateWeight(), generateAccelerationValue(),
                                  generateMPG(), generateCost());
        return randomCar;
    }
    
    private ArrayList<Car> generateRandomCars(int quantity)
    {
        ArrayList<Car> listWithRandomCars = new ArrayList<Car>(quantity);
        for (int i = 0; i < quantity; i++) 
        {
            Car randomCar = getRandomCar();
            listWithRandomCars.add(randomCar);
        }
        return listWithRandomCars;
    }
    
    private List<String> getVehicleDescriptions() 
    {
        List<String> listWithNames = new ArrayList<String>();
        listWithNames.add("Spider");
        listWithNames.add("Hawk");
        listWithNames.add("Tomcat");
        listWithNames.add("Gazelle");
        listWithNames.add("Mantis");
        listWithNames.add("Flash");
        listWithNames.add("Iguana");
        listWithNames.add("Swordfish");
        listWithNames.add("Rattler");
        listWithNames.add("Courier");
        listWithNames.add("Pisces");
        listWithNames.add("Superflash");
        listWithNames.add("Doublecharge");
        listWithNames.add("Dart");
        listWithNames.add("Enduro");
        listWithNames.add("King Crab");
        listWithNames.add("Vanguard");
        listWithNames.add("Camel");
        listWithNames.add("Husky");
        return listWithNames;
    }

    private List<String> getChassisDescriptions() 
    {
        List<String> listWithNames = new ArrayList<String>();
        listWithNames.add("Motorcycle");
        listWithNames.add("Subcompact");
        listWithNames.add("Mid-Size");
        listWithNames.add("Luxury");
        listWithNames.add("Station Wagon");
        listWithNames.add("Pickup");
        listWithNames.add("Van");
        listWithNames.add("Bus");
        listWithNames.add("Semi-Truck");
        return listWithNames;
    }
    
    private NumberFormat makeFormatter() 
    {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(false);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter;
    }
    
    private String generateName() 
    { return namesPool.get(randomizer.nextInt(namesPool.size())); }
	
    public String generateChassis()
    { return chassisPool.get(randomizer.nextInt(chassisPool.size())); }
    
    private int generateWeight() 
    { return 1000+randomizer.nextInt(15000); }
	
    private int generateAccelerationValue() 
    { return (1+randomizer.nextInt(3)) * 5; }
	
    private double generateMPG() 
    { return Double.parseDouble(numberFormatter.format( ((double)(3+randomizer.nextInt(15))) + randomizer.nextDouble())); }
	
    private double generateCost() 
    { return Double.parseDouble(numberFormatter.format( ((double)(2000+randomizer.nextInt(40000))) + randomizer.nextDouble()) ); }
}
