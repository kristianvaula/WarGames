package ntnu.idatt2001.projects.model.units;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UnitType {
    INFANTRY,
    CAVALRY,
    RANGED,
    COMMANDER;

    /**
     * Gets a list of all the unit types
     *
     * @return List of all UnitType values
     */
    public static List<UnitType> getUnitTypes(){
        return new ArrayList<>(Arrays.asList(UnitType.values()));
    }

    @Override
    public String toString() {
        if(this == INFANTRY){
            return "Infantry";
        }
        else if(this == CAVALRY){
            return "Cavalry";
        }
        else if(this == RANGED){
            return "Ranged";
        }
        else if(this == COMMANDER){
            return "Commander";
        }
        return null;
    }
}
