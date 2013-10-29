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

package org.icefaces.tutorials;

import org.icefaces.tutorials.util.TutorialMessageUtils;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dynamic implements Serializable {
    private static final long serialVersionUID = 4481683100331087579L;
    private int selectedTabIndex = 0;
    private List<Movie> movies = new ArrayList<Movie>();
    private String richText="";
    private int counter;

    public Dynamic() {
        String movieIdsCommaDelim = TutorialMessageUtils.getMessage("movies");
        String[] movieIds = movieIdsCommaDelim.split(",");
        for (String movieId : movieIds) {
            String title = TutorialMessageUtils.getMessage("movie." + movieId + ".title");
            String plot = TutorialMessageUtils.getMessage("movie." + movieId + ".plot");
            Movie movie = new Movie(movieId, title, plot);
            movies.add(movie);
        }
        counter = movies.size();
    }

    public String getRichText() {
        return richText;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void tabChangeListener(ValueChangeEvent event) {
        richText = TutorialMessageUtils.formatMessage("action.tabChange", new Object[] {event.getOldValue(), event.getNewValue()});
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }
    
    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    public void addTab(ActionEvent event) {
        int i = counter++;
        String title = TutorialMessageUtils.formatMessage("action.tabAdd.template.title", new Object[] {i});
        String plot = TutorialMessageUtils.formatMessage("action.tabAdd.template.plot", new Object[] {i});
        Movie movie = new Movie("movie"+i, title, plot);
        movies.add(movie);
        richText = TutorialMessageUtils.formatMessage("action.tabAdd", new Object[] {movie.getTitle()});
    }

    public void removeCurrent(ActionEvent event){
        Movie movie = movies.remove(this.selectedTabIndex);
        richText = TutorialMessageUtils.formatMessage("action.tabRemove", new Object[] {selectedTabIndex, movie.getTitle()});
        if (selectedTabIndex >= movies.size()) {
            selectedTabIndex = 0;
        }
    }

    
    public static class Movie implements Serializable {
        private String id;
        private String title;
        private String plot;
        
        public Movie() {
            super();
        }

        public Movie(String id, String title, String plot) {
            super();
            this.id = id;
            this.title = title;
            this.plot = plot;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getPlot() { return plot; }
    }
}
