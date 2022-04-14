package ntnu.idatt2001.projects.model.units;

public enum UnitType {
    INFANTRY,
    CAVALRY,
    RANGED,
    COMMANDER;

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
