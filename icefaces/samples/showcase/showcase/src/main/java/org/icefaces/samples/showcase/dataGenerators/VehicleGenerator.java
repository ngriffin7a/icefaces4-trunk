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

package org.icefaces.samples.showcase.dataGenerators;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;
import org.icefaces.samples.showcase.example.ace.dataTable.Car;
import org.icefaces.samples.showcase.example.ace.dataTable.SelectableCar;


public class VehicleGenerator implements Serializable
{
    private List<String> namesPool;
    private List<String> chassisPool;
    private List<String> colorPool;
    private Random randomizer = new Random(System.nanoTime());
    private NumberFormat numberFormatter;
    

    public VehicleGenerator() 
    {
        this.namesPool = getVehicleDescriptions();
        this.chassisPool = getChassisDescriptions();
        this.colorPool = getColorDescriptions();
        this.randomizer = new Random(System.nanoTime());
        this.numberFormatter = makeFormatter();
    }
    
    public ArrayList<Car> getRandomCars(int quantity)
    {
        ArrayList<Car> listWithCars = getPredefinedCars();
        
        if(quantity<listWithCars.size())
            return new ArrayList<Car>(listWithCars.subList(0, quantity));
        
        if(quantity>listWithCars.size())
            return addCarsToList(quantity - listWithCars.size(), listWithCars);
        
        //if quantity == listWithCars.size()
        return listWithCars;
    }
    
    public ArrayList<SelectableCar> getRandomSelectableCars(int quantity)
    {
        ArrayList<Car> listWithCars = getRandomCars(quantity);
        ArrayList<SelectableCar> listWithSelectableCars = new ArrayList<SelectableCar>(listWithCars.size());
        
        for (Car car : listWithCars) {
            listWithSelectableCars.add(new SelectableCar(car));
        }
        
        return listWithSelectableCars;
    }
    
    public ArrayList<Car> addCarsToList(int quantityToAdd, ArrayList<Car> list)
    {
        while (quantityToAdd > 0) {
            final int addThisRound = Math.min(list.size(), quantityToAdd);
System.out.println("quantityToAdd: " + quantityToAdd + "  currentListSize: " + list.size());

            for (int i = 0; i < addThisRound; i++, quantityToAdd--) {
                //add Car from the begining of the list to the end of it
                Car copyReference = list.get(i); //this pointer is mostly for code readability
                Car car = new Car(list.size()+1, copyReference.getName(),
                    copyReference.getChassis(), generateColor(), generateYear(),
                    generateWeight(), generateAccelerationValue(),
                    generateMPG(), generateCost());
                list.add(car);
            }
        }
        return list;
    }
    
    public ArrayList<Car> removeCarsFromList(int quantityToRemove, ArrayList<Car> list)
    {
        return new ArrayList<Car>(list.subList(0, list.size()-quantityToRemove-1));
    }
    
    private ArrayList<Car> getPredefinedCars()
    {
        ArrayList<Car> listWithCars = new ArrayList<Car>();
        
        listWithCars.add(new Car(1,"Enduro","Van",generateColor(),generateYear(),15383,10,17.86,6617.17));
        listWithCars.add(new Car(2,"Tamale","Bus",generateColor(),generateYear(),7331,15,16.65,31464.24));
        listWithCars.add(new Car(3,"Doublecharge","Pickup",generateColor(),generateYear(),5333,15,17.84,10922.73));
        listWithCars.add(new Car(4,"Swordfish","Bus",generateColor(),generateYear(),10956,5,5.17,6019.83));
        listWithCars.add(new Car(5,"Iguana","Pickup",generateColor(),generateYear(),1696,10,9.43,19736.16));
        listWithCars.add(new Car(6,"Dart","Motorcycle",generateColor(),generateYear(),9261,15,12.85,37947.84));
        listWithCars.add(new Car(7,"Pisces","Luxury",generateColor(),generateYear(),7846,10,15.13,19235.2));
        listWithCars.add(new Car(8,"Flash","Mid-Size",generateColor(),generateYear(),11499,10,12.74,29942.38));
        
        listWithCars.add(new Car(9,"Tomcat","Mid-Size",generateColor(),generateYear(),10766,15,7.04,14342.74));
        listWithCars.add(new Car(10,"Passion","Subcompact",generateColor(),generateYear(),2082,10,13.38,8015.01));
        listWithCars.add(new Car(11,"Superflash","Subcompact",generateColor(),generateYear(),14959,5,3.91,31941.1));
        listWithCars.add(new Car(12,"Husky","Mid-Size",generateColor(),generateYear(),14334,15,13.98,9303.69));
        listWithCars.add(new Car(13,"Gazelle","Semi-Truck",generateColor(),generateYear(),14248,5,8.64,9285.57));
        
        listWithCars.add(new Car(14,"Supraflash","Luxury",generateColor(),generateYear(),3037,15,8.44,36154.13));
        listWithCars.add(new Car(15,"Endino","Subcompact",generateColor(),generateYear(),4997,5,6.02,31221.48));
        listWithCars.add(new Car(16,"Tonto","Pickup",generateColor(),generateYear(),4555,10,4.06,35895.53));
        listWithCars.add(new Car(17,"Courage","Pickup",generateColor(),generateYear(),9848,10,16.15,27343.38));
        listWithCars.add(new Car(18,"Encyclo","Motorcycle",generateColor(),generateYear(),5725,5,14.17,34430.44));
        
        listWithCars.add(new Car(19,"Swansea","Luxury",generateColor(),generateYear(),1466,10,11.72,19266.56));
        listWithCars.add(new Car(20,"Toure","Station Wagon",generateColor(),generateYear(),15576,10,15.3,16844.05));
        listWithCars.add(new Car(21,"Ignite","Van",generateColor(),generateYear(),10973,15,3.77,33369.32));
        listWithCars.add(new Car(22,"Doublecharge","Pickup",generateColor(),generateYear(),9414,10,6.37,19370.61));
        listWithCars.add(new Car(23,"Hawk","Motorcycle",generateColor(),generateYear(),8545,5,7.98,39124.1));
        
        listWithCars.add(new Car(24,"Hook","Subcompact",generateColor(),generateYear(),1853,10,12.54,36068.19));
        listWithCars.add(new Car(25,"Tiny","Subcompact",generateColor(),generateYear(),12785,10,12.97,26141.79));
        listWithCars.add(new Car(26,"Hinterland","Semi-Truck",generateColor(),generateYear(),12453,10,10.4,13654.1));
        listWithCars.add(new Car(27,"Triplecharge","Van",generateColor(),generateYear(),11952,10,17.58,22599.82));
        listWithCars.add(new Car(28,"Freeze","Semi-Truck",generateColor(),generateYear(),6107,5,8.48,26096.26));
        
        listWithCars.add(new Car(29,"Courier","Bus",generateColor(),generateYear(),8187,5,4.25,25470.63));
        listWithCars.add(new Car(30,"Dart","Motorcycle",generateColor(),generateYear(),7177,10,12.16,35394.23));

        return listWithCars;
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
            generateChassis(), generateColor(), generateYear(),generateWeight(),
            generateAccelerationValue(), generateMPG(), generateCost());
        return randomCar;
    }
    
    private SelectableCar getRandomSelectableCar() {
        return new SelectableCar(getRandomCar()); 
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
    
    private ArrayList<SelectableCar> generateRandomSelectableCars(int quantity)
    {
        ArrayList<SelectableCar> listWithRandomCars = new ArrayList<SelectableCar>(quantity);
        for (int i = 0; i < quantity; i++) 
        {
            SelectableCar randomCar = getRandomSelectableCar();
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

    private List<String> getColorDescriptions() {
        List<String> list = new ArrayList<String>();
        list.add("Aquamarine");
        list.add("Auburn");
        list.add("Black");
        list.add("Blue");
        list.add("Bronze");
        list.add("Brown");
        list.add("Burgundy");
        list.add("Canary Yellow");
        list.add("Champagne");
        list.add("Cream");
        list.add("Emerald");
        list.add("Gold ");
        list.add("Green");
        list.add("Indigo");
        list.add("Jade");
        list.add("Lavender");
        list.add("Navy");
        list.add("Olive");
        list.add("Orange");
        list.add("Pink");
        list.add("Purple");
        list.add("Red");
        list.add("Sand");
        list.add("Silver");
        list.add("Slate");
        list.add("Steel");
        list.add("Teal");
        list.add("Turquoise");
        list.add("Violet");
        list.add("White");
        list.add("Yellow");
        return list;
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

    private String generateColor() {
        return colorPool.get(randomizer.nextInt(colorPool.size())); }

    private int generateYear() {
        return 1980+randomizer.nextInt(35); }
    
    private int generateWeight() 
    { return 1000+randomizer.nextInt(15000); }
	
    private int generateAccelerationValue() 
    { return (1+randomizer.nextInt(3)) * 5; }
	
    private double generateMPG() 
    { return Double.parseDouble(numberFormatter.format( ((double)(3+randomizer.nextInt(15))) + randomizer.nextDouble())); }
	
    private double generateCost() 
    { return Double.parseDouble(numberFormatter.format( ((double)(2000+randomizer.nextInt(40000))) + randomizer.nextDouble()) ); }
}
