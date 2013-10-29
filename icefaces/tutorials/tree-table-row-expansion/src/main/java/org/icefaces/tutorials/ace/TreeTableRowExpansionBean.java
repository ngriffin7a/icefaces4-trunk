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

package org.icefaces.tutorials.ace;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.*;

public class TreeTableRowExpansionBean implements Serializable {
    // Create sample data structure
    List<Person> persons = new ArrayList<Person>() {{
        Date now = Calendar.getInstance().getTime();

        add(new Person("Alexander Garcia", "555-5555", now));
        add(new Person("Angel Garcia", "555-5555", now));
        add(new Person("Anthony Garcia", "555-5555", now));

        add(new Person("Bruce Garcia", "555-5555", now));
        add(new Person("Bart Garcia", "555-5555", now));
        add(new Person("Bertha Garcia", "555-5555", now));
        
        add(new Person("Carl Garcia", "555-5555", now));
        add(new Person("Celia Garcia", "555-5555", now));
        add(new Person("Chuck Garcia", "555-5555", now));

        add(new Person("Alexander Rodriguez", "555-5555", now));
        add(new Person("Angel Rodriguez", "555-5555", now));
        add(new Person("Anthony Rodriguez", "555-5555", now));

        add(new Person("Bruce Rodriguez", "555-5555", now));
        add(new Person("Bart Rodriguez", "555-5555", now));
        add(new Person("Bertha Rodriguez", "555-5555", now));

        add(new Person("Carl Rodriguez", "555-5555", now));
        add(new Person("Celia Rodriguez", "555-5555", now));
        add(new Person("Chuck Rodriguez", "555-5555", now));

        add(new Person("Alexander Molinero", "555-5555", now));
        add(new Person("Angel Molinero", "555-5555", now));
        add(new Person("Anthony Molinero", "555-5555", now));

        add(new Person("Bruce Molinero", "555-5555", now));
        add(new Person("Bart Molinero", "555-5555", now));
        add(new Person("Bertha Molinero", "555-5555", now));

        add(new Person("Carl Molinero", "555-5555", now));
        add(new Person("Celia Molinero", "555-5555", now));
        add(new Person("Chuck Molinero", "555-5555", now));
    }};

    List<City> cities = new ArrayList<City>() {{
        add(new City("Saskatoon", persons.subList(0, 3)));
        add(new City("Winnipeg", persons.subList(3, 6)));
        add(new City("Regina", persons.subList(7, 9)));

        add(new City("Halifax", persons.subList(9, 12)));
        add(new City("Ottawa", persons.subList(12, 15)));
        add(new City("Montreal", persons.subList(15, 18)));

        add(new City("Calgary", persons.subList(18, 21)));
        add(new City("Edmonton", persons.subList(21, 24)));
        add(new City("Vancouver", persons.subList(24, 27)));
    }};

    List<Region> regions = new ArrayList<Region>() {{
        add(new Region("Central", cities.subList(0,3)));
        add(new Region("Eastern", cities.subList(3,6)));
        add(new Region("Western", cities.subList(6,9)));
    }};

    List<Organization> orgs = new ArrayList<Organization>() {{
        add(new Organization("Acme Ltd.", regions));
    }};

    // Convert sample data into tree table type
    List<Map.Entry> data = new ArrayList<Map.Entry>() {{
        for (Organization org : orgs) {
            List<Map.Entry> orgChildren = new ArrayList<Map.Entry>(org.getRegionList().size());

            for (Region region : org.getRegionList()) {
                List<Map.Entry> regionChildren = new ArrayList<Map.Entry>(region.getCityList().size());

                for (City city : region.getCityList()) {
                    List<Map.Entry> cityChildren = new ArrayList<Map.Entry>(city.getPersonList().size());

                    for (Person person : city.getPersonList())
                        cityChildren.add(new AbstractMap.SimpleEntry(
                                new PersonnelRowObject(person), null));

                    regionChildren.add(new AbstractMap.SimpleEntry(
                            new PersonnelRowObject(city), cityChildren)
                    );
                }
                orgChildren.add(new AbstractMap.SimpleEntry(
                        new PersonnelRowObject(region), regionChildren)
                );
            }
            add(new AbstractMap.SimpleEntry(
                    new PersonnelRowObject(org), orgChildren)
            );
        }
    }};

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public List<Organization> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<Organization> orgs) {
        this.orgs = orgs;
    }

    public List<Map.Entry> getData() {
        return data;
    }

    public void setData(List<Map.Entry> data) {
        this.data = data;
    }
}
