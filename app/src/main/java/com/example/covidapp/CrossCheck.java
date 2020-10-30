package com.example.covidapp;

import java.util.ArrayList;
import java.util.Collections;

public class CrossCheck {
//THis function checks whether there is a matching IDs.If yes, then it returns true; otherwise it returns false.
    public static Boolean checkLists(ArrayList<String> sharedList, ArrayList<String> personalList) {
        Boolean exposed = false;
       /* for (int i = 0; i < sharedList.size(); i++) {
            for (int j = 0; j < personalList.size(); j++) {
                if (sharedList.get(i).equals(personalList.get(j))) {
                    exposed = true;
                }
            }
        }*/

        exposed= !Collections.disjoint(sharedList, personalList);
        return exposed;
    }

}
