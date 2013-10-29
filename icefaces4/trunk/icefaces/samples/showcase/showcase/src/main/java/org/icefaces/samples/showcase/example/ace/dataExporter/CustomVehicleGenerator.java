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

package org.icefaces.samples.showcase.example.ace.dataExporter;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

public class CustomVehicleGenerator implements Serializable
{
    private List<String> namesPool;
    private List<String> chassisPool;
    private Random randomizer = new Random(System.nanoTime());
    private NumberFormat numberFormatter;
    

    public CustomVehicleGenerator() 
    {
        this.namesPool = getVehicleDescriptions();
        this.chassisPool = getChassisDescriptions();
        this.randomizer = new Random(System.nanoTime());
        this.numberFormatter = makeFormatter();
    }
    
    public ArrayList<CustomCar> getRandomCars(int quantity)
    {
        ArrayList<CustomCar> listWithCars = getPredefinedCars();
        
        if(quantity<listWithCars.size())
            return new ArrayList<CustomCar>(listWithCars.subList(0, quantity));
        
        if(quantity>listWithCars.size())
            return addCarsToList(quantity - listWithCars.size(), listWithCars);
        
        //if quantity == listWithCars.size()
        return listWithCars;
    }

    public ArrayList<CustomCar> addCarsToList(int quantityToAdd, ArrayList<CustomCar> list)
    {
        int currentListSize = list.size();
        int position = 0;
        /*by the end of this loop we will have partial copy, full copy or more then one copy of our list,
        appended to the end of it */
        for(int i = 0; i< quantityToAdd; i++)
        {
            //add Car from the begining of the list to the end of it
            CustomCar copyReference = list.get(position); //this pointer is mostly for code readabllity
            //new car.id =  car list size+position value+id of the 1st element in the predefined car list
            CustomCar car = new CustomCar(currentListSize+position+1, copyReference.getName(),
                                      copyReference.getChassis(), copyReference.getWeight(),
                                      copyReference.getAcceleration(), copyReference.getMpg(),
                                      copyReference.getCost(), generateExpansionData(), generateExpansionData());
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
    
    public ArrayList<CustomCar> removeCarsFromList(int quantityToRemove, ArrayList<CustomCar> list)
    {
        return new ArrayList<CustomCar>(list.subList(0, list.size()-quantityToRemove-1));
    }
    
    private ArrayList<CustomCar> getPredefinedCars()
    {
        ArrayList<CustomCar> listWithCars = new ArrayList<CustomCar>();
        
        listWithCars.add(new CustomCar(1,"Enduro","Van",15383,10,17.86,6617.17, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(2,"Tomcat","Bus",7331,15,16.65,31464.24, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(3,"Doublecharge","Pickup",5333,15,17.84,10922.73, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(4,"Swordfish","Bus",10956,5,5.17,6019.83, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(5,"Iguana","Pickup",1696,10,9.43,19736.16, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(6,"Dart","Motorcycle",9261,15,12.85,37947.84, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(7,"Pisces","Luxury",7846,10,15.13,19235.2, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(8,"Flash","Mid-Size",11499,10,12.74,29942.38, generateExpansionData(), generateExpansionData()));
        
        listWithCars.add(new CustomCar(9,"Tomcat","Mid-Size",10766,15,7.04,14342.74, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(10,"Pisces","Subcompact",2082,10,13.38,8015.01, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(11,"Superflash","Subcompact",14959,5,3.91,31941.1, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(12,"Husky","Mid-Size",14334,15,13.98,9303.69, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(13,"Gazelle","Semi-Truck",14248,5,8.64,9285.57, generateExpansionData(), generateExpansionData()));
        
        listWithCars.add(new CustomCar(14,"Superflash","Luxury",3037,15,8.44,36154.13, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(15,"Enduro","Subcompact",4997,5,6.02,31221.48, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(16,"Tomcat","Pickup",4555,10,4.06,35895.53, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(17,"Courier","Pickup",9848,10,16.15,27343.38, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(18,"Enduro","Motorcycle",5725,5,14.17,34430.44, generateExpansionData(), generateExpansionData()));
        
        listWithCars.add(new CustomCar(19,"Swordfish","Luxury",1466,10,11.72,19266.56, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(20,"Tomcat","Station Wagon",15576,10,15.3,16844.05, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(21,"Iguana","Van",10973,15,3.77,33369.32, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(22,"Doublecharge","Pickup",9414,10,6.37,19370.61, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(23,"Hawk","Motorcycle",8545,5,7.98,39124.1, generateExpansionData(), generateExpansionData()));
        
        listWithCars.add(new CustomCar(24,"Hawk","Subcompact",1853,10,12.54,36068.19, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(25,"Tomcat","Subcompact",12785,10,12.97,26141.79, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(26,"Hawk","Semi-Truck",12453,10,10.4,13654.1, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(27,"Doublecharge","Van",11952,10,17.58,22599.82, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(28,"Flash","Semi-Truck",6107,5,8.48,26096.26, generateExpansionData(), generateExpansionData()));
        
        listWithCars.add(new CustomCar(29,"Courier","Bus",8187,5,4.25,25470.63, generateExpansionData(), generateExpansionData()));
        listWithCars.add(new CustomCar(30,"Dart","Motorcycle",7177,10,12.16,35394.23, generateExpansionData(), generateExpansionData()));

        return listWithCars;
    }
    
    public ArrayList<CustomCar> getCarsForLazyLoading(int quantity)
    {
        return new ArrayList<CustomCar>(generateRandomCars(quantity));
    }
    
    public List<String> getChassisPool() { return chassisPool; }
    public List<String> getNamesPool() { return namesPool; }
    
    ///////////////////// THIS SECTION CONTAIN METHODS FOR TRUE RANDOM CAR GENERATION////////////////////
   //////////////// IT WAS REMOVED FROM PUBLIC CLASS INTERFACE DUE TO QC TEST REQUIREMENTS////////////
    
    private CustomCar getRandomCar()
    {
        CustomCar randomCar = new CustomCar(randomizer.nextInt(10000000), generateName(),
                                  generateChassis(), generateWeight(), generateAccelerationValue(),
                                  generateMPG(), generateCost(), generateExpansionData(), generateExpansionData());
        return randomCar;
    }
 
    private ArrayList<CustomCar> generateRandomCars(int quantity)
    {
        ArrayList<CustomCar> listWithRandomCars = new ArrayList<CustomCar>(quantity);
        for (int i = 0; i < quantity; i++) 
        {
            CustomCar randomCar = getRandomCar();
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
	
    private String generateChassis() 
    { return chassisPool.get(randomizer.nextInt(chassisPool.size())); }
    
    private int generateWeight() 
    { return 1000+randomizer.nextInt(15000); }
	
    private int generateAccelerationValue() 
    { return (1+randomizer.nextInt(3)) * 5; }
	
    private double generateMPG() 
    { return Double.parseDouble(numberFormatter.format( ((double)(3+randomizer.nextInt(15))) + randomizer.nextDouble())); }
	
    private double generateCost() 
    { return Double.parseDouble(numberFormatter.format( ((double)(2000+randomizer.nextInt(40000))) + randomizer.nextDouble()) ); }
	
	private List<ExpansionData> generateExpansionData() {
		ArrayList<ExpansionData> list = new ArrayList<ExpansionData>();
		list.add(new ExpansionData());
		list.add(new ExpansionData());
		list.add(new ExpansionData());
		return list;
	}
}
