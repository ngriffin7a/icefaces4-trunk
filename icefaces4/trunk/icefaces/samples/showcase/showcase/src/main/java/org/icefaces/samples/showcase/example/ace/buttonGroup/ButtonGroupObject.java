/*
 * Copyright 2004-2016 ICEsoft Technologies Canada Corp.
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
package org.icefaces.samples.showcase.example.ace.buttonGroup;

import java.io.Serializable;

public class ButtonGroupObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private int row;
    private boolean radio1, radio2, check1, check2, radio3, check3;


    public ButtonGroupObject(int row){
        this.radio1 = false;
        this.radio2 = false;
        this.check1 = false;
        this.check2 = false;
        this.radio3 = false;
        this.check3 = false;
        this.row = row;
    }

    public ButtonGroupObject(int row, boolean radio1, boolean radio2, boolean check1, boolean check2){
        this.row = row;
        this.radio1 = radio1;
        this.radio2 = radio2;
        this.check1 = check1;
        this.check2 = check2;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }


    public String getBoxValueDescription1() {
        if(radio1)
            return "Button1 selected";
        else
            return "Not Selected";
    }

    public String getBoxValueDescription2() {
        if(radio2)
            return "Button2 selected";
        else
            return "Not Selected";
    }


    public boolean isRadio1() {
        return radio1;
    }

    public void setRadio1(boolean radio1) {
        this.radio1 = radio1;
    }

    public boolean isRadio2() {
        return radio2;
    }

    public void setRadio2(boolean radio2) {
        this.radio2 = radio2;
    }

    public boolean isCheck1() {
        return check1;
    }

    public void setCheck1(boolean check1) {
        this.check1 = check1;
    }

    public boolean isCheck2() {
        return check2;
    }

    public void setCheck2(boolean check2) {
        this.check2 = check2;
    }

    public boolean isRadio3() {
        return radio3;
    }

    public void setRadio3(boolean radio3) {
        this.radio3 = radio3;
    }

    public boolean isCheck3() {
        return check3;
    }

    public void setCheck3(boolean check3) {
        this.check3 = check3;
    }
}
